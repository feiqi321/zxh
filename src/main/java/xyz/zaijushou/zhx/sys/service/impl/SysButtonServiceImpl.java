package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysButtonMapper;
import xyz.zaijushou.zhx.sys.entity.SysButtonEntity;
import xyz.zaijushou.zhx.sys.entity.SysToButtonAuthority;
import xyz.zaijushou.zhx.sys.service.SysButtonService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysButtonServiceImpl implements SysButtonService {

    @Resource
    private SysButtonMapper sysButtonMapper;

    @Override
    public List<SysButtonEntity> listAllButtons(SysButtonEntity sysButtonEntity) {
        return sysButtonMapper.listAllButtons(sysButtonEntity);
    }

    @Override
    public List<SysToButtonAuthority> listAllButtonAuthorities(SysToButtonAuthority sysToButtonAuthority) {
        return sysButtonMapper.listAllButtonAuthorities(sysToButtonAuthority);
    }
}
