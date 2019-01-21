package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysButtonEntity;
import xyz.zaijushou.zhx.sys.entity.SysToButtonAuthority;

import java.util.List;

public interface SysButtonService {
    List<SysButtonEntity> listAllButtons(SysButtonEntity sysButtonEntity);

    List<SysToButtonAuthority> listAllButtonAuthorities(SysToButtonAuthority sysToButtonAuthority);
}
