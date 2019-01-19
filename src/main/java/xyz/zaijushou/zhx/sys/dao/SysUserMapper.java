package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

@Mapper
public interface SysUserMapper {
    SysUserEntity findPasswordInfoByUsername(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordByUsername(SysUserEntity user);
}
