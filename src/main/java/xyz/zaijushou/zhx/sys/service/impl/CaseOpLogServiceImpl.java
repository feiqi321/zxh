package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CaseOpSortEnum;
import xyz.zaijushou.zhx.constant.CaseSortEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.CaseOpLogMapper;
import xyz.zaijushou.zhx.sys.entity.CaseOpLog;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.CaseOpLogService;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/8/17.
 */
@Service
public class CaseOpLogServiceImpl implements CaseOpLogService{

    @Resource
    private CaseOpLogMapper caseOpLogMapper;

    public WebResponse pageCaseOpLog(CaseOpLog caseOpLog){
        if(org.apache.commons.lang3.StringUtils.isEmpty(caseOpLog.getOrderBy())){
            caseOpLog.setSort("desc");
            caseOpLog.setOrderBy("t1.id");
        }else {
            caseOpLog.setOrderBy(CaseOpSortEnum.getEnumByKey(caseOpLog.getOrderBy()).getValue());
        }
        if (StringUtils.notEmpty(caseOpLog.getCreateTimeStart())){
            caseOpLog.setCreateTimeStart(caseOpLog.getCreateTimeStart()+" 00:00:00");
        }
        if (StringUtils.notEmpty(caseOpLog.getCreateTimeEnd())){
            caseOpLog.setCreateTimeEnd(caseOpLog.getCreateTimeEnd()+" 23:59:59");
        }
        if (StringUtils.notEmpty(caseOpLog.getCaseDateStart())){
            caseOpLog.setCaseDateStart(caseOpLog.getCaseDateStart()+" 00:00:00");
        }
        if (StringUtils.notEmpty(caseOpLog.getCaseDateEnd())){
            caseOpLog.setCaseDateEnd(caseOpLog.getCaseDateEnd()+" 23:59:59");
        }
        if (StringUtils.notEmpty(caseOpLog.getExpectStartTime())){
            caseOpLog.setExpectStartTime(caseOpLog.getExpectStartTime()+" 00:00:00");
        }
        if (StringUtils.notEmpty(caseOpLog.getExpectEndTime())){
            caseOpLog.setExpectEndTime(caseOpLog.getExpectEndTime()+" 23:59:59");
        }
        if (StringUtils.notEmpty(caseOpLog.getSeqNo())) {
            caseOpLog.setSeqNos(caseOpLog.getSeqNo().split(","));
        }
        if (StringUtils.notEmpty(caseOpLog.getIdentNo())){
            caseOpLog.setIdentNos(caseOpLog.getIdentNo().split(","));
        }
        if (StringUtils.notEmpty(caseOpLog.getName())){
            caseOpLog.setNames(caseOpLog.getName().split(","));
        }

        List<CaseOpLog> list = caseOpLogMapper.pageCaseOpLog(caseOpLog);

        for(int i=0;i<list.size();i++){
            CaseOpLog tempCaseOpLog = list.get(i);
            //委托方
            SysDictionaryEntity clientDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+tempCaseOpLog.getClient(),SysDictionaryEntity.class);
            tempCaseOpLog.setClient(clientDic==null?"":clientDic.getName());
            //催收状态
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+tempCaseOpLog.getCollectStatus(),SysDictionaryEntity.class);
            tempCaseOpLog.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            if (tempCaseOpLog!=null && tempCaseOpLog.getStatus()!=null &&  tempCaseOpLog.getStatus()==0){
                tempCaseOpLog.setStatusMsg("未退案");
            }else if (tempCaseOpLog!=null && tempCaseOpLog.getStatus()!=null &&   tempCaseOpLog.getStatus()==1){
                tempCaseOpLog.setStatusMsg("正常");
            }else if (tempCaseOpLog!=null && tempCaseOpLog.getStatus()!=null &&   tempCaseOpLog.getStatus()==2){
                tempCaseOpLog.setStatusMsg("暂停");
            }else if (tempCaseOpLog!=null && tempCaseOpLog.getStatus()!=null &&   tempCaseOpLog.getStatus()==3){
                tempCaseOpLog.setStatusMsg("关挡");
            }else if (tempCaseOpLog!=null && tempCaseOpLog.getStatus()!=null &&   tempCaseOpLog.getStatus()==4){
                tempCaseOpLog.setStatusMsg("退档");
            }
            //催收员和催收部门
            SysUserEntity odvUser = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ tempCaseOpLog.getOdv(), SysUserEntity.class);
            tempCaseOpLog.setDept(odvUser==null?"":odvUser.getDeptName());
            tempCaseOpLog.setOdv(odvUser == null ? "" : odvUser.getUserName());

            //上级催收员
            SysUserEntity lastOdv = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ tempCaseOpLog.getLastOdv(), SysUserEntity.class);
            tempCaseOpLog.setLastOdv(lastOdv==null?"":lastOdv.getUserName());

            //操作员
            SysUserEntity operatorUser = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ tempCaseOpLog.getCreator(), SysUserEntity.class);
            tempCaseOpLog.setCreatorName(operatorUser==null?"":operatorUser.getUserName());

            list.set(i,tempCaseOpLog);
        }

        return WebResponse.success(PageInfo.of(list));
    }

}
