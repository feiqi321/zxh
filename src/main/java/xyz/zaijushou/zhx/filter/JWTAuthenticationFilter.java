package xyz.zaijushou.zhx.filter;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import xyz.zaijushou.zhx.common.exception.TokenException;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.utils.SpringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * 自定义JWT认证过滤器
 * 该类继承自BasicAuthenticationFilter，在doFilterInternal方法中，
 * 从http头的Authorization 项读取token数据，然后用Jwts包提供的方法校验token的合法性。
 * 如果校验通过，就认为这是一个取得授权的合法请求
 * @author zhaoxinguo on 2017/9/13.
 */
public class JWTAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

    private StringRedisTemplate redisTemplate = SpringUtils.getBean(StringRedisTemplate.class);

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
        response.setHeader("Access-Control-Expose-Headers", "*");

        if (request.getMethod().equals( RequestMethod.OPTIONS.toString())){
            return;
        }

        try {
            UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (TokenException e) {
            logger.warn("token 验证失败：" + e);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(WebResponse.error("400", e.getMessage())));
            return;
        }

        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if(StringUtils.isEmpty(token)) {
            throw new TokenException("Token为空");
        }
        if(!token.startsWith("Bearer ")) {
            throw new TokenException("Token类型错误");
        }

        String redisTokenInfo = redisTemplate.opsForValue().get("login_token_" + token);
        if(StringUtils.isEmpty(redisTokenInfo)) {
            throw new TokenException("无效token");
        }
        try {
            String user = Jwts.parser()
                    .setSigningKey("zaijushouzhx")
                    .parseClaimsJws(token.replace("Bearer ", ""))
                    .getBody()
                    .getSubject();
            if (user != null) {
//                String[] split = user.split("-")[1].split(",");
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
//                for (int i=0; i < split.length; i++) {
//                    authorities.add(new GrantedAuthorityImpl());
//                }
                return new UsernamePasswordAuthenticationToken(user, null, authorities);
            }

        } catch (ExpiredJwtException e) {
            logger.error("Token已过期: {} " + e);
            throw new TokenException("Token已过期");
        } catch (UnsupportedJwtException e) {
            logger.error("Token格式错误: {} " + e);
            throw new TokenException("Token格式错误");
        } catch (MalformedJwtException e) {
            logger.error("Token没有被正确构造: {} " + e);
            throw new TokenException("Token没有被正确构造");
        } catch (SignatureException e) {
            logger.error("签名失败: {} " + e);
            throw new TokenException("签名失败");
        } catch (IllegalArgumentException e) {
            logger.error("非法参数异常: {} " + e);
            throw new TokenException("非法参数异常");
        }

        return null;
    }

}
