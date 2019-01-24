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
        userEntity.setDeleteFlag(0);//正常
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
    public List<SysOperationUserEntity> getDataList(SysOperationUserEntity userEntity){
        List<SysOperationUserEntity> userEntityList = new ArrayList<SysOperationUserEntity>();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("id",userEntity.getId());//用户Id
        map.put("status",userEntity.getStatus());//1-在职，0-离职
        userEntityList = sysUserMapper.getDataList(map);
        return  userEntityList;
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
}
