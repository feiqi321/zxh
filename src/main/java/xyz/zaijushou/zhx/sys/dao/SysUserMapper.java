package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
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

    int saveNewUser(SysNewUserEntity userEntity);

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
    List<SysNewUserEntity> pageDataList(SysNewUserEntity userEntity);

    List<SysNewUserEntity> getDataByRoleName(SysNewUserEntity userEntity);


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
}
