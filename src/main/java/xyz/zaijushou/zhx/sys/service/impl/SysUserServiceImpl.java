package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.sys.dao.SysToUserRoleMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysToUserRoleMapper sysToUserRoleMapper;

    @Resource
    private DelegatingPasswordEncoder delegatingPasswordEncoder;

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
    @Override
    @Transactional
    public void saveUser(SysNewUserEntity userEntity){
        userEntity.setLoginName(userEntity.getNumber());//编号作为登录名
        userEntity.setPassword(delegatingPasswordEncoder.encode("admin".trim()));//保存加密密码
        userEntity.setCreateTime(new Date());
        userEntity.setDeleteFlag(0);//默认正常
//        if (userEntity.getStatus() == 1){//在职
//            userEntity.setEnable(1);//启动
//        }else {//离职
//            userEntity.setEnable(0);//锁定
//            userEntity.setLeaveTime(new Date());//保存离职日期
//        }
        sysUserMapper.saveNewUser(userEntity);
        //保存角色中间表
        SysUserRoleEntity roleEntity = new SysUserRoleEntity();
        roleEntity.setUserId(userEntity.getId());
        if (StringUtils.notEmpty(userEntity.getRoleList())) {
            for (SysRoleEntity role : userEntity.getRoleList()){
                roleEntity.setRoleId(role.getId());
                sysToUserRoleMapper.saveUserRole(roleEntity);
            }
        }
    }

    /**
     * 修改用户
     * @param userEntity
     */
    @Override
    public void updateUser(SysNewUserEntity userEntity){
        if (userEntity.getStatus() == 0){
            userEntity.setEnable(0);
        }
        sysUserMapper.updateUser(userEntity);

        //保存角色中间表
        SysUserRoleEntity roleEntity = new SysUserRoleEntity();
        roleEntity.setUserId(userEntity.getId());
        sysToUserRoleMapper.deleteUserRole(roleEntity);
        if (StringUtils.notEmpty(userEntity.getRoleList())) {
            for (SysRoleEntity role : userEntity.getRoleList()){
                roleEntity.setRoleId(role.getId());
                sysToUserRoleMapper.saveUserRole(roleEntity);
            }
        }
    }

    @Override
    public void updateDataStatus(SysNewUserEntity userEntity){
        if (userEntity.getStatus() == 0){
            userEntity.setEnable(0);
            userEntity.setLeaveTime(new Date());//保存离职日期
        }
        sysUserMapper.updateDataStatus(userEntity);
    }

    @Override
    public void deleteById(SysNewUserEntity userEntity){
        sysUserMapper.deleteById(userEntity.getId());
    }


    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    @Override
    public PageInfo<SysNewUserEntity> pageDataList(SysNewUserEntity userEntity){

        return   PageInfo.of(sysUserMapper.pageDataList(userEntity));
    }

    /**
     * 查询用户信息
     * @param userEntity
     * @return
     */
    @Override
    public SysNewUserEntity getDataById(SysNewUserEntity userEntity){
        SysNewUserEntity userInfoEntity = new SysNewUserEntity();
        userInfoEntity = sysUserMapper.getDataById(userEntity.getId());
        return userInfoEntity;
    }

    @Override
    public List<SysNewUserEntity> getDataByRoleName(SysNewUserEntity userEntity){
        List<SysNewUserEntity> userInfoEntity = sysUserMapper.getDataByRoleName(userEntity);
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

    @Override
    public void passwordReset(SysNewUserEntity user) {
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        user.setId(userId);
        SysNewUserEntity queryUser = selectPasswordInfoById(user);
        if(delegatingPasswordEncoder.matches(user.getOldPassword(), queryUser.getPassword())) {
            user.setId(userId);
            user.setPassword(delegatingPasswordEncoder.encode(user.getPassword()));
            sysUserMapper.passwordReset(user);
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }

    private SysNewUserEntity selectPasswordInfoById(SysNewUserEntity user) {
        return sysUserMapper.selectPasswordInfoById(user);
    }

    @Override
    public void passwordResetByAdmin(SysNewUserEntity user) {
        user.setPassword(delegatingPasswordEncoder.encode(user.getPassword()));
        sysUserMapper.passwordReset(user);
    }


}
