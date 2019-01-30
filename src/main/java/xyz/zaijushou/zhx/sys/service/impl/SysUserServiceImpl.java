package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.SysOperationUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysToUserRole;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysUserService;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user) {
        return sysUserMapper.findUserInfoWithoutPasswordById(user);
    }

    @Override
    public List<SysToUserRole> listAllUserRoles(SysToUserRole sysToUserRole) {
        return sysUserMapper.listAllUserRoles(sysToUserRole);
    }

    @Override
    public List<SysUserEntity> listAllUsers(SysUserEntity userEntity) {
        return sysUserMapper.listAllUsers(userEntity);
    }

    private SysUserEntity findPasswordInfoByUsername(SysUserEntity user) {
        return sysUserMapper.findPasswordInfoByLoginName(user);
    }


    /**
     * 保存用户
     * @param userEntity
     */
    public void saveUser(SysOperationUserEntity userEntity){
        userEntity.setLoginName(userEntity.getNumber());//编号作为登录名
        BCryptPasswordEncoder encoder =new BCryptPasswordEncoder();
        userEntity.setPassword(encoder.encode("admin".trim()));//保存加密密码
        userEntity.setCreateTime(new Date());
        userEntity.setDeleteFlag(0);//默认正常
        userEntity.setEnable(1);//默认启用
        userEntity.setStatus(1);//默认在职
        sysUserMapper.saveUser(userEntity);
    }

    /**
     * 修改用户
     * @param userEntity
     */
    public void updateUser(SysOperationUserEntity userEntity){
        sysUserMapper.updateUser(userEntity);
    }

    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    public List<SysOperationUserEntity> pageDataList(SysOperationUserEntity userEntity){

        return   sysUserMapper.pageDataList(userEntity);
    }

    /**
     * 查询用户信息
     * @param userEntity
     * @return
     */
    public SysOperationUserEntity getDataById(SysOperationUserEntity userEntity){
        SysOperationUserEntity userInfoEntity = new SysOperationUserEntity();
        userInfoEntity = sysUserMapper.getDataById(userEntity.getId());
        return userInfoEntity;
    }

    /**
     * 根据用户属性查询用户列表
     * 属性：姓名
     * @param user
     * @return
     */
    @Override
    public List<SysUserEntity> listUsers(SysUserEntity user) {
        return sysUserMapper.listUsers(user);
    }
}
