package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysAuthorityMapper;
import xyz.zaijushou.zhx.sys.entity.SysAuthorityEntity;
import xyz.zaijushou.zhx.sys.service.SysAuthorityService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysAuthorityServiceImpl implements SysAuthorityService {

    @Resource
    private SysAuthorityMapper sysAuthorityMapper;

    @Override
    public List<SysAuthorityEntity> listAllAuthorities(SysAuthorityEntity sysAuthorityEntity) {
        return sysAuthorityMapper.listAllAuthorities(sysAuthorityEntity);
    }
}
