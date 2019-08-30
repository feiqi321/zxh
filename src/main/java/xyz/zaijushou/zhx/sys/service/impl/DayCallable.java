package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.utils.RedisUtils;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class DayCallable implements Callable<List<StatisticReturn2>> {

    DataCollectionTelMapper dataCollectionTelMapper;
    List<StatisticReturn2> list;
    String odv;
    CollectionStatistic bean;

    DayCallable(List<StatisticReturn2> list, String odv, DataCollectionTelMapper dataCollectionTelMapper, CollectionStatistic bean) {
        this.list = list;
        this.odv = odv;
        this.bean = bean;
        this.dataCollectionTelMapper = dataCollectionTelMapper;
    }

    @Override
    public List<StatisticReturn2> call() throws Exception {
        String[] timeAreaAttr = {"00:00-8:00", "08:00-12:00", "12:00-18:00", "18:00-24:00"};
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + odv, SysUserEntity.class);
        StatisticReturn2 conInfo = new StatisticReturn2();
        conInfo.setOdv(user.getUserName());
        conInfo.setId(user.getId());
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        List<CollectionStatisticDTO> colList = new ArrayList<>();
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
        for (String str : timeAreaAttr) {
            CollectionStatisticDTO col = new CollectionStatisticDTO();
            // 时间区域

            Date dateStart = sdf2.parse(str.split("-")[0]);
            Date dateEnd = sdf2.parse(str.split("-")[1]);
            Calendar dTime = Calendar.getInstance();
            dTime.setTime(bean.getDateSearchStart());
            int telNum = 0;
            int conNum = 0;
            int caseNum = 0;
            telNum = getNum(sdf2, sdf, sumList, dateStart, dateEnd, telNum);
            conNum = getNum(sdf2, sdf, conList, dateStart, dateEnd, conNum);
            HashSet<Integer> setId = getIntegers(sdf2, sdf, caseList, sets, dateStart, dateEnd);
            col.setCountPhoneNum(telNum);
            col.setCountConPhoneNum(conNum);
            col.setCountCasePhoneNum(setId.size());
            colList.add(col);
        }
        conInfo.setList(colList);
        conInfo.setSumCasePhoneNum(sets.size());
        conInfo.setSumConPhoneNum(conList.size());
        conInfo.setSumPhoneNum(sumList.size());
        list.add(conInfo);
        Collections.sort(list, new Comparator<StatisticReturn2>() {
            @Override
            public int compare(StatisticReturn2 o1, StatisticReturn2 o2) {
                return o1.getId() - o2.getId();
            }
        });
        return list;
    }

    private HashSet<Integer> getIntegers(SimpleDateFormat sdf2, SimpleDateFormat sdf, List<CollectionStatistic> caseList, HashSet<Integer> sets, Date dateStart, Date dateEnd) throws ParseException, ParseException {
        HashSet<Integer> setId = new HashSet<>();
        for (CollectionStatistic collection : caseList) {
            if (sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime() >= dateStart.getTime()
                    && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime() <= dateEnd.getTime()) {
                setId.add(collection.getId());
            }
            sets.add(collection.getId());
        }
        return setId;
    }

    private int getNum(SimpleDateFormat sdf2, SimpleDateFormat sdf, List<CollectionStatistic> listInfo, Date dateStart, Date dateEnd, int num) throws ParseException {
        for (CollectionStatistic collection : listInfo) {
            if (sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime() >= dateStart.getTime()
                    && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime() <= dateEnd.getTime()) {
                num++;
            }
        }
        return num;
    }
}
