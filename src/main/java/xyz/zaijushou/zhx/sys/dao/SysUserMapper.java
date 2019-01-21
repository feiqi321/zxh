package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysToUserRole;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUserEntity findPasswordInfoByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user);

    List<SysToUserRole> listAllUserRoles(SysToUserRole sysToUserRole);

    List<SysUserEntity> listAllUsers(SysUserEntity userEntity);
}
