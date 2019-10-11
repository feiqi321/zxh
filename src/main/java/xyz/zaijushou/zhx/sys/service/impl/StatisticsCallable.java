package xyz.zaijushou.zhx.sys.service.impl;

import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class StatisticsCallable implements Callable<List<DataCollectionEntity>> {
    private static Logger logger = LoggerFactory.getLogger(StatisticsCallable.class);
    DataCollectionEntity collection;
    List<DataCollectionEntity> list;
    Map<Integer, SysUserEntity> userMap;
    Map<Integer, SysDictionaryEntity> dictMap;
    int index;

    StatisticsCallable(List<DataCollectionEntity> list, DataCollectionEntity dataCollectionEntity, Map<Integer, SysDictionaryEntity> dictMap,Map<Integer, SysUserEntity> userMap, int index){
        this.collection = dataCollectionEntity;
        this.list = list;
        this.dictMap = dictMap;
        this.userMap = userMap;
        this.index = index;
    }

    public List<DataCollectionEntity> call() throws Exception{
        logger.info(index+"***");
        if (StringUtils.isEmpty(collection.getClient())){
            collection.setClient("");
        }else{
            collection.setClient(dictMap.get(Integer.parseInt(collection.getClient()))==null?"":dictMap.get(Integer.parseInt(collection.getClient())).getName());
        }
        if (StringUtils.isEmpty(collection.getAccountAge())){
            collection.setAccountAge("");
        }else{
            collection.setAccountAge(dictMap.get(Integer.parseInt(collection.getAccountAge()))==null?"":dictMap.get(Integer.parseInt(collection.getAccountAge())).getName());
        }
        if (StringUtils.isEmpty(collection.getConfimName())){
            collection.setConfimName("");
        }else{
            collection.setConfimName(userMap.get(Integer.parseInt(collection.getConfimName()))==null?"":userMap.get(Integer.parseInt(collection.getConfimName())).getUserName());
        }
        SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + collection.getCollectStatus(), SysDictionaryEntity.class);
        collection.setCollectStatusMsg(sysDictionaryEntity == null ? "" : sysDictionaryEntity.getName());
        if (org.apache.commons.lang3.StringUtils.isEmpty(collection.getOdv())){
            collection.setOdv("");
        }else {
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ collection.getOdv(), SysUserEntity.class);
            collection.setOdv(user == null ? "" : user.getUserName()+"("+user.getDeptName()+")");
        }
        collection.setRepaidAmtM(collection.getCpMoney().multiply(collection.getmVal()));
        collection.setRepaidBankAmtM(collection.getEnRepayAmt().multiply(collection.getmVal()));
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
