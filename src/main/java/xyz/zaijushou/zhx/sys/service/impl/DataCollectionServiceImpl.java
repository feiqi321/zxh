package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Case;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
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
        PageHelper.startPage(dataCollectionEntity.getPageNum(), dataCollectionEntity.getPageSize());
        List<DataCollectionEntity> list =  dataCollectionMapper.pageMyCollect(dataCollectionEntity);

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
                    bean.setTimeArea(sdf2.format(dateStart)+"-"+sdf2.format(dateEnd));

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
        int totalPageNum = 0 ;
        if (count%beanInfo.getPageSize()>0){
            totalPageNum = count/beanInfo.getPageSize()+1;
        }else{
            totalPageNum = count/beanInfo.getPageSize();
        }
        webResponse.setTotalNum(count);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setData(resultList);
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
            beanInfo.setOdv(user.getId()+"");//当前用户
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
            collection.setmValMsg(collection.getmVal()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getmVal().stripTrailingZeros()+""));
            collection.setBankAmtMsg(collection.getBankAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBankAmt().stripTrailingZeros()+""));
            collection.setEnRepayAmtMsg(collection.getEnRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getEnRepayAmt().stripTrailingZeros()+""));
            collection.setNewMoneyMsg(collection.getNewMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getNewMoney().stripTrailingZeros()+""));
            collection.setBalanceMsg(collection.getBalance()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getBalance().stripTrailingZeros()+""));
            collection.setMoneyMsg(collection.getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getMoney().stripTrailingZeros()+""));
            collection.setRepayAmtMsg(collection.getRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepayAmt().stripTrailingZeros()+""));
            collection.setRepaidAmtMMsg(collection.getRepaidAmtM()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepaidAmtM().stripTrailingZeros()+""));
            collection.setRepaidBankAmtMMsg(collection.getRepaidBankAmtM()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(collection.getRepaidBankAmtM().stripTrailingZeros()+""));
        }

        DataCaseEntity tempCase = new DataCaseEntity();
        tempCase.setOdv(user.getId()+"");//当前催收员
        SysNewUserEntity sysNewUserEntity = sysUserMapper.getDataById(user.getId());
        Date actualTime = sysNewUserEntity.getActualTime();//转正时间

        collectionReturn.setList(colList);
        //用户信息 包括角色
        SysNewUserEntity userNew = sysUserMapper.getDataById(user.getId());
        List<String> userName = new ArrayList<String>();
        for (SysRoleEntity roleEntity : userNew.getRoleList()){
            if (roleEntity.getId() == 4){//催收员
                userName.add(user.getUserName());
                tempCase.setCaseType("1");
            }else if (roleEntity.getId() == 9){//经理
                SysNewUserEntity userEntity = new SysNewUserEntity();
                userEntity.setId(user.getId());
                List<SysNewUserEntity> userList = sysUserMapper.listParent(userEntity);
                for (SysNewUserEntity newUser : userList){
                    userName.add(newUser.getUserName());
                }
                tempCase.setCaseType("2");
            }
        }
        tempCase.setUserName(userName);

        //如果转正时间没有或者不在本月，则按照正常判断进行，
        // 如果在本月的1号-25号之间，则本月所有的累计还款都要跟1万比较，低于1万没有提成，如果在25号-月末之间，则不判断1万的底线，全部都要计算提成
        //我的还款统计，上月和当月金额统计 查询
        //getLastMData(collectionReturn,actualTime,tempCase);
        //getThisMData(collectionReturn,actualTime,tempCase);
        //获取三个不同列表的统计金额
        //getStatisticsData(collectionReturn,beanInfo);

        webResponse.setTotalNum(count);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setData(collectionReturn);
        return webResponse;
    }
    /**
     * 经理提成
     * @param tempCase
     * @param type
     * @return
     */
    private BigDecimal royaltyManage(DataCaseEntity tempCase,int type){
        BigDecimal result = new BigDecimal("0.00");
        //获取案件条线类型
        SysPercent percentData = new SysPercent();
        for(String userName : tempCase.getUserName()){
            int num1 = 0 ;
            int num2 = 0 ;
            tempCase.setOdv(userName);
            List<DataCaseEntity> caseList1 = caseMapper.selectCommonCase(tempCase);
            for (int i = 0; i < caseList1.size(); i++) {
                if (caseList1.get(i)==null){
                    continue;
                }
                percentData.setClient(caseList1.get(i).getBusinessType());
                SysPercent percent =  sysPercentMapper.findByClient(percentData);
                caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt()==null?new BigDecimal(0):caseList1.get(i).getEnRepayAmt());
                if (StringUtils.notEmpty(percent)){
                    if (type == 1 ||
                            ((type == 2||type==3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW)>=0)){
                        if ("阶梯累加".equals(percent.getEnable())){
                            result = result.add(
                                    percent.getManageRewardRange2().multiply(caseList1.get(i).getEnRepayAmt())
                            );
                            if (num1 == 0){
                                result = result.add(percent.getManageRewardRange1());
                            }
                            num1 = 1;
                        }else if("特殊1".equals(percent.getEnable())){
                            result = result.add(
                                    percent.getManageRewardRange6().multiply(caseList1.get(i).getEnRepayAmt())
                            );
                            if (num2 == 0){
                                result = result.add(percent.getManageRewardRange5());
                            }
                            num2 = 1;
                        }

                    }
                }
            }
            //特殊1
            List<DataCaseEntity> caseList2 = caseMapper.selectTsCase1(tempCase);
            for (int i = 0; i < caseList2.size(); i++) {
                if (caseList2.get(i)==null){
                    continue;
                }
                percentData.setClient(caseList2.get(i).getBusinessType());
                SysPercent percent =  sysPercentMapper.findByClient(percentData);
                String settleFlag = caseList2.get(i).getSettleFlag();//1 已结清 0 未结清
                caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt()==null?new BigDecimal(0):caseList1.get(i).getEnRepayAmt());
                if ("已结清".equals(settleFlag)){
                    if (type == 1 ||
                            ((type == 2||type==3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW)>=0)){
                            if ("阶梯累加".equals(percent.getEnable())){
                                result = result.add(
                                        percent.getManageRewardRange2().multiply(caseList1.get(i).getEnRepayAmt())
                                );
                                if (num1 == 0){
                                    result = result.add(percent.getManageRewardRange1());
                                }
                                num1 = 1;
                            }else if("特殊1".equals(percent.getEnable())){
                                result = result.add(
                                        percent.getManageRewardRange6().multiply(caseList1.get(i).getEnRepayAmt())
                                );
                                if (num2 == 0){
                                    result = result.add(percent.getManageRewardRange5());
                                }
                                num2 = 2;
                            }
                    }
                }
            }

        }
        return result;
    }

    /**
     * 催收员提成
     * @param tempCase
     * @param type
     * @return
     */
    private BigDecimal royaltyType(DataCaseEntity tempCase,int type){
        BigDecimal result = new BigDecimal("0.00");
        //获取案件条线类型
        SysPercent percentData = new SysPercent();
        List<DataCaseEntity> caseList1 = caseMapper.selectCommonCase(tempCase);
        for (int i = 0; i < caseList1.size(); i++) {
            if (caseList1.get(i)==null){
                continue;
            }
            percentData.setClient(caseList1.get(i).getBusinessType());
            SysPercent percent =  sysPercentMapper.findByClient(percentData);
            caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt()==null?new BigDecimal(0):caseList1.get(i).getEnRepayAmt());
            if (StringUtils.notEmpty(percent)){
                if (type == 1 ||
                        ((type == 2||type==3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW)>=0)){
                    result = result.add(
                            royaltyCalculate(caseList1.get(i).getEnRepayAmt(),null,percent));
                }
            }
        }
        //特殊1
        List<DataCaseEntity> caseList2 = caseMapper.selectTsCase1(tempCase);
        for (int i = 0; i < caseList2.size(); i++) {
            if (caseList2.get(i)==null){
                continue;
            }
            percentData.setClient(caseList2.get(i).getBusinessType());
            SysPercent percent =  sysPercentMapper.findByClient(percentData);
            String settleDate = caseList2.get(i).getSettleDate();
            String settleFlag = caseList2.get(i).getSettleFlag();//1 已结清 0 未结清
            caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt()==null?new BigDecimal(0):caseList1.get(i).getEnRepayAmt());
            if ("已结清".equals(settleFlag)){
                if (type == 1 ||
                        ((type == 2||type==3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW)>=0)){
                    result = result.add(
                            royaltyCalculate( caseList2.get(i).getEnRepayAmt(), caseList2.get(i).getMoney(),percent));
                }
            }
        }
        //特殊2
        List<DataCaseEntity> caseList3 = caseMapper.selectTsCase2(tempCase);
        if (StringUtils.notEmpty(caseList3)){
            BigDecimal numHoursPay = new BigDecimal(0);
            BigDecimal numHoursMoney = new BigDecimal(0);
            for (int i = 0; i < caseList3.size(); i++) {
                caseList1.get(i).setEnRepayAmt(caseList1.get(i).getEnRepayAmt()==null?new BigDecimal(0):caseList1.get(i).getEnRepayAmt());
                if (type == 1 ||
                        ((type == 2||type==3) && caseList1.get(i).getEnRepayAmt().compareTo(CaseBaseConstant.MLOW)>=0)){
                    numHoursPay = numHoursPay.add(calHoursValue(caseList3.get(i).getEnRepayAmt()));
                    numHoursMoney = numHoursMoney.add(calHoursValue(caseList3.get(i).getMoney()));
                }
            }
            percentData.setClient(caseList3.get(0).getBusinessType());
            SysPercent percent =  sysPercentMapper.findByClient(percentData);
            result = royaltyCalculate(numHoursPay,numHoursMoney,percent);
        }
        return result;
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
     * @param enRepayAmt
     * @param money
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
                if (StringUtils.notEmpty(money) && StringUtils.notEmpty(enRepayAmt) && enRepayAmt.compareTo(money)>=0){
                    result = calMoneyOne(enRepayAmt,money,sysPercent);
                }
                break;
            case "特殊2"://特殊二
                result = calMoneyTwo(enRepayAmt,money,sysPercent);
                break;
            default://阶梯累加
                if (StringUtils.notEmpty(enRepayAmt)){
                    result = calPaidMoney(enRepayAmt,sysPercent);
                }
                break;
        }
        return  result;
    }


    private BigDecimal calPaidMoney(BigDecimal enRepayAmt,SysPercent sysPercent){
        BigDecimal result = new BigDecimal("0.00");
        if (enRepayAmt==null){
            enRepayAmt = new BigDecimal(0);
        }
        if (enRepayAmt.compareTo(sysPercent.getOdvBasic()) > 0){
            if (enRepayAmt.compareTo(sysPercent.getOdvHighBasic()) < 0 ){
                result = sysPercent.getOdvBasic().multiply(sysPercent.getOdvLow())
                        .add((enRepayAmt.subtract(sysPercent.getOdvBasic())).multiply(sysPercent.getOdvReward()));
            }else {
                result = sysPercent.getOdvBasic().multiply(sysPercent.getOdvLow())
                        .add(((sysPercent.getOdvHighBasic().subtract(sysPercent.getOdvHighBasic())).multiply(sysPercent.getOdvReward())))
                        .add(enRepayAmt.subtract(sysPercent.getOdvHighBasic()).multiply(sysPercent.getOdvHighReward()));
            }
        }else {
            result = enRepayAmt.multiply(sysPercent.getOdvLow());
        }
        return result;
    }
    private BigDecimal calMoneyOne(BigDecimal enRepayAmt,BigDecimal money,SysPercent sysPercent){
        BigDecimal result = new BigDecimal("0.00");
        if(enRepayAmt==null){
            enRepayAmt = new BigDecimal(0);
        }
        if (enRepayAmt.compareTo(sysPercent.getOdvBasic()) < 0){
            result = money.multiply(sysPercent.getOdvReward())
                    .add(enRepayAmt.subtract(money).multiply(sysPercent.getOdvLow()));
        }else{
            if (enRepayAmt.compareTo(sysPercent.getOdvHighBasic()) < 0){
                result = money.multiply(sysPercent.getOdvReward2())
                        .add(enRepayAmt.subtract(money).multiply(sysPercent.getOdvReward()));
            }else {
                result = money.multiply(sysPercent.getOdvReward3())
                        .add(enRepayAmt.subtract(money).multiply(sysPercent.getOdvReward2()));
            }
        }
        return result;
    }
    private BigDecimal calMoneyTwo(BigDecimal enRepayAmt,BigDecimal money,SysPercent sysPercent){
        if(enRepayAmt==null){
            enRepayAmt = new BigDecimal(0);
        }
        BigDecimal result = new BigDecimal("0.00");
        BigDecimal hourseholdRate = new BigDecimal("0.00");//综合户达率
        hourseholdRate = enRepayAmt.divide(money).setScale(2,BigDecimal.ROUND_UP).multiply(CaseBaseConstant.ROUND);
        if(enRepayAmt.compareTo(sysPercent.getOdvBasic()) < 0){
            result = enRepayAmt.multiply(sysPercent.getOdvLow());
        }else {
            if (hourseholdRate.compareTo(sysPercent.getOdvRewardRange1()) >=0 &&
                    hourseholdRate.compareTo(sysPercent.getManageRewardRange2()) < 0){
                result = enRepayAmt.multiply(sysPercent.getOdvReward());
            }else if (hourseholdRate.compareTo(sysPercent.getManageRewardRange2()) >= 0 &&
                    hourseholdRate.compareTo(sysPercent.getManageRewardRange5()) < 0){
                result = enRepayAmt.multiply(sysPercent.getOdvReward2());
            }else if(hourseholdRate.compareTo(sysPercent.getManageRewardRange5()) >= 0){
                result = enRepayAmt.multiply(sysPercent.getOdvReward3());
            }
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

    private void getLastMData(CollectionStatistic collectionReturn,Date actualTime, DataCaseEntity tempCase){
        CollectionStatistic beanInfoData = new CollectionStatistic();
        Calendar timeStart = Calendar.getInstance();
        Calendar timeEnd = Calendar.getInstance();
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        timeStart.add(Calendar.MONTH, -1);    //得到前一个月
        timeStart.set(Calendar.DAY_OF_MONTH,1);//设置为1号
        String first = sdf1.format(timeStart.getTime());
        beanInfoData.setMonthStart(first);//上月第一天
        timeEnd.add(Calendar.MONDAY,-1);
        timeEnd.set(Calendar.DAY_OF_MONTH,0);
        String last = sdf1.format(timeEnd.getTime());
        beanInfoData.setMonthEnd(last);//上月最后一天

        //获取上月25号
        Calendar ca25 = Calendar.getInstance();
        ca25.add(Calendar.MONDAY,-1);
        ca25.set(Calendar.DAY_OF_MONTH, 25);
        String day25 = sdf1.format(ca25.getTime());

        tempCase.setRepayDateStart(first);
        BigDecimal result = new BigDecimal("0.00");
        if ("1".equals(tempCase.getCaseType())) {//催收员
            if (actualTime==null || actualTime.compareTo(timeStart.getTime())<0 || actualTime.compareTo(timeEnd.getTime())>0) {
                tempCase.setRepayDateEnd(last);
                result = royaltyType(tempCase,1);
            }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
                tempCase.setRepayDateEnd(day25);
                result = royaltyType(tempCase,2);
                //25到月底
                tempCase.setRepayDateStart(day25);
                tempCase.setRepayDateEnd(last);
                result = result.add(royaltyType(tempCase,2));
            }else if (actualTime.compareTo(ca25.getTime())>=0){
                tempCase.setRepayDateEnd(last);
                result = royaltyType(tempCase,3);
            }
        }else {
            if (actualTime==null || actualTime.compareTo(timeStart.getTime())<0 || actualTime.compareTo(timeEnd.getTime())>0) {
                tempCase.setRepayDateEnd(last);
                result = royaltyType(tempCase,1);
            }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
                tempCase.setRepayDateEnd(day25);
                result = royaltyManage(tempCase,2);
                //25到月底
                tempCase.setRepayDateStart(day25);
                tempCase.setRepayDateEnd(last);
                result = result.add(royaltyManage(tempCase,2));
            }else if (actualTime.compareTo(ca25.getTime())>=0){
                tempCase.setRepayDateEnd(last);
                result = royaltyManage(tempCase,3);
            }
        }


        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfoData);
        collectionReturn.setLastBankAmt(collectonStatic.getBankAmtC());
//        collectionReturn.setLastPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setLastPaidMoney(result);
        collectionReturn.setLastRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setLastRepayAmt(collectonStatic.getRepayAmtP());
        collectionReturn.setLastRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }
   /* paidMoney;//已还款金额
    bankAmt;//上月銀行对账金额
    repayAmt;//上月承諾还款金额-PTP
    repaidAmt;//已还款金额的提成金额（M）
    repaidBankAmt;//月银行查账金额的提成金额（M）*/

    private void getThisMData(CollectionStatistic collectionReturn,Date actualTime, DataCaseEntity tempCase){
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

        tempCase.setRepayDateStart(first);

        BigDecimal result = new BigDecimal("0.00");
        if ("1".equals(tempCase.getCaseType())){//催收员
            if (actualTime==null || actualTime.compareTo(timeStart.getTime())<0 || actualTime.compareTo(timeEnd.getTime())>0) {
                //阶梯累加
                tempCase.setRepayDateEnd(last);
                result = royaltyType(tempCase,1);
            }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
                tempCase.setRepayDateEnd(day25);
                result = royaltyType(tempCase,2);
                //25到月底
                tempCase.setRepayDateStart(day25);
                tempCase.setRepayDateEnd(last);
                result = result.add(royaltyType(tempCase,2));
            }else if (actualTime.compareTo(ca25.getTime())>=0){
                tempCase.setRepayDateEnd(last);
                result = royaltyType(tempCase,3);
            }
        }else {//经理
            if (actualTime==null || actualTime.compareTo(timeStart.getTime())<0 || actualTime.compareTo(timeEnd.getTime())>0) {
                //阶梯累加
                tempCase.setRepayDateEnd(last);
                result = royaltyManage(tempCase,1);
            }else if (actualTime.compareTo(timeStart.getTime())>=0 || actualTime.compareTo(ca25.getTime())<0){
                tempCase.setRepayDateEnd(day25);
                result = royaltyManage(tempCase,2);
                //25到月底
                tempCase.setRepayDateStart(day25);
                tempCase.setRepayDateEnd(last);
                result = result.add(royaltyType(tempCase,2));
            }else if (actualTime.compareTo(ca25.getTime())>=0){
                tempCase.setRepayDateEnd(last);
                result = royaltyManage(tempCase,3);
            }
        }


        CollectionStatistic collectonStatic =
                dataCollectionMapper.statisticsCollectionPayM(beanInfoData);
        collectionReturn.setThisBankAmt(collectonStatic.getBankAmtC());
//        collectionReturn.setThisPaidMoney(collectonStatic.getPaidMoney());
        collectionReturn.setThisPaidMoney(result);
        collectionReturn.setThisRepaidAmt(collectonStatic.getRepaidAmt());
        collectionReturn.setThisRepayAmt(collectonStatic.getRepayAmtP());
        collectionReturn.setThisRepaidBankAmt(collectonStatic.getRepaidBankAmt());
    }
}
