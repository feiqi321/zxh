package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysButtonMapper;
import xyz.zaijushou.zhx.sys.dao.SysRoleMapper;
import xyz.zaijushou.zhx.sys.dao.SysToUserRoleMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.SysAuthorityService;
import xyz.zaijushou.zhx.sys.service.SysButtonService;
import xyz.zaijushou.zhx.sys.service.SysMenuService;
import xyz.zaijushou.zhx.sys.service.SysRoleService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
public class SysRoleServiceImpl implements SysRoleService {
    private static Logger logger = LoggerFactory.getLogger(SysRoleServiceImpl.class);
    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysButtonMapper sysButtonMapper;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysToUserRoleMapper sysToUserRoleMapper;

    @Resource
    private SysButtonService sysButtonService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SysAuthorityService sysAuthorityService;

    @Override
    public List<SysRoleEntity> listAllRoles() {
        return sysRoleMapper.listAllRoles();
    }

    @Override
    public List<SysRoleEntity> listAllRolesByRoleId(SysRoleEntity sysRoleEntity) {
        return sysRoleMapper.listAllRolesByRoleId(sysRoleEntity);
    }

    @Override
    public List<SysToRoleMenu> listAllRoleMenus(SysToRoleMenu sysToRoleMenu) {
        return sysRoleMapper.listAllRoleMenus(sysToRoleMenu);
    }

    @Override
    public List<SysToRoleMenu> listAllRoleMenusByRoleId(SysToRoleMenu sysToRoleMenu) {
        return sysRoleMapper.listAllRoleMenusByRoleId(sysToRoleMenu);
    }

    @Override
    public List<SysToRoleButton> listAllRoleButtons(SysToRoleButton sysToRoleButton) {
        return sysRoleMapper.listAllRoleButtons(sysToRoleButton);
    }

    @Override
    public List<SysToRoleButton> listAllRoleButtonsByRoleId(SysToRoleButton sysToRoleButton) {
        return sysRoleMapper.listAllRoleButtonsByRoleId(sysToRoleButton);
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
        SysUserRoleEntity role = new SysUserRoleEntity();
        role.setRoleId(roleEntity.getId());
        sysToUserRoleMapper.deleteUserRoleByRole(role);
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
    @Async
    public void refreshRoleRedis() {
        try {
            List<SysRoleEntity> allRole = listAllRoles();
            refreshRoleInfo(allRole);
            refreshUserRole(allRole);
            Map<Integer, SysRoleEntity> roleMap = refreshRoleMenu(allRole);

            List<SysButtonEntity> allButton = RedisUtils.scanEntityWithKeyPrefix(RedisKeyPrefix.BUTTON_INFO, SysButtonEntity.class);
            roleMap = initRoleButton(roleMap, allButton);
            initRoleAuthority(roleMap, allButton);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void refreshCuurentRoleRedis(Integer roleId) {
        try {
            logger.info("查询权限信息");
            SysRoleEntity sysRoleEntity = new SysRoleEntity();
            sysRoleEntity.setId(roleId);
            List<SysRoleEntity> allRole = listAllRolesByRoleId(sysRoleEntity);
            logger.info("结束查询权限信息");
            refreshNewRoleInfo(allRole);
            logger.info("更新完redis里面的角色信息");
            Map<Integer, SysRoleEntity> roleMap = refreshNewRoleMenu(allRole);
            logger.info("更新完redis里面的菜单信息");
            //List<SysButtonEntity> allButton = RedisUtils.scanEntityWithKeyPrefix(RedisKeyPrefix.BUTTON_INFO, SysButtonEntity.class);
            List<SysButtonEntity> allButton = sysButtonService.listAllButtons(new SysButtonEntity());
            logger.info("redis查询按钮信息");
            roleMap = initNewRoleButton(roleMap, allButton,sysRoleEntity);
            logger.info("更新完redis里面的按钮信息");
            initNewRoleAuthority(roleMap, allButton);
            logger.info("更新完redis里面的最后信息");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void refreshRoleInfo(List<SysRoleEntity> allRole) {
        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.ROLE_INFO);
        RedisUtils.refreshCommonEntityWithId(allRole, RedisKeyPrefix.ROLE_INFO);
    }

    private void refreshNewRoleInfo(List<SysRoleEntity> allRole) {
        try {
            for (Object object : allRole) {
                CommonEntity commonEntity = (CommonEntity) object;
                stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_INFO + commonEntity.getId(), JSONObject.toJSONString(object));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
            if(roleMap.containsKey(userRole.getRole().getId())) {
                userRolesMap.get(userRole.getUser().getId()).getRoles().add(roleMap.get(userRole.getRole().getId()));
            }
        }
        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.USER_ROLE);

        // 将map转为list
        List<SysUserEntity> list = userRolesMap.values().stream().collect(Collectors.toList());
        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(20);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;

        for (int i = 0;i<list.size();i++) {

            SysUserEntity sysUserEntity = list.get(i);

            task = new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_ROLE + sysUserEntity.getId(), sysUserEntity.getRoles() == null ? new JSONArray().toJSONString() : JSONArray.toJSONString(sysUserEntity.getRoles()));
                    return 1;
                }
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }

        try {
            List<Future<Integer>> results = exec.invokeAll(tasks);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 关闭线程池
        exec.shutdown();
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

    private Map<Integer, SysRoleEntity> initNewRoleAuthority(Map<Integer, SysRoleEntity> roleMap, List<SysButtonEntity> allButton) {

        Map<Integer, SysButtonEntity> buttonMap = CollectionsUtils.listToMap(allButton);

        List<SysToButtonAuthority> buttonAuthorities = sysButtonMapper.listAllButtonAuthorities(new SysToButtonAuthority());

        logger.info("查询完毕按钮权限");

        //List<SysAuthorityEntity> allAuthority = RedisUtils.scanEntityWithKeyPrefix(RedisKeyPrefix.AUTHORITY_INFO, SysAuthorityEntity.class);
        List<SysAuthorityEntity> allAuthority = sysAuthorityService.listAllAuthorities(new SysAuthorityEntity());
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
        logger.info("组装完按钮权限数据");
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

    private Map<Integer, SysRoleEntity> initNewRoleButton(Map<Integer, SysRoleEntity> roleMap, List<SysButtonEntity> allButton,SysRoleEntity sysRoleEntity) {
        Map<Integer, Map<Integer, SysMenuEntity>> roleMenuMap = new HashMap<>();
        for (Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            roleMenuMap.put(entry.getKey(), CollectionsUtils.listToMap(entry.getValue().getMenus()));
        }

        Map<Integer, SysButtonEntity> buttonMap = CollectionsUtils.listToMap(allButton);
        SysToRoleButton sysToRoleButton = new SysToRoleButton();
        sysToRoleButton.setRole(sysRoleEntity);
        List<SysToRoleButton> roleButtons = listAllRoleButtonsByRoleId(sysToRoleButton);
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

    private Map<Integer, SysRoleEntity> refreshNewRoleMenu(List<SysRoleEntity> allRole) {

        Map<Integer, SysRoleEntity> roleMap = CollectionsUtils.listToMap(allRole);
        //List<SysMenuEntity> allMenu = RedisUtils.scanEntityWithKeyPrefix(RedisKeyPrefix.MENU_INFO, SysMenuEntity.class);
        List<SysMenuEntity> allMenu = sysMenuService.listAllMenus(new SysMenuEntity());
        Map<Integer, SysMenuEntity> menuMap = CollectionsUtils.listToMap(allMenu);
        SysToRoleMenu sysToRoleMenu = new SysToRoleMenu();
        sysToRoleMenu.setRole(allRole.get(0));
        List<SysToRoleMenu> roleMenus = listAllRoleMenusByRoleId(sysToRoleMenu);
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
        for (Map.Entry<Integer, SysRoleEntity> entry : roleMap.entrySet()) {
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.ROLE_MENU + entry.getKey(), entry.getValue().getMenus() == null ? new JSONArray().toJSONString() : JSONArray.toJSONString(entry.getValue().getMenus()));
        }
        return roleMap;
    }

}
