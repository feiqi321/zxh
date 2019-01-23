package xyz.zaijushou.zhx.utils;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenUtil {

    private static String signKey;

    /**
     * 从request 中获取token
     * @return token
     */
    public static String token(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }

    /**
     * 生成token
     * @param subject token内容
     * @return token
     */
    public static String token(String subject){
        return Jwts.builder().setSubject(subject).signWith(SignatureAlgorithm.HS512, signKey).compact();
    }

    /**
     * 解析token，token 从request中获取
     * @return JSONObject
     */
    public static JSONObject tokenData() {
        String token = token();
        return tokenData(token);
    }

    /**
     * 解析token
     * @param token token 字符串
     * @return JSONObject
     */
    public static JSONObject tokenData(String token) {
        String tokenContent = Jwts.parser().setSigningKey(signKey).parseClaimsJws(token.replace("Bearer ", "")).getBody().getSubject();
        return JSONObject.parseObject(tokenContent);
    }

    @Value("${xyz.zaijushou.zhx.token.sign-key}")
    public void setSignKey(String signKey) {
        JwtTokenUtil.signKey = signKey;
    }
}
