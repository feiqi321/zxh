package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysMenuMapper;
import xyz.zaijushou.zhx.sys.entity.SysMenuEntity;
import xyz.zaijushou.zhx.sys.service.SysMenuService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysMenuServiceImpl implements SysMenuService {

    @Resource
    private SysMenuMapper sysMenuMapper;

    @Override
    public List<SysMenuEntity> listAllMenus(SysMenuEntity sysMenuEntity) {
        return sysMenuMapper.listAllMenus(sysMenuEntity);
    }
}
