package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.sys.web.DataCaseController;
import xyz.zaijushou.zhx.utils.*;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCaseServiceImpl implements DataCaseService {
    private static Logger logger = LoggerFactory.getLogger(DataCaseServiceImpl.class);
    @Resource
    private TelIpManageMapper telIpManageMapper;
    @Resource
    private DataCaseMapper dataCaseMapper;
    @Resource
    private DataCaseAddressMapper dataCaseAddressMapper;
    @Resource
    private DataCaseTelMapper dataCaseTelMapper;
    @Resource
    private DataCaseCommentMapper dataCaseCommentMapper;
    @Resource
    private DataCaseInterestMapper dataCaseInterestMapper;
    @Resource
    private DataCaseRepayMapper dataCaseRepayMapper;
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层
    @Autowired
    private SysDictionaryService sysDictionaryService;
    @Resource
    private SysDictionaryMapper sysDictionaryMapper;
    @Resource
    private DataCaseContactsMapper dataCaseContactsMapper;
    @Resource
    private DataCaseRemarkMapper dataCaseRemarkMapper;
    @Resource
    private DataBatchMapper dataBatchMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private DataLogService dataLogService;
    @Resource
    private DataCaseSynergisticMapper dataCaseSynergisticMapper;
    @Resource
    private SysUserMapper sysUserMapper;
    @Resource
    private SysRoleMapper sysRoleMapper;
    @Resource
    private SysPercentMapper sysPercentMapper;

    @Override
    public void save(DataCaseEntity dataCaseEntity){
        String address = "";
        String mobile = "";
        List<DataCaseAddressEntity> addressEntityList = dataCaseEntity.getAddressList();
        List<DataCaseTelEntity> telEntityList = dataCaseEntity.getTelList();
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = addressEntityList.get(i);
            address = address+","+dataCaseAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity dataCaseTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataCaseTelEntity.getTel();
        }
        int id = dataCaseMapper.saveCase(dataCaseEntity);
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = addressEntityList.get(i);
            dataCaseAddressEntity.setCaseId(id);
            address = address+","+dataCaseAddressEntity.getAddress();
            dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity dataCaseTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataCaseTelEntity.getTel();
            dataCaseTelEntity.setCaseId(id);
            dataCaseTelMapper.saveTel(dataCaseTelEntity);
        }

    }
    @Override
    public void update(DataCaseEntity dataCaseEntity){
        String address = "";
        String mobile = "";
        List<DataCaseAddressEntity> addressEntityList = dataCaseEntity.getAddressList();
        List<DataCaseTelEntity> telEntityList = dataCaseEntity.getTelList();
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = addressEntityList.get(i);
            address = address+","+dataCaseAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity dataCaseTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataCaseTelEntity.getTel();
        }
        DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
        dataCaseAddressEntity.setCaseId(dataCaseEntity.getId());
        dataCaseAddressMapper.deleteAddress(dataCaseAddressEntity);
        DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
        dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
        dataCaseTelMapper.deleteTel(dataCaseTelEntity);
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity temp = addressEntityList.get(i);
            temp.setCaseId(dataCaseEntity.getId());
            dataCaseAddressMapper.saveAddress(temp);
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity temp = telEntityList.get(j);
            temp.setCaseId(dataCaseEntity.getId());
            dataCaseTelMapper.saveTel(temp);
        }
        dataCaseMapper.updateCase(dataCaseEntity);
    }
    @Override
    public void delete(DataCaseEntity dataCaseEntity){
        DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
        dataCaseAddressEntity.setCaseId(dataCaseEntity.getId());
        dataCaseAddressMapper.deleteAddress(dataCaseAddressEntity);
        DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
        dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
        dataCaseTelMapper.deleteTel(dataCaseTelEntity);
        dataCaseMapper.deleteById(dataCaseEntity.getId());
        DataCaseEntity updateBatchEntity = dataCaseMapper.findById(dataCaseEntity);
        stringRedisTemplate.delete(RedisKeyPrefix.DATA_CASE + updateBatchEntity.getSeqNo());
        stringRedisTemplate.delete(RedisKeyPrefix.DATA_CASE+updateBatchEntity.getCardNo()+"@"+updateBatchEntity.getCaseDate());
        //修改批次信息

        DataBatchEntity dataBatchEntity = new DataBatchEntity();
        dataBatchEntity.setBatchNo(updateBatchEntity.getBatchNo());
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dataBatchEntity.setUploadTime(sdf.format(new Date()));
        dataBatchEntity.setTotalAmt(updateBatchEntity.getMoney()==null?new BigDecimal(0):new BigDecimal(0).subtract(updateBatchEntity.getMoney()));
        dataBatchEntity.setUserCount(-1);
        dataBatchMapper.updateUploadTimeByBatchNo(dataBatchEntity);
    }
    @Override
    public List<DataCaseEntity> pageDataCaseList(DataCaseEntity dataCaseEntity){
        dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        List<DataCaseEntity> list =  dataCaseMapper.pageDataCase(dataCaseEntity);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
            dataCaseAddressEntity.setCaseId(temp.getId());
            List<DataCaseAddressEntity> addressEntityList = dataCaseAddressMapper.findAll(dataCaseAddressEntity);
            temp.setAddressList(addressEntityList);
            DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
            dataCaseTelEntity.setCaseId(temp.getId());
            List<DataCaseTelEntity> telEntityList = dataCaseTelMapper.findAll(dataCaseTelEntity);
            temp.setTelList(telEntityList);
            list.set(i,temp);
        }
        return list;
    }

    @Override
    public WebResponse pageCaseList(DataCaseEntity dataCaseEntity) throws Exception{
        WebResponse webResponse = WebResponse.buildResponse();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        int totalCaseNum=0;
        BigDecimal totalAmt=new BigDecimal(0);
        int repayNum=0;
        BigDecimal repayTotalAmt=new BigDecimal(0);
        BigDecimal totalCp=new BigDecimal(0);
        BigDecimal totalPtp=new BigDecimal(0);
        if(org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setSort("desc");
            dataCaseEntity.setOrderBy("da.id");
        }else {
            dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        }
        String[] clients = dataCaseEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCaseEntity.setClientFlag(null);
        }else{
            dataCaseEntity.setClientFlag("1");
        }
        String[] odvs = dataCaseEntity.getOdvs();
        if (odvs == null || odvs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(odvs[0])){
            dataCaseEntity.setOdvFlag(null);
        }else{
            dataCaseEntity.setOdvFlag("1");
        }
        String[] batchNos = dataCaseEntity.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            dataCaseEntity.setBatchNoFlag(null);
        }else{
            dataCaseEntity.setBatchNoFlag("1");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdStr())){
            dataCaseEntity.setIdFlag(null);
        }else{
            String[] ids = dataCaseEntity.getIdStr().split(",");
            if (ids == null || ids.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(ids[0])){
                dataCaseEntity.setIdFlag(null);
            }else {
                dataCaseEntity.setIdFlag("1");
                dataCaseEntity.setIds(ids);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getName())){
            dataCaseEntity.setNameFlag(null);
        }else{
            String[] names = dataCaseEntity.getName().split(",");
            if (names == null || names.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(names[0])){
                dataCaseEntity.setNameFlag(null);
            }else {
                dataCaseEntity.setNameFlag("1");
                dataCaseEntity.setNames(names);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getArchiveNo())){
            dataCaseEntity.setArchiveNoFlag(null);
        }else{
            String[] archiveNos = dataCaseEntity.getArchiveNo().split(",");
            if (archiveNos == null || archiveNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(archiveNos[0])){
                dataCaseEntity.setArchiveNoFlag(null);
            }else {
                dataCaseEntity.setArchiveNoFlag("1");
                dataCaseEntity.setArchiveNos(archiveNos);
            }
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getAccount())){
            dataCaseEntity.setAccountFlag(null);
        }else{
            String[] accounts = dataCaseEntity.getAccount().split(",");
            if (accounts == null || accounts.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(accounts[0])){
                dataCaseEntity.setAccountFlag(null);
            }else {
                dataCaseEntity.setAccountFlag("1");
                dataCaseEntity.setAccounts(accounts);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getCardNo())){
            dataCaseEntity.setCardNoFlag(null);
        }else{
            String[] cardNos = dataCaseEntity.getCardNo().split(",");
            if (cardNos == null || cardNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(cardNos[0])){
                dataCaseEntity.setCardNoFlag(null);
            }else {
                dataCaseEntity.setCardNoFlag("1");
                dataCaseEntity.setCardNos(cardNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getSeqNo())){
            dataCaseEntity.setSeqNoFlag(null);
        }else{
            String[] seqNos = dataCaseEntity.getSeqNo().split(",");
            if (seqNos == null || seqNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(seqNos[0])){
                dataCaseEntity.setSeqNoFlag(null);
            }else {
                dataCaseEntity.setSeqNoFlag("1");
                dataCaseEntity.setSeqNos(seqNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdentNo())){
            dataCaseEntity.setIdentNoFlag(null);
        }else{
            String[] identNos = dataCaseEntity.getIdentNo().split(",");
            if (identNos == null || identNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(identNos[0])){
                dataCaseEntity.setIdentNoFlag(null);
            }else {
                dataCaseEntity.setIdentNoFlag("1");
                dataCaseEntity.setIdentNos(identNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getColor())){
            dataCaseEntity.setColor(null);
        }else{
            dataCaseEntity.setColor(ColorEnum.getEnumByKey(dataCaseEntity.getColor()).toString());
        }
        if (dataCaseEntity.getDistributeStatus()==1){
            dataCaseEntity.setDistributeStatusFlag("1");
        }else if(dataCaseEntity.getDistributeStatus()==2){
            dataCaseEntity.setDistributeStatusFlag("2");
        }else{
            dataCaseEntity.setDistributeStatusFlag(null);
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();

        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.pageBatchBoundsCaseList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                totalAmt = totalAmt.add(temp.getMoney());
                if (temp.getEnRepayAmt().compareTo(new BigDecimal(0))>0){
                    repayNum = repayNum+1;
                    repayTotalAmt =repayTotalAmt.add(temp.getEnRepayAmt());
                }
                totalCp = totalCp.add(temp.getBankAmt());
                totalPtp = totalPtp.add(temp.getProRepayAmt());
                if (temp.getCollectStatus()==0){
                    temp.setCollectStatusMsg("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                    temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getCollectArea())){
                    temp.setCollectArea("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectArea(),SysDictionaryEntity.class);
                    temp.setCollectArea(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getDistributeHistory())){
                    temp.setDistributeHistory(temp.getDistributeHistory().substring(1));
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getAccountAge())){
                    temp.setAccountAge("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
                    temp.setAccountAge(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
                    temp.setOdv("");
                }else {
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                    temp.setOdv(user == null ? "" : user.getUserName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getColor())){
                    temp.setColor("BLACK");
                }
                SysDictionaryEntity clientDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
                temp.setClient(clientDic==null?"":clientDic.getName());
                SysDictionaryEntity summaryDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getSummary(),SysDictionaryEntity.class);
                temp.setSummary(summaryDic==null?"":summaryDic.getName());
                SysDictionaryEntity collectionTypeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectionType(),SysDictionaryEntity.class);
                temp.setCollectionType(collectionTypeDic==null?"":collectionTypeDic.getName());
                if (StringUtils.notEmpty(temp.getDistributeHistory())){
                    temp.setDistributeHistory(temp.getDistributeHistory().substring(1));
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getColor())){
                    temp.setColor("BLACK");
                }
                temp.setMoneyMsg(temp.getMoney()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getMoney()+""));
                temp.setBankAmtMsg(temp.getBankAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getBankAmt()+""));
                temp.setBalanceMsg(temp.getBalance()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getBalance()+""));
                temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getProRepayAmt()+""));
                temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt()+""));
                list.set(i,temp);
            }
        }else {
            list = dataCaseMapper.pageCaseMangeList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                totalAmt = totalAmt.add(temp.getMoney()==null?new BigDecimal(0):temp.getMoney());
                if (temp.getEnRepayAmt()!=null && temp.getEnRepayAmt().compareTo(new BigDecimal(0))>0){
                    repayNum = repayNum+1;
                    repayTotalAmt =repayTotalAmt.add(temp.getEnRepayAmt());
                }
                totalCp = totalCp.add(temp.getBankAmt()==null?new BigDecimal(0):temp.getBankAmt());
                totalPtp = totalPtp.add(temp.getProRepayAmt()==null?new BigDecimal(0):temp.getProRepayAmt());
                CaseCallable caseCallable = new CaseCallable(list,temp,i);
                Future<List<DataCaseEntity>> future = executor.submit(caseCallable);
//                /list = future.get();
            }
            executor.shutdown();
            while(true){
                if(executor.isTerminated()){
                    break;
                }
                Thread.sleep(100);
            }

        }
        totalCaseNum = new Long(PageInfo.of(list).getTotal()).intValue();
        CaseResponse caseResponse = new CaseResponse();
        caseResponse.setTotalCaseNum(totalCaseNum);
        caseResponse.setTotalAmt(totalAmt);
        caseResponse.setRepayNum(repayNum);
        caseResponse.setRepayTotalAmt(repayTotalAmt);
        caseResponse.setTotalCp(totalCp);
        caseResponse.setTotalPtp(totalPtp);
        caseResponse.setPageInfo(PageInfo.of(combineData(list)));
        webResponse.setData(caseResponse);
        return webResponse;
    }

    @Override
    public WebResponse pageCaseListOnly(DataCaseEntity dataCaseEntity) throws Exception{
        WebResponse webResponse = WebResponse.buildResponse();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        int totalCaseNum=0;
        BigDecimal totalAmt=new BigDecimal(0);
        int repayNum=0;
        BigDecimal repayTotalAmt=new BigDecimal(0);
        BigDecimal totalCp=new BigDecimal(0);
        BigDecimal totalPtp=new BigDecimal(0);
        if(org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setSort("desc");
            dataCaseEntity.setOrderBy("da.id");
        }else {
            dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        }
        String[] clients = dataCaseEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCaseEntity.setClientFlag(null);
        }else{
            dataCaseEntity.setClientFlag("1");
        }
        String[] odvs = dataCaseEntity.getOdvs();
        if (odvs == null || odvs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(odvs[0])){
            dataCaseEntity.setOdvFlag(null);
        }else{
            dataCaseEntity.setOdvFlag("1");
        }
        String[] batchNos = dataCaseEntity.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            dataCaseEntity.setBatchNoFlag(null);
        }else{
            dataCaseEntity.setBatchNoFlag("1");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdStr())){
            dataCaseEntity.setIdFlag(null);
        }else{
            String[] ids = dataCaseEntity.getIdStr().split(",");
            if (ids == null || ids.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(ids[0])){
                dataCaseEntity.setIdFlag(null);
            }else {
                dataCaseEntity.setIdFlag("1");
                dataCaseEntity.setIds(ids);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getName())){
            dataCaseEntity.setNameFlag(null);
        }else{
            String[] names = dataCaseEntity.getName().split(",");
            if (names == null || names.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(names[0])){
                dataCaseEntity.setNameFlag(null);
            }else {
                dataCaseEntity.setNameFlag("1");
                dataCaseEntity.setNames(names);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getArchiveNo())){
            dataCaseEntity.setArchiveNoFlag(null);
        }else{
            String[] archiveNos = dataCaseEntity.getArchiveNo().split(",");
            if (archiveNos == null || archiveNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(archiveNos[0])){
                dataCaseEntity.setArchiveNoFlag(null);
            }else {
                dataCaseEntity.setArchiveNoFlag("1");
                dataCaseEntity.setArchiveNos(archiveNos);
            }
        }

        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getAccount())){
            dataCaseEntity.setAccountFlag(null);
        }else{
            String[] accounts = dataCaseEntity.getAccount().split(",");
            if (accounts == null || accounts.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(accounts[0])){
                dataCaseEntity.setAccountFlag(null);
            }else {
                dataCaseEntity.setAccountFlag("1");
                dataCaseEntity.setAccounts(accounts);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getCardNo())){
            dataCaseEntity.setCardNoFlag(null);
        }else{
            String[] cardNos = dataCaseEntity.getCardNo().split(",");
            if (cardNos == null || cardNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(cardNos[0])){
                dataCaseEntity.setCardNoFlag(null);
            }else {
                dataCaseEntity.setCardNoFlag("1");
                dataCaseEntity.setCardNos(cardNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getSeqNo())){
            dataCaseEntity.setSeqNoFlag(null);
        }else{
            String[] seqNos = dataCaseEntity.getSeqNo().split(",");
            if (seqNos == null || seqNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(seqNos[0])){
                dataCaseEntity.setSeqNoFlag(null);
            }else {
                dataCaseEntity.setSeqNoFlag("1");
                dataCaseEntity.setSeqNos(seqNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdentNo())){
            dataCaseEntity.setIdentNoFlag(null);
        }else{
            String[] identNos = dataCaseEntity.getIdentNo().split(",");
            if (identNos == null || identNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(identNos[0])){
                dataCaseEntity.setIdentNoFlag(null);
            }else {
                dataCaseEntity.setIdentNoFlag("1");
                dataCaseEntity.setIdentNos(identNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getColor())){
            dataCaseEntity.setColor(null);
        }else{
            dataCaseEntity.setColor(ColorEnum.getEnumByKey(dataCaseEntity.getColor()).toString());
        }
        if (dataCaseEntity.getDistributeStatus()==1){
            dataCaseEntity.setDistributeStatusFlag("1");
        }else if(dataCaseEntity.getDistributeStatus()==2){
            dataCaseEntity.setDistributeStatusFlag("2");
        }else{
            dataCaseEntity.setDistributeStatusFlag(null);
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();

        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.pageBatchBoundsCaseList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                totalAmt = totalAmt.add(temp.getMoney());
                if (temp.getEnRepayAmt().compareTo(new BigDecimal(0))>0){
                    repayNum = repayNum+1;
                    repayTotalAmt =repayTotalAmt.add(temp.getEnRepayAmt());
                }
                totalCp = totalCp.add(temp.getBankAmt());
                totalPtp = totalPtp.add(temp.getProRepayAmt());

                CaseCallable caseCallable = new CaseCallable(list,temp,i);
                Future<List<DataCaseEntity>> future = executor.submit(caseCallable);
            }
            while(true){
                if(executor.isTerminated()){
                    break;
                }
                Thread.sleep(50);
            }
        }else {
            list = dataCaseMapper.pageCaseMangeList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                totalAmt = totalAmt.add(temp.getMoney()==null?new BigDecimal(0):temp.getMoney());
                if (temp.getEnRepayAmt()!=null && temp.getEnRepayAmt().compareTo(new BigDecimal(0))>0){
                    repayNum = repayNum+1;
                    repayTotalAmt =repayTotalAmt.add(temp.getEnRepayAmt());
                }
                totalCp = totalCp.add(temp.getBankAmt()==null?new BigDecimal(0):temp.getBankAmt());
                totalPtp = totalPtp.add(temp.getProRepayAmt()==null?new BigDecimal(0):temp.getProRepayAmt());
                CaseCallable caseCallable = new CaseCallable(list,temp,i);
                Future<List<DataCaseEntity>> future = executor.submit(caseCallable);

            }
            executor.shutdown();
            while(true){
                if(executor.isTerminated()){
                    break;
                }
                Thread.sleep(50);
            }

        }
        totalCaseNum = new Long(PageInfo.of(list).getTotal()).intValue();
        CaseResponse caseResponse = new CaseResponse();
        caseResponse.setTotalCaseNum(totalCaseNum);
        caseResponse.setTotalAmt(totalAmt);
        caseResponse.setRepayNum(repayNum);
        caseResponse.setRepayTotalAmt(repayTotalAmt);
        caseResponse.setTotalCp(totalCp);
        caseResponse.setTotalPtp(totalPtp);
        caseResponse.setPageInfo(PageInfo.of(list));
        webResponse.setData(caseResponse);
        return webResponse;
    }


    /**
     * 催收模块-我的案件-列表查询（废弃）
     * @param dataCaseEntity
     * @return
     */
    @Override
    public List<DataCaseEntity> pageCaseInfoList(DataCaseEntity dataCaseEntity){
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return list;
        }
        dataCaseEntity.setName(user.getUserName());//当前用户
        list =  dataCaseMapper.pageDataCase(dataCaseEntity);

        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);

            list.set(i,temp);
        }
        return list;
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }

    /**
     *  催收管理-统计
     * @param bean
     */
    @Override
    public void sumCaseMoney(DataCaseEntity bean){
        dataCaseMapper.sumCaseMoney(bean);
        return ;
    }
    //未退案0/正常1/暂停2/关档3/退档4/全部5
    @Override
    public void updateStatus(DataCaseEntity bean){
        dataCaseMapper.updateStatus(bean);
    }

    @Override
    public void sendOdv(DataCaseEntity bean){
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ bean.getOdv(), SysUserEntity.class);
        bean.setDistributeHistory(",分配给"+user.getUserName());
        bean.setDept(user==null?"":user.getDepartment());
        dataCaseMapper.sendOdv(bean);
        DataOpLog log = new DataOpLog();
        log.setType("案件管理");
        log.setContext(bean.getDistributeHistory());
        log.setOper(getUserInfo().getId());
        log.setOperName(getUserInfo().getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.setOpTime(sdf.format(new Date()));
        log.setCaseId(bean.getId()+"");
        dataLogService.saveDataLog(log);
    }
    @Override
    public void sendOdvByProperty(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setOrderBy("id");
            dataCaseEntity.setSort("desc");
        }else {
            dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        }
        String[] clients = dataCaseEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCaseEntity.setClientFlag(null);
        }else{
            dataCaseEntity.setClientFlag("1");
        }
        String[] odvs = dataCaseEntity.getOdvs();
        if (odvs == null || odvs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(odvs[0])){
            dataCaseEntity.setOdvFlag(null);
        }else{
            dataCaseEntity.setOdvFlag("1");
        }
        String[] batchNos = dataCaseEntity.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            dataCaseEntity.setBatchNoFlag(null);
        }else{
            dataCaseEntity.setBatchNoFlag("1");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdStr())){
            dataCaseEntity.setIdFlag(null);
        }else{
            String[] ids = dataCaseEntity.getIdStr().split(",");
            if (ids == null || ids.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(ids[0])){
                dataCaseEntity.setIdFlag(null);
            }else {
                dataCaseEntity.setIdFlag("1");
                dataCaseEntity.setIds(ids);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getName())){
            dataCaseEntity.setNameFlag(null);
        }else{
            String[] names = dataCaseEntity.getName().split(",");
            if (names == null || names.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(names[0])){
                dataCaseEntity.setNameFlag(null);
            }else {
                dataCaseEntity.setNameFlag("1");
                dataCaseEntity.setNames(names);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getArchiveNo())){
            dataCaseEntity.setArchiveNoFlag(null);
        }else{
            String[] archiveNos = dataCaseEntity.getArchiveNo().split(",");
            if (archiveNos == null || archiveNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(archiveNos[0])){
                dataCaseEntity.setArchiveNoFlag(null);
            }else {
                dataCaseEntity.setArchiveNoFlag("1");
                dataCaseEntity.setArchiveNos(archiveNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getAccount())){
            dataCaseEntity.setAccountFlag(null);
        }else{
            String[] accounts = dataCaseEntity.getAccount().split(",");
            if (accounts == null || accounts.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(accounts[0])){
                dataCaseEntity.setAccountFlag(null);
            }else {
                dataCaseEntity.setAccountFlag("1");
                dataCaseEntity.setAccounts(accounts);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getCardNo())){
            dataCaseEntity.setCardNoFlag(null);
        }else{
            String[] cardNos = dataCaseEntity.getCardNo().split(",");
            if (cardNos == null || cardNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(cardNos[0])){
                dataCaseEntity.setCardNoFlag(null);
            }else {
                dataCaseEntity.setCardNoFlag("1");
                dataCaseEntity.setCardNos(cardNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getSeqNo())){
            dataCaseEntity.setSeqNoFlag(null);
        }else{
            String[] seqNos = dataCaseEntity.getSeqNo().split(",");
            if (seqNos == null || seqNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(seqNos[0])){
                dataCaseEntity.setSeqNoFlag(null);
            }else {
                dataCaseEntity.setSeqNoFlag("1");
                dataCaseEntity.setSeqNos(seqNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdentNo())){
            dataCaseEntity.setIdentNoFlag(null);
        }else{
            String[] identNos = dataCaseEntity.getIdentNo().split(",");
            if (identNos == null || identNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(identNos[0])){
                dataCaseEntity.setIdentNoFlag(null);
            }else {
                dataCaseEntity.setIdentNoFlag("1");
                dataCaseEntity.setIdentNos(identNos);
            }
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.pageBatchBoundsCaseList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                if (temp.getCollectStatus()==0){
                    temp.setCollectStatusMsg("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                    temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getCollectArea())){
                    temp.setCollectArea("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectArea(),SysDictionaryEntity.class);
                    temp.setCollectArea(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
                    temp.setOdv("");
                }else {
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                    temp.setOdv(user == null ? "" : user.getUserName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getColor())){
                    temp.setColor("BLACK");
                }
                list.set(i,temp);
            }
        }else {
            list = dataCaseMapper.pageCaseList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                if (temp.getCollectStatus()==0){
                    temp.setCollectStatusMsg("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                    temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getCollectArea())){
                    temp.setCollectArea("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectArea(),SysDictionaryEntity.class);
                    temp.setCollectArea(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
                    temp.setOdv("");
                }else {
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                    temp.setOdv(user == null ? "" : user.getUserName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getColor())){
                    temp.setColor("BLACK");
                }
                list.set(i,temp);
            }
        }
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataCaseEntity.getOdv(), SysUserEntity.class);
        String[] ids = new String[list.size()];
        for (int i=0;i<list.size();i++){
            DataCaseEntity bean = list.get(i);
            ids[i] =bean.getId()+"";

        }
        dataCaseEntity.setDistributeHistory(",分配给"+user.getUserName());
        dataCaseEntity.setIds(ids);
        dataCaseEntity.setDept(user==null?"":user.getDepartment());
        dataCaseMapper.sendOdvByProperty(dataCaseEntity);

    }
    @Override
    public void addComment(DataCaseEntity bean){
        DataCaseCommentEntity dataCaseCommentEntity = new DataCaseCommentEntity();
        dataCaseCommentEntity.setCaseId(bean.getId());
        dataCaseCommentEntity.setComment(bean.getComment());
        SysUserEntity user = this.getUserInfo();
        dataCaseCommentEntity.setCreatUser(user.getId());
        String color = "";
        if(org.apache.commons.lang3.StringUtils.isEmpty(bean.getColor())){
            color = "BLACK";
        }else{
            color = ColorEnum.getEnumByKey(bean.getColor()).getValue();
            DataOpLog log = new DataOpLog();
            log.setType("评语");
            log.setContext(bean.getComment()+"["+color+"]");
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(bean.getId()+"");
            dataLogService.saveDataLog(log);
        }
        dataCaseCommentEntity.setCommentColor(color);
        dataCaseCommentMapper.saveComment(dataCaseCommentEntity);


    }

    @Override
    public void addWarning(DataCaseEntity bean){
        dataCaseMapper.addWarning(bean);
    }
    @Override
    public DataCaseEntity nextCase(DataCaseEntity bean){
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        bean.setOdv(user.getId()+"");
        return dataCaseMapper.nextCase(bean);
    }
    @Override
    public DataCaseEntity lastCase(DataCaseEntity bean){
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        bean.setOdv(user.getId()+"");
        return dataCaseMapper.lastCase(bean);
    }
    @Override
    public void addColor(DataCaseEntity bean){
        String color = ColorEnum.getEnumByKey(bean.getColor()).getValue();
        bean.setColor(color);
        dataCaseMapper.addColor(bean);

        DataCollectionEntity dataCollectionEntity = new DataCollectionEntity();
        dataCollectionEntity.setColor(color);
        dataCollectionEntity.setCaseId(bean.getId()+"");
        dataCollectionMapper.addColor(dataCollectionEntity);

    }
    @Override
    public void addImportant(DataCaseEntity bean){
        dataCaseMapper.addImportant(bean);
    }
    @Override
    public void addCollectStatus(DataCaseEntity bean){
        dataCaseMapper.addCollectStatus(bean);
        DataCollectionEntity dataCollectionEntity = new DataCollectionEntity();
        dataCollectionEntity.setCaseId(bean.getId()+"");
        dataCollectionEntity.setCollectStatus(bean.getCollectStatus());
        dataCollectionMapper.addCollectStatus(dataCollectionEntity);
    }
    @Override
    public void addCollectArea(DataCaseEntity bean){
        dataCaseMapper.addCollectArea(bean);
    }
    @Override
    public void addMValue(DataCaseEntity bean){
        dataCaseMapper.addMValue(bean);
    }

    //2 待审核  1 最终同意申请  3代办 4撤销申请
    @Override
    public void addSynergy(DataCaseEntity bean){
        dataCaseMapper.addSynergy(bean);
        DataCaseSynergyDetailEntity dataCaseSynergyDetailEntity = new DataCaseSynergyDetailEntity();
        SysUserEntity curentuser = getUserInfo();
        dataCaseSynergyDetailEntity.setApplyer(curentuser.getId());
        dataCaseSynergyDetailEntity.setCaseId(bean.getId());
        dataCaseSynergyDetailEntity.setSynergisticType(Integer.parseInt(bean.getSynergyType()));
        dataCaseSynergyDetailEntity.setApplyContent(bean.getSynergyContext());
        dataCaseSynergisticMapper.saveApply(dataCaseSynergyDetailEntity);
    }

    public void updateSynergy(DataCaseEntity bean){
        dataCaseMapper.updateSynergy(bean);
    }
    //部门案件 --- 来电查询
    public WebResponse pageSynergyInfo(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCaseEntity> list =  dataCaseMapper.pageSynergyInfo(dataCaseEntity);
        int count = dataCaseMapper.countSynergyInfo(dataCaseEntity);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user.getUserName());
            list.set(i,temp);
        }
        webResponse.setData(list);
        int totalPageNum = 0 ;
        if (count%dataCaseEntity.getPageSize()>0){
            totalPageNum = count/dataCaseEntity.getPageSize()+1;
        }else{
            totalPageNum = count/dataCaseEntity.getPageSize();
        }

        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setTotalNum(count);
        return webResponse;
    }

    //部门案件 --- 来电查询
    public WebResponse pageCaseTel(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = dataCaseEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCaseEntity.setClientFlag(null);
        }else{
            dataCaseEntity.setClientFlag("1");
        }
        if (StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setOrderBy("id");
            dataCaseEntity.setSort("desc");
        }

        dataCaseEntity.setOrderBy(SynergySortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        List<DataCaseEntity> list =  dataCaseMapper.pageCaseTel(dataCaseEntity);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            temp.setAccountAge(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

            temp.setMoneyMsg(temp.getMoney()==null?"": "￥"+FmtMicrometer.fmtMicrometer(temp.getMoney()+""));
            temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"": "￥"+FmtMicrometer.fmtMicrometer(temp.getProRepayAmt()+""));
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"": "￥"+FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt()+""));
            list.set(i,temp);
        }
        webResponse.setData(PageInfo.of(list));

        return webResponse;
    }

    @Override
    public List<DataCaseEntity> listBySeqNoSet(DataCaseEntity queryEntity) {
        return dataCaseMapper.listBySeqNoSet(queryEntity);
    }

    @Override
    public void updateBySeqNo(DataCaseEntity entity) {
        dataCaseMapper.updateBySeqNo(entity);
    }

    @Transactional
    @Override
    public void updateCaseList(List<DataCaseEntity> dataCaseEntities) {
        List<SysDictionaryEntity> dictionaryList = sysDictionaryMapper.getDataList(new SysDictionaryEntity());
        Map<String, SysDictionaryEntity> dictMap = new HashMap<>();
        for(SysDictionaryEntity entity : dictionaryList) {
            dictMap.put(entity.getName(), entity);
        }
        //下面这段代码逻辑，判断导入的省份是否在字典里，如果存在，则更新案件省份的id，并相应继续判断地市和区县
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            DataCaseEntity entity = dataCaseEntities.get(i);
            if(entity.getCollectionUser()!=null && entity.getCollectionUser().getId()!=null) {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + entity.getCollectionUser().getId(), SysUserEntity.class);
                entity.setDept(user == null ? "" : user.getDepartment());
            }
            if(entity.getProvince() != null && !StringUtils.isEmpty(entity.getProvince().getName()) && dictMap.containsKey(entity.getProvince().getName())) {
                dataCaseEntities.get(i).getProvince().setId(dictMap.get(entity.getProvince().getName()).getId());
                if(entity.getCity() != null && !StringUtils.isEmpty(entity.getCity().getName()) && dictMap.containsKey(entity.getCity().getName())) {
                    if(dictMap.get(entity.getProvince().getName()).getId().equals(dictMap.get(entity.getCity().getName()).getParent().getId())) {
                        dataCaseEntities.get(i).getCity().setId(dictMap.get(entity.getCity().getName()).getId());
                        if(entity.getCounty() != null && !StringUtils.isEmpty(entity.getCounty().getName()) && dictMap.containsKey(entity.getCounty().getName())) {
                            if(dictMap.get(entity.getCity().getName()).getId().equals(dictMap.get(entity.getCounty().getName()).getParent().getId())) {
                                dataCaseEntities.get(i).getCounty().setId(dictMap.get(entity.getCounty().getName()).getId());
                            }
                        }
                    }
                }
            }
        }
        Set<Integer> caseIdsSet = new HashSet<>();
        DataCaseEntity updateCaseEntity = new DataCaseEntity();
        List<DataCaseRemarkEntity> remarks = new ArrayList<>();
        List<DataCaseContactsEntity> contacts = new ArrayList<>();
        for(DataCaseEntity entity : dataCaseEntities) {
            //totalAmt = totalAmt.add(entity.getMoney());
            dataCaseMapper.updateBySeqNo(entity);
            caseIdsSet.add(entity.getId());
            if (entity.getCaseRemarks()!=null && entity.getCaseRemarks().size()>0) {
                for (DataCaseRemarkEntity remark : entity.getCaseRemarks()) {
                    remark.setCaseId(entity.getId());
                    remarks.add(remark);
                }
            }
            int i = 0;
            if (entity.getContacts()!=null && entity.getContacts().size()>0) {
                for (DataCaseContactsEntity contact : entity.getContacts()) {
                    contact.setCaseId(entity.getId());
                    contacts.add(contact);
                    contact.setSort((++i) * 10);
                }
            }
        }
     ;
        //批量新增备注
        if(remarks.size()>0) {
            updateCaseEntity.setCaseRemarks(remarks);
            updateCaseEntity.setContacts(contacts);
            //批量删除备注
            DataCaseRemarkEntity remarkEntity = new DataCaseRemarkEntity();
            remarkEntity.setCaseIdsSet(caseIdsSet);
            dataCaseRemarkMapper.deleteCaseRemarkBatchByCaseIds(remarkEntity);
            dataCaseRemarkMapper.insertCaseRemarkBatch(updateCaseEntity);
        }

        //批量新增联系人
        if (contacts.size()>0){
            //批量删除联系人
            DataCaseContactsEntity contactsEntity = new DataCaseContactsEntity();
            contactsEntity.setCaseIdsSet(caseIdsSet);
            dataCaseContactsMapper.deleteCaseContactsBatchByCaseIds(contactsEntity);
            dataCaseContactsMapper.insertCaseContactsBatch(updateCaseEntity);


        }
        /*//修改批次信息
        DataBatchEntity dataBatchEntity = new DataBatchEntity();
        dataBatchEntity.setBatchNo(batchNo);
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dataBatchEntity.setUploadTime(sdf.format(new Date()));
        dataBatchEntity.setTotalAmt(totalAmt);
        dataBatchEntity.setUserCount(dataCaseEntities.size());
        dataBatchMapper.updateUploadTimeByBatchNo(dataBatchEntity);*/
    }

    @Transactional
    @Override
    public void saveCaseList(List<DataCaseEntity> dataCaseEntities,String batchNo) {
        BigDecimal totalAmt = new BigDecimal(0);
        List<SysDictionaryEntity> dictionaryList = sysDictionaryMapper.getDataList(new SysDictionaryEntity());
        Map<String, SysDictionaryEntity> dictMap = new HashMap<>();
        for(SysDictionaryEntity entity : dictionaryList) {
            dictMap.put(entity.getName(), entity);
        }
        //下面这段代码逻辑，判断导入的省份是否在字典里，如果存在，则更新案件省份的id，并相应继续判断地市和区县
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            DataCaseEntity entity = dataCaseEntities.get(i);
            if(entity.getCollectionUser()!=null && entity.getCollectionUser().getId()!=null) {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + entity.getCollectionUser().getId(), SysUserEntity.class);
                entity.setDept(user == null ? "" : user.getDepartment());
            }
            if(entity.getProvince() != null && !StringUtils.isEmpty(entity.getProvince().getName()) && dictMap.containsKey(entity.getProvince().getName())) {
                dataCaseEntities.get(i).getProvince().setId(dictMap.get(entity.getProvince().getName()).getId());
                if(entity.getCity() != null && !StringUtils.isEmpty(entity.getCity().getName()) && dictMap.containsKey(entity.getCity().getName())) {
                    if(dictMap.get(entity.getProvince().getName()).getId().equals(dictMap.get(entity.getCity().getName()).getParent().getId())) {
                        dataCaseEntities.get(i).getCity().setId(dictMap.get(entity.getCity().getName()).getId());
                        if(entity.getCounty() != null && !StringUtils.isEmpty(entity.getCounty().getName()) && dictMap.containsKey(entity.getCounty().getName())) {
                            if(dictMap.get(entity.getCity().getName()).getId().equals(dictMap.get(entity.getCounty().getName()).getParent().getId())) {
                                dataCaseEntities.get(i).getCounty().setId(dictMap.get(entity.getCounty().getName()).getId());
                            }
                        }
                    }
                }
            }
        }
        DataCaseEntity updateCaseEntity = new DataCaseEntity();
        List<DataCaseRemarkEntity> remarks = new ArrayList<>();
        List<DataCaseContactsEntity> contacts = new ArrayList<>();
        for(DataCaseEntity entity : dataCaseEntities) {
            dataCaseMapper.saveCase(entity);
            totalAmt = totalAmt.add(entity.getMoney());



            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + entity.getSeqNo(), JSONObject.toJSONString(entity));
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + entity.getCardNo()+"@"+entity.getCaseDate(), JSONObject.toJSONString(entity));
            if (entity.getCaseRemarks()!=null && entity.getCaseRemarks().size()>0) {
                for (DataCaseRemarkEntity remark : entity.getCaseRemarks()) {
                    remark.setCaseId(entity.getId());
                    remarks.add(remark);
                }
            }
            int i = 0;
            if (entity.getContacts()!=null && entity.getContacts().size()>0) {
                for (DataCaseContactsEntity contact : entity.getContacts()) {
                    contact.setCaseId(entity.getId());
                    contacts.add(contact);
                    contact.setSort((++i) * 10);
                    //联系人添加到
                    DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
                    dataCaseTelEntity.setCaseId(entity.getId());
                    dataCaseTelEntity.setName(contact.getName());
                    dataCaseTelEntity.setIdentNo(contact.getIdentNo());
                    dataCaseTelEntity.setRelation(contact.getRelation());
                    dataCaseTelEntity.setTel(contact.getMobile());
                    dataCaseTelEntity.setTelStatusMsg("未知");
                    dataCaseTelMapper.saveTel(dataCaseTelEntity);
                }
            }
        }
        updateCaseEntity.setCaseRemarks(remarks);
        updateCaseEntity.setContacts(contacts);
        //批量新增备注
        if (remarks.size()>0) {
            dataCaseRemarkMapper.insertCaseRemarkBatch(updateCaseEntity);
        }
        //批量新增联系人
        if (contacts.size()>0) {
            dataCaseContactsMapper.insertCaseContactsBatch(updateCaseEntity);

        }
        //修改批次信息
        DataBatchEntity dataBatchEntity = new DataBatchEntity();
        dataBatchEntity.setBatchNo(batchNo);
        SimpleDateFormat sdf  = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dataBatchEntity.setUploadTime(sdf.format(new Date()));
        dataBatchEntity.setTotalAmt(totalAmt);
        dataBatchEntity.setUserCount(dataCaseEntities.size());
        dataBatchMapper.updateUploadTimeByBatchNo(dataBatchEntity);
    }


    public List<DataCaseEntity> pageCaseListExport(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        if(org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setOrderBy("id");
            dataCaseEntity.setSort("desc");
        }else {
            dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        }
        String[] clients = dataCaseEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCaseEntity.setClientFlag(null);
        }else{
            dataCaseEntity.setClientFlag("1");
        }
        String[] odvs = dataCaseEntity.getOdvs();
        if (odvs == null || odvs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(odvs[0])){
            dataCaseEntity.setOdvFlag(null);
        }else{
            dataCaseEntity.setOdvFlag("1");
        }
        String[] batchNos = dataCaseEntity.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            dataCaseEntity.setBatchNoFlag(null);
        }else{
            dataCaseEntity.setBatchNoFlag("1");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdStr())){
            dataCaseEntity.setIdFlag(null);
        }else{
            String[] ids = dataCaseEntity.getIdStr().split(",");
            if (ids == null || ids.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(ids[0])){
                dataCaseEntity.setIdFlag(null);
            }else {
                dataCaseEntity.setIdFlag("1");
                dataCaseEntity.setIds(ids);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getName())){
            dataCaseEntity.setNameFlag(null);
        }else{
            String[] names = dataCaseEntity.getName().split(",");
            if (names == null || names.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(names[0])){
                dataCaseEntity.setNameFlag(null);
            }else {
                dataCaseEntity.setNameFlag("1");
                dataCaseEntity.setNames(names);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getArchiveNo())){
            dataCaseEntity.setArchiveNoFlag(null);
        }else{
            String[] archiveNos = dataCaseEntity.getArchiveNo().split(",");
            if (archiveNos == null || archiveNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(archiveNos[0])){
                dataCaseEntity.setArchiveNoFlag(null);
            }else {
                dataCaseEntity.setArchiveNoFlag("1");
                dataCaseEntity.setArchiveNos(archiveNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getAccount())){
            dataCaseEntity.setAccountFlag(null);
        }else{
            String[] accounts = dataCaseEntity.getAccount().split(",");
            if (accounts == null || accounts.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(accounts[0])){
                dataCaseEntity.setAccountFlag(null);
            }else {
                dataCaseEntity.setAccountFlag("1");
                dataCaseEntity.setAccounts(accounts);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getCardNo())){
            dataCaseEntity.setCardNoFlag(null);
        }else{
            String[] cardNos = dataCaseEntity.getCardNo().split(",");
            if (cardNos == null || cardNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(cardNos[0])){
                dataCaseEntity.setCardNoFlag(null);
            }else {
                dataCaseEntity.setCardNoFlag("1");
                dataCaseEntity.setCardNos(cardNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getSeqNo())){
            dataCaseEntity.setSeqNoFlag(null);
        }else{
            String[] seqNos = dataCaseEntity.getSeqNo().split(",");
            if (seqNos == null || seqNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(seqNos[0])){
                dataCaseEntity.setSeqNoFlag(null);
            }else {
                dataCaseEntity.setSeqNoFlag("1");
                dataCaseEntity.setSeqNos(seqNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdentNo())){
            dataCaseEntity.setIdentNoFlag(null);
        }else{
            String[] identNos = dataCaseEntity.getIdentNo().split(",");
            if (identNos == null || identNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(identNos[0])){
                dataCaseEntity.setIdentNoFlag(null);
            }else {
                dataCaseEntity.setIdentNoFlag("1");
                dataCaseEntity.setIdentNos(identNos);
            }
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.pageBatchBoundsCaseList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                if (temp.getCollectStatus()==0){
                    temp.setCollectStatusMsg("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                    temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
                    temp.setOdv("");
                }else {
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                    temp.setOdv(user == null ? "" : user.getUserName());
                }
                list.set(i,temp);
            }
        }else {
            list = dataCaseMapper.pageCaseMangeList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                if (temp.getCollectStatus()==0){
                    temp.setCollectStatusMsg("");
                }else{
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                    temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
                    temp.setOdv("");
                }else {
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                    temp.setOdv(user == null ? "" : user.getUserName());
                }
                temp.setMoneyMsg(temp.getMoney()==null?"": FmtMicrometer.fmtMicrometer(temp.getMoney()+""));
                temp.setBalanceMsg(temp.getBalance()==null?"": FmtMicrometer.fmtMicrometer(temp.getBalance()+""));
                temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"": FmtMicrometer.fmtMicrometer(temp.getProRepayAmt()+""));
                temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"": FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt()+""));
                temp.setBankAmtMsg(temp.getBankAmt()==null?"":FmtMicrometer.fmtMicrometer(temp.getBankAmt()+""));
                list.set(i,temp);
            }
        }

        return combineData(PageInfo.of(list).getList());
    }



    public List<DataCaseEntity> totalCaseListExport(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        ExecutorService executor = Executors.newFixedThreadPool(20);
        BigDecimal totalAmt=new BigDecimal(0);
        BigDecimal repayTotalAmt=new BigDecimal(0);
        BigDecimal totalCp=new BigDecimal(0);
        BigDecimal totalPtp=new BigDecimal(0);
        if(org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setOrderBy("id");
            dataCaseEntity.setSort("desc");
        }else {
            dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        }
        String[] clients = dataCaseEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCaseEntity.setClientFlag(null);
        }else{
            dataCaseEntity.setClientFlag("1");
        }
        String[] odvs = dataCaseEntity.getOdvs();
        if (odvs == null || odvs.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(odvs[0])){
            dataCaseEntity.setOdvFlag(null);
        }else{
            dataCaseEntity.setOdvFlag("1");
        }
        String[] batchNos = dataCaseEntity.getBatchNos();
        if (batchNos == null || batchNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])){
            dataCaseEntity.setBatchNoFlag(null);
        }else{
            dataCaseEntity.setBatchNoFlag("1");
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdStr())){
            dataCaseEntity.setIdFlag(null);
        }else{
            String[] ids = dataCaseEntity.getIdStr().split(",");
            if (ids == null || ids.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(ids[0])){
                dataCaseEntity.setIdFlag(null);
            }else {
                dataCaseEntity.setIdFlag("1");
                dataCaseEntity.setIds(ids);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getName())){
            dataCaseEntity.setNameFlag(null);
        }else{
            String[] names = dataCaseEntity.getName().split(",");
            if (names == null || names.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(names[0])){
                dataCaseEntity.setNameFlag(null);
            }else {
                dataCaseEntity.setNameFlag("1");
                dataCaseEntity.setNames(names);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getArchiveNo())){
            dataCaseEntity.setArchiveNoFlag(null);
        }else{
            String[] archiveNos = dataCaseEntity.getArchiveNo().split(",");
            if (archiveNos == null || archiveNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(archiveNos[0])){
                dataCaseEntity.setArchiveNoFlag(null);
            }else {
                dataCaseEntity.setArchiveNoFlag("1");
                dataCaseEntity.setArchiveNos(archiveNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getAccount())){
            dataCaseEntity.setAccountFlag(null);
        }else{
            String[] accounts = dataCaseEntity.getAccount().split(",");
            if (accounts == null || accounts.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(accounts[0])){
                dataCaseEntity.setAccountFlag(null);
            }else {
                dataCaseEntity.setAccountFlag("1");
                dataCaseEntity.setAccounts(accounts);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getCardNo())){
            dataCaseEntity.setCardNoFlag(null);
        }else{
            String[] cardNos = dataCaseEntity.getCardNo().split(",");
            if (cardNos == null || cardNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(cardNos[0])){
                dataCaseEntity.setCardNoFlag(null);
            }else {
                dataCaseEntity.setCardNoFlag("1");
                dataCaseEntity.setCardNos(cardNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getSeqNo())){
            dataCaseEntity.setSeqNoFlag(null);
        }else{
            String[] seqNos = dataCaseEntity.getSeqNo().split(",");
            if (seqNos == null || seqNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(seqNos[0])){
                dataCaseEntity.setSeqNoFlag(null);
            }else {
                dataCaseEntity.setSeqNoFlag("1");
                dataCaseEntity.setSeqNos(seqNos);
            }
        }
        if (org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getIdentNo())){
            dataCaseEntity.setIdentNoFlag(null);
        }else{
            String[] identNos = dataCaseEntity.getIdentNo().split(",");
            if (identNos == null || identNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(identNos[0])){
                dataCaseEntity.setIdentNoFlag(null);
            }else {
                dataCaseEntity.setIdentNoFlag("1");
                dataCaseEntity.setIdentNos(identNos);
            }
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.totalBatchBoundsCaseList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);

                CaseExportCallable caseCallable = new CaseExportCallable(list,temp,i);
                Future<List<DataCaseEntity>> future = executor.submit(caseCallable);
            }
            executor.shutdown();
            while(true){
                if(executor.isTerminated()){
                    break;
                }
                try {
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        }else {
            list = dataCaseMapper.totalCaseList(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);

                CaseExportCallable caseCallable = new CaseExportCallable(list,temp,i);
                Future<List<DataCaseEntity>> future = executor.submit(caseCallable);
            }
            executor.shutdown();
            while(true){
                if(executor.isTerminated()){
                    break;
                }
                try {
                    Thread.sleep(100);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


        }
        return combineData(list);
    }

    public List<DataCaseTelExport> selectCaseTelListExport(DataCaseEntity bean){
        List<DataCaseTelExport> list = new ArrayList<DataCaseTelExport>();

        list = dataCaseMapper.selectCaseTelListExport(bean);
        for (int i=0;i<list.size();i++){
            DataCaseTelExport temp = list.get(i);
            SysDictionaryEntity accountAgeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            temp.setAccountAge(accountAgeDic==null?"":accountAgeDic.getName());
            SysDictionaryEntity telTypeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getTelType(),SysDictionaryEntity.class);
            temp.setTelType(telTypeDic==null?"":telTypeDic.getName());
            list.set(i,temp);
        }

        return list;
    }

    public List<DataCaseEntity> selectCaseListExport(DataCaseEntity bean){

        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();

        list = dataCaseMapper.selectCaseList(bean);
        for(int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            if (temp.getCollectStatus()==0){
                temp.setCollectStatusMsg("");
            }else{
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            SysDictionaryEntity clientDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
            temp.setClient(clientDic==null?"":clientDic.getName());
            SysDictionaryEntity summaryDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getSummary(),SysDictionaryEntity.class);
            temp.setSummary(summaryDic==null?"":summaryDic.getName());
            SysDictionaryEntity collectionTypeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectionType(),SysDictionaryEntity.class);
            temp.setCollectionType(collectionTypeDic==null?"":collectionTypeDic.getName());
            SysDictionaryEntity accountAgeDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            temp.setAccountAge(accountAgeDic==null?"":accountAgeDic.getName());
            if (StringUtils.notEmpty(temp.getDistributeHistory())){
                temp.setDistributeHistory(temp.getDistributeHistory().substring(1));
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
                temp.setOdv("");
            }else {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                temp.setOdv(user == null ? "" : user.getUserName());
            }
            list.set(i,temp);
        }

        return combineData(list);
    }

    public List<DataCaseEntity> selectDataCaseExportByBatch(DataCaseEntity dataCaseEntity){
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if(org.apache.commons.lang3.StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setSort("desc");
            dataCaseEntity.setOrderBy("id");
        }else {
            dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        }
        list = dataCaseMapper.selectDataCaseExportByBatch(dataCaseEntity);
        for(int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            if (temp.getCollectStatus()==0){
                temp.setCollectStatusMsg("");
            }else{
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
                temp.setCollectStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getCollectArea())){
                temp.setCollectArea("");
            }else {
                SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectArea(),SysDictionaryEntity.class);
                temp.setCollectArea(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());
            }
            if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getOdv())){
                temp.setOdv("");
            }else {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                temp.setOdv(user == null ? "" : user.getUserName());
            }
            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getClient(),SysDictionaryEntity.class);
            temp.setClient(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());
            SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            temp.setAccountAge(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());
            SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectionType(),SysDictionaryEntity.class);
            temp.setCollectionType(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());
            if (StringUtils.notEmpty(temp.getDistributeHistory())){
                temp.setDistributeHistory(temp.getDistributeHistory().substring(1));
            }
            list.set(i,temp);
        }

        return combineData(list);
    }

    public void foreachData(DataCaseDetail dataCaseDetail,SysNewUserEntity sysNewUserEntity,Integer odv){
        List<SysNewUserEntity> list = sysUserMapper.listParent(sysNewUserEntity);
        if(list.size()>0){
            for (int m=0;m<list.size();m++){
                SysNewUserEntity sysNewUserEntityTemp = list.get(m);
                if (sysNewUserEntityTemp.getId()==odv) {
                    dataCaseDetail.setCurrentuser(true);
                }else{
                    foreachData(dataCaseDetail,sysNewUserEntityTemp,odv);
                }

            }

        }
    }

    public DataCaseDetail detail(DataCaseEntity bean){
        logger.info("查询详情开始");
        dataCaseMapper.watchDetail(bean);
        DataCaseDetail dataCaseDetail = dataCaseMapper.detail(bean);
        TelIpManage telIpManage = telIpManageMapper.findOne();
        dataCaseDetail.setTelIpManage(telIpManage);
        logger.info("查询详情结束");
        dataCaseDetail.setCurrentuser(false);
        SysUserEntity curentuser = getUserInfo();
        if (curentuser!=null){
            SysNewUserEntity temp = sysUserMapper.getDataById(curentuser.getId());
            dataCaseDetail.setOfficePhone(temp.getOfficePhone());
        }
        int role = 0;
        logger.info("查询角色开始");
        List<SysRoleEntity> roleList = sysRoleMapper.listRoleByUserId(curentuser);
        for (int i=0;i<roleList.size();i++){
            SysRoleEntity sysRoleEntity = roleList.get(i);
            if (sysRoleEntity.getId()==4){
                role = 4;
                break;
            }
            if (sysRoleEntity.getId()==9){
                role = 9;
                break;
            }
        }
        logger.info("查询角色结束");

       // SysPercent sysPercent = new SysPercent();
        /*sysPercent.setClient(dataCaseDetail.getClient()==null?0:Integer.parseInt(dataCaseDetail.getClient()));*/
        //sysPercent = sysPercentMapper.findByClient(sysPercent);

        //BigDecimal cuMoney = dataCaseDetail.getMoney()==null?new BigDecimal(0):dataCaseDetail.getMoney();
      /*  if (role==4){//当前用户是催收员
            if (sysPercent.getEnable()!=null && sysPercent.getEnable()==1) {
                dataCaseDetail.setPrePercent(cuMoney.divide(new BigDecimal(100)).multiply(sysPercent.getOdvBasic()==null?new BigDecimal(0):sysPercent.getOdvBasic()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() + "（基础提成）/" + cuMoney.divide(new BigDecimal(100)).multiply(sysPercent.getOdvReward()==null?new BigDecimal(0):sysPercent.getOdvReward()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString()  + "（奖励提成）");
                if (sysPercent.getOdvLow()==null || sysPercent.getOdvLow().compareTo(new BigDecimal(0))==0){
                    dataCaseDetail.setPrePercentTips("本月累计还款金额低于1万时，无提成；不低于1万时，按照奖励提成"+(sysPercent.getOdvReward()==null?new BigDecimal(0):sysPercent.getOdvReward().stripTrailingZeros().toPlainString())+"%计算");
                }else{
                    dataCaseDetail.setPrePercentTips("本月累计还款金额低于1万时，无提成；不低于1万且低于"+(sysPercent.getOdvLow()==null?"0":sysPercent.getOdvLow().stripTrailingZeros().toPlainString())+"万时，按照基础提成"+(sysPercent.getOdvBasic()==null?new BigDecimal(0):sysPercent.getOdvBasic().stripTrailingZeros().toPlainString())+"%计算；不低于"+(sysPercent.getOdvLow()==null?"0":sysPercent.getOdvLow().stripTrailingZeros().toPlainString())+"万时，按照奖励提成"+(sysPercent.getOdvReward()==null?"0":sysPercent.getOdvReward().stripTrailingZeros().toPlainString())+"%计算");
                }
            }else{
                dataCaseDetail.setPrePercent("-");
                dataCaseDetail.setPrePercentTips(sysPercent.getOdvRemark());
            }

        }else if(role == 9){//当前用户是经理
            if (sysPercent.getEnable()!=null && sysPercent.getEnable()==1) {
                dataCaseDetail.setPrePercent(cuMoney.multiply(sysPercent.getOdvBasic()).setScale(2,BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString() +"");
                dataCaseDetail.setPrePercentTips("按照奖励提成"+(sysPercent.getManageReward()==null?new BigDecimal(0):sysPercent.getManageReward().stripTrailingZeros().toPlainString())+"%计算");
            }else{
                dataCaseDetail.setPrePercent("-");
                dataCaseDetail.setPrePercentTips(sysPercent.getManageRemark());
            }
        }else{//既不是催收员也不是经理
            dataCaseDetail.setPrePercent("");
            dataCaseDetail.setPrePercentTips("");
        }*/


        logger.info("设置数据字典开始");
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(dataCaseDetail.getOdv()) && (!(curentuser.getId()+"").equals(dataCaseDetail.getOdv()))){
            SysNewUserEntity sysNewUserEntity = new SysNewUserEntity();
            sysNewUserEntity.setId(curentuser.getId());
            this.foreachData(dataCaseDetail,sysNewUserEntity,Integer.parseInt(dataCaseDetail.getOdv()));
        }else if (org.apache.commons.lang3.StringUtils.isNotEmpty(dataCaseDetail.getOdv()) && (curentuser.getId()+"").equals(dataCaseDetail.getOdv())){
            dataCaseDetail.setCurrentuser(true);
        }
        dataCaseDetail.setOdvId(dataCaseDetail.getOdv());
        SysUserEntity odvuser = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataCaseDetail.getOdv(), SysUserEntity.class);
        dataCaseDetail.setOdv(odvuser==null?"":odvuser.getUserName());


        SysDictionaryEntity clientDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ dataCaseDetail.getClient(), SysDictionaryEntity.class);
        dataCaseDetail.setClient(clientDic==null?"":clientDic.getName());

        SysDictionaryEntity collectDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ dataCaseDetail.getCollectStatus(), SysDictionaryEntity.class);
        dataCaseDetail.setCollectStatusMsg(collectDic==null?"":collectDic.getName());

        SysDictionaryEntity accountAgeDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ dataCaseDetail.getAccountAge(), SysDictionaryEntity.class);
        dataCaseDetail.setAccountAge(accountAgeDic==null?"":accountAgeDic.getName());

        SysDictionaryEntity collectAreaDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ dataCaseDetail.getCollectArea(), SysDictionaryEntity.class);
        dataCaseDetail.setCollectArea(collectAreaDic==null?"":collectAreaDic.getName());

        if (dataCaseDetail.getEnRepayAmt()==null || dataCaseDetail.getEnRepayAmt().compareTo(new BigDecimal(0))==0){
            dataCaseDetail.setEnRepayAmt(new BigDecimal(0.00));
        }else{
            dataCaseDetail.setEnRepayAmt(dataCaseDetail.getEnRepayAmt()==null?new BigDecimal(0.00): dataCaseDetail.getEnRepayAmt());
        }
        if (dataCaseDetail.getBalance()==null || dataCaseDetail.getBalance().compareTo(new BigDecimal(0))==0){
            dataCaseDetail.setBalanceMsg("￥0.00");
        }else{
            dataCaseDetail.setBalanceMsg("￥"+FmtMicrometer.fmtMicrometer(dataCaseDetail.getBalance()+""));
        }
        if(StringUtils.notEmpty(dataCaseDetail.getLatestOverdueMoney())){
            dataCaseDetail.setInterestDate(dataCaseDetail.getLatestOverdueMoney()+"(于"+dataCaseDetail.getInterestDate()+"导入)");
        }else{
            dataCaseDetail.setInterestDate("");
        }

        SysDictionaryEntity province = dataCaseDetail.getProvince();
        if (province==null){
            province = new SysDictionaryEntity();
            province.setName("");
            dataCaseDetail.setProvince(province);
        }else{
            province = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ province.getId(), SysDictionaryEntity.class);
            dataCaseDetail.setProvince(province);
        }

        SysDictionaryEntity city = dataCaseDetail.getCity();
        if (city==null){
            city = new SysDictionaryEntity();
            city.setName("");
            dataCaseDetail.setCity(city);
        }else{
            city = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ city.getId(), SysDictionaryEntity.class);
            dataCaseDetail.setCity(city);
        }

        SysDictionaryEntity county = dataCaseDetail.getCounty();
        if (county==null){
            county = new SysDictionaryEntity();
            county.setName("");
            dataCaseDetail.setCounty(county);
        }else{
            county = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ county.getId(), SysDictionaryEntity.class);
            dataCaseDetail.setCounty(county);
        }

        //电话
        DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
        dataCaseTelEntity.setCaseId(bean.getId());
        List<DataCaseTelEntity> dataCaseTelEntityList = dataCaseTelMapper.findAll(dataCaseTelEntity);

        SysDictionaryEntity dictionary = new SysDictionaryEntity();
        dictionary.setName("电话类型");
        List<SysDictionaryEntity> dictList = sysDictionaryService.listDataByName(dictionary);
        Map map = new HashMap();
        for (int i=0;i<dictList.size();i++){
            SysDictionaryEntity temp = dictList.get(i);
            map.put(temp.getId(),temp.getName());
        }
        for (int i=0;i<dataCaseTelEntityList.size();i++){
            DataCaseTelEntity temp = dataCaseTelEntityList.get(i);
            temp.setTypeMsg(temp.getType()==null?"":(map.get(Integer.parseInt(temp.getType()))==null?"":map.get(Integer.parseInt(temp.getType())).toString()));
            dataCaseTelEntityList.set(i,temp);
        }
        logger.info("设置数据字典结束");
        DataCaseCommentEntity dataCaseCommentEntity = new DataCaseCommentEntity();
        dataCaseCommentEntity.setCaseId(bean.getId());
        List<DataCaseCommentEntity> commentList = dataCaseCommentMapper.findAll(dataCaseCommentEntity);
        for (int i=0;i<commentList.size();i++){
            DataCaseCommentEntity temp = commentList.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getCreatUser(), SysUserEntity.class);
            temp.setCreatUserName(user == null ? "" : user.getUserName());
            commentList.set(i,temp);
        }
        DataCaseEntity caseTemp = dataCaseMapper.findById(bean);
        List<DataCaseEntity> sameBatchCaseList = dataCaseMapper.findSameBatchCase(caseTemp);
        dataCaseDetail.setSameBatchCaseList(sameBatchCaseList);
        dataCaseDetail.setDataCaseCommentEntityList(commentList);
        dataCaseDetail.setDataCaseTelEntityList(dataCaseTelEntityList);
        DataCaseEntity dataCaseEntity = new DataCaseEntity();
        dataCaseEntity.setId(bean.getId());
        List<DataCaseEntity> caseList = new ArrayList<DataCaseEntity>();
        caseList.add(dataCaseEntity);
        List<DataCaseEntity> resultCaseList = combineData(caseList);
        List<DataCaseRemarkEntity> caseRemarks = resultCaseList.get(0).getCaseRemarks();
        List<DataCaseContactsEntity> contacts = resultCaseList.get(0).getContacts();
        for (int i=0;i<6;i++){
            if (i==0) {
                if (caseRemarks==null || i >= caseRemarks.size()) {
                    dataCaseDetail.setRemark1("");
                }else{
                    dataCaseDetail.setRemark1(caseRemarks.get(i).getRemark());
                }
                if (contacts==null || i>=contacts.size()){
                    dataCaseDetail.setContactName1("");
                    dataCaseDetail.setContactAddress1("");
                    dataCaseDetail.setContactHomeTel1("");
                    dataCaseDetail.setContactIdentNo1("");
                    dataCaseDetail.setContactUnit1("");
                    dataCaseDetail.setContactUnitTel1("");
                    dataCaseDetail.setContactMobile1("");
                }else{
                    dataCaseDetail.setContactName1(contacts.get(i).getName());
                    dataCaseDetail.setContactAddress1(contacts.get(i).getAddress());
                    dataCaseDetail.setContactHomeTel1(contacts.get(i).getHomeTel());
                    dataCaseDetail.setContactIdentNo1(contacts.get(i).getIdentNo());
                    dataCaseDetail.setContactUnit1(contacts.get(i).getUnit());
                    dataCaseDetail.setContactUnitTel1(contacts.get(i).getUnitTel());
                    dataCaseDetail.setContactMobile1(contacts.get(i).getMobile());
                }
            }else if (i==1){
                if (caseRemarks==null || i >= caseRemarks.size()) {
                    dataCaseDetail.setRemark2("");
                }else{
                    dataCaseDetail.setRemark2(caseRemarks.get(i).getRemark());
                }
                if (contacts==null || i>=contacts.size()){
                    dataCaseDetail.setContactName2("");
                    dataCaseDetail.setContactAddress2("");
                    dataCaseDetail.setContactHomeTel2("");
                    dataCaseDetail.setContactIdentNo2("");
                    dataCaseDetail.setContactUnit2("");
                    dataCaseDetail.setContactUnitTel2("");
                    dataCaseDetail.setContactMobile2("");
                }else{
                    dataCaseDetail.setContactName2(contacts.get(i).getName());
                    dataCaseDetail.setContactAddress2(contacts.get(i).getAddress());
                    dataCaseDetail.setContactHomeTel2(contacts.get(i).getHomeTel());
                    dataCaseDetail.setContactIdentNo2(contacts.get(i).getIdentNo());
                    dataCaseDetail.setContactUnit2(contacts.get(i).getUnit());
                    dataCaseDetail.setContactUnitTel2(contacts.get(i).getUnitTel());
                    dataCaseDetail.setContactMobile2(contacts.get(i).getMobile());
                }
            }else if (i==2){
                if (caseRemarks==null ||  i >= caseRemarks.size()) {
                    dataCaseDetail.setRemark3("");
                }else{
                    dataCaseDetail.setRemark3(caseRemarks.get(i).getRemark());
                }
                if (contacts==null || i>=contacts.size()){
                    dataCaseDetail.setContactName3("");
                    dataCaseDetail.setContactAddress3("");
                    dataCaseDetail.setContactHomeTel3("");
                    dataCaseDetail.setContactIdentNo3("");
                    dataCaseDetail.setContactUnit3("");
                    dataCaseDetail.setContactUnitTel3("");
                    dataCaseDetail.setContactMobile3("");
                }else{
                    dataCaseDetail.setContactName3(contacts.get(i).getName());
                    dataCaseDetail.setContactAddress3(contacts.get(i).getAddress());
                    dataCaseDetail.setContactHomeTel3(contacts.get(i).getHomeTel());
                    dataCaseDetail.setContactIdentNo3(contacts.get(i).getIdentNo());
                    dataCaseDetail.setContactUnit3(contacts.get(i).getUnit());
                    dataCaseDetail.setContactUnitTel3(contacts.get(i).getUnitTel());
                    dataCaseDetail.setContactMobile3(contacts.get(i).getMobile());
                }
            }else if(i==3){
                if (caseRemarks==null ||  i >= caseRemarks.size()) {
                    dataCaseDetail.setRemark4("");
                }else{
                    dataCaseDetail.setRemark4(caseRemarks.get(i).getRemark());
                }
                if (contacts==null || i>=contacts.size()){
                    dataCaseDetail.setContactName4("");
                    dataCaseDetail.setContactAddress4("");
                    dataCaseDetail.setContactHomeTel4("");
                    dataCaseDetail.setContactIdentNo4("");
                    dataCaseDetail.setContactUnit4("");
                    dataCaseDetail.setContactUnitTel4("");
                    dataCaseDetail.setContactMobile4("");
                }else{
                    dataCaseDetail.setContactName4(contacts.get(i).getName());
                    dataCaseDetail.setContactAddress4(contacts.get(i).getAddress());
                    dataCaseDetail.setContactHomeTel4(contacts.get(i).getHomeTel());
                    dataCaseDetail.setContactIdentNo4(contacts.get(i).getIdentNo());
                    dataCaseDetail.setContactUnit4(contacts.get(i).getUnit());
                    dataCaseDetail.setContactUnitTel4(contacts.get(i).getUnitTel());
                    dataCaseDetail.setContactMobile4(contacts.get(i).getMobile());
                }
            }else if(i==4){
                if (caseRemarks==null ||  i >= caseRemarks.size()) {
                    dataCaseDetail.setRemark5("");
                }else{
                    dataCaseDetail.setRemark5(caseRemarks.get(i).getRemark());
                }
                if (contacts==null || i>=contacts.size()){
                    dataCaseDetail.setContactName5("");
                    dataCaseDetail.setContactAddress5("");
                    dataCaseDetail.setContactHomeTel5("");
                    dataCaseDetail.setContactIdentNo5("");
                    dataCaseDetail.setContactUnit5("");
                    dataCaseDetail.setContactUnitTel5("");
                    dataCaseDetail.setContactMobile5("");
                }else{
                    dataCaseDetail.setContactName5(contacts.get(i).getName());
                    dataCaseDetail.setContactAddress5(contacts.get(i).getAddress());
                    dataCaseDetail.setContactHomeTel5(contacts.get(i).getHomeTel());
                    dataCaseDetail.setContactIdentNo5(contacts.get(i).getIdentNo());
                    dataCaseDetail.setContactUnit5(contacts.get(i).getUnit());
                    dataCaseDetail.setContactUnitTel5(contacts.get(i).getUnitTel());
                    dataCaseDetail.setContactMobile5(contacts.get(i).getMobile());
                }
            }else if(i==5){
                if (caseRemarks==null || i >= caseRemarks.size()) {
                    dataCaseDetail.setRemark6("");
                }else{
                    dataCaseDetail.setRemark6(caseRemarks.get(i).getRemark());
                }
                if (contacts==null || i>=contacts.size()){
                    dataCaseDetail.setContactName6("");
                    dataCaseDetail.setContactAddress6("");
                    dataCaseDetail.setContactHomeTel6("");
                    dataCaseDetail.setContactIdentNo6("");
                    dataCaseDetail.setContactUnit6("");
                    dataCaseDetail.setContactUnitTel6("");
                    dataCaseDetail.setContactMobile6("");
                }else{
                    dataCaseDetail.setContactName6(contacts.get(i).getName());
                    dataCaseDetail.setContactAddress6(contacts.get(i).getAddress());
                    dataCaseDetail.setContactHomeTel6(contacts.get(i).getHomeTel());
                    dataCaseDetail.setContactIdentNo6(contacts.get(i).getIdentNo());
                    dataCaseDetail.setContactUnit6(contacts.get(i).getUnit());
                    dataCaseDetail.setContactUnitTel6(contacts.get(i).getUnitTel());
                    dataCaseDetail.setContactMobile6(contacts.get(i).getMobile());
                }
            }
        }



        return dataCaseDetail;
    }

    public void updateCaseTelStatus(DataCaseTelEntity bean){
        dataCaseTelMapper.updateCaseTelStatus(bean);
    }

    public void updateCaseAddressStatus(DataCaseAddressEntity bean){
        dataCaseAddressMapper.updateCaseAddressStatus(bean);
    }
    //地址
    public List<DataCaseAddressEntity> findAddressListByCaseId(DataCaseEntity bean){
        DataCaseAddressEntity addressEntity = new DataCaseAddressEntity();
        addressEntity.setCaseId(bean.getId());
        List<DataCaseAddressEntity> addressEntityList = dataCaseAddressMapper.findAll(addressEntity);
        return addressEntityList;
    }

    public List<DataCaseTelEntity> findTelListByCaseId(DataCaseEntity bean){
        DataCaseTelEntity telEntity = new DataCaseTelEntity();
        telEntity.setCaseId(bean.getId());
        List<DataCaseTelEntity> telEntityList = dataCaseTelMapper.findAll(telEntity);
        SysDictionaryEntity dictionary = new SysDictionaryEntity();
        dictionary.setName("电话类型");
        List<SysDictionaryEntity> dictList = sysDictionaryService.listDataByName(dictionary);
        Map map = new HashMap();
        for (int i=0;i<dictList.size();i++){
            SysDictionaryEntity temp = dictList.get(i);
            map.put(temp.getId(),temp.getName());
        }
        for (int i=0;i<telEntityList.size();i++){
            DataCaseTelEntity temp = telEntityList.get(i);
            temp.setTypeMsg(temp.getType()==null?"":(map.get(Integer.parseInt(temp.getType()))==null?"":map.get(Integer.parseInt(temp.getType())).toString()));
            telEntityList.set(i,temp);
        }
        return telEntityList;
    }
    //有效 无效 未知
    public void updateRemark(DataCaseEntity bean){
        dataCaseMapper.updateRemark(bean);
        DataCaseEntity temp = dataCaseMapper.findById(bean);
        DataOpLog log = new DataOpLog();
        log.setType("案件管理");
        log.setContext("将自定义信息[ "+temp.getRemark()+" ]修改为[ "+bean+" ] ");
        log.setOper(getUserInfo().getId());
        log.setOperName(getUserInfo().getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.setOpTime(sdf.format(new Date()));
        log.setCaseId(bean.getId()+"");
        dataLogService.saveDataLog(log);
    }
    //单位电话 家庭电话 电话 联系人电话 其他电话(类型)
    public DataCaseTelEntity saveCaseTel(DataCaseTelEntity bean){
        if(bean.getId()==null || bean.getId()==0) {
            dataCaseTelMapper.saveTel(bean);
        }else{
            dataCaseTelMapper.updateTel(bean);
        }
        return bean;
    }

    public void addBatchCaseTel(DataCaseTelEntity bean){
        String telMsgs = bean.getRemark();
        String[] telMsg = telMsgs.split("/");
        for (int i=0;i<telMsg.length;i++){
            String telInfos = telMsg[i];

            if (org.apache.commons.lang3.StringUtils.isNotEmpty(telInfos)){
                DataCaseTelEntity temp = new DataCaseTelEntity();
                String[] telInfo = telInfos.split("-");
                String relation = telInfo[0];
                String name = telInfo[1];
                String tel = telInfo[2];
                DataCaseEntity request = new DataCaseEntity();
                request.setId(bean.getCaseId());
                DataCaseEntity dataCaseEntity = dataCaseMapper.findById(request);
                temp.setCaseId(bean.getCaseId());
                temp.setSeqNo(dataCaseEntity.getSeqNo());
                temp.setArchiveNo(dataCaseEntity.getArchiveNo());
                temp.setCardNo(dataCaseEntity.getCardNo());
                temp.setIdentNo(dataCaseEntity.getIdentNo());
                temp.setCaseDate(dataCaseEntity.getCaseDate());
                temp.setName(name);
                temp.setRelation(relation);
                temp.setTel(tel);
                temp.setTelStatusMsg("有效");

                dataCaseTelMapper.saveTel(temp);
            }
        }

    }

    public void delCaseTel(DataCaseTelEntity bean){
        dataCaseTelMapper.deleteTel(bean);
    }
    //同步共债电话
    public WebResponse synchroSameTel(DataCaseEntity bean){
        WebResponse webResponse= WebResponse.buildResponse();
        List<DataCaseEntity> list = dataCaseMapper.findSameCase(bean);
        DataCaseTelEntity telEntity = new DataCaseTelEntity();
        telEntity.setCaseId(bean.getId());
        List<DataCaseTelEntity> mytelList = dataCaseTelMapper.findAll(telEntity);
        int[] caseIds = new int[list.size()];
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            caseIds[i] = temp.getId();
        }
        List<DataCaseTelEntity> sameOthertelList = dataCaseTelMapper.findByCaseIds(caseIds);
        for (int i=0;i<sameOthertelList.size();i++){
            DataCaseTelEntity temp1 = sameOthertelList.get(i);
            int flag = 0;
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp1.getTel())) {
                for (int j = 0; j < mytelList.size(); j++) {
                    DataCaseTelEntity temp2 = sameOthertelList.get(i);
                    if (temp1.getTel() != null && temp1.getTel().equals(temp2.getTel())) {
                        flag = 1;//本案件有共债案件的电话
                    }
                }
                if (flag == 1) {//本案件有共债案件的电话
                    DataCaseTelEntity updateTelBean = new DataCaseTelEntity();
                    updateTelBean.setCaseId(bean.getId());
                    updateTelBean.setTel(temp1.getTel());
                    updateTelBean.setTelStatusMsg(temp1.getTelStatusMsg());
                    dataCaseTelMapper.updateSameTelStatus(updateTelBean);
                } else {
                    DataCaseTelEntity  saveTelBean = new  DataCaseTelEntity();
                    BeanUtils.copyProperties(temp1,saveTelBean);
                    saveTelBean.setId(null);
                    saveTelBean.setCaseId(bean.getId());
                    dataCaseTelMapper.saveTel(saveTelBean);
                }
            }
        }

        return webResponse;
    }

    public List<DataCaseEntity> sameCaseList(DataCaseEntity bean){
        List<DataCaseEntity> list = dataCaseMapper.findSameCase(bean);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            SysDictionaryEntity collectStatusDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getCollectStatus(), SysDictionaryEntity.class);
            temp.setCollectStatusMsg(collectStatusDic==null?"":collectStatusDic.getName());
            SysDictionaryEntity clientDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getClient(), SysDictionaryEntity.class);
            temp.setClient(clientDic==null?"":clientDic.getName());
            SysUserEntity odvUser = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(odvUser == null ? "" : odvUser.getUserName());
            temp.setMoneyMsg(temp.getMoney()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getMoney()+""));
            temp.setBankAmtMsg(temp.getBankAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getBankAmt()+""));
            temp.setBalanceMsg(temp.getBalance()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getBalance()+""));
            temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getProRepayAmt()+""));
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt()+""));
            list.set(i,temp);
        }
        return list;
    }

    public List<DataCaseEntity> sameBatchCaseList(DataCaseEntity bean){
        DataCaseEntity caseTemp = dataCaseMapper.findById(bean);
        List<DataCaseEntity> list = dataCaseMapper.findSameBatchCase(caseTemp);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            SysDictionaryEntity accountAgeDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getAccountAge(), SysDictionaryEntity.class);
            temp.setAccountAge(accountAgeDic==null?"":accountAgeDic.getName());
            temp.setMoneyMsg(temp.getMoney()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getMoney()+""));
            temp.setBankAmtMsg(temp.getBankAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getBankAmt()+""));
            temp.setBalanceMsg(temp.getBalance()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getBalance()+""));
            temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getProRepayAmt()+""));
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"￥0.00": "￥"+FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt()+""));
            list.set(i,temp);
        }
        return list;
    }

    public void saveCaseAddress(DataCaseAddressEntity bean){
        DataCaseEntity request = new DataCaseEntity();
        request.setId(bean.getCaseId());
        DataCaseEntity dataCaseEntity = dataCaseMapper.findById(request);
        bean.setCaseId(bean.getCaseId());
        bean.setSeqNo(dataCaseEntity.getSeqNo());
        bean.setArchiveNo(dataCaseEntity.getArchiveNo());
        bean.setCardNo(dataCaseEntity.getCardNo());
        bean.setIdentNo(dataCaseEntity.getIdentNo());
        bean.setCaseDate(dataCaseEntity.getCaseDate());
        DataOpLog log = new DataOpLog();
        log.setType("地址管理");
        log.setOper(getUserInfo().getId());
        log.setOperName(getUserInfo().getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.setOpTime(sdf.format(new Date()));
        log.setCaseId(dataCaseEntity.getId()+"");

        if(bean.getId()==null || bean.getId()==0) {
            dataCaseAddressMapper.saveAddress(bean);
            bean.setName(dataCaseEntity.getName());
            log.setContext("新增地址: "+bean.getAddress());
            log.setCaseId(bean.getId()+"");

        }else{
            dataCaseAddressMapper.updateAddress(bean);
            log.setCaseId(bean.getId()+"");
            log.setContext("修改地址: "+bean.getAddress());
        }
        dataLogService.saveDataLog(log);
    }

    public void delCaseAddress(DataCaseAddressEntity bean){
        dataCaseAddressMapper.deleteAddress(bean);
        DataOpLog log = new DataOpLog();
        log.setType("地址管理");
        log.setOper(getUserInfo().getId());
        log.setOperName(getUserInfo().getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.setOpTime(sdf.format(new Date()));
        log.setCaseId(bean.getId()+"");
        log.setContext("删除地址: "+bean.getAddress());
        dataLogService.saveDataLog(log);
    }

    @Override
    public PageInfo<DataCaseEntity> pageSeqNos(DataCaseEntity dataCaseEntity) {
        List<DataCaseEntity> list = dataCaseMapper.pageSeqNos(dataCaseEntity);
        return PageInfo.of(list);
    }

    private List<DataCaseEntity> combineData(List<DataCaseEntity> list) {
        if(CollectionUtils.isEmpty(list)) {
            return list;
        }

        List<SysDictionaryEntity> dictionaryList = sysDictionaryMapper.getDataList(new SysDictionaryEntity());
        Map<Integer, SysDictionaryEntity> dictMap = new HashMap<>();
        for(SysDictionaryEntity entity : dictionaryList) {
            dictMap.put(entity.getId(), entity);
        }

        //组装省市县
        Set<Integer> caseIdsSet = new HashSet<>();
        Set<String> userIdsSet = new HashSet<>();
        for(int i = 0; i < list.size(); i ++) {
            DataCaseEntity entity = list.get(i);
            caseIdsSet.add(entity.getId());
            if(entity.getProvince() != null && entity.getProvince().getId() != null && dictMap.containsKey(entity.getProvince().getId())) {
                list.get(i).getProvince().setName(dictMap.get(entity.getProvince().getId()).getName());
            }
            if(entity.getCity() != null && entity.getCity().getId() != null && dictMap.containsKey(entity.getCity().getId())) {
                list.get(i).getCity().setName(dictMap.get(entity.getCity().getId()).getName());
            }
            if(entity.getCounty() != null && entity.getCounty().getId() != null && dictMap.containsKey(entity.getCounty().getId())) {
                list.get(i).getCounty().setName(dictMap.get(entity.getCounty().getId()).getName());
            }
            if(entity.getCollectionUser() != null &&entity.getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getCollectionUser().getId());
            }
        }
        List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
        Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);

        DataCaseContactsEntity queryContactsEntity = new DataCaseContactsEntity();
        queryContactsEntity.setCaseIdsSet(caseIdsSet);
        List<DataCaseContactsEntity> contacts = dataCaseContactsMapper.listByCaseIds(queryContactsEntity);
        Map<Integer, List<DataCaseContactsEntity>> contactMap = new HashMap<>();
        for(DataCaseContactsEntity entity : contacts) {
            if(!contactMap.containsKey(entity.getCaseId())) {
                contactMap.put(entity.getCaseId(), new ArrayList<>());
            }
            contactMap.get(entity.getCaseId()).add(entity);
        }

        DataCaseRemarkEntity queryRemarks = new DataCaseRemarkEntity();
        queryRemarks.setCaseIdsSet(caseIdsSet);
        List<DataCaseRemarkEntity> remarks = dataCaseRemarkMapper.listByCaseIds(queryRemarks);
        Map<Integer, List<DataCaseRemarkEntity>> remarkMap = new HashMap<>();
        for(DataCaseRemarkEntity entity : remarks) {
            if(!remarkMap.containsKey(entity.getCaseId())) {
                remarkMap.put(entity.getCaseId(), new ArrayList<>());
            }
            remarkMap.get(entity.getCaseId()).add(entity);
        }
        for(int i = 0; i < list.size(); i ++) {
            DataCaseEntity entity = list.get(i);
            if(entity.getCollectionUser() != null && entity.getCollectionUser().getId() != null && userMap.containsKey(entity.getCollectionUser().getId())) {
                list.get(i).getCollectionUser().setUserName(userMap.get(entity.getCollectionUser().getId()).getUserName());
            }
            if(contactMap.containsKey(entity.getId())) {
                list.get(i).setContacts(contactMap.get(entity.getId()));
            }
            if(remarkMap.containsKey(entity.getId())) {
                list.get(i).setCaseRemarks(remarkMap.get(entity.getId()));
            }
        }
        return list;
    }

    public List<DataCaseCommentEntity> listComment(DataCaseEntity dataCaseEntity){
        DataCaseCommentEntity dataCaseCommentEntity = new DataCaseCommentEntity();
        dataCaseCommentEntity.setCaseId(dataCaseEntity.getId());
        List<DataCaseCommentEntity> list = dataCaseCommentMapper.findAll(dataCaseCommentEntity);
        for (int i=0;i<list.size();i++){
            DataCaseCommentEntity temp = list.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getCreatUser(), SysUserEntity.class);
            temp.setCreatUserName(user == null ? "" : user.getUserName());
            list.set(i,temp);
        }
        return list;
    }

    public DataCaseCommentEntity detailComment(DataCaseCommentEntity bean){
        DataCaseCommentEntity dataCaseCommentEntity = new DataCaseCommentEntity();
        dataCaseCommentEntity.setCaseId(bean.getId());
        DataCaseCommentEntity detail = dataCaseCommentMapper.detail(dataCaseCommentEntity);
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ detail.getCreatUser(), SysUserEntity.class);
        detail.setCreatUserName(user == null ? "" : user.getUserName());
        return detail;
    }

    public List<DataCaseInterestEntity> listInterest(DataCaseEntity dataCaseEntity){
        DataCaseInterestEntity dataCaseInterestEntity = new DataCaseInterestEntity();
        dataCaseInterestEntity.setCaseId(dataCaseEntity.getId());
        List<DataCaseInterestEntity> list = dataCaseInterestMapper.findAll(dataCaseInterestEntity);

        return list;
    }

    public void updateComment(DataCaseCommentEntity bean){
        dataCaseCommentMapper.updateComment(bean);
    }

    public void delComment(DataCaseCommentEntity bean){
        dataCaseCommentMapper.delComment(bean);
    }

    public List<DataCaseEntity> listByBatchNos(String[] batchNos){
        return dataCaseMapper.listByBatchNos(batchNos);
    }

    //2 待审核  1 最终同意申请  3代办 4撤销申请
    public List<DataCaseEntity> listSynergy(DataCaseEntity dataCaseEntity){
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.getSynergy()==0){
            int[] status = new int[4];
            status[0]=1;
            status[1]=2;
            status[2]=3;
            status[3]=4;
            list = dataCaseMapper.listSynergy(status);
        }else{
            int[] status = new int[1];
            status[0]=dataCaseEntity.getSynergy();
            list = dataCaseMapper.listSynergy(status);
        }
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            if (StringUtils.isEmpty(temp.getOdv())){
                temp.setOdv("");
            }else {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
                temp.setOdv(user == null ? "" : user.getUserName());
            }
            if (temp.getSynerCkecker()==0){
                temp.setSynergyCkeckerName("");
            }else {
                SysUserEntity user2 = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getSynerCkecker(), SysUserEntity.class);
                temp.setSynergyCkeckerName(user2 == null ? "" : user2.getUserName());
            }
            list.set(i,temp);
        }
        return list;
    }


    public void updateCollectInfo(DataCaseEntity dataCaseEntity){
        dataCaseMapper.updateCollectInfo(dataCaseEntity);
    }

}
