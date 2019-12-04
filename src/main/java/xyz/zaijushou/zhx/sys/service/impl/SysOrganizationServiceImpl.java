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
import xyz.zaijushou.zhx.sys.entity.QueryEntity;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public List<SysOrganizationEntity> listAllOrganizations(final SysOrganizationEntity organizationEntity) {
        return sysOrganizationMapper.listAllOrganizations(organizationEntity);
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganization(final SysOrganizationEntity organizationEntity) {
        List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();
        //获取当前用户名
        final SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)) {
            return dictList;
        }
        //获取用于所在部门
        organizationEntity.setId(Integer.valueOf(user.getDepartment()));

        final List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        if (StringUtils.isEmpty(list)) {
            return dictList;
        }
        //递归查询列表
        for (final SysOrganizationEntity dictionaryEntity : list) {
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1) {
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity, dictList);
        }
        dictList = CollectionsUtils.listToTree(dictList);
        return dictList;
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganizationBy(final SysOrganizationEntity organizationEntity) {
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
        for (final SysOrganizationEntity dictionaryEntity : list) {
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1) {
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity, dictList);
        }
        dictList = CollectionsUtils.listToTree(dictList);
        return dictList;
    }

    @Override
    public List<SysOrganizationEntity> listChildOrganization2(final SysOrganizationEntity organizationEntity) {
        final List<SysOrganizationEntity> dictList = new ArrayList<SysOrganizationEntity>();
        //获取当前用户名
        if (organizationEntity.getId() == null || organizationEntity.getId() == 0) {
            final SysUserEntity user = getUserInfo();
            if (StringUtils.isEmpty(user)) {
                return dictList;
            }
            //获取用于所在部门
            organizationEntity.setId(Integer.valueOf(user.getDepartment()));
        }

        final List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizationEntity);
        if (StringUtils.isEmpty(list)) {
            return dictList;
        }
        //递归查询列表
        for (final SysOrganizationEntity dictionaryEntity : list) {
            if (StringUtils.notEmpty(organizationEntity.getTypeFlag()) && organizationEntity.getTypeFlag() == 1) {
                dictList.add(dictionaryEntity);
            }
            getChildDataInfo(dictionaryEntity, dictList);
        }
        return dictList;
    }

    //查询用户
    private SysUserEntity getUserInfo() {
        final Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        final SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + userId, SysUserEntity.class);
        return user;
    }

    //递归查询
    private void getChildDataInfo(final SysOrganizationEntity entity, final List<SysOrganizationEntity> dictList) {
        final List<SysOrganizationEntity> organizationList = sysOrganizationMapper.listAllOrganizationsByParentId(entity);
        if (StringUtils.isEmpty(organizationList)) {
            return;
        }
        for (final SysOrganizationEntity organizationEntity : organizationList) {
            dictList.add(organizationEntity);
            getChildDataInfo(organizationEntity, dictList);
        }
    }

    @Override
    public void saveOrg(final SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.saveOrg(organizationEntity);
    }

    @Override
    public void updateOrg(final SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.updateOrg(organizationEntity);
//        更新redis中对应的部门中文名称
        this.updateUserRedis(organizationEntity);
    }

    @Override
    public void deleteOrg(final SysOrganizationEntity organizationEntity) {
        sysOrganizationMapper.deleteOrg(organizationEntity);
    }

    @Override
    public SysOrganizationEntity findByName(final String name) {
        return sysOrganizationMapper.findByName(name);
    }

    @Override
    @Transactional
    public void moveUpDown(final Integer id, final Integer id1) {
        final SysOrganizationEntity org = sysOrganizationMapper.queryOrganizationById(id);
        final SysOrganizationEntity org1 = sysOrganizationMapper.queryOrganizationById(id1);
        sysOrganizationMapper.updateSortById(id, org1.getSort());
        sysOrganizationMapper.updateSortById(id1, org.getSort());
    }

    @Override
    public void updateDepartment(final SysOrganizationEntity organizations) {
        sysOrganizationMapper.updateDepartment(organizations);
    }

    @Override
    public Object deleteSelectDepartment(final SysOrganizationEntity organizations) {
        final Integer staffNumber = sysOrganizationMapper.findStaffNumberById(organizations.getId() + "");
        final Integer staffNumber2 = sysOrganizationMapper.findStaffNumberById2(organizations.getId() + "");
        if (staffNumber == 0 && staffNumber2 == 0) {
            final List<SysOrganizationEntity> dictList = new ArrayList<>();
            final List<SysOrganizationEntity> list = sysOrganizationMapper.listAllOrganizations(organizations);
            if (StringUtils.isEmpty(list)) {
                return dictList;
            }
            //递归查询列表
            for (final SysOrganizationEntity dictionaryEntity : list) {
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
    public List<SysOrganizationEntity> findTableData(final Integer id) {
        final List<SysOrganizationEntity> tableData = sysOrganizationMapper.findTableData(id);
        for (final SysOrganizationEntity sysOrganizationEntity : tableData) {
            if (sysOrganizationEntity.getUserNum() == null||sysOrganizationEntity.getUserNum() == 0) {
                final Integer staffNumber = sysOrganizationMapper.findStaffNumberById(sysOrganizationEntity.getId() + "");
                if (staffNumber == 0) {
                    final Integer staffNumber2 = sysOrganizationMapper.findStaffNumberById2(sysOrganizationEntity.getId() + "");
                    sysOrganizationEntity.setUserNum(staffNumber2);
                } else {
                    sysOrganizationEntity.setUserNum(staffNumber);
                }
            }
        }
        return tableData;
    }

    @Override
    public void addDept(final SysOrganizationEntity org) {
        final SysOrganizationEntity sortOrg = sysOrganizationMapper.selectMaxSort();
        final Integer sort = sortOrg.getSort() == null ? 0 : sortOrg.getSort() + 10;
        org.setSort(sort);
        sysOrganizationMapper.saveOrg(org);
    }

    @Override
    public void moveToTargetDepartment(final Integer id, final String pid) {
        sysOrganizationMapper.moveToTargetDepartment(id,pid);
    }

    /**
     * 递归删除
     */
    private void getChildDataInfo2(final SysOrganizationEntity entity, final List<SysOrganizationEntity> dictList) {
        final List<SysOrganizationEntity> organizationList = sysOrganizationMapper.listAllOrganizationsByParentId(entity);
        if (StringUtils.isEmpty(organizationList)) {
            return;
        }
        for (final SysOrganizationEntity organizationEntity : organizationList) {
            sysOrganizationMapper.deleteSelectDepartment(organizationEntity);
            dictList.add(organizationEntity);
            getChildDataInfo2(organizationEntity, dictList);
        }
    }

    private void updateUserRedis(final SysOrganizationEntity organizationEntity) {
        //根据orgId查询到用户
        final List<SysNewUserEntity> userList = sysOrganizationMapper.findUserByOrgId(organizationEntity.getId());
        //遍历循环更改用户部门名称
        for (final SysNewUserEntity obj : userList) {
            //判断部门名称是否被修改
            final SysNewUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + obj.getId(), SysNewUserEntity.class);
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

    @Override
    public List<QueryEntity> queryDept(final String deptName) {
        return sysOrganizationMapper.queryDept(deptName);
	}

    @Override
    public List<QueryEntity> queryDeptCase(final String deptName) {
        final Integer id = JwtTokenUtil.tokenData().getInteger("userId");
        // 100
        final String department= sysUserMapper.queryDepartment(id);
        return sysOrganizationMapper.queryDeptCase(department,deptName);
    }

    @Override
    public List<SysOrganizationEntity> listAllOrgsWithUserNum() {
        List<SysOrganizationEntity> orgs = sysOrganizationMapper.listAllOrgsWithUserNum();
        boolean isDone = false;
        for (SysOrganizationEntity org : orgs) {
            boolean isleaf = true;
            for (SysOrganizationEntity temp : orgs) {
                if(org.getId().equals(temp.getParentId())){
                    isleaf = false;
                    org.setTempChildrenSize(org.getTempChildrenSize()+1);
                }
            }
            if(isleaf){
                org.setTempStatus(1);
            }
        }
        while (!isDone) {
            for (int i = 0; i < orgs.size(); i++) {
                SysOrganizationEntity org = orgs.get(i);
                if(!org.getTempStatus().equals(0)){
                    continue;
                }
                for (int j = 0; j < orgs.size(); j++) {
                    SysOrganizationEntity temp = orgs.get(j);
                    if(temp.getParentId().equals(org.getId()) && temp.getTempStatus().equals(1)){
                        org.setUserNum(org.getUserNum()+temp.getUserNum());
                        org.setTempChildrenSize(org.getTempChildrenSize()-1);
                        temp.setTempStatus(2);
                        orgs.set(j, temp);
                    }
                }
                if(org.getTempChildrenSize().equals(0)){
                    org.setTempStatus(1);
                    orgs.set(i, org);
                }
            }
            isDone = orgs.get(0).getTempChildrenSize().equals(0);
        }
        return orgs;
    }
}
