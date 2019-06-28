package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import org.springframework.security.authentication.BadCredentialsException;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.*;

import java.util.List;

public interface SysUserService {

    SysUserEntity findPasswordInfoByUsername(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user);

    public SysUserEntity findUserInfoWithoutStatusById(SysUserEntity user);

    List<SysToUserRole> listAllUserRoles(SysToUserRole sysToUserRole);

    List<SysUserEntity> listAllUsers(SysUserEntity userEntity);

    /**
     * 新增用户
     * @param userEntity
     */
    WebResponse saveUser(SysNewUserEntity userEntity);

    /**
     * 修改用户
     * @param userEntity
     */
    WebResponse updateUser(SysNewUserEntity userEntity);

    /**
     * 修改用户部门
     * @param userEntity
     */
    void updateDept(SysNewUserEntity userEntity);

    /**
     * 修改用户部门
     * @param userEntity
     */
    void batchDelete(SysNewUserEntity userEntity);


    void updateDataStatus(SysNewUserEntity userEntity);

    void updateDataBatchStatus(SysNewUserEntity userEntity);

    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    PageInfo<SysNewUserEntity> userDataList(SysNewUserEntity userEntity);

    UserTree userTree();

    UserTree userTreeByRoleId(SysNewUserEntity userEntity);

    /**
     * 查询用户信息
     * @param userEntity
     * @return
     */
    SysNewUserEntity getDataById(SysNewUserEntity userEntity);


    List<SysNewUserEntity> getDataByRoleName(SysNewUserEntity userEntity);

    List<SysNewUserEntity> getDataByRoleNameForList(SysNewUserEntity userEntity);

    int deleteById(SysNewUserEntity userEntity);

    /**
     * 根据用户属性查询用户列表
     * 属性：姓名
     * @param user
     * @return
     */
    List<SysUserEntity> listUsers(SysUserEntity user);

    /**
     * 用户修改密码
     * @param user
     */
    void passwordReset(SysNewUserEntity user) throws BadCredentialsException;

    void setUserPassword(SysNewUserEntity user);

    /**
     * 管理员修改用户密码
     * @param user
     */
    void passwordResetByAdmin(SysNewUserEntity user);

    void updateLoginFailTimes(SysUserEntity user);

    public SysUserEntity getLoginName(SysUserEntity user) throws Exception;

    List<SysNewUserEntity> listByNameSet(SysNewUserEntity queryUser);

    List<SysNewUserEntity> listByDepartIdsSet(SysNewUserEntity queryUser);

   void insertUserList(List<SysNewUserEntity> list);

    void insertSimple(List<SysNewUserEntity> list);

    void insertDeptSimple(List<DepartmentEntity> list);

    /**
     * 导出用户列表
     * @param userEntity
     * @return
     */
    List<SysNewUserEntity> userExportList(SysNewUserEntity userEntity);

    /**
     * 查询锁定的用户数
     * @return
     */
    int countLockedUser();
}
