package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysUserRoleEntity;

import java.util.List;

@Mapper
public interface SysToUserRoleMapper {

    void saveUserRole(SysUserRoleEntity role);

    void deleteUserRole(SysUserRoleEntity role);

    List<SysUserRoleEntity> listUserRoles(SysUserRoleEntity role);
}
