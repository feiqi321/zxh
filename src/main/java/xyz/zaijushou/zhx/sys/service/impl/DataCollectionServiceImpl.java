package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CaseBaseConstant;
import xyz.zaijushou.zhx.constant.ColorEnum;
import xyz.zaijushou.zhx.constant.MyCollectSortEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCollectionServiceImpl implements DataCollectionService {
    private static Logger logger = LoggerFactory.getLogger(DataCollectionServiceImpl.class);
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;
    @Resource
    private SysUserMapper sysUserMapper;//用户业务控制层
    @Resource
    private DataCollectionMapper collectionMapper;//催收数据层
    @Resource
    private DataCaseMapper caseMapper;//案件数据层
    @Autowired
    private DataLogService dataLogService;
    @Autowired
    private SysOrganizationService sysOrganizationService;
    @Resource
    private SysDictionaryMapper sysDictionaryMapper;
    @Resource
    private SysPercentMapper sysPercentMapper;
    @Resource
    private OdvMapper odvMapper;
    @Resource
    private ManageMapper manageMapper;
    @Resource
    private DataCaseRepayRecordMapper dataCaseRepayRecordMapper;



    @Override
    public void save(DataCollectionEntity beanInfo){
        if(StringUtils.isEmpty(beanInfo.getCollectInfo())){
            //获取催收模板的通话记录
            SysDictionaryEntity sysBean =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+beanInfo.getModule(),SysDictionaryEntity.class);
            if (sysBean != null){
                beanInfo.setCollectInfo(sysBean.getDescription());
            }
        }
        SysUserEntity sysUserEntity = getUserInfo();
        beanInfo.setCreateUser(sysUserEntity);
        SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+beanInfo.getCollectStatus(),SysDictionaryEntity.class);
        DataCollectionTelEntity dataCollectionTelEntity = new DataCollectionTelEntity();
        dataCollectionTelEntity.setUserData(beanInfo.getSeq());
        dataCollectionTelEntity = dataCollectionTelMapper.findAll(dataCollectionTelEntity);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        beanInfo.setCollectTime(dataCollectionTelEntity==null?null:(dataCollectionTelEntity.getCollectTime()==null?null:sdf.format(dataCollectionTelEntity.getCollectTime())));
        if (dataCollectionTelEntity!=null && dataCollectionTelEntity.getTimeLength()!=null && dataCollectionTelEntity.getTimeLength()/60>0){
            beanInfo.setIsEnable(1);
        }else{
            beanInfo.setIsEnable(0);
        }
        beanInfo.setOdv(sysUserEntity==null?"":sysUserEntity.getId()+"");
        dataCollectionMapper.saveCollection(beanInfo);

        if (StringUtils.notEmpty(beanInfo.getsType())){
            if (beanInfo.getsType() == 1){
                //状态同步到同批次下的同身份证号的所有催收信息的状态
                DataCaseEntity caseInfo = new DataCaseEntity();
                if (StringUtils.isEmpty(beanInfo.getCaseId())){
                    return ;
                }
                caseInfo.setId(Integer.valueOf(beanInfo.getCaseId()));
                List<DataCaseEntity> listInfo = caseMapper.listAllCaseInfo(caseInfo);
                if (StringUtils.isEmpty(listInfo)){
                    return ;
                }
                DataCollectionEntity bean = new DataCollectionEntity();
                bean.setBatchNo(listInfo.get(0).getBatchNo());
                bean.setIdentNo(listInfo.get(0).getIdentNo());
                bean.setCollectStatus(beanInfo.getCollectStatus());
                collectionMapper.updateDataCollect(bean);
            }
        }

        DataOpLog log = new DataOpLog();
        log.setType("电话催收");
        if (sysDictionaryEntity==null || StringUtils.isEmpty(sysDictionaryEntity.getName())){
            log.setContext("联系人："+(beanInfo.getTargetName()==null?"未知":beanInfo.getTargetName())+"，电话号码："+(beanInfo.getMobile()==null?"未知":beanInfo.getMobile())+"[手机]，通话内容："+(beanInfo.getCollectInfo()==null?"":beanInfo.getCollectInfo()));
        }else {
            log.setContext("联系人：" + (beanInfo.getTargetName() == null ? "未知" : beanInfo.getTargetName()) + "，电话号码：" + (beanInfo.getMobile() == null ? "未知" : beanInfo.getMobile()) + "[手机]，通话内容：" + (beanInfo.getCollectInfo() == null ? "" : beanInfo.getCollectInfo()) + "，催收状态： " + (sysDictionaryEntity == null ? "" : sysDictionaryEntity.getName()));
        }
        log.setOper(getUserInfo().getId());
        log.setOperName(getUserInfo().getUserName());

        log.setOpTime(sdf.format(new Date()));
        log.setCaseId(beanInfo.getCaseId());
        dataLogService.saveDataLog(log);

        DataCaseEntity dataCaseEntity = new DataCaseEntity();
        dataCaseEntity.setId(Integer.parseInt(beanInfo.getCaseId()));
        dataCaseEntity.setNewCase(beanInfo.getCollectInfo());
        dataCaseEntity.setSummary(beanInfo.getResult());
        dataCaseEntity.setCollectStatus(beanInfo.getCollectStatus());
        dataCaseEntity.setProRepayAmt(beanInfo.getRepayAmt());
        dataCaseEntity.setProRepayDate(beanInfo.getRepayTime());
        caseMapper.updateDataCaseByCollectAdd(dataCaseEntity);
    }

    public void detailSave(DataCollectionEntity beanInfo){
        if(beanInfo.getId()==null || beanInfo.getId()==0){
            dataCollectionMapper.detailSave(beanInfo);
            DataCaseEntity dataCaseEntity = new DataCaseEntity();
            dataCaseEntity.setId(Integer.parseInt(beanInfo.getCaseId()));
            dataCaseEntity.setNewCase(beanInfo.getCollectInfo());
            caseMapper.updateDataCaseByCollect(dataCaseEntity);
        }else{
            dataCollectionMapper.detailUpdate(beanInfo);
            DataCaseEntity dataCaseEntity = new DataCaseEntity();
            dataCaseEntity.setId(Integer.parseInt(beanInfo.getCaseId()));
            dataCaseEntity.setNewCase(beanInfo.getCollectInfo());
            caseMapper.updateDataCaseByCollect2(dataCaseEntity);
        }


    }

    public void detailDel(DataCollectionEntity beanInfo){
        dataCollectionMapper.detailDel(beanInfo);

    }

    @Override
    public void update(DataCollectionEntity dataCollectionEntity){
        dataCollectionMapper.updateCollection(dataCollectionEntity);
        DataCollectionEntity temp = dataCollectionMapper.findById(dataCollectionEntity);
        DataCaseEntity dataCaseEntity = new DataCaseEntity();
        dataCaseEntity.setId(Integer.parseInt(temp.getCaseId()));
        dataCaseEntity.setNewCase(dataCollectionEntity.getCollectInfo());
        dataCaseEntity.setCollectDate(dataCollectionEntity.getCollectTime());
        caseMapper.updateDataCaseByCollect3(dataCaseEntity);

    }


    @Override
    public List<DataCollectionEntity> pageDataCollectionList(DataCollectionEntity dataCollectionEntity){
        List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollection(dataCollectionEntity);
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            temp.setBankAmtMsg(temp.getBankAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getBankAmt()+""));
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt()+""));
            temp.setNewMoneyMsg(temp.getNewMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getNewMoney()+""));
            temp.setBalanceMsg(temp.getBalance()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getBalance()+""));
            temp.setMoneyMsg(temp.getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getMoney()+""));
            temp.setRepayAmtMsg(temp.getRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getRepayAmt()+""));
            list.set(i,temp);
        }
        return list;
    }

    @Override
    @Transactional
    public WebResponse pageMyCase(DataCollectionEntity dataCollectionEntity) throws Exception{
        ExecutorService executor = Executors.newFixedThreadPool(20);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
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
        if (StringUtils.notEmpty(dataCollectionEntity.getNewCase()) && dataCollectionEntity.getNewCase().equals("1")){
            dataCollectionEntity.setDistributeStatus(1);
        }else if (StringUtils.notEmpty(dataCollectionEntity.getNewCase()) && dataCollectionEntity.getNewCase().equals("0")){
            dataCollectionEntity.setDistributeStatus(3);
        }
        if (dataCollectionEntity.getStatuss()!=null && Arrays.asList(dataCollectionEntity.getStatuss()).contains("0") ) {
            String[] newStatuss = new String[dataCollectionEntity.getStatuss().length+3];
            for (int i=0;i<dataCollectionEntity.getStatuss().length;i++){
                newStatuss[i] = dataCollectionEntity.getStatuss()[i];
            }
            newStatuss[dataCollectionEntity.getStatuss().length]="1";
            newStatuss[dataCollectionEntity.getStatuss().length+1]="2";
            newStatuss[dataCollectionEntity.getStatuss().length+2]="3";
            dataCollectionEntity.setStatuss(newStatuss);
        }
        if (dataCollectionEntity.getStatuss()!=null && Arrays.asList(dataCollectionEntity.getStatuss()).contains("0") ) {
            String[] newStatuss = new String[dataCollectionEntity.getStatuss().length+3];
            for (int i=0;i<dataCollectionEntity.getStatuss().length;i++){
                newStatuss[i] = dataCollectionEntity.getStatuss()[i];
            }
            newStatuss[dataCollectionEntity.getStatuss().length]="1";
            newStatuss[dataCollectionEntity.getStatuss().length+1]="2";
            newStatuss[dataCollectionEntity.getStatuss().length+2]="3";
            dataCollectionEntity.setStatuss(newStatuss);
        }
        if (dataCollectionEntity.getStatuss()!=null && Arrays.asList(dataCollectionEntity.getStatuss()).contains("1") ) {
            String[] newStatuss = new String[dataCollectionEntity.getStatuss().length+2];
            for (int i=0;i<dataCollectionEntity.getStatuss().length;i++){
                newStatuss[i] = dataCollectionEntity.getStatuss()[i];
            }
            newStatuss[dataCollectionEntity.getStatuss().length]="0";
            newStatuss[dataCollectionEntity.getStatuss().length+1]="2";
            dataCollectionEntity.setStatuss(newStatuss);
        }
        if (dataCollectionEntity.getStatuss()!=null && Arrays.asList(dataCollectionEntity.getStatuss()).contains("5")) {
            dataCollectionEntity.setStatuss(null);
        }
        WebResponse webResponse = WebResponse.buildResponse();
        CollectionReturnEntity collectionReturn = new CollectionReturnEntity();


        if(StringUtils.isEmpty(dataCollectionEntity.getOrderBy())){
            dataCollectionEntity.setOrderBy("id");
        }else {
            if (MyCollectSortEnum.getEnumByKey(dataCollectionEntity.getOrderBy())==null){
                dataCollectionEntity.setOrderBy("id");
            }else{
                dataCollectionEntity.setOrderBy(MyCollectSortEnum.getEnumByKey(dataCollectionEntity.getOrderBy()).getValue());
            }

        }
        if (StringUtils.isEmpty(dataCollectionEntity.getSort())){
            dataCollectionEntity.setSort("desc");
        }else if (dataCollectionEntity.getOrderBy().equals("leaveDays")){
            if (dataCollectionEntity.getSort().equals("desc")){
                dataCollectionEntity.setSort("asc");
            }else{
                dataCollectionEntity.setSort("desc");
            }
        }
        logger.info("********************开始查询案件");
        if (StringUtils.notEmpty(dataCollectionEntity.getLastFollDateStart())){
            dataCollectionEntity.setLastFollDateStart(dataCollectionEntity.getLastFollDateStart() +"  00:00:00");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getLastFollDateEnd())){
            dataCollectionEntity.setLastFollDateEnd(dataCollectionEntity.getLastFollDateEnd() +"  23:59:59");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getCaseAllotTimeStart())){
            dataCollectionEntity.setCaseAllotTimeStart(dataCollectionEntity.getCaseAllotTimeStart() +"  00:00:00");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getCaseAllotTimeEnd())){
            dataCollectionEntity.setCaseAllotTimeEnd(dataCollectionEntity.getCaseAllotTimeEnd() +"  23:59:59");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getNextFollDateStart())){
            dataCollectionEntity.setNextFollDateStart(dataCollectionEntity.getNextFollDateStart() +"  00:00:00");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getNextFollDateEnd())){
            dataCollectionEntity.setNextFollDateEnd(dataCollectionEntity.getNextFollDateEnd() +"  23:59:59");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getExpectTimeStart())){
            dataCollectionEntity.setExpectTimeStart(dataCollectionEntity.getExpectTimeStart() +"  00:00:00");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getExpectTimeEnd())){
            dataCollectionEntity.setExpectTimeEnd(dataCollectionEntity.getExpectTimeEnd() +"  23:59:59");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getRepayTimeStart())){
            dataCollectionEntity.setRepayTime(dataCollectionEntity.getRepayTimeStart() +"  00:00:00");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getRepayTimeEnd())){
            dataCollectionEntity.setRepayTime(dataCollectionEntity.getRepayTimeEnd() +"  23:59:59");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getCaseDateStart())){
            dataCollectionEntity.setCaseDateStart(dataCollectionEntity.getCaseDateStart() +"  00:00:00");
        }
        if (StringUtils.notEmpty(dataCollectionEntity.getCaseDateEnd())){
            dataCollectionEntity.setCaseDateEnd(dataCollectionEntity.getCaseDateEnd() +"  23:59:59");
        }
        List<DataCollectionEntity> list =  dataCollectionMapper.pageMyCollect(dataCollectionEntity);

        logger.info("********************查询案件结束");

        dataCollectionEntity.setPageNum(null);
        dataCollectionEntity.setPageSize(null);
        DataCollectionEntity dataCollectionEntity1= dataCollectionMapper.querySum1(dataCollectionEntity);


        int countCase = 0;//列表案量
        BigDecimal sumMoney = new BigDecimal("0");//列表金额
        int countCasePay = 0;//列表还款案量
        BigDecimal sumPayMoney = new BigDecimal("0");//列表还款数额
        BigDecimal  bankAmt= new BigDecimal("0");//列表待銀行对账金额
        BigDecimal  repayAmt= new BigDecimal("0");//列表承诺还款金额
        List<String> caseIds = new ArrayList<String>();//案件ID数组
        if(StringUtils.isEmpty(list)) {
            collectionReturn.setCountCase(countCase);
            collectionReturn.setCountCasePay(countCasePay);
            collectionReturn.setSumMoneyMsg(sumMoney==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(sumMoney.stripTrailingZeros()+""));
            collectionReturn.setSumPayMoneyMsg(sumPayMoney==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(sumPayMoney.stripTrailingZeros()+""));
            collectionReturn.setBankAmtMsg(bankAmt==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(bankAmt.stripTrailingZeros()+""));
            collectionReturn.setRepayAmtMsg(repayAmt==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(repayAmt.stripTrailingZeros()+""));
            webResponse.setData(collectionReturn);
            return  webResponse;
        }
        logger.info("********************开始处理结果集");
        for (int i=0;i<list.size();i++){
            DataCollectionEntity collection = list.get(i);
            CollectCaseCallable collectCaseCallable = new CollectCaseCallable(list,collection,i);
            Future<List<DataCollectionEntity>> future = executor.submit(collectCaseCallable);
        }
        executor.shutdown();
        while(true){
            if(executor.isTerminated()){
                break;
            }
            Thread.sleep(100);
        }
        logger.info("********************处理完毕结果集");
        int count = new Long(PageInfo.of(list).getTotal()).intValue() ;
        collectionReturn.setList(list);
        collectionReturn.setCountCase(count);
        collectionReturn.setCountCasePay(dataCollectionEntity1.getCountCasePay());
        collectionReturn.setBankAmtMsg(dataCollectionEntity1.getBankAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(dataCollectionEntity1.getBankAmt().stripTrailingZeros()+""));
        collectionReturn.setSumMoneyMsg(dataCollectionEntity1.getSumMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(dataCollectionEntity1.getSumMoney().stripTrailingZeros()+""));
        collectionReturn.setRepayAmtMsg(dataCollectionEntity1.getRepayAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(dataCollectionEntity1.getRepayAmt().stripTrailingZeros()+""));
        collectionReturn.setSumPayMoneyMsg(dataCollectionEntity1.getSumPayMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(dataCollectionEntity1.getSumPayMoney().stripTrailingZeros()+""));
        webResponse.setData(collectionReturn);
        webResponse.setTotalNum(count);
        return webResponse;
    }


    @Override
    public List<DataCollectionEntity> listCaseBatchIdNo(DataCollectionEntity beanInfo){
        DataCaseEntity caseInfo = new DataCaseEntity();
        if (StringUtils.isEmpty(beanInfo.getCaseId())){
            return new ArrayList<>();
        }
        caseInfo.setId(Integer.valueOf(beanInfo.getCaseId()));
        List<DataCaseEntity> listInfo = caseMapper.listAllCaseInfo(caseInfo);
        if (StringUtils.isEmpty(listInfo)){
            return new ArrayList<>();
        }


        DataCollectionEntity bean = new DataCollectionEntity();
        if (listInfo.size()>0 && StringUtils.notEmpty(listInfo.get(0).getBatchNo())) {
            caseInfo.setBatchNo(listInfo.get(0).getBatchNo());
            caseInfo.setIdentNo(listInfo.get(0).getIdentNo());
            List<DataCaseEntity> samelistInfo = caseMapper.listSameAllCaseInfo(caseInfo);
            int[] ids = new int[samelistInfo.size()];
            for (int i=0;i<samelistInfo.size();i++){
                ids[i] = samelistInfo.get(i).getId();
            }
            bean.setIds(ids);
            List<DataCollectionEntity> list = collectionMapper.listDataCollectByIds(bean);
            if (StringUtils.isEmpty(list)) {
                return new ArrayList<>();
            } else {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                for (int i=0;i<list.size();i++){
                    DataCollectionEntity temp = list.get(i);
                    if (temp==null){
                        continue;
                    }
                    temp.setCollectTime(temp.getCollectDate()==null?"":sdf.format(temp.getCollectDate()));
                    temp.setCaseDate(temp.getCaseDateD()==null?"":sdf.format(temp.getCaseDateD()));
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                    temp.setOdv(user==null?"":user.getUserName());
                    list.set(i,temp);
                }
                return list;
            }
        }else{
            return new ArrayList<>();
        }
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return user;
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
        beanInfo.setOdv(user.getId()+"");//当前用户

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        Calendar time = Calendar.getInstance();

        try {
            Date startNow = sdf.parse(sdf1.format(new Date())+" 00:00:01");
            Date endNow = sdf.parse(sdf1.format(new Date())+" 23:59:59");
            //循环查询日期条件的时间范围
            beanInfo.setDateSearchStart(beanInfo.getDateSearchStart()==null ? startNow:beanInfo.getDateSearchStart());
            beanInfo.setDateSearchEnd(beanInfo.getDateSearchEnd()==null ? endNow:beanInfo.getDateSearchEnd());
            List<CollectionStatistic> sumList = dataCollectionTelMapper.statisticsCollectionSum(beanInfo);
            List<CollectionStatistic> conList = dataCollectionTelMapper.statisticsCollectionCon(beanInfo);
            List<CollectionStatistic> caseList = dataCollectionTelMapper.statisticsCollectionCase(beanInfo);

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

                int countSum = 0;
                int countCon = 0;
                int countCase = 0;
                Calendar dTime = Calendar.getInstance();
                dTime.setTime(beanInfo.getDateSearchStart());

                for (CollectionStatistic collection : sumList){
                    if(collection.getCollectTime()!=null && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                            && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime() <= dateEnd.getTime()){
                        countSum++;
                    }
                }
                for (CollectionStatistic collection : conList){
                    if(collection.getCollectTime()!=null && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                            && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()<= dateEnd.getTime()){
                        countCon++;
                    }
                }
                for (CollectionStatistic collection : caseList){
                    if(collection.getCollectTime()!=null && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                            && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()<= dateEnd.getTime()){
                        countCase++;
                    }
                }
                bean.setTimeArea(sdf2.format(dateStart)+"-"+sdf2.format(dateEnd));
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
    public WebResponse statisticsCollectionState(CollectionStatistic beanInfo){
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.add(Calendar.MONTH, -1);
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        beanInfo.setDateStart(cal_1.getTime());


        //获取前月的最后一天
        Calendar cale = Calendar.getInstance();
        cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
        beanInfo.setDateEnd(cale.getTime());

        List<CollectionStatistic> colList =
                dataCollectionMapper.statisticsCollectionState(beanInfo);
       /* int count = dataCollectionMapper.countStatisticsCollectionState(beanInfo);*/
        List<CollectionStatistic> resultList = new ArrayList<CollectionStatistic>();
        CollectionStatistic nullCollectionStatistic = new CollectionStatistic();
        for(CollectionStatistic colInfno : colList){
            if(StringUtils.notEmpty(colInfno.getCollectStatus())){
                SysDictionaryEntity collectDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ colInfno.getCollectStatus(), SysDictionaryEntity.class);
                if (collectDic==null || collectDic.getName()==null || collectDic.getName().equals("")){
                    nullCollectionStatistic.setCollectStatusMsg("未知");
                    nullCollectionStatistic.setPaidMoney((nullCollectionStatistic.getPaidMoney()==null?new BigDecimal(0):nullCollectionStatistic.getPaidMoney()).add(colInfno.getPaidMoney().stripTrailingZeros()));
                    nullCollectionStatistic.setCommisionMoney((nullCollectionStatistic.getCommisionMoney()==null?new BigDecimal(0):nullCollectionStatistic.getCommisionMoney()).add(colInfno.getCommisionMoney().stripTrailingZeros()));
                    nullCollectionStatistic.setSumCase(nullCollectionStatistic.getSumCase()+colInfno.getSumCase());
                }else {
                    if (colInfno.getPaidMoney()==null){
                        colInfno.setPaidMoney(new BigDecimal(0));
                    }else{
                        colInfno.setPaidMoney(colInfno.getPaidMoney().stripTrailingZeros());
                    }
                    if (colInfno.getCommisionMoney()==null){
                        colInfno.setCommisionMoney(new BigDecimal(0));
                    }else{
                        colInfno.setCommisionMoney(colInfno.getCommisionMoney().stripTrailingZeros());
                    }
                    colInfno.setCollectStatusMsg(collectDic == null ? "" : collectDic.getName());
                    resultList.add(colInfno);
                }
            }else{
                nullCollectionStatistic.setCollectStatusMsg("未知");
                nullCollectionStatistic.setPaidMoney((nullCollectionStatistic.getPaidMoney()==null?new BigDecimal(0):nullCollectionStatistic.getPaidMoney()).add(colInfno.getPaidMoney()==null?new BigDecimal(0):colInfno.getPaidMoney()).stripTrailingZeros());
                nullCollectionStatistic.setCommisionMoney((nullCollectionStatistic.getCommisionMoney()==null?new BigDecimal(0):nullCollectionStatistic.getCommisionMoney()).add(colInfno.getCommisionMoney()==null?new BigDecimal(0):colInfno.getCommisionMoney()).stripTrailingZeros());
                nullCollectionStatistic.setSumCase(nullCollectionStatistic.getSumCase()+colInfno.getSumCase());
            }
        }
        if(StringUtils.notEmpty(nullCollectionStatistic.getCollectStatusMsg())){
            resultList.add(nullCollectionStatistic);
        }
/*        webResponse.setTotalNum(count);*/
        webResponse.setData(resultList);
        return webResponse;
    }

    /**
     * 批次分类统计
     * @param beanInfo
     * @return
     */
    @Override
    public WebResponse statisticsCollectionBatch(CollectionStatistic beanInfo){
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
            beanInfo.setOdv(user.getId()+"");//当前用户
        }
        List<CollectionStatistic> colList =
                dataCollectionMapper.statisticsCollectionBatch(beanInfo);
        //int count = dataCollectionMapper.countStatisticsCollectionBatch(beanInfo);
        //webResponse.setTotalNum(count);
        webResponse.setData(colList);
        return webResponse;
    }

    /**
     * 我的还款统计
     * @param beanInfo
     * @return
     */
    @Override
    public WebResponse statisticsCollectionPay(CollectionStatistic beanInfo) throws Exception{
        ExecutorService thisExecutor = Executors.newFixedThreadPool(20);
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
            beanInfo.setOdv(user.getId()+"");//当前用户
        }
        CollectionStatistic collectionReturn = new CollectionStatistic();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        if (beanInfo.getExpectTimeStart()==null || beanInfo.getExpectTimeStart().equals("")){
            //获取前月的第一天
            Calendar   cal_1=Calendar.getInstance();//获取当前日期
            cal_1.add(Calendar.MONTH, -1);
            cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
            collectionReturn.setExpectTimeStart(format.format(cal_1.getTime()));
        }
        if (beanInfo.getExpectTimeEnd()==null || beanInfo.getExpectTimeEnd().equals("")) {
            //获取前月的最后一天
            Calendar cale = Calendar.getInstance();
            cale.set(Calendar.DAY_OF_MONTH, 0);//设置为1号,当前日期既为本月第一天
            collectionReturn.setExpectTimeEnd(format.format(cale.getTime()));
        }
        //我的还款列表统计查询
        PageHelper.startPage(beanInfo.getPageNum(), beanInfo.getPageSize());
        List<DataCollectionEntity> colList = dataCollectionMapper.pageStatisticsCollectionPay(beanInfo);
        List<SysDictionaryEntity> dictionaryList = sysDictionaryMapper.getDataList(new SysDictionaryEntity());
        Map<Integer, SysDictionaryEntity> dictMap = new HashMap<>();
        for(SysDictionaryEntity entity : dictionaryList) {
            dictMap.put(entity.getId(), entity);
        }
        List<SysUserEntity> userList = sysUserMapper.listAllUsers(new SysUserEntity());
        Map<Integer, SysUserEntity> userMap = new HashMap<>();
        for(int i=0;i<colList.size();i++){
            SysUserEntity temp = userList.get(i);
            userMap.put(temp.getId(),temp);
        }
        for(int i=0;i<colList.size();i++){
            DataCollectionEntity collection = colList.get(i);

            StatisticsCallable caseCallable = new StatisticsCallable(colList,collection,dictMap,userMap,i);
            Future<List<DataCollectionEntity>> future = thisExecutor.submit(caseCallable);
        }
        thisExecutor.shutdown();
        while(true){
            if(thisExecutor.isTerminated()){
                break;
            }
            Thread.sleep(100);
        }
        collectionReturn.setList(colList);


        //如果转正时间没有或者不在本月，则按照正常判断进行，
        // 如果在本月的1号-25号之间，则本月所有的累计还款都要跟1万比较，低于1万没有提成，如果在25号-月末之间，则不判断1万的底线，全部都要计算提成
        //我的还款统计，上月和当月金额统计 查询
       /* getThisMData(collectionReturn,actualTime,tempCase);
        getLastMData(collectionReturn,actualTime,tempCase);*/
        getOldLastMData(collectionReturn,beanInfo);
        getOldThisMData(collectionReturn,beanInfo);
        //获取三个不同列表的统计金额
        getStatisticsData(collectionReturn,beanInfo);
        int count = new Long(PageInfo.of(colList).getTotal()).intValue() ;
        collectionReturn.setTotalNum(count);
        /*webResponse.setTotalNum(count);*/
      /*  webResponse.setTotalPageNum(totalPageNum);*/
        webResponse.setData(collectionReturn);
        return webResponse;
    }

    private void getOldLastMData(CollectionStatistic collectionReturn,CollectionStatistic beanInfo){
        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfo);
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

    private void getOldThisMData(CollectionStatistic collectionReturn,CollectionStatistic beanInfo){
        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfo);
        collectionReturn.setThisBankAmt(collectonStatic.getBankAmtC());
        collectionReturn.setThisPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setThisRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setThisRepayAmt(collectonStatic.getRepayAmtP());
        collectionReturn.setThisRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }


    /**
     * 根据回款金额，算户数
     * @param value
     * @return
     */
    private BigDecimal calHoursValue(BigDecimal value){
        BigDecimal result = new BigDecimal(0);
        if (value==null){
            value = new BigDecimal(0);
        }
        if (value.compareTo(CaseBaseConstant.MLOW) < 0){
            result = result.add(CaseBaseConstant.H1);
        }else if(value.compareTo(CaseBaseConstant.MMIDDLE) < 0){
            result = result.add(CaseBaseConstant.H2);
        }else if (value.compareTo(CaseBaseConstant.MHIGH) < 0){
            result = result.add(CaseBaseConstant.H3);
        }else {
            result = result.add(CaseBaseConstant.H4);
        }
        return result;
    }
    /**
     * 提成计算
     * @param enRepayAmt 已还金额
     * @param money 委案金额
     * @param sysPercent
     */
    private BigDecimal royaltyCalculate(BigDecimal enRepayAmt,BigDecimal money,SysPercent sysPercent){
        BigDecimal result = new BigDecimal("0.00");
        if (enRepayAmt==null){
            enRepayAmt = new BigDecimal(0);
        }
        if (money==null){
            money = new BigDecimal(0);
        }
        switch (sysPercent.getEnable()){
            case "特殊1"://特殊一
                if (enRepayAmt.compareTo(money)>=0){
                    result = calMoneyOne(enRepayAmt,money,sysPercent);
                }
                break;
            case "特殊2"://特殊二.findThisMonthById
                if (money.compareTo(new BigDecimal("0")) > 0){
                    result = calMoneyTwo(enRepayAmt,money,sysPercent);
                }
                break;
            default://阶梯累加
                    result = this.calPaidMoney(enRepayAmt,sysPercent);
                break;
        }
        return  result;
    }


    /**
     * 阶梯累加提成计算
     * @param enRepayAmt 已还金额
     * @param sysPercent
     * @return
     */
    private BigDecimal calPaidMoney(BigDecimal enRepayAmt,SysPercent sysPercent){

        //基本提成
        BigDecimal baseAmount ;
        //低标提成初始化
        BigDecimal lowAmount = new BigDecimal("0.00");
        //高标提成初始化
        BigDecimal highAmount = new BigDecimal("0.00");

        //低标金额
        BigDecimal odvBasic = sysPercent.getOdvBasic();
        //高标金额
        BigDecimal odvHighBasic = sysPercent.getOdvHighBasic();

        //低标
        BigDecimal odvLow = sysPercent.getOdvLow();
        //低标提成
        BigDecimal odvReward = sysPercent.getOdvReward();
        //高标提成
        BigDecimal odvHighReward = sysPercent.getOdvHighReward();


        //低标提成标识
        boolean isLow = enRepayAmt.compareTo(sysPercent.getOdvBasic()) > 0;
        //高标提成标识
        boolean isHigh = enRepayAmt.compareTo(sysPercent.getOdvHighBasic()) > 0;

        //计算公式
        //低标 低标提成=已还金额*低标
        //含低标 低标提成=低标提成=低标金额*低标+（已还金额-低标金额）*低标提成
        //含高标 低标提成=低标金额*低标+（已还金额-低标金额）*低标提成+（已还金额-高标金额）*高标提成

        //基本提成
        baseAmount = isLow ?  odvBasic.multiply(odvLow): enRepayAmt.multiply(odvLow);

        if(isLow){
            //低标提成
            lowAmount = isHigh ?odvHighBasic.subtract(odvBasic).multiply(odvReward)
                    : enRepayAmt.subtract(odvBasic).multiply(odvReward);

            if (isHigh){
                //高标提成
                highAmount =  enRepayAmt.subtract(sysPercent.getOdvHighBasic()).multiply(odvHighReward);
            }
        }

        return baseAmount.add(lowAmount).add(highAmount);

    }

    /**
     * 特殊1计算
     * @param enRepayAmt 已还金额
     * @param money 委案金额
     * @param sysPercent
     * @return
     */
    private BigDecimal calMoneyOne(BigDecimal enRepayAmt,BigDecimal money,SysPercent sysPercent){
        if (enRepayAmt.compareTo(new BigDecimal(0))<=0){
            return new BigDecimal(0);
        }

        boolean flag = enRepayAmt.subtract(money).compareTo(new BigDecimal("0")) >= 0;
        //还款-委案金额
        BigDecimal overAmount = flag ? enRepayAmt.subtract(money) : new BigDecimal("0.0");

        //低于低标
        BigDecimal odvLow = sysPercent.getOdvLow();
        //低于低标2
        BigDecimal odvLow2 = sysPercent.getOdvReward2();

        //高于低标
        BigDecimal odvReward = sysPercent.getOdvReward();
        //高于低标
        BigDecimal odvReward2 = sysPercent.getOdvReward3();

        //低标提成标识
        boolean isLow = enRepayAmt.compareTo(sysPercent.getOdvBasic()) < 0;

        BigDecimal result = isLow ? money.multiply(odvLow).add(overAmount.multiply(odvLow2))
                : money.multiply(odvReward).add(overAmount.multiply(odvReward2));

        return result;
    }

    /**
     * 特殊2计算
     * @param enRepayAmt
     * @param money
     * @param sysPercent
     * @return
     */
    private BigDecimal calMoneyTwo(BigDecimal enRepayAmt,BigDecimal money,SysPercent sysPercent){

        //最低提成基数
        BigDecimal odvLow = sysPercent.getOdvLow();
        //基本提成户数
        BigDecimal ovdBasic = sysPercent.getOdvBasic();

        //判断条件一：回款户数是否大于基本提成户数ovdBasic
        if (enRepayAmt.compareTo(ovdBasic)<0){
            //小于1基本提成户数ovdBasic，提成=户数*odvLow
            return enRepayAmt.multiply(odvLow);
        }

        //判断条件二，综合达标率
        //计算综合达标率
        BigDecimal standardRate = enRepayAmt.divide(money);
        //达标率区间一
        BigDecimal odvReward = sysPercent.getOdvReward();
        BigDecimal dvRewardRange1 = sysPercent.getOdvRewardRange1();
        //达标率区间二
        BigDecimal odvReward2 = sysPercent.getOdvReward2();
        BigDecimal dvRewardRange2 = sysPercent.getOdvRewardRange2();
        //达标率区间三
        BigDecimal odvReward3 = sysPercent.getOdvReward3();
        BigDecimal dvRewardRange5 = sysPercent.getOdvRewardRange5();

        if (standardRate.compareTo(dvRewardRange1)<0){
            //综合达标率小于区间一，提成=户数*odvLow
            return enRepayAmt.multiply(odvLow);
        }else if (standardRate.compareTo(dvRewardRange2)<0){
            //综合达标率在区间一，提成=户数*odvReward
            return enRepayAmt.multiply(odvReward);
        }else if (standardRate.compareTo(dvRewardRange5)<0){
            //综合达标率在区间二，提成=户数*odvReward2
            return enRepayAmt.multiply(odvReward2);
        }else {
            //综合达标率在区间三，提成=户数*odvReward3
            return enRepayAmt.multiply(odvReward3);
        }

    }

    private BigDecimal calSpecialManagTwo(BigDecimal enRepayAmt,BigDecimal money,SysPercent sysPercent){
        if(enRepayAmt==null){
            enRepayAmt = new BigDecimal(0);
        }
        BigDecimal result = new BigDecimal("0.00");
        BigDecimal hourseholdRate = new BigDecimal("0.00");//综合户达率
        hourseholdRate = enRepayAmt.divide(money).setScale(2,BigDecimal.ROUND_UP).multiply(CaseBaseConstant.ROUND);
        if (hourseholdRate.compareTo(sysPercent.getManageRewardRange1()) >=0 &&
                hourseholdRate.compareTo(sysPercent.getManageRewardRange4()) < 0){
            result = enRepayAmt.multiply(sysPercent.getManageRewardRange2());
        }else if (hourseholdRate.compareTo(sysPercent.getManageRewardRange4()) >= 0 &&
                hourseholdRate.compareTo(sysPercent.getManageRewardRange3()) < 0){
            result = enRepayAmt.multiply(sysPercent.getManageReward());
        }else if(hourseholdRate.compareTo(sysPercent.getManageRewardRange3()) >= 0){
            result = enRepayAmt.multiply(sysPercent.getManageRewardRange5());
        }
        return result;
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



    public WebResponse loadDataOdv(){
        List<OdvPercentage> list = new ArrayList<OdvPercentage>();
        SysUserEntity user = getUserInfo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        OdvPercentage odvPercentage = new OdvPercentage();
        odvPercentage.setOdv(user.getId());
        odvPercentage.setLineDate(sdf.format(new Date()));
        list = odvMapper.list(odvPercentage);

        return WebResponse.success(list);
    }

    public WebResponse showOdv(OdvPercentage bean){
        List<OdvPercentage> list = new ArrayList<OdvPercentage>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        OdvPercentage odvPercentage = new OdvPercentage();
        odvPercentage.setOdv(bean.getOdv());
        odvPercentage.setLineDate(sdf.format(new Date()));
        list = odvMapper.list(odvPercentage);
        return WebResponse.success(list);
    }

    public WebResponse loadDataManage(){
        List<ManagePercentage> list = new ArrayList<ManagePercentage>();
        SysUserEntity user = getUserInfo();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        ManagePercentage managePercentage = new ManagePercentage();
        managePercentage.setManage(user.getId());
        managePercentage.setLineDate(sdf.format(new Date()));
        list = manageMapper.list(managePercentage);
        for (int i=0;i<list.size();i++){
            ManagePercentage temp = list.get(i);
            SysUserEntity odvUser = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdvName(odvUser.getUserName());
            list.set(i,temp);
        }

        return WebResponse.success(list);
    }


    @Override
    public void calRoyalti(Integer caseId,Integer userId){
        DataCaseEntity bean = new DataCaseEntity();
        bean.setId(caseId);
        bean = caseMapper.findById(bean);
        bean.setOdv(userId+"");
        if (StringUtils.isEmpty(bean.getBusinessType())){
            return ;
        }
        SysNewUserEntity sysNewUserEntity = sysUserMapper.getDataById(userId);
        Date actualTime = sysNewUserEntity==null?null:sysNewUserEntity.getActualTime();//转正时间

        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timeStart.add(Calendar.MONTH, 0);    //得到前一个月
        timeStart.set(Calendar.DAY_OF_MONTH,1);//设置为1号
        CollectionStatistic beanInfoData = new CollectionStatistic();

        String first = sdf1.format(timeStart.getTime());
        beanInfoData.setMonthStart(first);//月第一天

        timeEnd.set(Calendar.DAY_OF_MONTH, timeEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = sdf1.format(timeEnd.getTime());
        beanInfoData.setMonthEnd(last);//月最后一天

        //获取当前月25号
        Calendar ca25 = Calendar.getInstance();
        ca25.set(Calendar.DAY_OF_MONTH, 25);

        bean.setRepayDateStart(first);

        if (actualTime==null || actualTime.compareTo(timeStart.getTime())<0 || actualTime.compareTo(timeEnd.getTime())>0) {
            //阶梯累加
            bean.setRepayDateEnd(last);
            royaltyTypeOdv(bean,1,userId);
        }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
            royaltyTypeOdv(bean,2,userId);
        }else if (actualTime.compareTo(ca25.getTime())>=0){
            royaltyTypeOdv(bean,3,userId);
        }

    }

    @Override
    public void calRoyaltiManage(Integer caseId,Integer userId){

        ManagePercentage managePercentage = new ManagePercentage();

        DataCaseEntity bean = new DataCaseEntity();
        bean.setId(caseId);
        bean = caseMapper.findById(bean);
        bean.setOdv(userId+"");
        managePercentage.setOdv(bean.getOdv()==null?0:(bean.getOdv().equals("")?0:Integer.parseInt(bean.getOdv())));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        managePercentage.setLineDate(sdf.format(new Date()));
        SysNewUserEntity sysNewUserEntity = sysUserMapper.getDataById(userId);
        Date actualTime = sysNewUserEntity==null?null: sysNewUserEntity.getActualTime();//转正时间

        CollectionStatistic beanInfoData = new CollectionStatistic();
        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timeStart.add(Calendar.MONTH, 0);    //得到前一个月
        timeStart.set(Calendar.DAY_OF_MONTH,1);//设置为1号

        String first = sdf1.format(timeStart.getTime());
        beanInfoData.setMonthStart(first);//月第一天

        timeEnd.set(Calendar.DAY_OF_MONTH, timeEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        String last = sdf1.format(timeEnd.getTime());
        beanInfoData.setMonthEnd(last);//月最后一天

        //获取当前月25号
        Calendar ca25 = Calendar.getInstance();
        ca25.set(Calendar.DAY_OF_MONTH, 25);
        String day25 = sdf1.format(ca25.getTime());

        bean.setRepayDateStart(first);

        if (actualTime==null || actualTime.compareTo(timeStart.getTime())<0 || actualTime.compareTo(timeEnd.getTime())>0) {
            //阶梯累加
            bean.setRepayDateEnd(last);
            royaltyTypeManage(bean,1,managePercentage);
        }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
            bean.setRepayDateEnd(last);
            royaltyTypeManage(bean,2,managePercentage);
        }else if (actualTime.compareTo(ca25.getTime())>=0){
            bean.setRepayDateEnd(last);
            royaltyTypeManage(bean,3,managePercentage);
        }
        SysNewUserEntity userEntity = new SysNewUserEntity();
        userEntity.setId(bean.getOdv()==null?0:(bean.getOdv().equals("")?0:Integer.parseInt(bean.getOdv())));
        List<SysNewUserEntity> userList = sysUserMapper.showManage(userEntity);
        if (userList.size()>0){
            managePercentage.setManage(userList.get(0).getId());
        }
        List<ManagePercentage> manageList = manageMapper.list(managePercentage);
        if (manageList.size()>0){
            managePercentage.setRepayAmt(managePercentage.getRepayAmt()==null?new BigDecimal(0):managePercentage.getRepayAmt());
            managePercentage.setPercentage(managePercentage.getPercentage()==null?new BigDecimal(0):managePercentage.getPercentage());
            managePercentage.setId(manageList.get(0).getId());
            manageMapper.update(managePercentage);
        }else{
            managePercentage.setRepayAmt(managePercentage.getRepayAmt()==null?new BigDecimal(0):managePercentage.getRepayAmt());
            managePercentage.setPercentage(managePercentage.getPercentage()==null?new BigDecimal(0):managePercentage.getPercentage());
            manageMapper.save(managePercentage);
        }

    }


    /**
     * 催收员提成
     * @param tempCase
     * @param type
     * @return
     */
    private void royaltyTypeOdv(DataCaseEntity tempCase,int type,int userId){

        //判空处理
        if(org.apache.commons.lang3.StringUtils.isBlank(tempCase.getBusinessType())){
            return;
        }

        OdvPercentage odvPercentage= new OdvPercentage();
        odvPercentage.setOdv(tempCase.getOdv()==null?0:(tempCase.getOdv().equals("")?0:Integer.parseInt(tempCase.getOdv())));
        odvPercentage.setLine(tempCase.getBusinessType());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        odvPercentage.setLineDate(sdf.format(new Date()));

        //获取案件条线类型
        SysPercent percentData = new SysPercent();
        String part1 = "京东白条,太平洋,I贷,平安后手181-360天,平安后手361-720天,平安后手721天+,平安前手,中信抢案,中信普案,财富0-90天,财富91-180天,财富181-360天,财富361-720天,财富721天+,浦发M3,浦发1手,浦发2手,浦发3手,捷信,银谷M3以下,银谷M4-M6,银谷M7-M9,银谷M10+,宜信1手,宜信2手,宜信3手,宜信4手,小牛0-90天,小牛91-180天,小牛181-360天,小牛361-720天,小牛721天+";
        String part2 = "上汽长账龄上汽拖车,上汽长账龄其他";
        String part3 = "上汽短期,安吉蓝海";


        //提成初始化,未达标时,提成为0元
        BigDecimal resultBean = new BigDecimal("0.00");
        //获取提成计算信息
        //设置条线
        String client = tempCase.getBusinessType();
        percentData.setClient(client);
        SysPercent percent =  sysPercentMapper.findByClient(percentData);

        //提成标识
        boolean isCommission = tempCase.getEnRepayAmt().compareTo(CaseBaseConstant.MLOW)>=0;
        //提成计算标识
        boolean isCalculate =  StringUtils.notEmpty(percent) && (type != 3 || isCommission);

        //阶段
        if (part1.indexOf(client.trim())>=0 && isCalculate){
           // DataCaseEntity dataCaseEntity = caseMapper.findThisMonthById(tempCase);
           // tempCase.setEnRepayAmt(dataCaseEntity.getEnRepayAmt());

            //设置已还金额,从还款记录中取值
            Map<String,Integer> dbMap = new HashMap<>();
            dbMap.put("collectUser",userId);
            dbMap.put("dataCase",tempCase.getId());
            BigDecimal repayAmt = dataCaseRepayRecordMapper.getRepayByCollectUser(dbMap);
            tempCase.setEnRepayAmt(repayAmt ==null?new BigDecimal(0):repayAmt);
            odvPercentage.setRepayAmt(tempCase.getEnRepayAmt());
//            tempCase.setEnRepayAmt(tempCase.getEnRepayAmt()==null?new BigDecimal(0):tempCase.getEnRepayAmt());
            resultBean = this.calPaidMoney(tempCase.getEnRepayAmt(),percent);

        }
        //特殊1
        if (part2.indexOf(client.trim())>=0 && isCalculate){

            //todo 特殊1是按照单案件来算的，首先查询出催收员的本月特殊1还款的案件的，然后循环判断案件的的还款金额、结清状态以及委案金额，再进行判断，最后将提成金额合在一起就是此条线的催收员提成统计
            List<DataCaseEntity> caseList = caseMapper.findThisMonthTSById1(tempCase);
            for (int i=0;i<caseList.size();i++){
                DataCaseEntity temp = caseList.get(i);
                String settleFlag = temp.getSettleFlag();//1 已结清 0 未结清

                BigDecimal enRepayAmt = temp.getEnRepayAmt()==null?new BigDecimal(0):temp.getEnRepayAmt();   //还款金额
                BigDecimal money = temp.getMoney();     //委案金额
                odvPercentage.setRepayAmt((odvPercentage.getRepayAmt()==null?new BigDecimal(0):odvPercentage.getRepayAmt()).add(enRepayAmt==null?new BigDecimal(0):enRepayAmt));

                if ("上汽长账龄上汽拖车".equals(tempCase.getBusinessType())&&"已结清".equals(settleFlag)) {
                    resultBean = resultBean.add(this.calMoneyOne(enRepayAmt, money, percent));
                }
                if ("上汽长账龄其他".equals(tempCase.getBusinessType()) && enRepayAmt.compareTo(money) >= 0) {
                    resultBean = resultBean.add(this.calMoneyOne(enRepayAmt,money,percent));
                }
            }


        }

        //特殊2
        if (part3.indexOf(client.trim())>=0 && isCalculate){

            // TODO: 2019/6/6 假定汇款户数与委案户数是正确的
            //todo 特殊2的户数是先算单个案件，然后再累计，再判断计算提成的，首先查询出催收员的本月特殊2还款的案件，然后循环算出每个案件的户数，再把催收员的条线的户数以及委案户数合计后再计算
            List<DataCaseEntity> caseList = caseMapper.findThisMonthTSById2(tempCase);
            BigDecimal numHoursPay = new BigDecimal(0);//当月回款户数
            BigDecimal numHoursMoney = new BigDecimal(0); //当月委案户数
            for (int i=0;i<caseList.size();i++) {
                DataCaseEntity temp = caseList.get(i);

                odvPercentage.setRepayAmt((odvPercentage.getRepayAmt()==null?new BigDecimal(0):odvPercentage.getRepayAmt()).add(temp.getEnRepayAmt()==null?new BigDecimal(0):temp.getEnRepayAmt()));

                //当月回款户数
                numHoursPay = numHoursPay.add(calHoursValue(temp.getEnRepayAmt()));
                //当月委案户数
                numHoursMoney = numHoursMoney.add(calHoursValue(temp.getMoney()));
            }

            resultBean = this.calMoneyTwo(numHoursPay,numHoursMoney,percent);

        }

        //设置提成
        odvPercentage.setPercentage(resultBean);

        List<OdvPercentage> odvList = odvMapper.list(odvPercentage);
        if (odvList.size()>0){
            odvPercentage.setId(odvList.get(0).getId());
            odvMapper.update(odvPercentage);
        }else{
            odvMapper.save(odvPercentage);
        }

    }



    /**
     * 经理提成
     * @param tempCase
     * @param type
     * @return
     */
    private void royaltyTypeManage(DataCaseEntity tempCase,int type,ManagePercentage managePercentage){

        SysPercent percentData = new SysPercent();

        SysPercent percent;
        //一、经理阶梯提成、特殊1提成
        BigDecimal rangeCommission = dataCollectionMapper.getRangeCommission(managePercentage.getOdv());
        managePercentage.setPercentage(rangeCommission);

        //三、经理特殊2提成
        BigDecimal caseCommission2;

        //         -------------------------------上汽短期---------------------------------------
        tempCase.setBusinessType("上汽短期");
        List<DataCaseEntity> caseList = caseMapper.findThisMonthTSById2(tempCase);
        BigDecimal numHoursPay = new BigDecimal(0);//当月回款户数
        BigDecimal numHoursMoney = new BigDecimal(0); //当月委案户数
        BigDecimal enRepayAmtTotal = new BigDecimal(0); //当月委案户数
        BigDecimal moneyTotal = new BigDecimal(0); //当月委案户数
        for (int i = 0; i < caseList.size(); i++) {
            DataCaseEntity temp = caseList.get(i);
            //当月回款户数
            numHoursPay = numHoursPay.add(calHoursValue(temp.getEnRepayAmt()));
            //当月委案户数
            numHoursMoney = numHoursMoney.add(calHoursValue(temp.getMoney()));
            //当月还款总额
            enRepayAmtTotal = enRepayAmtTotal.add(temp.getEnRepayAmt());
            managePercentage.setRepayAmt((managePercentage.getRepayAmt() == null ? new BigDecimal(0) : managePercentage.getRepayAmt()).add(temp.getEnRepayAmt() == null ? new BigDecimal(0) : temp.getEnRepayAmt()));
            //但与委案总额
            moneyTotal = moneyTotal.add(temp.getMoney());
        }

        //上汽短租综合达标率
        BigDecimal composite1 = numHoursMoney.compareTo(new BigDecimal(0)) <= 0 ? new BigDecimal(0) : numHoursPay.divide(numHoursMoney);

        percentData.setClient(tempCase.getBusinessType());
        percent = sysPercentMapper.findByClient(percentData);

        //上汽短租综合达标率 提成基数
        BigDecimal odvReward = percent.getManageReward();
        //上汽短租综合达标率 提成标准
        BigDecimal odvRewardRange1 = percent.getManageRewardRange1();
        //上汽短租 提成
        BigDecimal reward1 = composite1.compareTo(odvRewardRange1) >= 0 ? odvReward : new BigDecimal("0.0");

//         -------------------------------上汽短期回款---------------------------------------
        //上汽短回款完成率
        BigDecimal composite3 = moneyTotal.compareTo(new BigDecimal(0)) <= 0 ? new BigDecimal(0) : enRepayAmtTotal.divide(moneyTotal);
        //上汽短回款完成率 提成基数
        BigDecimal odvReward3 = percent.getManageRewardRange5();
        //上汽短回款完成率 标准
        BigDecimal odvRewardRange3 = percent.getManageRewardRange4();
        //上汽短回款 提成
        BigDecimal reward3 = composite3.compareTo(odvRewardRange3) >= 0 ? odvReward3 : new BigDecimal("0.0");


// -------------------------------安吉蓝海---------------------------------------
        tempCase.setBusinessType("安吉蓝海");
        List<DataCaseEntity> caseList2 = caseMapper.findThisMonthTSById2(tempCase);
        BigDecimal numHoursPay2 = new BigDecimal(0);//当月回款户数
        BigDecimal numHoursMoney2 = new BigDecimal(0); //当月委案户数
        for (int i=0;i<caseList2.size();i++) {
            DataCaseEntity temp = caseList2.get(i);

            managePercentage.setRepayAmt((managePercentage.getRepayAmt()==null?new BigDecimal(0):managePercentage.getRepayAmt()).add(temp.getEnRepayAmt()==null?new BigDecimal(0):temp.getEnRepayAmt()));
            //当月回款户数
            numHoursPay2 = numHoursPay2.add(calHoursValue(temp.getEnRepayAmt()));
            //当月委案户数
            numHoursMoney2 = numHoursMoney2.add(calHoursValue(temp.getMoney()));
        }
        //安吉蓝海综合达标率
        BigDecimal composite2 = numHoursMoney2.compareTo(new BigDecimal(0))<=0?new BigDecimal(0): numHoursPay2.divide(numHoursMoney2);

        percentData.setClient(tempCase.getBusinessType());
        percent =  sysPercentMapper.findByClient(percentData);


        //安吉蓝海综合达标率 提成基数
        BigDecimal odvReward2 = percent.getManageRewardRange3()==null?new BigDecimal(0):percent.getManageRewardRange3();
        //安吉蓝海综合达标率 标准
        BigDecimal odvRewardRange2 = percent.getManageRewardRange2()==null?new BigDecimal(0):percent.getManageRewardRange2();
        //安吉蓝海 提成
        BigDecimal reward2 =  composite2.compareTo(odvRewardRange2)>=0 ? odvReward2: new BigDecimal("0.0");



        //全部达标提成 基数
        BigDecimal zero = new BigDecimal("0");
        BigDecimal odvReward4 = percent.getManageRewardRange6()==null?new BigDecimal(0):percent.getManageRewardRange6();
        //全部达标提成
        BigDecimal reward4 = reward1.compareTo(zero) > 0 && reward1.compareTo(zero) > 0 && reward1.compareTo(zero) > 0
                ? odvReward4 : new BigDecimal("0.0");

        caseCommission2 = reward1.add(reward2).add(reward3).add(reward4);

            managePercentage.setPercentage((caseCommission2==null?new BigDecimal(0):caseCommission2).add(rangeCommission==null?new BigDecimal(0):rangeCommission));




    }




}
