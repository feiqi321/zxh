package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysRoleEntity;
import xyz.zaijushou.zhx.sys.entity.SysToRoleButton;
import xyz.zaijushou.zhx.sys.entity.SysToRoleMenu;

import java.util.List;

public interface SysRoleService {
    List<SysRoleEntity> listAllRoles(SysRoleEntity sysRoleEntity);

    List<SysToRoleMenu> listAllRoleMenus(SysToRoleMenu sysToRoleMenu);

    List<SysToRoleButton> listAllRoleButtons(SysToRoleButton sysToRoleButton);

    void updateRole(SysRoleEntity roleEntity);

    void saveRole(SysRoleEntity roleEntity);

    void deleteRole(SysRoleEntity roleEntity);

    SysRoleEntity selectByRoleName(SysRoleEntity roleEntity);
}
