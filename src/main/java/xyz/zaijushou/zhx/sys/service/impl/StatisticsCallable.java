package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
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
public class StatisticsCallable implements Callable<List<DataCollectionEntity>> {

    DataCollectionEntity collection;
    List<DataCollectionEntity> list;
    int index;

    StatisticsCallable(List<DataCollectionEntity> list, DataCollectionEntity dataCollectionEntity, int index){
        this.collection = dataCollectionEntity;
        this.list = list;
        this.index = index;
    }

    public List<DataCollectionEntity> call() throws Exception{

        SysDictionaryEntity client = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ collection.getClient(), SysDictionaryEntity.class);
        collection.setClient(client==null?"":client.getName());
        collection.setRepaidAmtM(collection.getCpMoney().multiply(collection.getmVal()));
        collection.setRepaidBankAmtM(collection.getEnRepayAmt().multiply(collection.getmVal()));
        SysDictionaryEntity account_age = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ collection.getAccountAge(), SysDictionaryEntity.class);
        collection.setAccountAge(account_age==null?"":account_age.getName());
        SysUserEntity temp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ collection.getConfimName(), SysUserEntity.class);
        collection.setConfimName(temp==null?"":temp.getUserName());
        collection.setBankTime(collection.getBankTime()==null?"":(collection.getBankTime().equals("")?"":collection.getBankTime().substring(0,10)) );
        collection.setRepayTime(collection.getRepayTime()==null?"":(collection.getRepayTime().equals("")?"":collection.getRepayTime().substring(0,10)) );
        collection.setmValMsg(collection.getmVal()==null?"": FmtMicrometer.fmtMicrometer(collection.getmVal().stripTrailingZeros()+""));
        collection.setBankAmtMsg(collection.getBankAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBankAmt().stripTrailingZeros()+""));
        collection.setEnRepayAmtMsg(collection.getEnRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getEnRepayAmt().stripTrailingZeros()+""));
        collection.setNewMoneyMsg(collection.getNewMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getNewMoney().stripTrailingZeros()+""));
        collection.setBalanceMsg(collection.getBalance()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBalance().stripTrailingZeros()+""));
        collection.setMoneyMsg(collection.getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getMoney().stripTrailingZeros()+""));
        collection.setRepayAmtMsg(collection.getRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepayAmt().stripTrailingZeros()+""));
        collection.setRepaidAmtMMsg(collection.getRepaidAmtM()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepaidAmtM().stripTrailingZeros()+""));
        collection.setRepaidBankAmtMMsg(collection.getRepaidBankAmtM()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepaidBankAmtM().stripTrailingZeros()+""));
        list.set(index,collection);
        return list;
    }

}
