package xyz.zaijushou.zhx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import xyz.zaijushou.zhx.sys.security.Md5WithoutSaltPasswordEncoder;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class PasswordEncoderConfig {

    @Resource
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Resource
    private Md5WithoutSaltPasswordEncoder md5PasswordEncoder;

    @Resource
    private PasswordEncoder noOpPasswordEncoder;


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Md5WithoutSaltPasswordEncoder md5PasswordEncoder() {
        return new Md5WithoutSaltPasswordEncoder();
    }

    @Bean
    public PasswordEncoder noOpPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public DelegatingPasswordEncoder delegatingPasswordEncoder() {
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("bcrypt", bCryptPasswordEncoder);
        encoders.put("md5", md5PasswordEncoder);
        encoders.put("noop", noOpPasswordEncoder);
        return new DelegatingPasswordEncoder("noop", encoders);
    }

}
