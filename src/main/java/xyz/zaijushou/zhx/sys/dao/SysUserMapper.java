package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysOperationUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysToUserRole;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

@Mapper
public interface SysUserMapper {
    SysUserEntity findPasswordInfoByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordByLoginName(SysUserEntity user);

    SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user);

    List<SysToUserRole> listAllUserRoles(SysToUserRole sysToUserRole);

    List<SysUserEntity> listAllUsers(SysUserEntity userEntity);

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
    List<SysOperationUserEntity> pageDataList(SysOperationUserEntity userEntity);


    /**
     * 查询用户信息
     * @param id
     * @return
     */
    SysOperationUserEntity getDataById(Integer id);

    /**
     * 根据用户属性查询用户列表
     * 属性：姓名
     * @param user
     * @return
     */
    List<SysUserEntity> listUsers(SysUserEntity user);
}
