package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.CollectionReturnEntity;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

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
    public CollectionReturnEntity pageMyCase(DataCollectionEntity dataCollectionEntity){
        CollectionReturnEntity collectionReturn = new CollectionReturnEntity();
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return collectionReturn;
        }
       // dataCollectionEntity.setTargetName(user.getUserName());//当前用户
        List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollect(dataCollectionEntity);
        int countCase = 0;//列表案量
        BigDecimal sumMoney = new BigDecimal("0.00");//列表金额
        int countCasePay = 0;//列表还款案量
        BigDecimal sumPayMoney = new BigDecimal("0.00");//列表还款数额
        BigDecimal sumRepay = new BigDecimal("0.00");//列表CP值
        BigDecimal sumBank = new BigDecimal("0.00");//列表PTP值
        List<String> caseIds = new ArrayList<String>();//案件ID数组
        if(StringUtils.isEmpty(list)) {
            collectionReturn.setList(new PageInfo<>());
            return  collectionReturn;
        }
        for (int i=0;i<list.size();i++){
           for (DataCollectionEntity collection : list){
               if(!caseIds.contains(collection.getCaseId())){
                   caseIds.add(collection.getCaseId());
                   ++countCase;
                   sumMoney = sumMoney.add(collection.getMoney());
                   if (collection.getCaseStatus() == 1){
                       ++countCasePay;
                       sumPayMoney = sumPayMoney.add(collection.getEnRepayAmt());
                   }
               }
               sumRepay = sumRepay.add(collection.getRepayAmt());
               sumBank = sumBank.add(collection.getBankAmt());
           }
        }
        collectionReturn.setList(PageInfo.of(list));
        collectionReturn.setCountCase(countCase);
        collectionReturn.setCountCasePay(countCasePay);
        collectionReturn.setSumBank(sumBank);
        collectionReturn.setSumMoney(sumMoney);
        collectionReturn.setSumRepay(sumRepay);
        collectionReturn.setSumPayMoney(sumPayMoney);
        return collectionReturn;
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
        // dataCollectionEntity.setTargetName(user.getUserName());//当前用户

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
                bean.setDateSearchEnd(beanInfo.getDateSearchEnd());
                bean.setDateSearchStart(beanInfo.getDateSearchStart());
                bean.setDateStart(sdf.parse(sdf1.format(bean.getDateSearchStart())+sdf2.format(dateStart)));;
                bean.setDateEnd(sdf.parse(sdf1.format(bean.getDateSearchEnd())+sdf2.format(dateEnd)));;

                int countSum = dataCollectionTelMapper.statisticsCollectionSum(bean);
                int countCon = dataCollectionTelMapper.statisticsCollectionCon(bean);
                int countCase = dataCollectionTelMapper.statisticsCollectionCase(bean);
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
    public PageInfo<CollectionStatistic> pageStatisticsCollectionState(CollectionStatistic beanInfo){
        List<CollectionStatistic> colList =
                dataCollectionMapper.statisticsCollectionState(beanInfo);
        return PageInfo.of(colList);
    }

    /**
     * 批次分类统计
     * @param beanInfo
     * @return
     */
    @Override
    public PageInfo<CollectionStatistic> pageStatisticsCollectionBatch(CollectionStatistic beanInfo){
        List<CollectionStatistic> colList =
                dataCollectionMapper.statisticsCollectionBatch(beanInfo);
        return PageInfo.of(colList);
    }

    /**
     * 我的还款统计
     * @param beanInfo
     * @return
     */
    @Override
    public CollectionStatistic pageStatisticsCollectionPay(CollectionStatistic beanInfo){
        CollectionStatistic collectionReturn = new CollectionStatistic();
        List<DataCollectionEntity> colList = new ArrayList<DataCollectionEntity>();
        colList = dataCollectionMapper.pageStatisticsCollectionPay(beanInfo);//我的还款列表统计查询
        collectionReturn.setList(PageInfo.of(colList));

        //我的还款统计，上月和当月金额统计 查询
        getLastMData(collectionReturn,beanInfo);
        getThisMData(collectionReturn,beanInfo);

        return collectionReturn;
    }

    private void getLastMData(CollectionStatistic collectionReturn,CollectionStatistic beanInfo){
        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timeStart.add(Calendar.MONTH, -1);    //得到前一个月
        timeStart.set(Calendar.DAY_OF_MONTH,1);//设置为1号
        beanInfo.setMonthStart(sdf1.format(timeStart.getTime()));//上月第一天
        timeEnd.set(Calendar.DAY_OF_MONTH,0);
        beanInfo.setMonthEnd(sdf1.format(timeEnd.getTime()));//上月最后一天
        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfo);
        collectionReturn.setLastBankAmt(collectonStatic.getBankAmt());
        collectionReturn.setLastPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setLastRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setLastRepayAmt(collectonStatic.getRepayAmt());
        collectionReturn.setLastRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }
   /* paidMoney;//已还款金额
    bankAmt;//上月銀行对账金额
    repayAmt;//上月承諾还款金额-PTP
    repaidAmt;//已还款金额的提成金额（M）
    repaidBankAmt;//月银行查账金额的提成金额（M）*/

    private void getThisMData(CollectionStatistic collectionReturn,CollectionStatistic beanInfo){
        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timeStart.add(Calendar.MONTH, 0);    //得到前一个月
        timeStart.set(Calendar.DAY_OF_MONTH,1);//设置为1号
        beanInfo.setMonthStart(sdf1.format(timeStart.getTime()));//月第一天
        timeEnd.set(Calendar.DAY_OF_MONTH, timeEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        beanInfo.setMonthEnd(sdf1.format(timeEnd.getTime()));//月最后一天
        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfo);
        collectionReturn.setThisBankAmt(collectonStatic.getBankAmt());
        collectionReturn.setThisPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setThisRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setThisRepayAmt(collectonStatic.getRepayAmt());
        collectionReturn.setThisRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }
}
