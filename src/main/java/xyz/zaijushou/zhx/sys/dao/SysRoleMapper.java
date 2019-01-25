package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysRoleEntity;
import xyz.zaijushou.zhx.sys.entity.SysToRoleButton;
import xyz.zaijushou.zhx.sys.entity.SysToRoleMenu;

import java.util.List;

@Mapper
public interface SysRoleMapper {

    List<SysRoleEntity> listAllRoles(SysRoleEntity sysRoleEntity);

    List<SysToRoleMenu> listAllRoleMenus(SysToRoleMenu sysToRoleMenu);

    List<SysToRoleButton> listAllRoleButtons(SysToRoleButton sysToRoleButton);
}
