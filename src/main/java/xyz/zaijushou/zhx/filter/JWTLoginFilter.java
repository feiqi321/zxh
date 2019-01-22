package xyz.zaijushou.zhx.filter;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.SpringUtils;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 验证用户名密码正确后，生成一个token，并将token返回给客户端
 * 该类继承自UsernamePasswordAuthenticationFilter，重写了其中的2个方法
 * attemptAuthentication ：接收并解析用户凭证。
 * successfulAuthentication ：用户成功登录后，这个方法会被调用，我们在这个方法里生成token。
 * @author zhaoxinguo on 2017/9/12.
 */

public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {

    @Value("${xyz.zaijushou.zhx.redis.login-token-expire-time}")
    private Integer loginTokenExpireTime;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;



    // 接收并解析用户凭证
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
        try {
            SysUserEntity user = new ObjectMapper().readValue(req.getInputStream(), SysUserEntity.class);
            return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(user.getLoginName(),user.getPassword(),new ArrayList<>()));
        } catch (AuthenticationException e) {
            try {
                res.setHeader("Access-Control-Allow-Origin", "*");
                res.setHeader("Access-Control-Allow-Credentials", "true");
                res.setHeader("Access-Control-Allow-Methods", "*");
                res.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
                res.setHeader("Access-Control-Expose-Headers", "*");
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(JSONObject.toJSONString(WebResponse.error("500", "账号或密码错误")));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 用户成功登录后，这个方法会被调用，我们在这个方法里生成token
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        try {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            // 定义存放角色集合的对象
            SysUserEntity user = new SysUserEntity();
            user.setLoginName(auth.getName());
            user = sysUserMapper.findUserInfoWithoutPasswordByLoginName(user);
            JSONObject subject = new JSONObject();
            subject.put("userId", user.getId());
            subject.put("loginName", auth.getName());
            subject.put("loginTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            String token = JwtTokenUtil.token(subject.toJSONString());
            user.setToken("Bearer " + token);
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.LOGIN_TOKEN + user.getToken(), JSONObject.toJSONString(token), 30, TimeUnit.MINUTES);
            response.setHeader("Access-Control-Allow-Origin", "*");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Methods", "*");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type,Access-Token");
            response.setHeader("Access-Control-Expose-Headers", "*");
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(WebResponse.success(user)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
