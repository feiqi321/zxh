package xyz.zaijushou.zhx.config;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

        List<SysToButtonAuthority> buttonAuthorities = sysButtonService.listAllButtonAuthorities(new SysToButtonAuthority());

        initUserInfo(allUser);
        initRoleInfo(allRole);
        initMenuInfo(allMenu);
        initButtonInfo(allButton);
        initAuthorityInfo(allAuthority);
        initUserRole(allRole, userRoles);
        initRoleAuthority(allRole, allMenu, allAuthority, roleMenus, allButton, buttonAuthorities);
    }

    private void initRoleAuthority(List<SysRoleEntity> allRole, List<SysMenuEntity> allMenu, List<SysAuthorityEntity> allAuthority, List<SysToRoleMenu> roleMenus, List<SysButtonEntity> allButton, List<SysToButtonAuthority> buttonAuthorities) {

        Map<Integer, SysRoleEntity> roleMap = associatedRoleAuthoriry(allRole, allMenu, allAuthority, roleMenus, allButton, buttonAuthorities);

        Map<Integer, Set<SysAuthorityEntity>> roleAuthoryMap = combineRoleAuthorities(roleMap);

        deleteKeys(RedisKeyPrefix.ROLE_AUTHORITY);
        for(Map.Entry<Integer, Set<SysAuthorityEntity>> entry : roleAuthoryMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_AUTHORITY + entry.getKey(), JSONArray.toJSONString(entry.getValue()));
        }
    }

    private Map<Integer, Set<SysAuthorityEntity>> combineRoleAuthorities(Map<Integer, SysRoleEntity> roleMap) {
        Map<Integer, Set<SysAuthorityEntity>> roleAuthorityMap = new HashMap<>();

        for(Map.Entry<Integer, SysRoleEntity> roleEntry : roleMap.entrySet()) {
            Map<Integer, SysAuthorityEntity> authorityMap = new HashMap<>();
            if(!CollectionUtils.isEmpty(roleEntry.getValue().getMenus())) {
                for(SysMenuEntity menu : roleEntry.getValue().getMenus()) {
                    if(CollectionUtils.isEmpty(menu.getButtonList())) {
                        continue;
                    }
                    for(SysButtonEntity button : menu.getButtonList()) {
                        if(CollectionUtils.isEmpty(button.getAuthorityList())) {
                            continue;
                        }
                        for(SysAuthorityEntity authority : button.getAuthorityList()) {
                            authorityMap.put(authority.getId(), authority);
                        }
                    }
                }
            }
            roleAuthorityMap.put(roleEntry.getKey(), new HashSet<>(authorityMap.values()));
        }
        return roleAuthorityMap;
    }

    private Map<Integer, SysRoleEntity> associatedRoleAuthoriry(List<SysRoleEntity> allRole, List<SysMenuEntity> allMenu, List<SysAuthorityEntity> allAuthority, List<SysToRoleMenu> roleMenus, List<SysButtonEntity> allButton, List<SysToButtonAuthority> buttonAuthorities) {
        Map<Integer, SysAuthorityEntity> authorityMap = new HashMap<>();
        for(SysAuthorityEntity authority : allAuthority) {
            authorityMap.put(authority.getId(), authority);
        }

        Map<Integer, SysButtonEntity> buttonMap = new HashMap<>();
        for(SysButtonEntity button : allButton) {
            button.setAuthorityList(new ArrayList<>());
            buttonMap.put(button.getId(), button);
        }
        for(SysToButtonAuthority buttonAuthority : buttonAuthorities) {
            if(!buttonMap.containsKey(buttonAuthority.getButton().getId())) {
                continue;
            }
            buttonMap.get(buttonAuthority.getButton().getId()).getAuthorityList().add(authorityMap.get(buttonAuthority.getAuthority().getId()));
        }

        Map<Integer, SysMenuEntity> menuMap = new HashMap<>();
        for(SysMenuEntity menu : allMenu) {
            menu.setButtonList(new ArrayList<>());
            menuMap.put(menu.getId(), menu);
        }

        for(Map.Entry<Integer, SysButtonEntity> entry : buttonMap.entrySet()) {
            SysButtonEntity button = entry.getValue();
            if(!menuMap.containsKey(button.getParentMenu().getId())) {
                continue;
            }
            menuMap.get(button.getParentMenu().getId()).getButtonList().add(button);
        }

        Map<Integer, SysRoleEntity> roleMap = new HashMap<>();
        for(SysRoleEntity role : allRole) {
            role.setMenus(new ArrayList<>());
            roleMap.put(role.getId(), role);
        }
        for(SysToRoleMenu roleMenu : roleMenus) {
            if(!roleMap.containsKey(roleMenu.getRole().getId())) {
                continue;
            }
            roleMap.get(roleMenu.getRole().getId()).getMenus().add(menuMap.get(roleMenu.getMenu().getId()));
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
