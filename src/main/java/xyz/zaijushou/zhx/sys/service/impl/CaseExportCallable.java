package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class CaseExportCallable implements Callable<List<DataCaseEntity>> {

    DataCaseEntity temp;
    List<DataCaseEntity> list;
    int index;

    CaseExportCallable(List<DataCaseEntity> list, DataCaseEntity dataCaseEntity, int index){
        this.temp = dataCaseEntity;
        this.list = list;
        this.index = index;
    }

    public List<DataCaseEntity> call() throws Exception{

        if (temp.getCollectStatus()==0){
            temp.setCollectStatusMsg("");
        }else{
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
            temp.setOdv("");
        }else {
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setDept(user==null?"":user.getDeptName());
            temp.setOdv(user == null ? "" : user.getUserName());
        }

        if (temp!=null && temp.getStatus()!=null &&  temp.getStatus()==0){
            temp.setStatusMsg("未退案");
        }else if (temp!=null && temp.getStatus()!=null &&   temp.getStatus()==1){
            temp.setStatusMsg("正常");
        }else if (temp!=null && temp.getStatus()!=null &&   temp.getStatus()==2){
            temp.setStatusMsg("暂停");
        }else if (temp!=null && temp.getStatus()!=null &&   temp.getStatus()==3){
            temp.setStatusMsg("关挡");
        }else if (temp!=null && temp.getStatus()!=null &&   temp.getStatus()==4){
            temp.setStatusMsg("退档");
        }

        if (temp!=null && temp.getColor()!=null &&  temp.getColor().equals("RED")){
            temp.setColor("红色");
        }else if (temp!=null &&  temp.getColor()!=null &&  temp.getColor().equals("BLUE")){
            temp.setColor("蓝色");
        }else{
            temp.setColor("正常");
        }

        SysDictionaryEntity clientDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
        temp.setClient(clientDic==null?"":clientDic.getName());
        SysDictionaryEntity summaryDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getSummary(),SysDictionaryEntity.class);
        temp.setSummary(summaryDic==null?"":summaryDic.getName());
       /* SysDictionaryEntity collectionTypeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectionType(),SysDictionaryEntity.class);
        temp.setCollectionType(collectionTypeDic==null?"":collectionTypeDic.getName());*/
        SysDictionaryEntity accountAgeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
        temp.setAccountAge(accountAgeDic==null?"":accountAgeDic.getName());
        if (StringUtils.notEmpty(temp.getDistributeHistory())){
            temp.setDistributeHistory(temp.getDistributeHistory().substring(1));
        }


        return list;
    }

}
