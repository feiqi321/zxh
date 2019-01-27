package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

@Api("登录/注销")
@RestController
@RequestMapping(value = "/zxh")
public class LoginController {


    @ApiOperation(value = "登录", notes = "用户登录")
    @PostMapping("/login")
    public Object login(@RequestBody SysUserEntity user) {
        return WebResponse.success();
    }

    @ApiOperation(value = "登出", notes = "用户注销")
    @PostMapping("/logout")
    public Object logout() {
        return WebResponse.success();
    }

}
