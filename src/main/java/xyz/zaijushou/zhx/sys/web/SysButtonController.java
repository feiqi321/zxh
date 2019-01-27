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
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Api("按钮操作")
@RestController
@RequestMapping(value = "/button")
public class SysButtonController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("treeAllButtonsWithMenuByToken")
    @ApiOperation(value = "根据token获取按钮信息", notes = "根据token获取按钮信息，返回带菜单的树状结构")
    public Object userInfo() {
        Map<Integer, SysMenuEntity> menuMap = new ConcurrentHashMap<>();
        Map<Integer, Map<Integer, SysButtonEntity>> parentMenuButtonMap = new HashMap<>();    //parentMenu.id - (button-id, button)
        JSONObject redisJson = JwtTokenUtil.tokenData();
        String userRolesString = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.USER_ROLE + redisJson.getInteger("userId"));
        JSONArray roles = JSONArray.parseArray(userRolesString);
        if(CollectionUtils.isEmpty(roles)) {
            return WebResponse.success(new ArrayList<>());
        }
        Set<String> menuRedisKeys = new HashSet<>();
        Set<String> buttonRedisKeys = new HashSet<>();
        for(int i = 0; i < roles.size(); i ++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            menuRedisKeys.add(RedisKeyPrefix.ROLE_MENU + role.getId());
            buttonRedisKeys.add(RedisKeyPrefix.ROLE_BUTTON + role.getId());
        }

        if(buttonRedisKeys.size() > 0) {
            List<String> buttonsString = stringRedisTemplate.opsForValue().multiGet(buttonRedisKeys);
            if(!CollectionUtils.isEmpty(buttonsString)) {
                for(String buttonsStr : buttonsString) {
                    SysButtonEntity[] buttons = JSONArray.parseObject(buttonsStr, SysButtonEntity[].class);
                    for(SysButtonEntity button : buttons) {
                        if(!parentMenuButtonMap.containsKey(button.getParentMenu().getId())) {
                            parentMenuButtonMap.put(button.getParentMenu().getId(), new HashMap<>());
                        }
                        if(!CollectionUtils.isEmpty(button.getAuthorityList())) {
                            button.setAuthorityList(null);
                        }
                        parentMenuButtonMap.get(button.getParentMenu().getId()).put(button.getId(), button);
                    }
                }
            }
        }

        if(menuRedisKeys.size() > 0) {
            List<String> menusString = stringRedisTemplate.opsForValue().multiGet(menuRedisKeys);
            if(CollectionUtils.isEmpty(menusString)) {
                return WebResponse.success(new ArrayList<>());
            }
            for(String menusStr : menusString) {
                SysMenuEntity[] menus = JSONArray.parseObject(menusStr, SysMenuEntity[].class);
                for(SysMenuEntity menu : menus) {
                    if(parentMenuButtonMap.containsKey(menu.getId())) {
                        menu.setButtonList(new ArrayList<>(parentMenuButtonMap.get(menu.getId()).values()));
                    }
                    menuMap.put(menu.getId(), menu);
                }
            }
        }

        List<SysMenuEntity> menuList = new ArrayList<>(menuMap.values());
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
