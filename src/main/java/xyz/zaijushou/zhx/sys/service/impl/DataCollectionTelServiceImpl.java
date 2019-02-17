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
import java.util.*;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCollectionTelServiceImpl implements DataCollectionTelService {
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;

    @Resource
    private SysUserService sysUserService;//用户业务控制层

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
        if (StringUtils.isEmpty(bean.getDateSearchStart())){
            bean.setDateSearchStart(new Date());
        }
        if (StringUtils.isEmpty(bean.getDateSearchEnd())){
            bean.setDateSearchEnd(new Date());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        List<StatisticReturn> list = dataCollectionTelMapper.pageCollectionMonth(bean);;

        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }

        try {
            for (StatisticReturn conInfo:list) {
                int sumConPhoneNum = 0;//接通电话数
                int sumPhoneNum = 0;//总通话数
                int sumCasePhoneNum = 0;//通话涉及到的案件数
                List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
                Calendar dTime = Calendar.getInstance();
                dTime.setTime(bean.getDateSearchStart());
                CollectionStatistic col = new CollectionStatistic();
                col.setOdv(conInfo.getOdv());
                while(!dTime.getTime().after(bean.getDateSearchEnd())){
                    col.setDateStart(sdf.parse(sdf1.format(dTime.getTime())));
                    col.setDateEnd(sdf.parse(sdf1.format(dTime.getTime())));
                    int telNum = dataCollectionTelMapper.statisticsCollectionSum(col);
                    int conNum = dataCollectionTelMapper.statisticsCollectionCon(col);
                    int caseNum = dataCollectionTelMapper.statisticsCollectionCase(col);
                    col.setCountPhoneNum(col.getCountPhoneNum()+telNum);
                    col.setCountConPhoneNum(col.getCountConPhoneNum()+conNum);
                    col.setCountCasePhoneNum(col.getCountCasePhoneNum()+caseNum);
                    sumPhoneNum += telNum;
                    sumConPhoneNum += conNum;
                    sumCasePhoneNum += caseNum;
                    dTime.add(Calendar.MONTH,1);
                }
                colList.add(col);
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

    /**
     * 获得该月第一天
     * @param year
     * @param month
     * @return
     */
    private static String getFirstDayOfMonth(int year,int month){
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
     * @param year
     * @param month
     * @return
     */
    private static String getLastDayOfMonth(int year,int month){
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
        List<CollectionStatistic> list = dataCollectionTelMapper.pageCollectionDayAction(bean);
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }
        return  PageInfo.of(list);
    }
}
