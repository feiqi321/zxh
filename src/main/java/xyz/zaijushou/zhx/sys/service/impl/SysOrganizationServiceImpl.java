package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysOrganizationMapper;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Resource
    private SysOrganizationMapper sysOrganizationMapper;

    @Override
    public List<SysOrganizationEntity> listAllOrganizations(SysOrganizationEntity organizationEntity) {
        return sysOrganizationMapper.listAllOrganizations(organizationEntity);
    }

    @Override
    public void saveOrg(SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.saveOrg(organizationEntity);
    }

    @Override
    public void updateOrg(SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.updateOrg(organizationEntity);
    }

    @Override
    public void deleteOrg(SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.deleteOrg(organizationEntity);
    }
}
