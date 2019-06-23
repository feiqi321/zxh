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
public class CaseCallable implements Callable<List<DataCaseEntity>> {

    DataCaseEntity temp;
    List<DataCaseEntity> list;
    int index;

    CaseCallable(List<DataCaseEntity> list,DataCaseEntity dataCaseEntity,int index){
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
        if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getCollectArea())){
            temp.setCollectArea("");
        }else{
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectArea(),SysDictionaryEntity.class);
            temp.setCollectArea(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getDistributeHistory())){
            temp.setDistributeHistory(temp.getDistributeHistory().substring(1));
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getAccountAge())){
            temp.setAccountAge("");
        }else{
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            temp.setAccountAge(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
            temp.setOdv("");
        }else {
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user == null ? "" : user.getUserName());
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getColor())){
            temp.setColor("BLACK");
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
        temp.setMoneyMsg(temp.getMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getMoney().stripTrailingZeros()+""));
        temp.setBankAmtMsg(temp.getBankAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getBankAmt().stripTrailingZeros()+""));
        temp.setBalanceMsg(temp.getBalance()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getBalance().stripTrailingZeros()+""));
        temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getProRepayAmt().stripTrailingZeros()+""));
        temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt().stripTrailingZeros()+""));
        temp.setPrinciple(temp.getPrinciple()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getPrinciple()));
        temp.setLatestOverdueMoney(temp.getLatestCollectMomorize()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getLatestCollectMomorize()));
        list.set(index,temp);
        return list;
    }

}
