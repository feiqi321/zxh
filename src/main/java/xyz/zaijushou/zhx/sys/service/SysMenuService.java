package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysMenuEntity;

import java.util.List;

public interface SysMenuService {
    List<SysMenuEntity> listAllMenus(SysMenuEntity sysMenuEntity);
}
