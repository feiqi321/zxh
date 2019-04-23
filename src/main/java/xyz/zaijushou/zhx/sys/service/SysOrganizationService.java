package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;

import java.util.List;

public interface SysOrganizationService {
    List<SysOrganizationEntity> listAllOrganizations(SysOrganizationEntity organizationEntity);

    List<SysOrganizationEntity> listChildOrganization(SysOrganizationEntity organizationEntity);

    void saveOrg(SysOrganizationEntity organizationEntity);

    void updateOrg(SysOrganizationEntity organizationEntity);

    void deleteOrg(SysOrganizationEntity organizationEntity);
}
