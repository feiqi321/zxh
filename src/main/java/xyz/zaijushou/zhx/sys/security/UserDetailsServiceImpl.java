package xyz.zaijushou.zhx.sys.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import javax.annotation.Resource;
import java.util.ArrayList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public UserDetails loadUserByUsername(String loginname) throws UsernameNotFoundException {
        SysUserEntity user = new SysUserEntity();
        user.setUserName(loginname);
        user = sysUserMapper.findPasswordInfoByUsername(user);
        if(user == null){
            throw new UsernameNotFoundException("账号 " + loginname + " 不存在");
        }
        return new User(user.getUserName(), user.getPassword(), new ArrayList<>());
    }
}
