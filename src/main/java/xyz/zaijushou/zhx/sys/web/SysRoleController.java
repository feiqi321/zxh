package xyz.zaijushou.zhx.sys.web;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.SysButtonEntity;
import xyz.zaijushou.zhx.sys.entity.SysMenuEntity;
import xyz.zaijushou.zhx.sys.entity.SysRoleEntity;
import xyz.zaijushou.zhx.sys.service.SysRoleService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/role")
public class SysRoleController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SysRoleService sysRoleService;

    /**
     * 权限列表，管理员返回所有权限，非管理员返回所拥有的权限
     *
     * @param roleEntity
     * @return
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('sys_role_list')")
    @PostMapping("/listRole")
    public Object listRole(@RequestBody SysRoleEntity roleEntity) {

        List<SysRoleEntity> list = new ArrayList<>();
        JSONObject redisJson = JwtTokenUtil.tokenData();
        String userRolesString = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.USER_ROLE + redisJson.getInteger("userId"));
        JSONArray roles = JSONArray.parseArray(userRolesString);
        if (CollectionUtils.isEmpty(roles)) {
            return WebResponse.success(list);
        }
        boolean isAdmin = false;
        for (int i = 0; i < roles.size(); i++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            if ("ROLE_ADMIN".equals(role.getRoleAuthSymbol())) {
                isAdmin = true;
                break;
            }
            if (roleEntity == null || (StringUtils.isNoneBlank(roleEntity.getRoleName()) && role.getRoleName().contains(roleEntity.getRoleName()))) {
                list.add(role);
            }
        }
        if (isAdmin) {
            list = sysRoleService.listRoles(roleEntity);
        }
        CollectionsUtils.userInfoSet(list);
        return WebResponse.success(list);
    }

    /**
     * 新增或修改权限
     *
     * @param roleEntity
     * @return
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('sys_role_save')")
    @PostMapping("/save")
    public Object save(@RequestBody SysRoleEntity roleEntity) {
        if (roleEntity == null || StringUtils.isBlank(roleEntity.getRoleName())) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "角色名称未填写");
        }
        SysRoleEntity queryRole = sysRoleService.selectByRoleName(roleEntity);
        if (queryRole != null) {
            if (!queryRole.getId().equals(roleEntity.getId())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "角色名已存在");
            }
        }

        sysRoleService.saveRole(roleEntity);

        return WebResponse.success(roleEntity);
    }

    /**
     * 删除权限
     *
     * @param roleEntity
     * @return
     */
    @PreAuthorize("hasRole('ADMIN') or hasAuthority('sys_role_delete')")
    @PostMapping("/delete")
    public Object delete(@RequestBody SysRoleEntity roleEntity) {
        if (roleEntity == null || roleEntity.getId() == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "角色id未传");
        }
        SysRoleEntity queryRole = RedisUtils.entityGet(RedisKeyPrefix.ROLE_INFO + roleEntity.getId(), SysRoleEntity.class);
        if(queryRole == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "上传角色id有误");
        }
        if("系统管理员 催收员 项目经理 项目总监".contains(queryRole.getRoleName())) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "系统管理员 催收员 项目经理 项目总监 角色不得删除");
        }
        sysRoleService.deleteRole(roleEntity);
        sysRoleService.refreshRoleRedis();
        return WebResponse.success();
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('sys_role_auth')")
    @PostMapping("/auth")
    public WebResponse auth(@RequestBody SysRoleEntity roleEntity) {
        if (roleEntity == null || roleEntity.getId() == null || roleEntity.getMenus() == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "角色id未传或角色权限为空");
        }
        if(roleEntity.getId() == 1) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "系统管理员角色权限不得修改");
        }
        JSONObject redisJson = JwtTokenUtil.tokenData();
        String userRolesString = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.USER_ROLE + redisJson.getInteger("userId"));
        JSONArray roles = JSONArray.parseArray(userRolesString);
        if (CollectionUtils.isEmpty(roles)) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "角色权限不足，不能对该角色进行授权");
        }

        Map<Integer, SysMenuEntity> menuMap = new ConcurrentHashMap<>();
        Set<String> menuRedisKeys = new HashSet<>();
        for (int i = 0; i < roles.size(); i++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            menuRedisKeys.add(RedisKeyPrefix.ROLE_MENU + role.getId());
        }
        if (menuRedisKeys.size() > 0) {
            List<String> menusString = stringRedisTemplate.opsForValue().multiGet(menuRedisKeys);
            if (CollectionUtils.isEmpty(menusString)) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "菜单权限不足，不能对该角色进行授权");
            }
            for (String menusStr : menusString) {
                SysMenuEntity[] menus = JSONArray.parseObject(menusStr, SysMenuEntity[].class);
                for (SysMenuEntity menu : menus) {
                    menuMap.put(menu.getId(), menu);
                }
            }
        }
        if (CollectionUtils.isEmpty(menuMap)) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "菜单权限不足，不能对该角色进行授权");
        }

        Set<String> buttonRedisKeys = new HashSet<>();
        for (int i = 0; i < roles.size(); i++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            buttonRedisKeys.add(RedisKeyPrefix.ROLE_BUTTON + role.getId());
        }

        Map<Integer, SysButtonEntity> buttonMap = new HashMap<>();
        if (buttonRedisKeys.size() > 0) {
            List<String> buttonsString = stringRedisTemplate.opsForValue().multiGet(buttonRedisKeys);
            if (!CollectionUtils.isEmpty(buttonsString)) {
                for (String buttonsStr : buttonsString) {
                    SysButtonEntity[] buttons = JSONArray.parseObject(buttonsStr, SysButtonEntity[].class);
                    if (buttons == null || buttons.length == 0) {
                        continue;
                    }
                    for (SysButtonEntity button : buttons) {
                        buttonMap.put(button.getId(), button);
                    }
                }
            }
        }

        List<SysMenuEntity> changeMenuList = CollectionsUtils.treeToList(roleEntity.getMenus());
        changeMenuList = new ArrayList<>(parentSelect(changeMenuList).values());

        List<SysButtonEntity> changeButtonList = new ArrayList<>();
        List<SysMenuEntity> addMenusList = new ArrayList<>();
        for (SysMenuEntity menu : changeMenuList) {
            if (!menuMap.containsKey(menu.getId())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "菜单权限不足,不能对该角色进行授权");
            }
            if (!CollectionUtils.isEmpty(menu.getButtonList())) {
                changeButtonList.addAll(menu.getButtonList());
            }
            if (menu.isSelect()) {
                addMenusList.add(menu);
            }
        }
        List<SysButtonEntity> addButtonsList = new ArrayList<>();
        for (SysButtonEntity button : changeButtonList) {
            if (!buttonMap.containsKey(button.getId())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "按钮权限不足,不能对该角色进行授权");
            }
            if (button.isSelect()) {
                addButtonsList.add(button);
            }
        }

        //更新菜单权限
        roleEntity.setMenus(addMenusList);
        sysRoleService.deleteRoleMenus(roleEntity);
        if (!CollectionUtils.isEmpty(roleEntity.getMenus())) {
            sysRoleService.saveRoleMenus(roleEntity);
        }

        //更新按钮权限
        roleEntity.setButtons(addButtonsList);
        sysRoleService.deleteRoleButtons(roleEntity);

        if (!CollectionUtils.isEmpty(roleEntity.getButtons())) {
            sysRoleService.saveRoleButtons(roleEntity);
        }

        sysRoleService.refreshRoleRedis();

        return WebResponse.success();
    }

    private Map<Integer, SysMenuEntity> parentSelect(List<SysMenuEntity> changeMenuList) {
        Map<Integer, SysMenuEntity> menuMap = new ConcurrentHashMap<>(CollectionsUtils.listToMap(changeMenuList));
        if(!menuMap.containsKey(1)) {
            SysMenuEntity index = new SysMenuEntity();
            index.setId(1);
            SysMenuEntity parent = new SysMenuEntity();
            parent.setId(0);
            index.setParent(parent);
            menuMap.put(1, index);
        }
        for(Map.Entry<Integer, SysMenuEntity> entry : menuMap.entrySet()) {
            SysMenuEntity menu = entry.getValue();
            if(new Integer(1).equals(menu.getId())) {
                menuMap.get(menu.getId()).setSelect(true);
                continue;
            }
            if(menu.isSelect()) {
                while (menu.getParent() != null && menu.getParent().getId() != null && menu.getParent().getId() != 0 && !menu.getId().equals(menu.getParent().getId())) {
                    menuMap.get(menu.getParent().getId()).setSelect(true);
                    menu = menuMap.get(menu.getParent().getId());
                }
            }
        }
        return menuMap;
    }

    @PreAuthorize("hasRole('ADMIN') or hasAuthority('sys_role_auth')")
    @PostMapping("/listAuth")
    public Object listAuth(@RequestBody SysRoleEntity roleEntity) {
        JSONObject redisJson = JwtTokenUtil.tokenData();
        String userRolesString = stringRedisTemplate.opsForValue().get(RedisKeyPrefix.USER_ROLE + redisJson.getInteger("userId"));
        JSONArray roles = JSONArray.parseArray(userRolesString);
        if (CollectionUtils.isEmpty(roles)) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "角色权限不足,不能对该角色进行授权");
        }

        SysMenuEntity[] selectMenus = null;
        if (roleEntity != null && roleEntity.getId() != null) {
            selectMenus = JSONArray.parseObject(stringRedisTemplate.opsForValue().get(RedisKeyPrefix.ROLE_MENU + roleEntity.getId()), SysMenuEntity[].class);
        }
        List<Integer> selectMenuIds = new ArrayList<>();
        if (selectMenus != null && selectMenus.length > 0) {
            for (SysMenuEntity menu : selectMenus) {
                selectMenuIds.add(menu.getId());
            }
        }

        Map<Integer, SysMenuEntity> menuMap = new ConcurrentHashMap<>();
        Set<String> menuRedisKeys = new HashSet<>();
        for (int i = 0; i < roles.size(); i++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            menuRedisKeys.add(RedisKeyPrefix.ROLE_MENU + role.getId());
        }
        if (menuRedisKeys.size() > 0) {
            List<String> menusString = stringRedisTemplate.opsForValue().multiGet(menuRedisKeys);
            if (CollectionUtils.isEmpty(menusString)) {
                return menuMap;
            }
            for (String menusStr : menusString) {
                SysMenuEntity[] menus = JSONArray.parseObject(menusStr, SysMenuEntity[].class);
                for (SysMenuEntity menu : menus) {
                    if (selectMenuIds.contains(menu.getId()) || menu.getId() == 1) {
                        menu.setSelect(true);
                    }
                    menuMap.put(menu.getId(), menu);
                }
            }
        }
        if (CollectionUtils.isEmpty(menuMap)) {
            return WebResponse.success(new ArrayList<>());
        }

        SysButtonEntity[] selectButtons = null;
        if (roleEntity != null && roleEntity.getId() != null) {
            selectButtons = JSONArray.parseObject(stringRedisTemplate.opsForValue().get(RedisKeyPrefix.ROLE_BUTTON + roleEntity.getId()), SysButtonEntity[].class);
        }

        List<Integer> selectButtonIds = new ArrayList<>();
        if (selectButtons != null && selectButtons.length > 0) {
            for (SysButtonEntity button : selectButtons) {
                selectButtonIds.add(button.getId());
            }
        }

        Set<String> buttonRedisKeys = new HashSet<>();
        for (int i = 0; i < roles.size(); i++) {
            SysRoleEntity role = JSONObject.parseObject(roles.getString(i), SysRoleEntity.class);
            buttonRedisKeys.add(RedisKeyPrefix.ROLE_BUTTON + role.getId());
        }

        Map<Integer, SysButtonEntity> buttonMap = new HashMap<>();
        if (buttonRedisKeys.size() > 0) {
            List<String> buttonsString = stringRedisTemplate.opsForValue().multiGet(buttonRedisKeys);
            if (!CollectionUtils.isEmpty(buttonsString)) {
                for (String buttonsStr : buttonsString) {
                    SysButtonEntity[] buttons = JSONArray.parseObject(buttonsStr, SysButtonEntity[].class);
                    if (buttons == null || buttons.length == 0) {
                        continue;
                    }
                    for (SysButtonEntity button : buttons) {
                        if (selectButtonIds.contains(button.getId())) {
                            button.setSelect(true);
                        }
                        buttonMap.put(button.getId(), button);
                    }
                }
            }
            for (Map.Entry<Integer, SysButtonEntity> entry : buttonMap.entrySet()) {
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

}
