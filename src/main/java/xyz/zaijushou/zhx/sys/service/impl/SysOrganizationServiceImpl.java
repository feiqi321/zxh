package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.dao.SysOrganizationMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
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
@Transactional(rollbackFor = Exception.class)
public class SysOrganizationServiceImpl implements SysOrganizationService {

    @Resource
    private SysOrganizationMapper sysOrganizationMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SysUserMapper sysUserMapper;

    @Override
    public List<SysOrganizationEntity> listAllOrganizations(SysOrganizationEntity organizationEntity) {
        return sysOrganizationMapper.listAllOrganizations(organizationEntity);
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganization(SysOrganizationEntity organizationEntity) {
        List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)) {
            return dictList;
        }
        //获取用于所在部门
        organizationEntity.setId(Integer.valueOf(user.getDepartment()));

        List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        if (StringUtils.isEmpty(list)) {
            return dictList;
        }
        //递归查询列表
        for (SysOrganizationEntity dictionaryEntity : list) {
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1) {
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity, dictList);
        }
        dictList = CollectionsUtils.listToTree(dictList);
        return dictList;
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganizationBy(SysOrganizationEntity organizationEntity) {
        List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();

        List<SysOrganizationEntity> list = new ArrayList<>();
        if (StringUtils.isEmpty(organizationEntity.getOrgName())) {
            organizationEntity.setId(1);

            list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        } else {
            list = sysOrganizationMapper.listChildOrganizationBy(organizationEntity);
        }
        if (StringUtils.isEmpty(list)) {
            return dictList;
        }
        //递归查询列表
        for (SysOrganizationEntity dictionaryEntity : list) {
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1) {
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity, dictList);
        }
        dictList = CollectionsUtils.listToTree(dictList);
        return dictList;
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganization2(SysOrganizationEntity organizationEntity) {
        List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();
        //获取当前用户名
        if (organizationEntity.getId() == null || organizationEntity.getId() == 0) {
            SysUserEntity user = getUserInfo();
            if (StringUtils.isEmpty(user)) {
                return dictList;
            }
            //获取用于所在部门
            organizationEntity.setId(Integer.valueOf(user.getDepartment()));
        }

        List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        if (StringUtils.isEmpty(list)) {
            return dictList;
        }
        //递归查询列表
        for (SysOrganizationEntity dictionaryEntity : list) {
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1) {
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity, dictList);
        }
        return dictList;
    }

    //查询用户
    private SysUserEntity getUserInfo() {
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + userId, SysUserEntity.class);
        return user;
    }

    //递归查询
    private void getChildDataInfo(SysOrganizationEntity entity, List<SysOrganizationEntity> dictList) {
        List<SysOrganizationEntity> organizationList = sysOrganizationMapper.listAllOrganizationsByParentId(entity);
        if (StringUtils.isEmpty(organizationList)) {
            return;
        }
        for (SysOrganizationEntity organizationEntity : organizationList) {
            dictList.add(organizationEntity);
            getChildDataInfo(organizationEntity, dictList);
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

    @Override
    public Object findStaffNumber() {
        return null;
    }

    @Override
    public void moveUpDown(Integer id, String sort, Integer id1, String sort1) {
        sysOrganizationMapper.moveUpDown(id, sort1);
        sysOrganizationMapper.moveUpDown1(id1, sort);
    }

    @Override
    public void updateDepartment(SysOrganizationEntity organizations) {
        sysOrganizationMapper.updateDepartment(organizations);
    }

    @Override
    public Object deleteSelectDepartment(SysOrganizationEntity organizations) {
        Integer staffNumber = sysOrganizationMapper.findStaffNumberById(organizations.getId() + "");
        Integer staffNumber2 = sysOrganizationMapper.findStaffNumberById2(organizations.getId() + "");
        if (staffNumber == 0 && staffNumber2 == 0) {
            List<SysOrganizationEntity> dictList = new ArrayList<>();
            List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizations);
            if (StringUtils.isEmpty(list)) {
                return dictList;
            }
            //递归查询列表
            for (SysOrganizationEntity dictionaryEntity : list) {
                if (StringUtils.notEmpty(organizations.getTypeFlag()) && organizations.getTypeFlag() == 1) {
                    dictList.add(dictionaryEntity);
                }
                sysOrganizationMapper.deleteSelectDepartment(dictionaryEntity);
                getChildDataInfo2(dictionaryEntity, dictList);
            }
        } else {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "待删除的部门尚有员工，不得删除");
        }
        return WebResponse.success();
    }

    @Override
    public List<SysOrganizationEntity> findTableData(Integer id) {
        List<SysOrganizationEntity> tableData = sysOrganizationMapper.findTableData(id);
        for (SysOrganizationEntity sysOrganizationEntity : tableData) {
                if (sysOrganizationEntity.getUserNum() == null||sysOrganizationEntity.getUserNum() == 0) {
                    Integer staffNumber = sysOrganizationMapper.findStaffNumberById(sysOrganizationEntity.getId() + "");
                    if (staffNumber == 0) {
                        Integer staffNumber2 = sysOrganizationMapper.findStaffNumberById2(sysOrganizationEntity.getId() + "");
                        sysOrganizationEntity.setUserNum(staffNumber2);
                    } else {
                        sysOrganizationEntity.setUserNum(staffNumber);
                    }
                }
            }
        return tableData;
    }

    @Override
    public void addDept(SysOrganizationEntity organizations) {
        String sort = organizations.getSort();
        int sortInt = Integer.parseInt(sort);
        sortInt += 10;
        StringBuilder sortStr = new StringBuilder();
        for(int k = String.valueOf(sortInt).length(); k < 6; k ++) {
            sortStr.append(0);
        }
        sortStr.append(sortInt);
        organizations.setSort(sortStr.toString());
        sysOrganizationMapper.addDept(organizations);
    }

    @Override
    public void moveToTargetDepartment(Integer id, String pid) {
        sysOrganizationMapper.moveToTargetDepartment(id,pid);
    }

    /**
     * 递归删除
     */
    private void getChildDataInfo2(SysOrganizationEntity entity, List<SysOrganizationEntity> dictList) {
        List<SysOrganizationEntity> organizationList = sysOrganizationMapper.listAllOrganizationsByParentId(entity);
        if (StringUtils.isEmpty(organizationList)) {
            return;
        }
        for (SysOrganizationEntity organizationEntity : organizationList) {
            sysOrganizationMapper.deleteSelectDepartment(organizationEntity);
            dictList.add(organizationEntity);
            getChildDataInfo2(organizationEntity, dictList);
        }
    }

    private void updateUserRedis(SysOrganizationEntity organizationEntity) {

        //根据orgId查询到用户
        List<SysNewUserEntity> userList = sysOrganizationMapper.findUserByOrgId(organizationEntity.getId());
        //遍历循环更改用户部门名称
        for (SysNewUserEntity obj : userList) {
            //判断部门名称是否被修改
            SysNewUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + obj.getId(), SysNewUserEntity.class);
            if (obj.getDeptName().equals(userTemp.getDeptName())) {
                return;
            }
            obj.setDeptName(organizationEntity.getOrgName());
            if (StringUtils.notEmpty(obj)) {
                //修改redis
                stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_INFO + obj.getId(), JSONObject.toJSONString(obj));
            }
        }

    }
}
