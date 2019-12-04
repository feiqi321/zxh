package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import xyz.zaijushou.zhx.sys.entity.QueryEntity;
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

    Integer findStaffNumberById(String department);

    Integer findStaffNumberById2(String department);

    void updateSortById(Integer id, Integer sort);

    List<SysNewUserEntity> findUserByOrgId(Integer id);

    void updateDepartment(SysOrganizationEntity organizations);

    void deleteSelectDepartment(SysOrganizationEntity organizations);

    List<SysOrganizationEntity> findTableData(Integer id);

    void moveToTargetDepartment(Integer id, String pid);

	List<QueryEntity> queryDept(String deptName);

    List<QueryEntity> findOrganizationsByParentId(String department,String odvName);

    List<QueryEntity> queryDeptCase(String department,String deptName);

	SysOrganizationEntity queryOrganizationById(Integer id);

	List<SysOrganizationEntity> listAllOrgsWithUserNum();
}
