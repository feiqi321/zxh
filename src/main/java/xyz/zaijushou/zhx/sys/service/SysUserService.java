package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysOperationUserEntity;
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
    void saveUser(SysOperationUserEntity userEntity);

    /**
     * 修改用户
     * @param userEntity
     */
    void updateUser(SysOperationUserEntity userEntity);

    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    List<SysOperationUserEntity> getDataList(SysOperationUserEntity userEntity);

    /**
     * 查询用户信息
     * @param userEntity
     * @return
     */
    SysOperationUserEntity getDataById(SysOperationUserEntity userEntity);
}
