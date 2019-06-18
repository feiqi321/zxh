package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class DayCallable implements Callable<List<StatisticReturn>> {

    DataCollectionTelMapper dataCollectionTelMapper;
    List<StatisticReturn> list;
    String odv;
    CollectionStatistic bean ;

    DayCallable(List<StatisticReturn> list,String odv,DataCollectionTelMapper dataCollectionTelMapper,CollectionStatistic bean){
        this.list = list;
        this.odv = odv;
        this.bean = bean;
        this.dataCollectionTelMapper = dataCollectionTelMapper;
    }

    public List<StatisticReturn> call() throws Exception{

        int sumConPhoneNum = 0;//接通电话数
        int sumPhoneNum = 0;//总通话数
        int sumCasePhoneNum = 0;//通话涉及到的案件数
        String[] timeAreaAttr = {"00:00-8:00","08:00-12:00","12:00-18:00","18:00-24:00"};
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odv, SysUserEntity.class);
        StatisticReturn conInfo = new StatisticReturn();
        conInfo.setOdv(user.getUserName());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
        bean.setOdv(odv);
        List<CollectionStatistic> sumList = dataCollectionTelMapper.statisticsCollectionSum(bean);
        List<CollectionStatistic> conList = dataCollectionTelMapper.statisticsCollectionCon(bean);
        List<CollectionStatistic> caseList = dataCollectionTelMapper.statisticsCollectionCase(bean);
        for (String str : timeAreaAttr){
            CollectionStatistic col = new CollectionStatistic();
            col.setArea(str);//时间区域
            col.setOdv(odv);
            Date dateStart = sdf2.parse(str.split("-")[0]);
            Date dateEnd = sdf2.parse(str.split("-")[1]);
            Calendar dTime = Calendar.getInstance();
            dTime.setTime(bean.getDateSearchStart());
            int telNum = 0;
            int conNum = 0;
            int caseNum = 0;
            for (CollectionStatistic collection : sumList){
                if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                        && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()<= dateEnd.getTime()){
                    telNum++;
                    sumPhoneNum++;
                }
            }
            for (CollectionStatistic collection : conList){
                if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                        && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()<= dateEnd.getTime()){
                    conNum++;
                    sumConPhoneNum++;
                }
            }
            for (CollectionStatistic collection : caseList){
                if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                        && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()<= dateEnd.getTime()){
                    caseNum++;
                    sumCasePhoneNum++;
                }
            }
            col.setCountPhoneNum(telNum);
            col.setCountConPhoneNum(conNum);
            col.setCountCasePhoneNum(caseNum);
            colList.add(col);
        }
        conInfo.setList(colList);
        conInfo.setSumCasePhoneNum(sumCasePhoneNum);
        conInfo.setSumConPhoneNum(sumConPhoneNum);
        conInfo.setSumPhoneNum(sumPhoneNum);
        list.add(conInfo);

        return list;
    }

}
