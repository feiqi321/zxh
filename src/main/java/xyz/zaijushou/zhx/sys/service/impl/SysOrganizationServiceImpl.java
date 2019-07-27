package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysOrganizationMapper;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Resource
    private SysOrganizationMapper sysOrganizationMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public List<SysOrganizationEntity> listAllOrganizations(SysOrganizationEntity organizationEntity) {
        return sysOrganizationMapper.listAllOrganizations(organizationEntity);
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganization(SysOrganizationEntity organizationEntity) {
        List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return dictList;
        }
        //获取用于所在部门
        organizationEntity.setId(Integer.valueOf(user.getDepartment()));

        List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        if (StringUtils.isEmpty(list)){
            return dictList;
        }
        //递归查询列表
        for (SysOrganizationEntity dictionaryEntity : list){
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1){
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity,dictList);
        }
        dictList = CollectionsUtils.listToTree(dictList);
        return dictList;
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganizationBy(SysOrganizationEntity organizationEntity) {
        List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();

        List<SysOrganizationEntity> list = new ArrayList<>();
        if (StringUtils.isEmpty(organizationEntity.getOrgName())){
            organizationEntity.setId(1);

            list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        }else {
            list = sysOrganizationMapper.listChildOrganizationBy(organizationEntity);
        }
        if (StringUtils.isEmpty(list)){
            return dictList;
        }
        //递归查询列表
        for (SysOrganizationEntity dictionaryEntity : list){
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1){
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity,dictList);
        }
        dictList = CollectionsUtils.listToTree(dictList);
        return dictList;
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganization2(SysOrganizationEntity organizationEntity) {
        List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();
        //获取当前用户名
        if (organizationEntity.getId()==null || organizationEntity.getId()==0) {
            SysUserEntity user = getUserInfo();
            if (StringUtils.isEmpty(user)) {
                return dictList;
            }
            //获取用于所在部门
            organizationEntity.setId(Integer.valueOf(user.getDepartment()));
        }

        List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        if (StringUtils.isEmpty(list)){
            return dictList;
        }
        //递归查询列表
        for (SysOrganizationEntity dictionaryEntity : list){
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1){
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity,dictList);
        }

        return dictList;
    }
    //查询用户
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return user;
    }
    //递归查询
    private void getChildDataInfo(SysOrganizationEntity entity,List<SysOrganizationEntity> dictList){
        List<SysOrganizationEntity> organizationList = sysOrganizationMapper.listAllOrganizationsByParentId(entity);
        if (StringUtils.isEmpty(organizationList)){
            return ;
        }
        for (SysOrganizationEntity organizationEntity : organizationList){
            dictList.add(organizationEntity);
            getChildDataInfo(organizationEntity,dictList);
        }
    }
    @Override
    public void saveOrg(SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.saveOrg(organizationEntity);
    }

    @Override
    public void updateOrg(SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.updateOrg(organizationEntity);

//        更新redis中对应的部门中文名称
        this.updateUserRedis(organizationEntity);

}

    @Override
    public void deleteOrg(SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.deleteOrg(organizationEntity);
    }

    @Override
    public SysOrganizationEntity findByName(String name) {
        return sysOrganizationMapper.findByName(name);
    }

    private void updateUserRedis(SysOrganizationEntity organizationEntity){

        //根据orgId查询到用户
        List<SysNewUserEntity> userList =sysOrganizationMapper.findUserByOrgId(organizationEntity.getId());
        //遍历循环更改用户部门名称
        for (SysNewUserEntity obj:userList){
            //判断部门名称是否被修改
            SysNewUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ obj.getId(), SysNewUserEntity.class);
            if (obj.getDeptName().equals(userTemp.getDeptName())) {
                return;
            }
            obj.setDeptName(organizationEntity.getOrgName());
            if (StringUtils.notEmpty(obj)){
                //修改redis
                stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_INFO + obj.getId(), JSONObject.toJSONString(obj));
            }
        }

    }
}
