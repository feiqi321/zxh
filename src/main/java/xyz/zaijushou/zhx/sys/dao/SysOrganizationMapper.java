package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysNewUserDataForm;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;

import java.util.List;

@Mapper
public interface SysOrganizationMapper {
    List<SysOrganizationEntity> listAllOrganizations(SysOrganizationEntity organizationEntity);

    List<SysOrganizationEntity> listChildOrganizationBy(SysOrganizationEntity organizationEntity);

    List<SysOrganizationEntity> listAllOrganizationsByParentId(SysOrganizationEntity organizationEntity);

    void saveOrg(SysOrganizationEntity organizationEntity);

    List<SysOrganizationEntity> listAll();

    void updateOrg(SysOrganizationEntity organizationEntity);

    void deleteOrg(SysOrganizationEntity organizationEntity);

    SysOrganizationEntity findByName(String name);

    SysOrganizationEntity selectMaxSort();

    List<SysNewUserEntity> findById(String department);

    List<SysNewUserDataForm> findSysNewUserInformationById(String department);
}
