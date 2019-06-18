package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.RedisUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class MonthCallable implements Callable<List<StatisticReturn>> {

    DataCollectionTelMapper dataCollectionTelMapper;
    List<StatisticReturn> list;
    String odv;
    CollectionStatistic bean ;

    MonthCallable(List<StatisticReturn> list, String odv, DataCollectionTelMapper dataCollectionTelMapper, CollectionStatistic bean){
        this.list = list;
        this.odv = odv;
        this.bean = bean;
        this.dataCollectionTelMapper = dataCollectionTelMapper;
    }

    /**
     * 获得该月第一天
     * @param timeStr
     * @return
     */
    public static String getFirstDayOfMonth(String timeStr){
        String[] strAttr= timeStr.split("-");
        int year = Integer.valueOf(strAttr[0]);
        int month = Integer.valueOf(strAttr[1]);
        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最小天数
        int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最小天数
        cal.set(Calendar.DAY_OF_MONTH, firstDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String firstDayOfMonth = sdf.format(cal.getTime());
        return firstDayOfMonth;
    }

    /**
     * 获得该月最后一天
     * @param timeStr
     * @return
     */
    public static String getLastDayOfMonth(String timeStr){
        String[] strAttr= timeStr.split("-");
        int year = Integer.valueOf(strAttr[0]);
        int month = Integer.valueOf(strAttr[1]);

        Calendar cal = Calendar.getInstance();
        //设置年份
        cal.set(Calendar.YEAR,year);
        //设置月份
        cal.set(Calendar.MONTH, month-1);
        //获取某月最大天数
        int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        //设置日历中月份的最大天数
        cal.set(Calendar.DAY_OF_MONTH, lastDay);
        //格式化日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String lastDayOfMonth = sdf.format(cal.getTime());
        return lastDayOfMonth;
    }

    public Date getDateInfo(Date date,int type){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        try {
            if (type == 1){
                return sdf.parse(getFirstDayOfMonth(sdf1.format(date))+" 00:00");
            }else{
                return sdf.parse(getLastDayOfMonth(sdf1.format(date))+" 23:59");
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public List<StatisticReturn> call() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        int sumConPhoneNum = 0;//接通电话数
        int sumPhoneNum = 0;//总通话数
        int sumCasePhoneNum = 0;//通话涉及到的案件数
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odv, SysUserEntity.class);
        StatisticReturn conInfo = new StatisticReturn();
        conInfo.setOdv(user .getUserName());
        List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
        bean.setOdv(odv);
        List<CollectionStatistic> sumList = dataCollectionTelMapper.statisticsCollectionSum(bean);
        List<CollectionStatistic> conList = dataCollectionTelMapper.statisticsCollectionCon(bean);
        List<CollectionStatistic> caseList = dataCollectionTelMapper.statisticsCollectionCase(bean);
        try {
            Calendar dTime = Calendar.getInstance();
            Date dateEnd = new Date() ;
            dTime.setTime(sdf1.parse(bean.getMonthStart()));
            dateEnd = sdf1.parse(bean.getMonthEnd());

            while(!dTime.getTime().after(dateEnd)){
                CollectionStatistic col = new CollectionStatistic();
                col.setOdv(conInfo.getOdv());
                col.setDateStart(getDateInfo(dTime.getTime(),1));
                col.setDateEnd(getDateInfo(dTime.getTime(),0));

                for (CollectionStatistic collection : sumList){
                    if(sdf.parse(collection.getCollectTime()).getTime()>= col.getDateStart().getTime()
                            && sdf.parse(collection.getCollectTime()).getTime()<= col.getDateEnd().getTime()){
                        sumPhoneNum++;
                    }
                }
                for (CollectionStatistic collection : conList){
                    if(sdf.parse(collection.getCollectTime()).getTime()>=  col.getDateStart().getTime()
                            && sdf.parse(collection.getCollectTime()).getTime()<=  col.getDateEnd().getTime()){
                        sumConPhoneNum++;
                    }
                }
                for (CollectionStatistic collection : caseList){
                    if(sdf.parse(collection.getCollectTime()).getTime()>=  col.getDateStart().getTime()
                            && sdf.parse(collection.getCollectTime()).getTime()<=  col.getDateEnd().getTime()){
                        sumCasePhoneNum++;
                    }
                }
                col.setCountPhoneNum(sumConPhoneNum);
                col.setCountConPhoneNum(sumPhoneNum);
                col.setCountCasePhoneNum(sumConPhoneNum);

                col.setArea(sdf1.format(dTime.getTime()));
                colList.add(col);
                dTime.add(Calendar.MONTH,1);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        conInfo.setList(colList);
        conInfo.setSumCasePhoneNum(sumCasePhoneNum);
        conInfo.setSumConPhoneNum(sumConPhoneNum);
        conInfo.setSumPhoneNum(sumPhoneNum);
        list.add(conInfo);

        return list;
    }

}
