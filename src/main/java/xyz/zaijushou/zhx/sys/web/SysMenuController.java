package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Api("用户操作")
@RestController
@RequestMapping(value = "/user")
public class SysMenuController {

    @Resource
    private SysUserService sysUserService;

    @ApiIgnore
    @ApiOperation(value = "测试", notes = "测试接口")
    @PostMapping("index")
    public Object index() {
        return WebResponse.success();
    }

    @PostMapping("treeAllMenusByToken")
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    public Object userInfo(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String tokenData = Jwts.parser()
                .setSigningKey("zaijushouzhx")
                .parseClaimsJws(token.replace("Bearer ", ""))
                .getBody()
                .getSubject();
        Integer userId = JSONObject.parseObject(tokenData).getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        user = sysUserService.findUserInfoWithoutPasswordById(user);
        return WebResponse.success(user);
    }



}
