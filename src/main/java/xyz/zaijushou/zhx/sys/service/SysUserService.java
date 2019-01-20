package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

public interface SysUserService {

    SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user);
}
