package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class CollectCaseCallable implements Callable<List<DataCollectionEntity>> {

    DataCollectionEntity collection;
    List<DataCollectionEntity> list;
    int index;

    CollectCaseCallable(List<DataCollectionEntity> list, DataCollectionEntity dataCollectionEntity, int index){
        this.collection = dataCollectionEntity;
        this.list = list;
        this.index = index;
    }

    public List<DataCollectionEntity> call() throws Exception{

        SysDictionaryEntity telTypeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getTelType(),SysDictionaryEntity.class);
        collection.setTelType(telTypeDic==null?"":telTypeDic.getName());

        SysDictionaryEntity accountAgeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getAccountAge(),SysDictionaryEntity.class);
        collection.setAccountAge(accountAgeDic==null?"":accountAgeDic.getName());

        SysDictionaryEntity collectType =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getCollectionType(),SysDictionaryEntity.class);
        collection.setCollectionType(collectType==null?"":collectType.getName());

        SysDictionaryEntity collectInfoType =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getCollectInfo(),SysDictionaryEntity.class);
        collection.setCollectInfo(collectInfoType==null?"":collectInfoType.getName());

        SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getCollectStatus(),SysDictionaryEntity.class);
        collection.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

        if (org.apache.commons.lang3.StringUtils.isEmpty(collection.getOdv())){
            collection.setOdv("");
        }else {
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ collection.getOdv(), SysUserEntity.class);
            collection.setOdv(user == null ? "" : user.getUserName());
        }

        collection.setBankAmtMsg(collection.getBankAmt()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBankAmt()+""));
        collection.setEnRepayAmtMsg(collection.getEnRepayAmt()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(collection.getEnRepayAmt()+""));
        collection.setNewMoneyMsg(collection.getNewMoney()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(collection.getNewMoney()+""));
        collection.setBalanceMsg(collection.getBalance()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBalance()+""));
        collection.setMoneyMsg(collection.getMoney()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(collection.getMoney()+""));
        collection.setRepayAmtMsg(collection.getRepayAmt()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepayAmt()+""));

        if(collection.getDistributeStatus()==null || collection.getDistributeStatus()==2){
            collection.setDistributeStatusMsg("未分配");
        }else if (collection.getDistributeStatus()==1){
            collection.setDistributeStatusMsg("新分配");
        }else if (collection.getDistributeStatus()==3){
            collection.setDistributeStatusMsg("已分配");
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(collection.getCountFollow())){
            collection.setCountFollow("0");
        }
        list.set(index,collection);
        return list;
    }

}
