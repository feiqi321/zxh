package xyz.zaijushou.zhx.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {

    private static String SECRET_KEY = "zaijushou.zhx";

    public static String generateToken(SysUserEntity userEntity) {
        Map<String, Object> claims = new HashMap<>(2);
        claims.put("sub", userEntity.getLoginName());
        claims.put("created", new Date());
        return generateToken(claims);
    }

    private static String generateToken(Map<String, Object> claims) {
        Date expirationDate = new Date(System.currentTimeMillis() + 2592000L * 1000);
        return Jwts.builder().setClaims(claims).setExpiration(expirationDate).signWith(SignatureAlgorithm.HS512, SECRET_KEY).compact();
    }

}
