package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysRoleEntity;
import xyz.zaijushou.zhx.sys.entity.SysToRoleMenu;

import java.util.List;

public interface SysRoleService {
    List<SysRoleEntity> listAllRoles(SysRoleEntity sysRoleEntity);

    List<SysToRoleMenu> listAllRoleMenus(SysToRoleMenu sysToRoleMenu);
}
