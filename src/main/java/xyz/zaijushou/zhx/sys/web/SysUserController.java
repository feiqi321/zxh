package xyz.zaijushou.zhx.sys.web;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysUserService;

import javax.annotation.Resource;

@RestController
@RequestMapping(value = "/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @PostMapping("index")
    public Object index() {
        return WebResponse.success();
    }

    @PostMapping(value = "/login")
    public Object login(SysUserEntity user) throws AuthenticationException {
        return sysUserService.login(user);
    }

}
