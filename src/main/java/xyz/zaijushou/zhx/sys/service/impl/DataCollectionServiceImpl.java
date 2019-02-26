package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CollectSortEnum;
import xyz.zaijushou.zhx.constant.ColorEnum;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
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
public class DataCollectionServiceImpl implements DataCollectionService {
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;

    @Resource
    private SysDictionaryMapper dictionaryMapper;

    @Resource
    private SysUserService sysUserService;//用户业务控制层

    @Override
    public void save(DataCollectionEntity dataCollectionEntity){

    }
    @Override
    public void update(DataCollectionEntity dataCollectionEntity){
    }
    @Override
    public void delete(DataCollectionEntity dataCollectionEntity){

    }
    @Override
    public List<DataCollectionEntity> pageDataCollectionList(DataCollectionEntity dataCollectionEntity){
        List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollection(dataCollectionEntity);
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            list.set(i,temp);
        }
        return list;
    }

    @Override
    public WebResponse pageMyCase(DataCollectionEntity dataCollectionEntity){
        String[] clients = dataCollectionEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCollectionEntity.setClientFlag(null);
        }else{
            dataCollectionEntity.setClientFlag("1");
        }
        String[] batchs = dataCollectionEntity.getBatchNos();
        if (batchs == null || batchs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchs[0])){
            dataCollectionEntity.setBatchFlag(null);
        }else{
            dataCollectionEntity.setBatchFlag("1");
        }
        if (StringUtils.isEmpty(dataCollectionEntity.getColor())) {

        }else{
            dataCollectionEntity.setColor(ColorEnum.getEnumByKey(dataCollectionEntity.getColor()).getValue());
        }
        WebResponse webResponse = WebResponse.buildResponse();
        CollectionReturnEntity collectionReturn = new CollectionReturnEntity();

        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return webResponse;
        }

        if (dataCollectionEntity.getsType() == 0){//查询个人
            dataCollectionEntity.setOdv(user.getUserName());
        }
        if(StringUtils.isEmpty(dataCollectionEntity.getOrderBy())){
            dataCollectionEntity.setOrderBy("id");
        }else {
            dataCollectionEntity.setOrderBy(CollectSortEnum.getEnumByKey(dataCollectionEntity.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(dataCollectionEntity.getSort())){
            dataCollectionEntity.setSort(" desc");
        }
        List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollect(dataCollectionEntity);
        int count = dataCollectionMapper.countDataCollect(dataCollectionEntity);
        int countCase = 0;//列表案量
        BigDecimal sumMoney = new BigDecimal("0.00");//列表金额
        int countCasePay = 0;//列表还款案量
        BigDecimal sumPayMoney = new BigDecimal("0.00");//列表还款数额
        BigDecimal sumRepay = new BigDecimal("0.00");//列表CP值
        BigDecimal sumBank = new BigDecimal("0.00");//列表PTP值
        List<String> caseIds = new ArrayList<String>();//案件ID数组
        if(StringUtils.isEmpty(list)) {
            return  webResponse;
        }
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            List<SysDictionaryEntity> dictList = dictionaryMapper.getDataById(temp.getCollectStatus());
            if (dictList.size() > 0) {
                SysDictionaryEntity sysDictionaryEntity = dictList.get(0);
                temp.setCollectStatusMsg(sysDictionaryEntity.getName());
            }
            list.set(i,temp);
        }
        for (DataCollectionEntity collection : list){
            if(!caseIds.contains(collection.getCaseId())){
                caseIds.add(collection.getCaseId());
                ++countCase;
                sumMoney = sumMoney.add(collection.getMoney()==null?new BigDecimal("0"):collection.getMoney());
                if (collection.getCaseStatus() == 1){
                    ++countCasePay;
                    sumPayMoney = sumPayMoney.add(collection.getEnRepayAmt()==null?new BigDecimal("0"):collection.getEnRepayAmt());
                }
            }
            sumRepay = sumRepay.add(collection.getRepayAmt()==null?new BigDecimal("0"):collection.getRepayAmt());
            sumBank = sumBank.add(collection.getBankAmt()==null?new BigDecimal("0"):collection.getBankAmt());
        }
        int totalPageNum = 0 ;
        if (count%dataCollectionEntity.getPageSize()>0){
            totalPageNum = count/dataCollectionEntity.getPageSize()+1;
        }else{
            totalPageNum = count/dataCollectionEntity.getPageSize();
        }

        collectionReturn.setList(list);
        collectionReturn.setCountCase(countCase);
        collectionReturn.setCountCasePay(countCasePay);
        collectionReturn.setSumBank(sumRepay);
        collectionReturn.setSumMoney(sumMoney);
        collectionReturn.setSumRepay(sumBank);
        collectionReturn.setSumPayMoney(sumPayMoney);
        webResponse.setData(collectionReturn);
        webResponse.setTotalNum(count);
        webResponse.setTotalPageNum(totalPageNum);
        return webResponse;
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutPasswordById(user);
    }


    /**
     * 单日电催量
     * @param beanInfo
     * @return
     */
    @Override
    public List<CollectionStatistic> statisticsCollectionDay(CollectionStatistic beanInfo){
        List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return colList;
        }
        // dataCollectionEntity.setObv(user.getUserName());//当前用户

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd ");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        Calendar time = Calendar.getInstance();
        try {

            Date date = sdf2.parse("09:00");
            Date dateStart = new Date();
            Date dateEnd = new Date();
            for (int i=0 ;i <12 ;i++){
                dateStart = date;
                time.setTime(dateStart);
                time.add(Calendar.HOUR,1);
                dateEnd = time.getTime();
                date = dateEnd;
                CollectionStatistic bean = new CollectionStatistic();
                //循环查询日期条件的时间范围
                beanInfo.setDateSearchStart(beanInfo.getDateSearchStart()==null?new Date():beanInfo.getDateSearchStart());
                beanInfo.setDateSearchEnd(beanInfo.getDateSearchEnd()==null?new Date():beanInfo.getDateSearchEnd());
                int countSum = 0;
                int countCon = 0;
                int countCase = 0;
                Calendar dTime = Calendar.getInstance();
                dTime.setTime(beanInfo.getDateSearchStart());
                while(!dTime.getTime().after(beanInfo.getDateSearchEnd())){
                    bean.setDateStart(sdf.parse(sdf1.format(dTime.getTime()) + sdf2.format(dateStart)));
                    bean.setDateEnd(sdf.parse(sdf1.format(dTime.getTime())+sdf2.format(dateEnd)));;
                    countSum += dataCollectionTelMapper.statisticsCollectionSum(bean);
                    countCon += dataCollectionTelMapper.statisticsCollectionCon(bean);
                    countCase += dataCollectionTelMapper.statisticsCollectionCase(bean);
                    dTime.add(Calendar.DAY_OF_MONTH,1);
                }
                bean.setCountPhoneNum(countSum);
                bean.setCountConPhoneNum(countCon);
                bean.setCountCasePhoneNum(countCase);
                bean.setComlun(i);
                colList.add(bean);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return colList;
    }



    /**
     * 催收状况统计
     * @param beanInfo
     * @return
     */
    @Override
    public WebResponse pageStatisticsCollectionState(CollectionStatistic beanInfo){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = beanInfo.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            beanInfo.setClientFlag(null);
        }else{
            beanInfo.setClientFlag("1");
        }
        String[] batchs = beanInfo.getBatchNos();
        if (batchs == null || batchs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchs[0])){
            beanInfo.setBatchFlag(null);
        }else{
            beanInfo.setBatchFlag("1");
        }
        List<CollectionStatistic> colList =
                dataCollectionMapper.statisticsCollectionState(beanInfo);
        int count = dataCollectionMapper.countStatisticsCollectionState(beanInfo);
        int totalPageNum = 0 ;
        if (count%beanInfo.getPageSize()>0){
            totalPageNum = count/beanInfo.getPageSize()+1;
        }else{
            totalPageNum = count/beanInfo.getPageSize();
        }
        webResponse.setTotalNum(count);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setData(colList);
        return webResponse;
    }

    /**
     * 批次分类统计
     * @param beanInfo
     * @return
     */
    @Override
    public WebResponse pageStatisticsCollectionBatch(CollectionStatistic beanInfo){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = beanInfo.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            beanInfo.setClientFlag(null);
        }else{
            beanInfo.setClientFlag("1");
        }
        String[] batchs = beanInfo.getBatchNos();
        if (batchs == null || batchs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchs[0])){
            beanInfo.setBatchFlag(null);
        }else{
            beanInfo.setBatchFlag("1");
        }
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return webResponse;
        }
        if (beanInfo.getsType() == 0){//查询个人
            beanInfo.setOdv(user.getUserName());//当前用户
        }
        List<CollectionStatistic> colList =
                dataCollectionMapper.statisticsCollectionBatch(beanInfo);
        int count = dataCollectionMapper.countStatisticsCollectionBatch(beanInfo);
        int totalPageNum = 0 ;
        if (count%beanInfo.getPageSize()>0){
            totalPageNum = count/beanInfo.getPageSize()+1;
        }else{
            totalPageNum = count/beanInfo.getPageSize();
        }
        webResponse.setTotalNum(count);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setData(colList);
        return webResponse;
    }

    /**
     * 我的还款统计
     * @param beanInfo
     * @return
     */
    @Override
    public WebResponse pageStatisticsCollectionPay(CollectionStatistic beanInfo){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = beanInfo.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            beanInfo.setClientFlag(null);
        }else{
            beanInfo.setClientFlag("1");
        }
        String[] odvs = beanInfo.getOdvs();
        if (odvs == null || odvs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(odvs[0])){
            beanInfo.setOdvFlag(null);
        }else{
            beanInfo.setOdvFlag("1");
        }
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return webResponse;
        }
        if (beanInfo.getsType() == 0){//查询个人
            beanInfo.setOdv(user.getUserName());//当前用户
        }
        CollectionStatistic collectionReturn = new CollectionStatistic();
        List<DataCollectionEntity> colList = new ArrayList<DataCollectionEntity>();
        colList = dataCollectionMapper.pageStatisticsCollectionPay(beanInfo);//我的还款列表统计查询
        int count = dataCollectionMapper.countStatisticsCollectionPay(beanInfo);
        int totalPageNum = 0 ;
        if (count%beanInfo.getPageSize()>0){
            totalPageNum = count/beanInfo.getPageSize()+1;
        }else{
            totalPageNum = count/beanInfo.getPageSize();
        }
        collectionReturn.setList(colList);

        //我的还款统计，上月和当月金额统计 查询
        getLastMData(collectionReturn,beanInfo);
        getThisMData(collectionReturn,beanInfo);
        //获取三个不同列表的统计金额
        getStatisticsData(collectionReturn,beanInfo);

        webResponse.setTotalNum(count);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setData(collectionReturn);
        return webResponse;
    }

    private void getStatisticsData(CollectionStatistic collectionReturn,CollectionStatistic beanInfo){
        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfo);
        collectionReturn.setBankAmtC(collectonStatic.getBankAmtC());
        collectionReturn.setPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setRepayAmtP(collectonStatic.getRepayAmtP());
        collectionReturn.setRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }

    private void getLastMData(CollectionStatistic collectionReturn,CollectionStatistic beanInfo){
        CollectionStatistic beanInfoData = new CollectionStatistic();
        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timeStart.add(Calendar.MONTH, -1);    //得到前一个月
        timeStart.set(Calendar.DAY_OF_MONTH,1);//设置为1号
        beanInfoData.setMonthStart(sdf1.format(timeStart.getTime()));//上月第一天
        timeEnd.set(Calendar.DAY_OF_MONTH,0);
        beanInfoData.setMonthEnd(sdf1.format(timeEnd.getTime()));//上月最后一天
        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfoData);
        collectionReturn.setLastBankAmt(collectonStatic.getBankAmtC());
        collectionReturn.setLastPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setLastRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setLastRepayAmt(collectonStatic.getRepayAmtP());
        collectionReturn.setLastRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }
   /* paidMoney;//已还款金额
    bankAmt;//上月銀行对账金额
    repayAmt;//上月承諾还款金额-PTP
    repaidAmt;//已还款金额的提成金额（M）
    repaidBankAmt;//月银行查账金额的提成金额（M）*/

    private void getThisMData(CollectionStatistic collectionReturn,CollectionStatistic beanInfo){
        CollectionStatistic beanInfoData = new CollectionStatistic();
        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timeStart.add(Calendar.MONTH, 0);    //得到前一个月
        timeStart.set(Calendar.DAY_OF_MONTH,1);//设置为1号
        beanInfoData.setMonthStart(sdf1.format(timeStart.getTime()));//月第一天
        timeEnd.set(Calendar.DAY_OF_MONTH, timeEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        beanInfoData.setMonthEnd(sdf1.format(timeEnd.getTime()));//月最后一天
        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfoData);
        collectionReturn.setThisBankAmt(collectonStatic.getBankAmtC());
        collectionReturn.setThisPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setThisRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setThisRepayAmt(collectonStatic.getRepayAmtP());
        collectionReturn.setThisRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }
}
