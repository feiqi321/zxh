package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysOperationUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Api("用户操作")
@RestController
@RequestMapping(value = "/user")
public class SysUserController {

    @Resource
    private SysUserService sysUserService;

    @ApiIgnore
    @ApiOperation(value = "测试", notes = "测试接口")
    @PostMapping("index")
    public Object index() {
        return WebResponse.success();
    }

//    @PreAuthorize("hasAuthority('auth_userinfo')")
    @PostMapping("userInfo")
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    public Object userInfo(HttpServletRequest request) {
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        user = sysUserService.findUserInfoWithoutPasswordById(user);
        return WebResponse.success(user);
    }

    @ApiOperation(value = "新增用户", notes = "新增用户")
    @PostMapping("/insert")
    public Object saveData(@RequestBody SysNewUserEntity userEntity) {
        sysUserService.saveUser(userEntity);
        return WebResponse.success();
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @PostMapping("/update")
    public Object updateData(@RequestBody SysNewUserEntity userEntity) {
        sysUserService.updateUser(userEntity);
        return WebResponse.success();
    }

    @ApiOperation(value = "查询用户数据列表", notes = "查询用户数据列表")
    @PostMapping("/select/list")
    public Object getDataList(@RequestBody SysNewUserEntity userEntity) {
        List<SysNewUserEntity> userEntityList = sysUserService.pageDataList(userEntity);
        return WebResponse.success(userEntityList);
    }

    @ApiOperation(value = "查询指定用户数据", notes = "查询指定用户数据")
    @PostMapping("/select/id")
    public Object getDataById(@RequestBody SysNewUserEntity userEntity) {
        SysNewUserEntity userInfoEntity = sysUserService.getDataById(userEntity);
        return WebResponse.success(userInfoEntity);
    }

}
