package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysButtonMapper;
import xyz.zaijushou.zhx.sys.dao.SysRoleMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.SysRoleService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysButtonMapper sysButtonMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<SysRoleEntity> listAllRoles(SysRoleEntity sysRoleEntity) {
        return sysRoleMapper.listAllRoles(sysRoleEntity);
    }

    @Override
    public List<SysToRoleMenu> listAllRoleMenus(SysToRoleMenu sysToRoleMenu) {
        return sysRoleMapper.listAllRoleMenus(sysToRoleMenu);
    }

    @Override
    public List<SysToRoleButton> listAllRoleButtons(SysToRoleButton sysToRoleButton) {
        return sysRoleMapper.listAllRoleButtons(sysToRoleButton);
    }

    @Override
    public void updateRole(SysRoleEntity roleEntity) {
        sysRoleMapper.updateRole(roleEntity);
    }

    @Override
    public void saveRole(SysRoleEntity roleEntity) {

        if (roleEntity.getId() != null) {
            sysRoleMapper.updateRole(roleEntity);
        } else {
            sysRoleMapper.saveRole(roleEntity);
        }
    }

    @Override
    public void deleteRole(SysRoleEntity roleEntity) {
        sysRoleMapper.deleteRole(roleEntity);
    }

    @Override
    public SysRoleEntity selectByRoleName(SysRoleEntity roleEntity) {
        return sysRoleMapper.selectByRoleName(roleEntity);
    }

    @Override
    public void deleteRoleMenus(SysRoleEntity roleEntity) {
        sysRoleMapper.deleteRoleMenus(roleEntity);
    }

    @Override
    public void saveRoleMenus(SysRoleEntity roleEntity) {
        sysRoleMapper.saveRoleMenus(roleEntity);
    }

    @Override
    public void deleteRoleButtons(SysRoleEntity roleEntity) {
        sysRoleMapper.deleteRoleButtons(roleEntity);
    }

    @Override
    public void saveRoleButtons(SysRoleEntity roleEntity) {
        sysRoleMapper.saveRoleButtons(roleEntity);
    }

    @Override
    public List<SysRoleEntity> listRoles(SysRoleEntity roleEntity) {
        return sysRoleMapper.listRoles(roleEntity);
    }

    @Override
    public void refreshRoleRedis() {

        List<SysRoleEntity> allRole = listAllRoles(new SysRoleEntity());

        refreshRoleInfo(allRole);

        refreshUserRole(allRole);
        Map<Integer, SysRoleEntity> roleMap = refreshRoleMenu(allRole);

        List<SysButtonEntity> allButton = RedisUtils.scanEntityWithKeyPrefix(RedisKeyPrefix.BUTTON_INFO, SysButtonEntity.class);
        roleMap = initRoleButton(roleMap, allButton);
        initRoleAuthority(roleMap, allButton);
    }

    private void refreshRoleInfo(List<SysRoleEntity> allRole) {
        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.ROLE_INFO);
        RedisUtils.refreshCommonEntityWithId(allRole, RedisKeyPrefix.ROLE_INFO);
    }

    private void refreshUserRole(List<SysRoleEntity> allRole) {
        Map<Integer, SysRoleEntity> roleMap = new HashMap<>();
        for (SysRoleEntity role : allRole) {
            roleMap.put(role.getId(), role);
        }

        List<SysToUserRole> userRoles = sysUserMapper.listAllUserRoles(new SysToUserRole());

        Map<Integer, SysUserEntity> userRolesMap = new HashMap<>();
        for (SysToUserRole userRole : userRoles) {
            if (!userRolesMap.containsKey(userRole.getUser().getId())) {
                SysUserEntity user = new SysUserEntity();
                user.setId(userRole.getUser().getId());
                user.setRoles(new ArrayList<>());
                userRolesMap.put(userRole.getUser().getId(), user);
            }
            userRolesMap.get(userRole.getUser().getId()).getRoles().add(roleMap.get(userRole.getRole().getId()));
        }
        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.USER_ROLE);
        for (Map.Entry<Integer, SysUserEntity> entry : userRolesMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_ROLE + entry.getValue().getId(), entry.getValue().getRoles() == null ? new JSONArray().toJSONString() : JSONArray.toJSONString(entry.getValue().getRoles()));
        }
    }

    private Map<Integer, SysRoleEntity> initRoleAuthority(Map<Integer, SysRoleEntity> roleMap, List<SysButtonEntity> allButton) {

        Map<Integer, SysButtonEntity> buttonMap = CollectionsUtils.listToMap(allButton);

        List<SysToButtonAuthority> buttonAuthorities = sysButtonMapper.listAllButtonAuthorities(new SysToButtonAuthority());

        List<SysAuthorityEntity> allAuthority = RedisUtils.scanEntityWithKeyPrefix(RedisKeyPrefix.AUTHORITY_INFO, SysAuthorityEntity.class);
        Map<Integer, SysAuthorityEntity> authorityMap = CollectionsUtils.listToMap(allAuthority);
        Map<Integer, Map<Integer, SysAuthorityEntity>> buttonAuthorityMap = new HashMap<>();
        Iterator<SysToButtonAuthority> buttonAuthorityIterator = buttonAuthorities.iterator();
        while (buttonAuthorityIterator.hasNext()) {
            SysToButtonAuthority buttonAuthority = buttonAuthorityIterator.next();
            if (!buttonMap.containsKey(buttonAuthority.getButton().getId())) {
                buttonAuthorityIterator.remove();
                continue;
            }
            if (!authorityMap.containsKey(buttonAuthority.getAuthority().getId())) {
                buttonAuthorityIterator.remove();
                continue;
            }
            if (!buttonAuthorityMap.containsKey(buttonAuthority.getButton().getId())) {
                buttonAuthorityMap.put(buttonAuthority.getButton().getId(), new HashMap<>());
            }
            buttonAuthorityMap.get(buttonAuthority.getButton().getId()).put(buttonAuthority.getAuthority().getId(), authorityMap.get(buttonAuthority.getAuthority().getId()));
        }

        Map<Integer, Map<Integer, SysAuthorityEntity>> roleAuthorityMap = new HashMap<>();
        for (Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            roleAuthorityMap.put(entry.getKey(), new HashMap<>());
            SysRoleEntity role = entry.getValue();
            if (role.getAuthorities() == null) {
                role.setAuthorities(new ArrayList<>());
            }
            if (!CollectionUtils.isEmpty(role.getButtons())) {
                for (SysButtonEntity button : role.getButtons()) {
                    if (buttonAuthorityMap.get(button.getId()) != null) {
                        roleAuthorityMap.get(entry.getKey()).putAll(buttonAuthorityMap.get(button.getId()));
                    }
                }

            }
        }
        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.ROLE_AUTHORITY);
        for (Map.Entry<Integer, Map<Integer, SysAuthorityEntity>> entry : roleAuthorityMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_AUTHORITY + entry.getKey(), JSONArray.toJSONString(entry.getValue().values()));
            roleMap.get(entry.getKey()).setAuthorities(new ArrayList<>(entry.getValue().values()));
        }
        return roleMap;
    }

    private Map<Integer, SysRoleEntity> initRoleButton(Map<Integer, SysRoleEntity> roleMap, List<SysButtonEntity> allButton) {
        Map<Integer, Map<Integer, SysMenuEntity>> roleMenuMap = new HashMap<>();
        for (Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            roleMenuMap.put(entry.getKey(), CollectionsUtils.listToMap(entry.getValue().getMenus()));
        }

        Map<Integer, SysButtonEntity> buttonMap = CollectionsUtils.listToMap(allButton);

        List<SysToRoleButton> roleButtons = listAllRoleButtons(new SysToRoleButton());
        Iterator<SysToRoleButton> roleButtonIterator = roleButtons.iterator();
        while (roleButtonIterator.hasNext()) {
            SysToRoleButton roleButton = roleButtonIterator.next();
            if (!roleMap.containsKey(roleButton.getRole().getId())) {
                roleButtonIterator.remove();
                continue;
            }
            if (!buttonMap.containsKey(roleButton.getButton().getId())) {
                roleButtonIterator.remove();
                continue;
            }
            SysButtonEntity button = buttonMap.get(roleButton.getButton().getId());
            if (!roleMenuMap.get(roleButton.getRole().getId()).containsKey(button.getParentMenu().getId())) {
                roleButtonIterator.remove();
                continue;
            }
            if (roleMap.get(roleButton.getRole().getId()).getButtons() == null) {
                roleMap.get(roleButton.getRole().getId()).setButtons(new ArrayList<>());
            }
            roleMap.get(roleButton.getRole().getId()).getButtons().add(button);
        }

        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.ROLE_BUTTON);
        for (Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_BUTTON + entry.getKey(), entry.getValue().getButtons() == null ? new JSONArray().toJSONString() : JSONArray.toJSONString(entry.getValue().getButtons()));
        }

        return roleMap;
    }

    private Map<Integer, SysRoleEntity> refreshRoleMenu(List<SysRoleEntity> allRole) {

        Map<Integer, SysRoleEntity> roleMap = CollectionsUtils.listToMap(allRole);

        List<SysMenuEntity> allMenu = RedisUtils.scanEntityWithKeyPrefix(RedisKeyPrefix.MENU_INFO, SysMenuEntity.class);
        Map<Integer, SysMenuEntity> menuMap = CollectionsUtils.listToMap(allMenu);

        List<SysToRoleMenu> roleMenus = listAllRoleMenus(new SysToRoleMenu());
        Iterator<SysToRoleMenu> roleMenuIterator = roleMenus.iterator();
        while (roleMenuIterator.hasNext()) {
            SysToRoleMenu roleMenu = roleMenuIterator.next();
            if (!roleMap.containsKey(roleMenu.getRole().getId())) {
                roleMenuIterator.remove();
                continue;
            }
            if (!menuMap.containsKey(roleMenu.getMenu().getId())) {
                roleMenuIterator.remove();
            }
        }

        for (SysToRoleMenu roleMenu : roleMenus) {
            if (roleMap.get(roleMenu.getRole().getId()).getMenus() == null) {
                roleMap.get(roleMenu.getRole().getId()).setMenus(new ArrayList<>());
            }
            roleMap.get(roleMenu.getRole().getId()).getMenus().add(menuMap.get(roleMenu.getMenu().getId()));
        }

        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.ROLE_MENU);
        for (Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_MENU + entry.getKey(), entry.getValue().getMenus() == null ? new JSONArray().toJSONString() : JSONArray.toJSONString(entry.getValue().getMenus()));
        }
        return roleMap;
    }

}
