package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;

import java.util.List;

public interface SysOrganizationService {
    List<SysOrganizationEntity> listAllOrganizations(SysOrganizationEntity organizationEntity);

    List<SysOrganizationEntity> listChildOrganization(SysOrganizationEntity organizationEntity);

    List<SysOrganizationEntity> listChildOrganizationBy(SysOrganizationEntity organizationEntity);

    List<SysOrganizationEntity> listChildOrganization2(SysOrganizationEntity organizationEntity);

    void saveOrg(SysOrganizationEntity organizationEntity);

    void updateOrg(SysOrganizationEntity organizationEntity);

    void deleteOrg(SysOrganizationEntity organizationEntity);

    SysOrganizationEntity findByName(String name);

    Object findStaffNumber();

    void moveUpDown(Integer id, String sort,Integer id1,String sort1);

    void updateDepartment(SysOrganizationEntity organizations);

    Object deleteSelectDepartment(SysOrganizationEntity organizations);

    List<SysOrganizationEntity> findTableData(Integer id);

    void addDept(SysOrganizationEntity organizations);
}
