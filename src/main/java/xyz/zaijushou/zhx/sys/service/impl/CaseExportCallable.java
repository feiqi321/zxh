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
            temp.setOdv(user == null ? "" : user.getUserName());
        }

        SysDictionaryEntity clientDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
        temp.setClient(clientDic==null?"":clientDic.getName());
        SysDictionaryEntity summaryDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getSummary(),SysDictionaryEntity.class);
        temp.setSummary(summaryDic==null?"":summaryDic.getName());
        SysDictionaryEntity collectionTypeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectionType(),SysDictionaryEntity.class);
        temp.setCollectionType(collectionTypeDic==null?"":collectionTypeDic.getName());
        if (StringUtils.notEmpty(temp.getDistributeHistory())){
            temp.setDistributeHistory(temp.getDistributeHistory().substring(1));
        }
        list.set(index,temp);
        return list;
    }

}
