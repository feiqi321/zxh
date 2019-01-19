package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;

@Service
public class SysUserServiceImpl implements SysUserService {

//    @Resource
//    private AuthenticationManager authenticationManager;

//    @Resource
//    private UserDetailsService userDetailsService;

    @Resource
    private SysUserMapper sysUserMapper;



    @Override
    public String login(SysUserEntity user) {
//        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
//        Authentication authentication = authenticationManager.authenticate(upToken);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        authentication.getPrincipal();
        user = sysUserMapper.findUserInfoWithoutPasswordByUsername(user);
        return JwtTokenUtil.generateToken(user);
    }

    private SysUserEntity findPasswordInfoByUsername(SysUserEntity user) {
        return sysUserMapper.findPasswordInfoByUsername(user);
    }
}
