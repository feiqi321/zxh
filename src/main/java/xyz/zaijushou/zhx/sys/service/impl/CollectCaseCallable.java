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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class CollectCaseCallable implements Callable<List<DataCollectionEntity>> {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    DataCollectionEntity collection;
    List<DataCollectionEntity> list;
    Map collectMap;
    int index;

    CollectCaseCallable(List<DataCollectionEntity> list, DataCollectionEntity dataCollectionEntity, int index,Map collectMap){
        this.collection = dataCollectionEntity;
        this.list = list;
        this.index = index;
        this.collectMap = collectMap;
    }
    public static int differentDays(Date date1,Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)   //同一年
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)    //闰年
                {
                    timeDistance += 366;
                }
                else    //不是闰年
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else    //不同年
        {
         /*   System.out.println("判断day2 - day1 : " + (day2-day1));*/
            return day2-day1;
        }
    }
    public List<DataCollectionEntity> call() throws Exception{


            if (StringUtils.notEmpty(collection.getCollectTime())){
                collection.setLastPhoneTime(collection.getCollectTime());
                collection.setLeaveDays(differentDays(format.parse(collection.getCollectTime()),new Date())+"");
            }else{
                collection.setLastPhoneTime(collection.getDistributeTime());
                if (StringUtils.notEmpty(collection.getDistributeTime())) {
                    collection.setLeaveDays(differentDays(format.parse(collection.getDistributeTime()), new Date()) + "");
                }else{
                    collection.setLeaveDays("0");
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        DataCollectionEntity dataCollectionEntity = (DataCollectionEntity)collectMap.get(collection.getCaseId());
        collection.setCollectTime(dataCollectionEntity==null?"":sdf.format(dataCollectionEntity.getCollectDate()));

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

        collection.setBankAmtMsg(collection.getBankAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBankAmt().stripTrailingZeros()+""));
        collection.setEnRepayAmtMsg(collection.getEnRepayAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(collection.getEnRepayAmt().stripTrailingZeros()+""));
        collection.setNewMoneyMsg(collection.getNewMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(collection.getNewMoney().stripTrailingZeros()+""));
        collection.setBalanceMsg(collection.getBalance()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBalance().stripTrailingZeros()+""));
        collection.setMoneyMsg(collection.getMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(collection.getMoney().stripTrailingZeros()+""));
        collection.setRepayAmtMsg(collection.getRepayAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepayAmt().stripTrailingZeros()+""));

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
