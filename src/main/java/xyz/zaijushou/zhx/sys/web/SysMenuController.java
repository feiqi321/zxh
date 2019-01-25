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
import xyz.zaijushou.zhx.sys.entity.SysButtonEntity;
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
    public Object treeAllMenusByToken() {
        JSONArray roles = userRole();
        if(CollectionUtils.isEmpty(roles)) {
            return WebResponse.success(new ArrayList<>());
        }

        Map<Integer, SysMenuEntity> menuMap = menuMap(roles);
        if(CollectionUtils.isEmpty(menuMap)) {
            return WebResponse.success(new ArrayList<>());
        }
        List<SysMenuEntity> menuList = CollectionsUtils.listToTree(new ArrayList<>(menuMap.values()));
        return WebResponse.success(menuList);
    }

    @PostMapping("treeAllMenusWithButtonByToken")
    @ApiOperation(value = "根据token获取菜单信息", notes = "根据token获取菜单信息")
    public Object treeAllMenusWithButtonByToken() {
        JSONArray roles = userRole();
        if(CollectionUtils.isEmpty(roles)) {
            return WebResponse.success(new ArrayList<>());
        }

        Map<Integer, SysMenuEntity> menuMap = menuMap(roles);
        if(CollectionUtils.isEmpty(menuMap)) {
            return WebResponse.success(new ArrayList<>());
        }

        Set<String> buttonReisKeys = roleAttrKeys(roles, RedisKeyPrefix.ROLE_BUTTON);

        Map<Integer, SysButtonEntity> buttonMap = new HashMap<>();
        if(buttonReisKeys.size() > 0) {
            List<String> buttonsString = stringRedisTemplate.opsForValue().multiGet(buttonReisKeys);
            if(!CollectionUtils.isEmpty(buttonsString)) {
                for(String buttonsStr : buttonsString) {
                    SysButtonEntity[] buttons = JSONArray.parseObject(buttonsStr, SysButtonEntity[].class);
                    if(buttons == null || buttons.length == 0) {
                        continue;
                    }
                    for(SysButtonEntity button : buttons) {
                        buttonMap.put(button.getId(), button);
                    }
                }
            }

            for(Map.Entry<Integer, SysButtonEntity> entry : buttonMap.entrySet()) {
                if (menuMap.containsKey(entry.getValue().getParentMenu().getId())) {
                    if (menuMap.get(entry.getValue().getParentMenu().getId()).getButtonList() == null) {
                        menuMap.get(entry.getValue().getParentMenu().getId()).setButtonList(new ArrayList<>());
                    }
                    menuMap.get(entry.getValue().getParentMenu().getId()).getButtonList().add(entry.getValue());
                }
            }
        }

        List<SysMenuEntity> menuList = CollectionsUtils.listToTree(new ArrayList<>(menuMap.values()));
        return WebResponse.success(menuList);
    }

    private JSONArray userRole() {
        JSONObject redisJson = JwtTokenUtil.tokenData();
        String userRolesString = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.USER_ROLE + redisJson.getInteger("userId"));
        return JSONArray.parseArray(userRolesString);
    }

    private Map<Integer, SysMenuEntity> menuMap(JSONArray roles) {
        Map<Integer, SysMenuEntity> menuMap = new ConcurrentHashMap<>();
        Set<String> menuReisKeys = roleAttrKeys(roles, RedisKeyPrefix.ROLE_MENU);
        if(menuReisKeys.size() > 0) {
            List<String> menusString = stringRedisTemplate.opsForValue().multiGet(menuReisKeys);
            if(CollectionUtils.isEmpty(menusString)) {
                return menuMap;
            }
            for(String menusStr : menusString) {
                SysMenuEntity[] menus = JSONArray.parseObject(menusStr, SysMenuEntity[].class);
                for(SysMenuEntity menu : menus) {
                    menuMap.put(menu.getId(), menu);
                }
            }
        }
        return menuMap;
    }

    private Set<String> roleAttrKeys(JSONArray roles, String redisPrefix) {
        Set<String> menuRedisKeys = new HashSet<>();
        for(int i = 0; i < roles.size(); i ++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            menuRedisKeys.add(redisPrefix + role.getId());
        }
        return menuRedisKeys;
    }


}
