package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.SysMenuEntity;
import xyz.zaijushou.zhx.sys.entity.SysRoleEntity;
import xyz.zaijushou.zhx.sys.service.SysMenuService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Api("菜单操作")
@RestController
@RequestMapping(value = "/menu")
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("treeAllMenusByToken")
    @ApiOperation(value = "根据token获取菜单信息", notes = "根据token获取菜单信息")
    public Object userInfo(HttpServletRequest request) {
        Map<Integer, SysMenuEntity> menuMap = new ConcurrentHashMap<>();
        JSONObject redisJson = JwtTokenUtil.tokenData();
        String userRolesString = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.USER_ROLE + redisJson.getInteger("userId"));
        JSONArray roles = JSONArray.parseArray(userRolesString);
        if(CollectionUtils.isEmpty(roles)) {
            return WebResponse.success(new ArrayList<>());
        }
        Set<String> redisKeys = new HashSet<>();
        for(int i = 0; i < roles.size(); i ++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            redisKeys.add(RedisKeyPrefix.ROLE_MENU + role.getId());
        }
        if(redisKeys.size() > 0) {
            List<String> menusString = stringRedisTemplate.opsForValue().multiGet(redisKeys);
            if(CollectionUtils.isEmpty(menusString)) {
                return WebResponse.success(new ArrayList<>());
            }
            for(String menusStr : menusString) {
                SysMenuEntity[] menus = JSONArray.parseObject(menusStr, SysMenuEntity[].class);
                for(SysMenuEntity menu : menus) {
                    menuMap.put(menu.getId(), menu);
                }
            }
        }

        List<SysMenuEntity> menuList = CollectionsUtils.listToTree(new ArrayList<>(menuMap.values()));
//        for(SysMenuEntity menu : menuList) {
//            menuMap.put(menu.getId(), menu);
//        }
//        for(SysMenuEntity menu : menuList) {
//            if(menu.getParent().getId() == 0) {
//                continue;
//            }
//            if(menuMap.get(menu.getParent().getId()).getChildren() == null) {
//                menuMap.get(menu.getParent().getId()).setChildren(new ArrayList<>());
//            }
//            menuMap.get(menu.getParent().getId()).getChildren().add(menuMap.get(menu.getId()));
//        }
//
//        for(Map.Entry<Integer, SysMenuEntity> entry : menuMap.entrySet()) {
//            if(entry.getValue().getParent().getId() == 0) {
//                continue;
//            }
//            menuMap.remove(entry.getKey());
//        }
//
//        menuList = new ArrayList<>();
//        for(Map.Entry<Integer, SysMenuEntity> entry : menuMap.entrySet()) {
//            menuList.add(entry.getValue());
//        }
        return WebResponse.success(menuList);
    }



}
