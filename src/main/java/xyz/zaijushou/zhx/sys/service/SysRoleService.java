package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysRoleEntity;
import xyz.zaijushou.zhx.sys.entity.SysToRoleButton;
import xyz.zaijushou.zhx.sys.entity.SysToRoleMenu;

import java.util.List;

public interface SysRoleService {
    List<SysRoleEntity> listAllRoles();

    List<SysRoleEntity> listAllRolesByRoleId(SysRoleEntity sysRoleEntity);

    List<SysToRoleMenu> listAllRoleMenus(SysToRoleMenu sysToRoleMenu);

    List<SysToRoleMenu> listAllRoleMenusByRoleId(SysToRoleMenu sysToRoleMenu);

    List<SysToRoleButton> listAllRoleButtons(SysToRoleButton sysToRoleButton);

    List<SysToRoleButton> listAllRoleButtonsByRoleId(SysToRoleButton sysToRoleButton);

    void updateRole(SysRoleEntity roleEntity);

    void refreshCuurentRoleRedis(Integer roleId);

    void saveRole(SysRoleEntity roleEntity);

    void deleteRole(SysRoleEntity roleEntity);

    SysRoleEntity selectByRoleName(SysRoleEntity roleEntity);

    void deleteRoleMenus(SysRoleEntity roleEntity);

    void saveRoleMenus(SysRoleEntity roleEntity);

    void deleteRoleButtons(SysRoleEntity roleEntity);

    void saveRoleButtons(SysRoleEntity roleEntity);

    List<SysRoleEntity> listRoles(SysRoleEntity roleEntity);

    void refreshRoleRedis();
}
