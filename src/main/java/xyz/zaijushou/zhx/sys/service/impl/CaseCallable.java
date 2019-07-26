package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.util.Calendar;
import java.util.Date;
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

        if (temp.getCollectDate()==null){
            temp.setCollectStatusMsg("新案");
            temp.setLeaveDays(0);
        }else {
            if (temp.getCollectStatus() == 0) {
                temp.setCollectStatusMsg("");
            } else {
                SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + temp.getCollectStatus(), SysDictionaryEntity.class);
                temp.setCollectStatusMsg(sysDictionaryEntity == null ? "" : sysDictionaryEntity.getName());
            }
            temp.setLeaveDays(differentDays(temp.getCollectTime()));
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
            temp.setOdv(user == null ? "" : user.getUserName()+"("+user.getDeptName()+")");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getColor())){
            temp.setColor("BLACK");
        }
        SysDictionaryEntity clientDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
        temp.setClient(clientDic==null?"":clientDic.getName());
        SysDictionaryEntity summaryDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getSummary(),SysDictionaryEntity.class);
        temp.setSummary(summaryDic==null?"":summaryDic.getName());
       /* SysDictionaryEntity collectionTypeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectionType(),SysDictionaryEntity.class);
        temp.setCollectionType(collectionTypeDic==null?"":collectionTypeDic.getName());*/
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


    public static int differentDays(Date date1)
    {

        if (date1 == null){
            return 0;
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(new Date());
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

}
