package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

@Mapper
public interface SysUserMapper {
    SysUserEntity findPasswordInfoByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user);
}
