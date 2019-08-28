package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.utils.RedisUtils;

import java.text.SimpleDateFormat;
import java.util.*;
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
        String[] timeAreaAttr = {"00:00-8:00","08:00-12:00","12:00-18:00","18:00-24:00"};
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odv, SysUserEntity.class);
        StatisticReturn conInfo = new StatisticReturn();
        conInfo.setOdv(user.getUserName());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<CollectionStatistic> colList = new ArrayList<>();
        CollectionStatistic collectionStatistic = new CollectionStatistic();
        collectionStatistic.setOdv(odv);
        collectionStatistic.setClients(bean.getClients());
        collectionStatistic.setAreas(bean.getAreas());
        collectionStatistic.setDateSearchStart(bean.getDateSearchStart());
        collectionStatistic.setDateSearchEnd(bean.getDateSearchEnd());
        List<CollectionStatistic> conList = dataCollectionTelMapper.statisticsCollectionCon(collectionStatistic);
        List<CollectionStatistic> sumList = dataCollectionTelMapper.statisticsCollectionSum(collectionStatistic);
        List<CollectionStatistic> caseList = dataCollectionTelMapper.statisticsCollectionCase(collectionStatistic);
        HashSet<Integer> sets = new HashSet<>();
        for (String str : timeAreaAttr){
            CollectionStatistic col = new CollectionStatistic();
            // 时间区域
            col.setArea(str);
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
                }
            }
            for (CollectionStatistic collection : conList){
                if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTimeDate()))).getTime()>= dateStart.getTime()
                        && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTimeDate()))).getTime()<= dateEnd.getTime()){
                    conNum++;
                }
            }
            for (CollectionStatistic collection : caseList){
                 sets.add( collection.getId()) ;
                if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                        && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()<= dateEnd.getTime()){
                    caseNum++;
                }
            }
            col.setCountPhoneNum(telNum);
            col.setCountConPhoneNum(conNum);
            col.setCountCasePhoneNum(caseNum);
            colList.add(col);
        }
        conInfo.setList(colList);
        conInfo.setSumCasePhoneNum(sets.size());
        conInfo.setSumConPhoneNum(conList.size());
        conInfo.setSumPhoneNum(sumList.size());
        list.add(conInfo);
        return list;
    }
}
