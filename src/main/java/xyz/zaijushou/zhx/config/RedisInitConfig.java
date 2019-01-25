package xyz.zaijushou.zhx.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.models.auth.In;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.*;

import javax.annotation.Resource;
import java.util.*;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.utils.CollectionsUtils;

@Component
public class RedisInitConfig implements ApplicationRunner {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysButtonService sysButtonService;

    @Resource
    private SysAuthorityService sysAuthorityService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<SysUserEntity> allUser = sysUserService.listAllUsers(new SysUserEntity());

        List<SysRoleEntity> allRole = sysRoleService.listAllRoles(new SysRoleEntity());

        List<SysMenuEntity> allMenu = sysMenuService.listAllMenus(new SysMenuEntity());

        List<SysButtonEntity> allButton = sysButtonService.listAllButtons(new SysButtonEntity());

        List<SysAuthorityEntity> allAuthority = sysAuthorityService.listAllAuthorities(new SysAuthorityEntity());

        List<SysToUserRole> userRoles = sysUserService.listAllUserRoles(new SysToUserRole());

        List<SysToRoleMenu> roleMenus = sysRoleService.listAllRoleMenus(new SysToRoleMenu());

        List<SysToRoleButton> roleButtons = sysRoleService.listAllRoleButtons(new SysToRoleButton());

        List<SysToButtonAuthority> buttonAuthorities = sysButtonService.listAllButtonAuthorities(new SysToButtonAuthority());

        initUserInfo(allUser);
        initRoleInfo(allRole);
        initMenuInfo(allMenu);
        initButtonInfo(allButton);
        initAuthorityInfo(allAuthority);
        initUserRole(allRole, userRoles);
        Map<Integer, SysRoleEntity> roleMap = initRoleMenu(allRole, allMenu, roleMenus);
        roleMap = initRoleButton(roleMap, allButton, roleButtons);
        initRoleAuthority(roleMap, allButton, allAuthority, buttonAuthorities);

    }

    private Map<Integer, SysRoleEntity> initRoleAuthority(Map<Integer, SysRoleEntity> roleMap, List<SysButtonEntity> allButton, List<SysAuthorityEntity> allAuthority, List<SysToButtonAuthority> buttonAuthorities) {
        Map<Integer, Map<Integer, SysButtonEntity>> roleButtonMap = new HashMap<>();
        for(Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            roleButtonMap.put(entry.getKey(), CollectionsUtils.listToMap(entry.getValue().getButtons()));
        }

        Map<Integer, SysButtonEntity> buttonMap = CollectionsUtils.listToMap(allButton);
        Map<Integer, SysAuthorityEntity> authorityMap = CollectionsUtils.listToMap(allAuthority);
        Map<Integer, Map<Integer, SysAuthorityEntity>> buttonAuthorityMap = new HashMap<>();
        Iterator<SysToButtonAuthority> buttonAuthorityIterator = buttonAuthorities.iterator();
        while(buttonAuthorityIterator.hasNext()) {
            SysToButtonAuthority buttonAuthority = buttonAuthorityIterator.next();
            if(!buttonMap.containsKey(buttonAuthority.getButton().getId())) {
                buttonAuthorityIterator.remove();
                continue;
            }
            if(!authorityMap.containsKey(buttonAuthority.getAuthority().getId())) {
                buttonAuthorityIterator.remove();
                continue;
            }
            if(!buttonAuthorityMap.containsKey(buttonAuthority.getButton().getId())) {
                buttonAuthorityMap.put(buttonAuthority.getButton().getId(), new HashMap<>());
            }
            buttonAuthorityMap.get(buttonAuthority.getButton().getId()).put(buttonAuthority.getAuthority().getId(), authorityMap.get(buttonAuthority.getAuthority().getId()));
        }

        Map<Integer, Map<Integer, SysAuthorityEntity>> roleAuthorityMap = new HashMap<>();
        for(Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            roleAuthorityMap.put(entry.getKey(), new HashMap<>());
            SysRoleEntity role = entry.getValue();
            if(role.getAuthorities() == null) {
                role.setAuthorities(new ArrayList<>());
            }
            if(!CollectionUtils.isEmpty(role.getButtons())) {
                for(SysButtonEntity button : role.getButtons()) {
                    roleAuthorityMap.get(entry.getKey()).putAll(buttonAuthorityMap.get(button.getId()));
                }

            }
        }
        deleteKeys(RedisKeyPrefix.ROLE_AUTHORITY);
        for(Map.Entry<Integer, Map<Integer, SysAuthorityEntity>> entry : roleAuthorityMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_AUTHORITY + entry.getKey(), JSONArray.toJSONString(entry.getValue().values()));
            roleMap.get(entry.getKey()).setAuthorities(new ArrayList<>(entry.getValue().values()));
        }
        return roleMap;
    }

    private Map<Integer, SysRoleEntity> initRoleButton(Map<Integer, SysRoleEntity> roleMap, List<SysButtonEntity> allButton, List<SysToRoleButton> roleButtons) {
        Map<Integer, Map<Integer, SysMenuEntity>> roleMenuMap = new HashMap<>();
        for(Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            roleMenuMap.put(entry.getKey(), CollectionsUtils.listToMap(entry.getValue().getMenus()));
        }

        Map<Integer, SysButtonEntity> buttonMap = CollectionsUtils.listToMap(allButton);
        Iterator<SysToRoleButton> roleButtonIterator = roleButtons.iterator();
        while(roleButtonIterator.hasNext()) {
            SysToRoleButton roleButton = roleButtonIterator.next();
            if(!roleMap.containsKey(roleButton.getRole().getId())) {
                roleButtonIterator.remove();
                continue;
            }
            if(!buttonMap.containsKey(roleButton.getButton().getId())) {
                roleButtonIterator.remove();
                continue;
            }
            SysButtonEntity button = buttonMap.get(roleButton.getButton().getId());
            if(!roleMenuMap.get(roleButton.getRole().getId()).containsKey(button.getParentMenu().getId())) {
                roleButtonIterator.remove();
                continue;
            }
            if(roleMap.get(roleButton.getRole().getId()).getButtons() == null) {
                roleMap.get(roleButton.getRole().getId()).setButtons(new ArrayList<>());
            }
            roleMap.get(roleButton.getRole().getId()).getButtons().add(button);
        }

        deleteKeys(RedisKeyPrefix.ROLE_BUTTON);
        for(Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_BUTTON + entry.getKey(), JSONArray.toJSONString(entry.getValue().getButtons()));
        }

        return roleMap;
    }

    private Map<Integer, SysRoleEntity> initRoleMenu(List<SysRoleEntity> allRole, List<SysMenuEntity> allMenu, List<SysToRoleMenu> roleMenus) {

        Map<Integer, SysRoleEntity> roleMap = CollectionsUtils.listToMap(allRole);
        Map<Integer, SysMenuEntity> menuMap = CollectionsUtils.listToMap(allMenu);
        Iterator<SysToRoleMenu> roleMenuIterator = roleMenus.iterator();
        while(roleMenuIterator.hasNext()) {
            SysToRoleMenu roleMenu = roleMenuIterator.next();
            if(!roleMap.containsKey(roleMenu.getRole().getId())) {
                roleMenuIterator.remove();
                continue;
            }
            if(!menuMap.containsKey(roleMenu.getMenu().getId())) {
                roleMenuIterator.remove();
            }
        }

        for(SysToRoleMenu roleMenu : roleMenus) {
            if(roleMap.get(roleMenu.getRole().getId()).getMenus() == null) {
                roleMap.get(roleMenu.getRole().getId()).setMenus(new ArrayList<>());
            }
            roleMap.get(roleMenu.getRole().getId()).getMenus().add(menuMap.get(roleMenu.getMenu().getId()));
        }

        deleteKeys(RedisKeyPrefix.ROLE_MENU);
        for(Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_MENU + entry.getKey(), JSONArray.toJSONString(entry.getValue().getMenus()));
        }
        return roleMap;
    }

    private void initUserRole(List<SysRoleEntity> allRole, List<SysToUserRole> userRoles) {
        Map<Integer, SysRoleEntity> roleMap = new HashMap<>();
        for(SysRoleEntity role : allRole) {
            roleMap.put(role.getId(), role);
        }
        Map<Integer, SysUserEntity> userRolesMap = new HashMap<>();
        for(SysToUserRole userRole : userRoles) {
            if(!userRolesMap.containsKey(userRole.getUser().getId())) {
                SysUserEntity user = new SysUserEntity();
                user.setId(userRole.getUser().getId());
                user.setRoles(new ArrayList<>());
                userRolesMap.put(userRole.getUser().getId(), user);
            }
            userRolesMap.get(userRole.getUser().getId()).getRoles().add(roleMap.get(userRole.getRole().getId()));
        }
        deleteKeys(RedisKeyPrefix.USER_ROLE);
        for(Map.Entry<Integer, SysUserEntity> entry : userRolesMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_ROLE + entry.getValue().getId(), JSONArray.toJSONString(entry.getValue().getRoles()));
        }
    }

    private void initUserInfo(List<SysUserEntity> allUser) {
        refreshCommonEntity(allUser, RedisKeyPrefix.USER_INFO);
    }

    private void initRoleInfo(List<SysRoleEntity> allRole) {
        refreshCommonEntity(allRole, RedisKeyPrefix.ROLE_INFO);
    }

    private void initMenuInfo(List<SysMenuEntity> allMenu) {
        refreshCommonEntity(allMenu, RedisKeyPrefix.MENU_INFO);
    }

    private void initButtonInfo(List<SysButtonEntity> allButton) {
        refreshCommonEntity(allButton, RedisKeyPrefix.BUTTON_INFO);
    }

    private void initAuthorityInfo(List<SysAuthorityEntity> allAuthority) {
        refreshCommonEntity(allAuthority, RedisKeyPrefix.AUTHORITY_INFO);
    }

    private void refreshCommonEntity(List list, String redisKeyPrefix) {
        deleteKeys(redisKeyPrefix);
        for(Object object : list) {
            CommonEntity commonEntity = (CommonEntity) object;
            stringRedisTemplate.opsForValue().set(redisKeyPrefix + commonEntity.getId(), JSONObject.toJSONString(object));
        }
    }

    private void deleteKeys(String redisKeyPrefix) {
        Set<String> keys = new HashSet<>();
        ScanOptions options = ScanOptions.scanOptions().match(redisKeyPrefix + "*").count(Integer.MAX_VALUE).build();
        Cursor cursor = stringRedisTemplate.getConnectionFactory().getConnection().scan(options);
        while (cursor.hasNext()) {
            keys.add(new String((byte[])cursor.next()));
        }
        stringRedisTemplate.delete(keys);
    }

}
