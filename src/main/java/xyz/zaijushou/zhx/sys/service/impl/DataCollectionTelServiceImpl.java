package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.CaseBaseConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectionTelService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import javax.swing.text.html.Option;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCollectionTelServiceImpl implements DataCollectionTelService {
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;

    @Resource
    private SysDictionaryMapper sysDictionaryMapper;

    @Override
    public PageInfo<StatisticReturn> pageCollectionDay(CollectionStatistic bean){
        ExecutorService executor = Executors.newFixedThreadPool(20);
        if (StringUtils.isEmpty(bean.getDateSearchStart())){
            bean.setDateSearchStart(new Date());
            bean.setDateStart(new Date());
        }
        if (StringUtils.isEmpty(bean.getDateSearchEnd())){
            bean.setDateSearchEnd(new Date());
            bean.setDateEnd(new Date());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        String[] timeAreaAttr = {"00:00-8:00","08:00-12:00","12:00-18:00","18:00-24:00"};
        List<StatisticReturn> list = Lists.newArrayList();

        int pageNo = bean.getPageNum();
        int pageSize = bean.getPageSize();
        int total = 0;

        try {
            if(CollectionUtils.isEmpty(bean.getOdvAttr())){

                Set<String >  redisUserIds = RedisUtils.listAllKeyWithKeyPrefix(RedisKeyPrefix.USER_INFO);
                List<String>  allUserList  =  CollectionUtils.isEmpty(redisUserIds) ? Collections.EMPTY_LIST
                        : redisUserIds.stream().map(i->i.replaceFirst(RedisKeyPrefix.USER_INFO,"")).collect(Collectors.toList());
                bean.setOdvAttr(allUserList);
            }
            total = bean.getOdvAttr().size();
            int start = (pageNo-1)*pageSize;
            int end = start + pageSize;
            int k = 0;
            List<String> temp = new ArrayList<>();
            for (String odv: bean.getOdvAttr()){
                if(start > k  ){
                    continue;
                }
                if( k >= end){
                    break;
                }
                temp.add(odv);
                ++k;
            }
            bean.setOdvAttr(temp);
            for (String odv: bean.getOdvAttr()) {
                /*int sumConPhoneNum = 0;//接通电话数
                int sumPhoneNum = 0;//总通话数
                int sumCasePhoneNum = 0;//通话涉及到的案件数
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odv, SysUserEntity.class);
                StatisticReturn conInfo = new StatisticReturn();
                conInfo.setOdv(user.getUserName());
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
                list.add(conInfo);*/
                DayCallable dayCallable = new DayCallable(list,odv,dataCollectionTelMapper,bean);
                Future<List<StatisticReturn>> future = executor.submit(dayCallable);
            }
            executor.shutdown();
            while(true){
                if(executor.isTerminated()){
                    break;
                }
                Thread.sleep(100);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo p = PageInfo.of(list);
        p.setPageNum(pageNo);
        p.setPageSize(pageSize);
        p.setTotal(total);
        return  p;
    }

    @Override
    public PageInfo<StatisticReturn> pageCollectionMonth(CollectionStatistic bean){
        ExecutorService executor = Executors.newFixedThreadPool(20);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        if (StringUtils.isEmpty(bean.getMonthStart())){
            bean.setMonthStart(sdf.format(new Date()));
        }else {
            bean.setMonthStart(bean.getMonthStart()+"-01");
        }
        if (StringUtils.isEmpty(bean.getMonthEnd())){
            bean.setMonthEnd(sdf.format(new Date()));
        }else{
            bean.setMonthEnd(bean.getMonthEnd()+"-01");
        }
        List<StatisticReturn> list = Lists.newArrayList();

        if(CollectionUtils.isEmpty(bean.getOdvAttr())){

            Set<String >  redisUserIds = RedisUtils.listAllKeyWithKeyPrefix(RedisKeyPrefix.USER_INFO);
            List<String>  allUserList  =  CollectionUtils.isEmpty(redisUserIds) ? Collections.EMPTY_LIST
                    : redisUserIds.stream().map(i->i.replaceFirst(RedisKeyPrefix.USER_INFO,"")).collect(Collectors.toList());
            bean.setOdvAttr(allUserList);
        }

        int pageNo = bean.getPageNum();
        int pageSize = bean.getPageSize();

        int total = bean.getOdvAttr().size();
        int start = (pageNo-1)*pageSize;
        int end = start + pageSize;
        int k = 0;
        List<String> temp = new ArrayList<>();
        for (String odv: bean.getOdvAttr()){
            if(start > k  ){
                continue;
            }
            if( k >= end){
                break;
            }
            temp.add(odv);
            ++k;
        }
        bean.setOdvAttr(temp);
        try {
            for (String odv : bean.getOdvAttr()) {
            /*if(start > k  ){
                continue;
            }
            if( k >= end){
                break;
            }*/
           /* int sumConPhoneNum = 0;//接通电话数
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
            ++k;
            list.add(conInfo);*/
                MonthCallable monthCallable = new MonthCallable(list, odv, dataCollectionTelMapper, bean);
                Future<List<StatisticReturn>> future = executor.submit(monthCallable);
            }
            executor.shutdown();
            while (true) {
                if (executor.isTerminated()) {
                    break;
                }
                Thread.sleep(100);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        PageInfo p = PageInfo.of(list);
        p.setPageNum(pageNo);
        p.setPageSize(pageSize);
        p.setTotal(total);
        return  p;

    }

    private Date getDateInfo(Date date,int type){
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

    /**
     * 获得该月第一天
     * @param timeStr
     * @return
     */
    private static String getFirstDayOfMonth(String timeStr){
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
    private static String getLastDayOfMonth(String timeStr){
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

    @Override
    public PageInfo<CollectionStatistic> pageCollectionDayAction(CollectionStatistic bean){
        ExecutorService executor = Executors.newFixedThreadPool(20);
        if (StringUtils.isEmpty(bean.getDateSearchStart())){
            bean.setDateStart(new Date());
        }else {
            bean.setDateStart(bean.getDateSearchStart());
        }
        if (StringUtils.isEmpty(bean.getDateSearchEnd())){
            bean.setDateEnd(new Date());
        }else {
            bean.setDateEnd(bean.getDateSearchEnd());
        }
        List<CollectionStatistic> list = Lists.newArrayList();

        if(CollectionUtils.isEmpty(bean.getOdvAttr())){

            Set<String >  redisUserIds = RedisUtils.listAllKeyWithKeyPrefix(RedisKeyPrefix.USER_INFO);
            List<String>  allUserList  =  CollectionUtils.isEmpty(redisUserIds) ? Collections.EMPTY_LIST
                    : redisUserIds.stream().map(i->i.replaceFirst(RedisKeyPrefix.USER_INFO,"")).collect(Collectors.toList());
            bean.setOdvAttr(allUserList);
        }

        int pageNo = bean.getPageNum();
        int pageSize = bean.getPageSize();

        int total = bean.getOdvAttr().size();
        int start = (pageNo-1)*pageSize;
        int end = start + pageSize;
        int k = 0;
        List<String> temp = new ArrayList<>();
        for (String odv: bean.getOdvAttr()){
            if(start > k  ){
                continue;
            }
            if( k >= end){
                break;
            }
            temp.add(odv);
            ++k;
        }
        bean.setOdvAttr(temp);
        try {
            for (String odv: bean.getOdvAttr()) {
            /*if(start > k  ){
                continue;
            }
            if( k >= end){
                break;
            }*/
            /*bean.setOdv(odv);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odv, SysUserEntity.class);
            CollectionStatistic col = new CollectionStatistic();
            col.setOdv(user.getUserName());
            List<CollectionStatistic> colData = dataCollectionTelMapper.countCollectionDayAction(bean);
            List<SysDictionaryEntity> collectionIds = sysDictionaryMapper.getCollectionDataByDicId(CaseBaseConstant.COLLECTION_TYPE);
            List<String> descs = collectionIds.stream().map(each -> each.getDescription()).collect(Collectors.toList());
            ConnectionListToInfo(descs,colData, col);
            ++k;
            list.add(col);*/
                DayActionCallable dayActionCallable = new DayActionCallable(list, odv, dataCollectionTelMapper,sysDictionaryMapper, bean);
                Future<List<CollectionStatistic>> future = executor.submit(dayActionCallable);
            }
            executor.shutdown();
            while (true) {
                if (executor.isTerminated()) {
                    break;
                }
                Thread.sleep(100);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        PageInfo p = PageInfo.of(list);
        p.setPageNum(pageNo);
        p.setPageSize(pageSize);
        p.setTotal(total);
        return  p;
    }
    //1-114查询无效,2-DX1,3-DX2,4-DX3,5-DX4,6-承诺还款,7-可联本人,8-可联村委,
    // 9-可联第三人,10-可联家人,11-空号错号,12-网搜无效,13-无人接听,14-无效电话
    private void ConnectionListToInfo(List<String> descs, List<CollectionStatistic> colData,CollectionStatistic col){
        col.setCollectionDics(descs);
        List<Integer> numbers = Lists.newArrayList();
        for(String desc : descs) {
            Optional<CollectionStatistic> current = colData.stream().filter(each -> desc.equals(each.getCollectionResult())).findFirst();
            if (!current.isPresent()) {
                numbers.add(0);
            } else {
                numbers.add(current.get().getCountResult());
                col.setCountResult(col.getCountResult() + current.get().getCountResult());
            }
        }
        col.setCollectionDicResults(numbers);
//            switch (colInfo.getCollectionResult()){
//                case "114查询无效":
//                    col.setCountSearchNo(colInfo.getCountResult());
//                    break;
//                case "DX1":
//                    col.setCountDX1(colInfo.getCountResult());
//                    break;
//                case "DX2":
//                    col.setCountDX2(colInfo.getCountResult());
//                    break;
//                case "DX3":
//                    col.setCountDX3(colInfo.getCountResult());
//                    break;
//                case "DX4":
//                    col.setCountDX4(colInfo.getCountResult());
//                    break;
//
//                case "承诺还款":
//                    col.setCountRepay(colInfo.getCountResult());
//                    break;
//                case "可联本人":
//                    col.setCountConSelf(colInfo.getCountResult());
//                    break;
//                case "可联村委":
//                    col.setCountConVillage(colInfo.getCountResult());
//                    break;
//                case "可联第三人":
//                    col.setCountConVillage(colInfo.getCountResult());
//                    break;
//                case "可联家人":
//                    col.setCountConFamily(colInfo.getCountResult());
//                    break;
//
//                case "空号错号":
//                    col.setCountDeadNumber(colInfo.getCountResult());
//                    break;
//                case "网搜无效":
//                    col.setCountSearchInvalid(colInfo.getCountResult());
//                    break;
//                case "无人接听":
//                    col.setCountNoAnswer(colInfo.getCountResult());
//                    break;
//                case "无效电话":
//                    col.setCountInvalidCall(colInfo.getCountResult());
//                    break;
//                default:
//                    break;
//            }
//        }
    }


    @Override
    public PageInfo<CollectionStatistic> pageCollectionTelInfo(CollectionStatistic bean){
        if (StringUtils.isEmpty(bean.getDateStart())){
            bean.setDateStart(new Date());
        }else {
            bean.setDateStart(bean.getDateStart());
        }
        List<CollectionStatistic> list = dataCollectionTelMapper.pageCollectionTelInfo(bean);
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }
        return  PageInfo.of(list);
    }
}
