package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.jsonwebtoken.Jwts;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysMenuEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysMenuService;
import xyz.zaijushou.zhx.sys.service.SysUserService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Api("用户操作")
@RestController
@RequestMapping(value = "/menu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

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

        List<SysMenuEntity> menuList = sysMenuService.listAllMenus(new SysMenuEntity());
        Map<Integer, SysMenuEntity> menuMap = new ConcurrentHashMap<>();
        for(SysMenuEntity menu : menuList) {
            menuMap.put(menu.getId(), menu);
        }
        for(SysMenuEntity menu : menuList) {
            if(menu.getParent().getId() == 0) {
                continue;
            }
            if(menuMap.get(menu.getParent().getId()).getChildren() == null) {
                menuMap.get(menu.getParent().getId()).setChildren(new ArrayList<>());
            }
            menuMap.get(menu.getParent().getId()).getChildren().add(menuMap.get(menu.getId()));
        }

        for(Map.Entry<Integer, SysMenuEntity> entry : menuMap.entrySet()) {
            if(entry.getValue().getParent().getId() == 0) {
                continue;
            }
            menuMap.remove(entry.getKey());
        }

        menuList = new ArrayList<>();
        for(Map.Entry<Integer, SysMenuEntity> entry : menuMap.entrySet()) {
            menuList.add(entry.getValue());
        }
        return WebResponse.success(menuList);
    }



}
