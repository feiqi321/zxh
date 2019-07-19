package xyz.zaijushou.zhx.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import xyz.zaijushou.zhx.filter.JWTAuthenticationFilter;
import xyz.zaijushou.zhx.filter.JWTLoginFilter;
import xyz.zaijushou.zhx.filter.OperationLogFilter;
import xyz.zaijushou.zhx.sys.security.DbAuthenticationProvider;
import xyz.zaijushou.zhx.sys.security.JwtLogoutSuccessHandler;
import xyz.zaijushou.zhx.sys.security.ZhxAccessDeniedHandler;

import javax.annotation.Resource;

/**
 * SpringSecurity的配置
 * 通过SpringSecurity的配置，将JWTLoginFilter，JWTAuthenticationFilter组合在一起
 *
 * @author zhaoxinguo on 2017/9/13.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 需要放行的URL
     */
    private static final String[] AUTH_WHITELIST = {
            "/",
            // -- register url
            "/users/signup",
            // -- swagger ui
            "/v2/api-docs",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/csrf",
            "/user/select/list",
            "/fileManage/download",
            "/statistics/collection/dataCollection/save",
            "/fileManage/downloadArchive",
            "/webjars/**"
            // other public endpoints of your API may be appended to this array
    };

    @Resource
    private DbAuthenticationProvider dbAuthenticationProvider;

    @Resource
    private JwtLogoutSuccessHandler jwtLogoutSuccessHandler;

    @Resource
    private ZhxAccessDeniedHandler zhxAccessDeniedHandler;

    @Resource
    private OperationLogFilter operationLogFilter;

    // 设置 HTTP 验证规则
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()  // 所有请求需要身份认证
                .and()
                .exceptionHandling()
//                    .authenticationEntryPoint(new Http401AuthenticationEntryPoint("Basic realm=\"MyApp\""))
                .and()
//                .exceptionHandling().accessDeniedHandler(customAccessDeniedHandler) // 自定义访问失败处理器
//                .and()
                .addFilter(jwtLoginFilter())
                .addFilter(jwtAuthenticationFilter())
//                .addFilter(jwtAuthenticationFilter)
                .logout() // 默认注销行为为logout，可以通过下面的方式来修改
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")// 设置注销成功后跳转页面，默认是跳转到登录页面;
                .logoutSuccessHandler(jwtLogoutSuccessHandler)
                .permitAll()
                .and()
                .csrf()
                .disable()
                .exceptionHandling()
                .accessDeniedHandler(zhxAccessDeniedHandler)
                .and()
                .addFilterBefore(operationLogFilter, UsernamePasswordAuthenticationFilter.class);
    }

    // 该方法是登录的时候会进入
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        // 使用自定义身份验证组件
        auth.authenticationProvider(dbAuthenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers(AUTH_WHITELIST).antMatchers(HttpMethod.GET);
    }

    @Bean
    public JWTLoginFilter jwtLoginFilter() throws Exception {
        JWTLoginFilter jwtLoginFilter = new JWTLoginFilter();
        jwtLoginFilter.setAuthenticationManager(authenticationManager());
        return jwtLoginFilter;
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        return new JWTAuthenticationFilter(authenticationManager());
    }


}
