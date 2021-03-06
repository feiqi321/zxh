package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import xyz.zaijushou.zhx.sys.entity.*;

import java.util.List;

@Mapper
public interface SysUserMapper {
    SysUserEntity findPasswordInfoByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user);

    SysUserEntity findUserInfoWithoutStatusById(SysUserEntity user);

    List<SysToUserRole> listAllUserRoles(SysToUserRole sysToUserRole);

    List<SysToUserRole> listAllUserRolesByUserId(SysToUserRole sysToUserRole);

    List<SysUserEntity> listAllUsers(SysUserEntity userEntity);

    int saveNewUser(SysNewUserEntity userEntity);

    /**
     * 修改用户
     * @param userEntity
     */
    void updateUser(SysNewUserEntity userEntity);

    void updateDataStatus(SysNewUserEntity userEntity);

    void updateDataBatchStatus(SysNewUserEntity userEntity);

    void updateDept(SysNewUserEntity userEntity);

    void updateOfficePhone(SysNewUserEntity userEntity);

    void updateDeptByName(SysNewUserEntity userEntity);

    void updateUserInfo(SysNewUserEntity userEntity);

    void batchDelete(SysNewUserEntity userEntity);

    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    List<SysNewUserEntity> userDataList(SysNewUserEntity userEntity);

    List<SysNewUserEntity> userDataListIn(SysNewUserEntity userEntity);

    int countUserData(SysNewUserEntity userEntity);

    List<SysNewUserEntity> getDataByRoleName(SysNewUserEntity userEntity);

    List<SysNewUserEntity> getDataListByRoleName(SysNewUserEntity userEntity);


    /**
     * 查询用户信息
     * @param id
     * @return
     */
    SysNewUserEntity getDataById(Integer id);

    void deleteById(Integer id);

    /**
     * 根据用户属性查询用户列表
     * 属性：姓名
     * @param user
     * @return
     */
    List<SysUserEntity> listUsers(SysUserEntity user);

    /**
     * 根据用户属性查询用户列表
     * 属性：姓名
     * @param user
     * @return
     */
    List<SysUserEntity> listUsersByName(SysUserEntity user);

    /**
     * 修改密码
     * @param user
     */
    void passwordReset(SysNewUserEntity user);

    SysNewUserEntity selectPasswordInfoById(SysNewUserEntity user);

    List<SysNewUserEntity> selectPasswordInfoByOffice(SysNewUserEntity user);

    void updateLoginFailTimes(SysUserEntity user);

    void updateLoginFailInfo(SysUserEntity user);

    void updateLoginFailLockInfo(SysUserEntity user);

    List<SysUserEntity> countByLoginName(SysUserEntity user);

    int countUserNameAndNumber(SysNewUserEntity user);

    List<SysNewUserEntity> listByNameSet(SysNewUserEntity queryUser);

    List<SysNewUserEntity> listByDepartIdsSet(SysNewUserEntity queryUser);

    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    List<SysNewUserEntity> userExportList(SysNewUserEntity userEntity);

    int countLockedUser();

    List<SysNewUserEntity> listParent(SysNewUserEntity userEntity);

    List<SysNewUserEntity> showManage(SysNewUserEntity userEntity);

    List<SysNewUserEntity> listUserByDept(SysNewUserEntity userEntity);

    List<SysNewUserEntity> userDataListByDept(SysNewUserEntity userEntity);

    SysNewUserEntity findDepartment(Integer id);

	List<SysNewUserEntity> queryOdvs(String[] depts);

	List<QueryEntity> queryOdv(String odvName);

    List<QueryEntity> findUserByDept(String department,String odvName);

    String queryDepartment(Integer id);

    List<SysNewUserEntity> queryOdvs1(Integer[] depts);

    Integer queryLoginName(String loginName);

    void saveRole(Integer userId, String roleName);

    List<SysRoleEntity> findRole(String[] roleName);

    List<DepartmentEntity> findParentDept(String downDept);

    int findLoginName(String loginName);

    List<SysUserEntity> listUsersByLoginName(SysUserEntity tempUser);

    void updatedeptInfo(Integer id, String departId);
}
