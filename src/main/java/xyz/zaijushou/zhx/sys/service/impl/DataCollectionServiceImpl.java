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
    ExecutorService executor = Executors.newFixedThreadPool(20);
    @Resource
    private SysUserMapper sysUserMapper;//用户业务控制层
    @Resource
    private DataCollectionMapper collectionMapper;//催收数据层
    @Resource
    private DataCaseMapper caseMapper;//案件数据层

    private DataCaseServiceImpl dataCaseService;//用户业务控制层
    @Autowired
    private DataLogService dataLogService;
    @Autowired
    private SysOrganizationService sysOrganizationService;

    @Resource
    private SysPercentMapper sysPercentMapper;

    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private OdvMapper odvMapper;
    @Resource
    private ManageMapper manageMapper;


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
        log.setContext("联系人："+beanInfo.getTargetName()+"，电话号码："+beanInfo.getMobile()+"[手机]，通话内容："+(beanInfo.getCollectInfo()==null?"":beanInfo.getCollectInfo())+"，催收状态： "+(sysDictionaryEntity==null?"":sysDictionaryEntity.getName()));
        log.setOper(getUserInfo().getId());
        log.setOperName(getUserInfo().getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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
    public void updateCollection(DataCollectionEntity dataCollectionEntity){
        dataCollectionMapper.updateCollection(dataCollectionEntity);
    }
    @Override
    public void delete(DataCollectionEntity dataCollectionEntity){

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
        WebResponse webResponse = WebResponse.buildResponse();
        CollectionReturnEntity collectionReturn = new CollectionReturnEntity();

        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return webResponse;
        }
        //查询个人  and 查询部门
        if (user.getId()==1){

        }else {
            if (dataCollectionEntity.getsType() == 0) {
                dataCollectionEntity.setOdv(user.getId() + "");
            } else if (dataCollectionEntity.getsType() == 1 && StringUtils.isEmpty(dataCollectionEntity.getDept())) {
                List<String> deptList = new ArrayList<String>();
                SysOrganizationEntity organizationEntity = new SysOrganizationEntity();

                //查询标识
                organizationEntity.setTypeFlag(1);
                List<SysOrganizationEntity> orgList = sysOrganizationService.listChildOrganization(organizationEntity);
                for (SysOrganizationEntity orgEntity : orgList) {
                    deptList.add(orgEntity.getId()+"");
                }
                dataCollectionEntity.setDeptFlag(1);
                SysNewUserEntity queryUser = new SysNewUserEntity();
                queryUser.setDepartIdsSet(new HashSet(deptList));
                List<SysNewUserEntity> odvList = sysUserMapper.listByDepartIdsSet(queryUser);
                String[] odvs = new String[odvList.size()];
                for (int i=0;i<odvList.size();i++){
                    SysNewUserEntity sysNewUserEntity = odvList.get(i);
                    odvs[i] = sysNewUserEntity.getId()+"";
                }
                dataCollectionEntity.setOdvs(odvs);
            }
        }
        if(StringUtils.isEmpty(dataCollectionEntity.getOrderBy())){
            dataCollectionEntity.setOrderBy("id");
        }else {
            dataCollectionEntity.setOrderBy(MyCollectSortEnum.getEnumByKey(dataCollectionEntity.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(dataCollectionEntity.getSort())){
            dataCollectionEntity.setSort("desc");
        }
        logger.info("********************开始查询案件");
        PageHelper.startPage(dataCollectionEntity.getPageNum(), dataCollectionEntity.getPageSize());
        List<DataCollectionEntity> list =  dataCollectionMapper.pageMyCollect(dataCollectionEntity);
        logger.info("********************查询案件结束");
        /*int[] caseIdArray = new int[list.size()];
        for (int i=0;i<list.size();i++){
            caseIdArray[i] = list.get(i).getId();
        }
        Map collectMap = new HashMap();
        if (caseIdArray.length>0){
            DataCollectionEntity dataCollectionEntity1 = new DataCollectionEntity();
            dataCollectionEntity1.setIds(caseIdArray);
            logger.info("********************开始查询催收时间");
            //dataCollectionMapper.deleteTemp();
            //dataCollectionMapper.createTemp();
            dataCollectionMapper.saveBatchCollect(dataCollectionEntity1);
            List<DataCollectionEntity> collectList = dataCollectionMapper.showCollectTime();
            dataCollectionMapper.deletBatchCollect(dataCollectionEntity1);
            for (int i=0;i<collectList.size();i++){
                DataCollectionEntity tempCollect = collectList.get(i);
                collectMap.put(tempCollect.getCaseId(),tempCollect);
            }
            logger.info("********************处理完毕催收时间");
        }*/


        int countCase = 0;//列表案量
        BigDecimal sumMoney = new BigDecimal("0");//列表金额
        int countCasePay = 0;//列表还款案量
        BigDecimal sumPayMoney = new BigDecimal("0");//列表还款数额
        BigDecimal sumRepay = new BigDecimal("0");//列表CP值
        BigDecimal sumBank = new BigDecimal("0");//列表PTP值
        List<String> caseIds = new ArrayList<String>();//案件ID数组
        if(StringUtils.isEmpty(list)) {
            collectionReturn.setCountCase(countCase);
            collectionReturn.setCountCasePay(countCasePay);
            collectionReturn.setSumBank(sumRepay.stripTrailingZeros());
            collectionReturn.setSumMoney(sumMoney.stripTrailingZeros());
            collectionReturn.setSumRepay(sumBank.stripTrailingZeros());
            collectionReturn.setSumPayMoney(sumPayMoney.stripTrailingZeros());
            webResponse.setData(collectionReturn);
            return  webResponse;
        }
        logger.info("********************开始处理结果集");
        for (int i=0;i<list.size();i++){
            DataCollectionEntity collection = list.get(i);

            ++countCase;
            sumMoney = sumMoney.add(collection.getMoney()==null?new BigDecimal("0"):collection.getMoney());
            collection.setEnRepayAmt(collection.getEnRepayAmt()==null?new BigDecimal(0):collection.getEnRepayAmt());
            if (collection.getEnRepayAmt()!=null && collection.getEnRepayAmt().compareTo(new BigDecimal(0))>0){
                ++countCasePay;
                sumPayMoney = sumPayMoney.add(collection.getEnRepayAmt()==null?new BigDecimal("0"):collection.getEnRepayAmt());
            }
            sumRepay = sumRepay.add(collection.getRepayAmt()==null?new BigDecimal("0"):collection.getRepayAmt());
            sumBank = sumBank.add(collection.getBankAmt()==null?new BigDecimal("0"):collection.getBankAmt());
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
        collectionReturn.setCountCasePay(countCasePay);
        //collectionReturn.setSumBank(sumBank);
        collectionReturn.setSumBankMsg(sumBank==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(sumBank.stripTrailingZeros()+""));
        //collectionReturn.setSumMoney(sumMoney);
        collectionReturn.setSumMoneyMsg(sumMoney==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(sumMoney.stripTrailingZeros()+""));
        //collectionReturn.setSumRepay(sumRepay);
        collectionReturn.setSumRepayMsg(sumRepay==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(sumRepay.stripTrailingZeros()+""));
        //collectionReturn.setSumPayMoney(sumPayMoney);
        collectionReturn.setSumPayMoneyMsg(sumPayMoney==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(sumPayMoney.stripTrailingZeros()+""));
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
        bean.setBatchNo(listInfo.get(0).getBatchNo());
        bean.setIdentNo(listInfo.get(0).getIdentNo());
        List<DataCollectionEntity> list = collectionMapper.listDataCollect(bean);
        if (StringUtils.isEmpty(list)){
            return new ArrayList<>();
        }else {
            return list;
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
                    if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                            && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime() <= dateEnd.getTime()){
                        countSum++;
                    }
                }
                for (CollectionStatistic collection : conList){
                    if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
                            && sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()<= dateEnd.getTime()){
                        countCon++;
                    }
                }
                for (CollectionStatistic collection : caseList){
                    if(sdf2.parse(sdf2.format(sdf.parse(collection.getCollectTime()))).getTime()>= dateStart.getTime()
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
    public WebResponse statisticsCollectionPay(CollectionStatistic beanInfo){
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
        List<DataCollectionEntity> colList = dataCollectionMapper.statisticsCollectionPay(beanInfo);
        int count = dataCollectionMapper.countStatisticsCollectionPay(beanInfo);
        int totalPageNum = 0 ;
        if (beanInfo.getPageSize()==null || beanInfo.getPageSize()==0){
            beanInfo.setPageSize(100);
        }
        if (count%beanInfo.getPageSize()>0){
            totalPageNum = count/beanInfo.getPageSize()+1;
        }else{
            totalPageNum = count/beanInfo.getPageSize();
        }
        for(int i=0;i<colList.size();i++){
            DataCollectionEntity collection = colList.get(i);
            SysDictionaryEntity client = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ collection.getClient(), SysDictionaryEntity.class);
            collection.setClient(client==null?"":client.getName());
            SysDictionaryEntity account_age = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ collection.getAccountAge(), SysDictionaryEntity.class);
            collection.setAccountAge(account_age==null?"":account_age.getName());
            SysUserEntity temp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ collection.getConfimName(), SysUserEntity.class);
            collection.setConfimName(temp==null?"":temp.getUserName());
            collection.setBankTime(collection.getBankTime()==null?"":(collection.getBankTime().equals("")?"":collection.getBankTime().substring(0,10)) );
            collection.setRepayTime(collection.getRepayTime()==null?"":(collection.getRepayTime().equals("")?"":collection.getRepayTime().substring(0,10)) );
            collection.setmValMsg(collection.getmVal()==null?"": FmtMicrometer.fmtMicrometer(collection.getmVal().stripTrailingZeros()+""));
            collection.setBankAmtMsg(collection.getBankAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBankAmt().stripTrailingZeros()+""));
            collection.setEnRepayAmtMsg(collection.getEnRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getEnRepayAmt().stripTrailingZeros()+""));
            collection.setNewMoneyMsg(collection.getNewMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getNewMoney().stripTrailingZeros()+""));
            collection.setBalanceMsg(collection.getBalance()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBalance().stripTrailingZeros()+""));
            collection.setMoneyMsg(collection.getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getMoney().stripTrailingZeros()+""));
            collection.setRepayAmtMsg(collection.getRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepayAmt().stripTrailingZeros()+""));
            collection.setRepaidAmtMMsg(collection.getRepaidAmtM()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepaidAmtM().stripTrailingZeros()+""));
            collection.setRepaidBankAmtMMsg(collection.getRepaidBankAmtM()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepaidBankAmtM().stripTrailingZeros()+""));
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

        webResponse.setTotalNum(count);
        webResponse.setTotalPageNum(totalPageNum);
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
            result.add(CaseBaseConstant.H1);
        }else if(value.compareTo(CaseBaseConstant.MMIDDLE) < 0){
            result.add(CaseBaseConstant.H2);
        }else if (value.compareTo(CaseBaseConstant.MHIGH) < 0){
            result.add(CaseBaseConstant.H3);
        }else {
            result.add(CaseBaseConstant.H4);
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
        baseAmount = isLow ? enRepayAmt.multiply(odvLow) : odvBasic.multiply(odvLow);

        if(isLow){
            //低标提成
            lowAmount = isHigh ? enRepayAmt.subtract(odvBasic).multiply(odvReward)
                    : odvHighBasic.subtract(odvBasic).multiply(odvReward);

            if (isHigh){
                //高标提成
                highAmount =  enRepayAmt.subtract(odvHighReward).multiply(odvHighReward);
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
    public void calRoyalti(Integer caseId){
        DataCaseEntity bean = new DataCaseEntity();
        bean.setId(caseId);
        bean = caseMapper.findById(bean);
        SysNewUserEntity sysNewUserEntity = sysUserMapper.getDataById(Integer.parseInt(bean.getOdv()==null?"0":(bean.getOdv().equals("")?"0":bean.getOdv())));
        Date actualTime = sysNewUserEntity.getActualTime();//转正时间

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
        String day25 = sdf1.format(ca25.getTime());

        bean.setRepayDateStart(first);

        if (actualTime==null || actualTime.compareTo(timeStart.getTime())<0 || actualTime.compareTo(timeEnd.getTime())>0) {
            //阶梯累加
            bean.setRepayDateEnd(last);
            DataCaseEntity tempCase = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase.getEnRepayAmt());
            bean.setSettleDate(tempCase.getSettleDate());
            bean.setSettleFlag(tempCase.getSettleFlag());
            royaltyTypeOdv(bean,1);

        }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
            bean.setRepayDateEnd(day25);
            DataCaseEntity tempCase = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase.getEnRepayAmt());
            bean.setSettleDate(tempCase.getSettleDate());
            bean.setSettleFlag(tempCase.getSettleFlag());
            royaltyTypeOdv(bean,2);
            //25到月底
            bean.setRepayDateStart(day25);
            bean.setRepayDateEnd(last);
            DataCaseEntity tempCase2 = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase2.getEnRepayAmt());
            bean.setSettleDate(tempCase2.getSettleDate());
            bean.setSettleFlag(tempCase2.getSettleFlag());
            royaltyTypeOdv(bean,2);


        }else if (actualTime.compareTo(ca25.getTime())>=0){
            bean.setRepayDateEnd(last);
            DataCaseEntity tempCase = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase.getEnRepayAmt());
            bean.setSettleDate(tempCase.getSettleDate());
            bean.setSettleFlag(tempCase.getSettleFlag());
            royaltyTypeOdv(bean,3);
        }

    }

    @Override
    public void calRoyaltiManage(Integer caseId){
        ManagePercentage managePercentage = new ManagePercentage();

        DataCaseEntity bean = new DataCaseEntity();
        bean.setId(caseId);
        bean = caseMapper.findById(bean);
        managePercentage.setOdv(bean.getOdv()==null?0:(bean.getOdv().equals("")?0:Integer.parseInt(bean.getOdv())));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        managePercentage.setLineDate(sdf.format(new Date()));
        SysNewUserEntity sysNewUserEntity = sysUserMapper.getDataById(Integer.parseInt(bean.getOdv()==null?"0":(bean.getOdv().equals("")?"0":bean.getOdv())));
        Date actualTime = sysNewUserEntity.getActualTime();//转正时间

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
            DataCaseEntity tempCase = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase.getEnRepayAmt());
            bean.setSettleDate(tempCase.getSettleDate());
            bean.setSettleFlag(tempCase.getSettleFlag());
            royaltyTypeManage(bean,1,managePercentage);


        }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
            bean.setRepayDateStart(first);
            bean.setRepayDateEnd(day25);
            DataCaseEntity tempCase = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase.getEnRepayAmt());
            bean.setSettleDate(tempCase.getSettleDate());
            bean.setSettleFlag(tempCase.getSettleFlag());
            royaltyTypeManage(bean,2,managePercentage);
            //25到月底
            bean.setRepayDateStart(day25);
            bean.setRepayDateEnd(last);
            DataCaseEntity tempCase2 = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase2.getEnRepayAmt());
            bean.setSettleDate(tempCase2.getSettleDate());
            bean.setSettleFlag(tempCase2.getSettleFlag());
            royaltyTypeManage(bean,2,managePercentage);
        }else if (actualTime.compareTo(ca25.getTime())>=0){
            bean.setRepayDateEnd(last);
            DataCaseEntity tempCase = caseMapper.findThisMonthById(bean);
            bean.setEnRepayAmt(tempCase.getEnRepayAmt());
            bean.setSettleDate(tempCase.getSettleDate());
            bean.setSettleFlag(tempCase.getSettleFlag());
            royaltyTypeManage(bean,3,managePercentage);
        }
        SysNewUserEntity userEntity = new SysNewUserEntity();
        userEntity.setId(bean.getOdv()==null?0:(bean.getOdv().equals("")?0:Integer.parseInt(bean.getOdv())));
        List<SysNewUserEntity> userList = sysUserMapper.listParent(userEntity);
        if (userList.size()>0){
            managePercentage.setManage(userList.get(0).getId());
        }
        List<ManagePercentage> manageList = manageMapper.list(managePercentage);
        if (manageList.size()>0){
            managePercentage.setId(manageList.get(0).getId());
            manageMapper.update(managePercentage);
        }else{
            manageMapper.save(managePercentage);
        }

    }


    /**
     * 催收员提成
     * @param tempCase
     * @param type
     * @return
     */
    private void royaltyTypeOdv(DataCaseEntity tempCase,int type){

        //判空处理
        if(org.apache.commons.lang3.StringUtils.isBlank(tempCase.getBusinessType())){
            return;
        }

        OdvPercentage odvPercentage= new OdvPercentage();
        odvPercentage.setOdv(tempCase.getOdv()==null?0:(tempCase.getOdv().equals("")?0:Integer.parseInt(tempCase.getOdv())));
        odvPercentage.setLine(tempCase.getBusinessType());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        odvPercentage.setLineDate(sdf.format(new Date()));
        odvPercentage.setRepayAmt(tempCase.getEnRepayAmt());
        //获取案件条线类型
        SysPercent percentData = new SysPercent();
        String part1 = "京东白条,太平洋,I贷,平安后手181-360天,平安后手361-720天,平安后手721天+,平安前手,中信抢案,中信普案,财富0-90天,财富91-180天,财富181-360天,财富361-720天,财富721天+,浦发M3,浦发1手,浦发2手,浦发3手,捷信,银谷M3以下,银谷M4-M6,银谷M7-M9,银谷M10+,宜信1手,宜信2手,宜信3手,宜信4手,小牛0-90天,小牛91-180天,小牛181-360天,小牛361-720天,小牛721天+";
        String part2 = "上汽长账龄上汽拖车,上汽长账龄其他";
        String part3 = "上汽短期,安吉蓝海";


        //提成初始化,未达标时,提成为0元
        BigDecimal resultBean = new BigDecimal("0.00");
        //获取提成计算信息
        SysPercent percent =  sysPercentMapper.findByClient(percentData);
        //设置条线
        String client = tempCase.getBusinessType();
        percentData.setClient(client);
        //设置已还金额
        tempCase.setEnRepayAmt(tempCase.getEnRepayAmt()==null?new BigDecimal(0):tempCase.getEnRepayAmt());

        //提成标识
        boolean isCommission = tempCase.getEnRepayAmt().compareTo(CaseBaseConstant.MLOW)>=0;
        //提成计算标识
        boolean isCalculate =  StringUtils.notEmpty(percent) && (type != 3 || isCommission);

        //阶段
        if (part1.indexOf(client.trim())>=0 && isCalculate){

            resultBean = this.calPaidMoney(tempCase.getEnRepayAmt(),percent);

        }
        //特殊1
        if (part2.indexOf(client.trim())>=0 && isCalculate){

            String settleFlag = tempCase.getSettleFlag();//1 已结清 0 未结清

            BigDecimal enRepayAmt = tempCase.getEnRepayAmt();   //还款金额
            BigDecimal money = tempCase.getMoney();     //委案金额

            if ("上汽长账龄上汽拖车".equals(tempCase.getBusinessType())&&"已结清".equals(settleFlag)) {
                resultBean = this.calMoneyOne(enRepayAmt, money, percent);
            }
            if ("上汽长账龄其他".equals(tempCase.getBusinessType()) && enRepayAmt.compareTo(money) >= 0) {
                resultBean = this.calMoneyOne(enRepayAmt,money,percent);
            }

        }

        //特殊2
        if (part3.indexOf(client.trim())>=0 && isCalculate){

            // TODO: 2019/6/6 假定汇款户数与委案户数是正确的
            //当月回款户数
            BigDecimal numHoursPay = calHoursValue(tempCase.getEnRepayAmt());
            //当月委案户数
            BigDecimal numHoursMoney = calHoursValue(tempCase.getMoney());

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
        // TODO: 2019/6/7 以下为伪代码，还需具体进行实现
        //一、经理阶段提成
        BigDecimal rangeCommission = dataCollectionMapper.getRangeCommission(tempCase.getBusinessType());

        //二、经理特殊1提成
        BigDecimal caseCommission1 = dataCollectionMapper.getCaseCommission1();

        //三、经理特殊2提成
        BigDecimal caseCommission2 =  new BigDecimal("0.00");

        SysPercent percentData = new SysPercent();
        percentData.setClient(tempCase.getBusinessType());
        SysPercent percent =  sysPercentMapper.findByClient(percentData);

        //上汽短租综合达标率
        BigDecimal composite1 = new BigDecimal("0.0");
        //上汽短租综合达标率 提成基数
        BigDecimal odvReward = percent.getManageReward();
        //上汽短租综合达标率 提成标准
        BigDecimal odvRewardRange1 = percent.getManageRewardRange1();
        //上汽短租 提成
        BigDecimal reward1 =  composite1.compareTo(odvRewardRange1)>=0 ? odvReward: new BigDecimal("0.0");


        //安吉蓝海综合达标率
        BigDecimal composite2 = new BigDecimal("0.0");
        //安吉蓝海综合达标率 提成基数
        BigDecimal odvReward2 = percent.getManageRewardRange3();
        //安吉蓝海综合达标率 标准
        BigDecimal odvRewardRange2 = percent.getManageRewardRange2();
        //安吉蓝海 提成
        BigDecimal reward2 =  composite1.compareTo(odvRewardRange2)>=0 ? odvReward: new BigDecimal("0.0");


        //上汽短回款完成率
        BigDecimal composite3 = new BigDecimal("0.0");
        //上汽短回款完成率 提成基数
        BigDecimal odvReward3 = percent.getManageRewardRange5();
        //上汽短回款完成率 标准
        BigDecimal odvRewardRange3 = percent.getManageRewardRange4();
        //上汽短回款 提成
        BigDecimal reward3 =  composite1.compareTo(odvRewardRange3)>=0 ? odvReward: new BigDecimal("0.0");

        //全部达标提成 基数
        BigDecimal zero = new BigDecimal("0");
        BigDecimal odvReward4 = percent.getManageRewardRange6();
        //全部达标提成
        BigDecimal reward4 = reward1.compareTo(zero) > 0 && reward1.compareTo(zero) > 0 && reward1.compareTo(zero) > 0
                ? odvReward4 : new BigDecimal("0.0");

       BigDecimal resultManage = reward1.add(reward2).add(reward3).add(reward4);

        managePercentage.setPercentage(resultManage);
    }

}
