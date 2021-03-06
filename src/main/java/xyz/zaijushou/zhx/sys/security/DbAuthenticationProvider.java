package xyz.zaijushou.zhx.sys.security;

import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Component;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;

@Component
public class DbAuthenticationProvider implements AuthenticationProvider {

    @Resource
    private UserDetailsService userDetailsService;

    @Resource
    private DelegatingPasswordEncoder delegatingPasswordEncoder;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String name = authentication.getName();
        String password = authentication.getCredentials().toString();
        // 认证逻辑
        UserDetails userDetails = userDetailsService.loadUserByUsername(name);
        if (null != userDetails) {
            SysUserEntity user = new SysUserEntity();
            user.setLoginName(name);
            user = sysUserMapper.findPasswordInfoByLoginName(user);
            if (delegatingPasswordEncoder.matches(password, userDetails.getPassword())) {
                if(user.getLoginFailTimes() > 0) {
                    user.setLoginFailTimes(0);
                    sysUserMapper.updateLoginFailTimes(user);
                }
                // 这里设置权限和角色
                ArrayList<GrantedAuthority> authorities = new ArrayList<>();
//                authorities.add( new GrantedAuthorityImpl("ROLE_ADMIN"));
//                authorities.add( new GrantedAuthorityImpl("AUTH_WRITE"));
                // 生成令牌 这里令牌里面存入了:name,password,authorities, 当然你也可以放其他内容
                return new UsernamePasswordAuthenticationToken(name, password, authorities);
            } else {
                throw new BadCredentialsException("密码错误");
            }
        } else {
            throw new UsernameNotFoundException("用户不存在");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
