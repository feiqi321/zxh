package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysAuthorityEntity;

import java.util.List;

public interface SysAuthorityService {
    List<SysAuthorityEntity> listAllAuthorities(SysAuthorityEntity sysAuthorityEntity);
}
