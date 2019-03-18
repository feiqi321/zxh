package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;
import xyz.zaijushou.zhx.sys.service.DataCollectionTelService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCollectionTelServiceImpl implements DataCollectionTelService {
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;


    @Override
    public PageInfo<StatisticReturn> pageCollectionDay(CollectionStatistic bean){
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
        List<StatisticReturn> list = dataCollectionTelMapper.pageCollectionDay(bean);

        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }

        try {
            for (StatisticReturn conInfo:list) {
                int sumConPhoneNum = 0;//接通电话数
                int sumPhoneNum = 0;//总通话数
                int sumCasePhoneNum = 0;//通话涉及到的案件数
                List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
                for (String str : timeAreaAttr){
                    CollectionStatistic col = new CollectionStatistic();
                    col.setArea(str);//时间区域
                    col.setOdv(conInfo.getOdv());
                    Date dateStart = sdf2.parse(str.split("-")[0]);
                    Date dateEnd = sdf2.parse(str.split("-")[1]);
                    Calendar dTime = Calendar.getInstance();
                    dTime.setTime(bean.getDateSearchStart());
                    while(!dTime.getTime().after(bean.getDateSearchEnd())){
                        col.setDateStart(sdf.parse(sdf1.format(dTime.getTime()) + sdf2.format(dateStart)));
                        col.setDateEnd(sdf.parse(sdf1.format(dTime.getTime())+sdf2.format(dateEnd)));
                        int telNum = dataCollectionTelMapper.statisticsCollectionSum(col);
                        int conNum = dataCollectionTelMapper.statisticsCollectionCon(col);
                        int caseNum = dataCollectionTelMapper.statisticsCollectionCase(col);
                        col.setCountPhoneNum(col.getCountPhoneNum()+telNum);
                        col.setCountConPhoneNum(col.getCountConPhoneNum()+conNum);
                        col.setCountCasePhoneNum(col.getCountCasePhoneNum()+caseNum);
                        sumPhoneNum += telNum;
                        sumConPhoneNum += conNum;
                        sumCasePhoneNum += caseNum;
                        dTime.add(Calendar.DAY_OF_MONTH,1);
                    }
                    colList.add(col);
                }
                conInfo.setList(colList);
                conInfo.setSumCasePhoneNum(sumCasePhoneNum);
                conInfo.setSumConPhoneNum(sumConPhoneNum);
                conInfo.setSumPhoneNum(sumPhoneNum);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  PageInfo.of(list);
    }

    @Override
    public PageInfo<StatisticReturn> pageCollectionMonth(CollectionStatistic bean){

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
        List<StatisticReturn> list = dataCollectionTelMapper.pageCollectionMonth(bean);;

        if(StringUtils.notEmpty(list)){
            for (StatisticReturn conInfo:list) {
                int sumConPhoneNum = 0;//接通电话数
                int sumPhoneNum = 0;//总通话数
                int sumCasePhoneNum = 0;//通话涉及到的案件数
                List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
                Calendar dTime = Calendar.getInstance();
                Date dateEnd = new Date() ;
                try {
                    dTime.setTime(sdf1.parse(bean.getMonthStart()));
                    dateEnd = sdf1.parse(bean.getMonthEnd());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                while(!dTime.getTime().after(dateEnd)){
                    CollectionStatistic col = new CollectionStatistic();
                    col.setOdv(conInfo.getOdv());
                    col.setDateStart(getDateInfo(dTime.getTime(),1));
                    col.setDateEnd(getDateInfo(dTime.getTime(),0));
                    int telNum = dataCollectionTelMapper.statisticsCollectionSum(col);
                    int conNum = dataCollectionTelMapper.statisticsCollectionCon(col);
                    int caseNum = dataCollectionTelMapper.statisticsCollectionCase(col);
                    col.setCountPhoneNum(col.getCountPhoneNum()+telNum);
                    col.setCountConPhoneNum(col.getCountConPhoneNum()+conNum);
                    col.setCountCasePhoneNum(col.getCountCasePhoneNum()+caseNum);
                    sumPhoneNum += telNum;
                    sumConPhoneNum += conNum;
                    sumCasePhoneNum += caseNum;
                    col.setArea(sdf1.format(dTime.getTime()));
                    colList.add(col);
                    dTime.add(Calendar.MONTH,1);
                }
                conInfo.setList(colList);
                conInfo.setSumCasePhoneNum(sumCasePhoneNum);
                conInfo.setSumConPhoneNum(sumConPhoneNum);
                conInfo.setSumPhoneNum(sumPhoneNum);
            }
        }else {
            StatisticReturn conInfo = new StatisticReturn();
            List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
            Calendar dTime = Calendar.getInstance();
            Date dateEnd = new Date() ;
            try {
                dTime.setTime(sdf1.parse(bean.getMonthStart()));
                dateEnd = sdf1.parse(bean.getMonthEnd());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            while(!dTime.getTime().after(dateEnd)){
                CollectionStatistic col = new CollectionStatistic();
                col.setDateStart(getDateInfo(dTime.getTime(),1));
                col.setDateEnd(getDateInfo(dTime.getTime(),0));
                col.setArea(sdf1.format(dTime.getTime()));
                colList.add(col);
                dTime.add(Calendar.MONTH,1);
            }
            conInfo.setList(colList);
            list.add(conInfo);
        }

        return  PageInfo.of(list);
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
        List<CollectionStatistic> list = dataCollectionTelMapper.pageCollectionDayAction(bean);
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }
        for (CollectionStatistic col : list){
            bean.setOdv(col.getOdv());
            List<CollectionStatistic> colData = dataCollectionTelMapper.countCollectionDayAction(bean);
            if (StringUtils.notEmpty(colData)){
                ConnectionListToInfo(colData,col);
            }
        }
        return  PageInfo.of(list);
    }
    //1-114查询无效,2-DX1,3-DX2,4-DX3,5-DX4,6-承诺还款,7-可联本人,8-可联村委,
    // 9-可联第三人,10-可联家人,11-空号错号,12-网搜无效,13-无人接听,14-无效电话
    private void ConnectionListToInfo(List<CollectionStatistic> colData,CollectionStatistic col){
        for (CollectionStatistic colInfo : colData){
            col.setCountResult(col.getCountResult()+colInfo.getCountResult());
            switch (colInfo.getCollectionResult()){
                case "114查询无效":
                    col.setCountSearchNo(colInfo.getCountResult());
                    break;
                case "DX1":
                    col.setCountDX1(colInfo.getCountResult());
                    break;
                case "DX2":
                    col.setCountDX2(colInfo.getCountResult());
                    break;
                case "DX3":
                    col.setCountDX3(colInfo.getCountResult());
                    break;
                case "DX4":
                    col.setCountDX4(colInfo.getCountResult());
                    break;

                case "承诺还款":
                    col.setCountRepay(colInfo.getCountResult());
                    break;
                case "可联本人":
                    col.setCountConSelf(colInfo.getCountResult());
                    break;
                case "可联村委":
                    col.setCountConVillage(colInfo.getCountResult());
                    break;
                case "可联第三人":
                    col.setCountConVillage(colInfo.getCountResult());
                    break;
                case "可联家人":
                    col.setCountConFamily(colInfo.getCountResult());
                    break;

                case "空号错号":
                    col.setCountDeadNumber(colInfo.getCountResult());
                    break;
                case "网搜无效":
                    col.setCountSearchInvalid(colInfo.getCountResult());
                    break;
                case "无人接听":
                    col.setCountNoAnswer(colInfo.getCountResult());
                    break;
                case "无效电话":
                    col.setCountInvalidCall(colInfo.getCountResult());
                    break;
                default:
                    break;
            }
        }
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
