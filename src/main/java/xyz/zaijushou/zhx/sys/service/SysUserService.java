package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysToUserRole;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.util.List;

public interface SysUserService {

    SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user);

    List<SysToUserRole> listAllUserRoles(SysToUserRole sysToUserRole);

    List<SysUserEntity> listAllUsers(SysUserEntity userEntity);

    /**
     * 新增用户
     * @param userEntity
     */
    void saveUser(SysNewUserEntity userEntity);

    /**
     * 修改用户
     * @param userEntity
     */
    void updateUser(SysNewUserEntity userEntity);


    void updateDataStatus(SysNewUserEntity userEntity);

    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    PageInfo<SysNewUserEntity> pageDataList(SysNewUserEntity userEntity);

    /**
     * 查询用户信息
     * @param userEntity
     * @return
     */
    SysNewUserEntity getDataById(SysNewUserEntity userEntity);


    List<SysNewUserEntity> getDataByRoleName(SysNewUserEntity userEntity);

    void deleteById(SysNewUserEntity userEntity);

    /**
     * 根据用户属性查询用户列表
     * 属性：姓名
     * @param user
     * @return
     */
    List<SysUserEntity> listUsers(SysUserEntity user);
}
