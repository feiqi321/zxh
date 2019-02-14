package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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

    @ApiOperation(value = "修改状态", notes = "修改状态")
    @PostMapping("/update/status")
    public Object updateDataStatus(@RequestBody SysNewUserEntity userEntity) {
        sysUserService.updateDataStatus(userEntity);
        return WebResponse.success();
    }

    @ApiOperation(value = "员工删除", notes = "员工删除")
    @PostMapping("/delete/id")
    public Object deleteById(@RequestBody SysNewUserEntity userEntity) {
        sysUserService.deleteById(userEntity);
        return WebResponse.success();
    }

    @ApiOperation(value = "查询用户数据列表", notes = "查询用户数据列表")
    @PostMapping("/select/list")
    public Object getDataList(@RequestBody SysNewUserEntity userEntity) {

        PageInfo<SysNewUserEntity> userEntityList = sysUserService.userDataList(userEntity);

        return WebResponse.success(userEntityList);
    }

    @ApiOperation(value = "查询指定用户数据", notes = "查询指定用户数据")
    @PostMapping("/select/id")
    public Object getDataById(@RequestBody SysNewUserEntity userEntity) {
        SysNewUserEntity userInfoEntity = sysUserService.getDataById(userEntity);
        return WebResponse.success(userInfoEntity);
    }

    @ApiOperation(value = "通过角色查询员工列表", notes = "通过角色查询员工列表")
    @PostMapping("/select/role")
    public Object getDataByRoleName(@RequestBody SysNewUserEntity userEntity) {
        List<SysNewUserEntity> userInfoEntity = sysUserService.getDataByRoleName(userEntity);
        return WebResponse.success(userInfoEntity);
    }

    @ApiOperation(value = "获取职位列表", notes = "获取职位列表")
    @PostMapping("/select/position")
    public Object getPosition() {
        List<SysNewUserEntity> userInfoEntity = new ArrayList<SysNewUserEntity>();
        SysNewUserEntity user1 = new SysNewUserEntity();
        user1.setJob(1);
        user1.setPosition("业务员");
        userInfoEntity.add(user1);
        SysNewUserEntity user2 = new SysNewUserEntity();
        user2.setJob(2);
        user2.setPosition("职能经理");
        userInfoEntity.add(user2);
        return WebResponse.success(userInfoEntity);
    }

    @ApiOperation(value = "密码重置", notes = "重置用户密码")
    @PostMapping("/passwordReset")
    public Object passwordReset(@RequestBody SysNewUserEntity user) {
        if(user == null || user.getOldPassword() == null || user.getPassword() == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "缺少参数");
        }
        sysUserService.passwordReset(user);
        return WebResponse.success();
    }

    @ApiOperation(value = "管理员重置用户密码", notes = "管理员重置用户密码")
    @PostMapping("/passwordResetByAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public Object passwordResetByAdmin(@RequestBody SysNewUserEntity user) {
        if(user == null || user.getPassword() == null || user.getId() == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "缺少参数");
        }
        sysUserService.passwordResetByAdmin(user);
        return WebResponse.success();
    }



}
