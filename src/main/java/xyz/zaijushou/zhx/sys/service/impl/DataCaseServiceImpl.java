package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.java2d.pipe.SpanShapeRenderer;
import xyz.zaijushou.zhx.common.exception.CustomerException;
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
import java.math.BigInteger;
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
    private SysOperationLogMapper sysOperationLogMapper;
    @Resource
    private CaseTimeAreaMapper caseTimeAreaMapper;

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
    public void delete(List<DataCaseEntity> list){
        String[] ids = new String[list.size()];
        for (int i=0;i<list.size();i++) {
            DataCaseEntity dataCaseEntity = list.get(i);
            ids[i] = dataCaseEntity.getId()+"";
        }
        DataCaseEntity temp = new DataCaseEntity();
        temp.setIds(ids);
        dataCaseMapper.deleteByIds(temp);
        this.asyDel(list);
    }
    @Async
    public void asyDel(List<DataCaseEntity> list){
        for (int i=0;i<list.size();i++) {
            DataCaseEntity dataCaseEntity = list.get(i);
            DataCaseEntity updateBatchEntity = dataCaseMapper.findById(dataCaseEntity);
            //修改批次信息
            DataBatchEntity dataBatchEntity = new DataBatchEntity();
            dataBatchEntity.setBatchNo(updateBatchEntity.getBatchNo());
            dataBatchEntity.setTotalAmt(updateBatchEntity.getMoney() == null ? new BigDecimal(0) : new BigDecimal(0).subtract(updateBatchEntity.getMoney()));
            dataBatchEntity.setUserCount(-1);
            dataBatchMapper.updateByBatchNo(dataBatchEntity);

            DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
            dataCaseAddressEntity.setCaseId(dataCaseEntity.getId());
            dataCaseAddressMapper.deleteAddress(dataCaseAddressEntity);
            DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
            dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
            dataCaseTelMapper.deleteTel(dataCaseTelEntity);
            stringRedisTemplate.delete(RedisKeyPrefix.DATA_CASE + dataCaseEntity.getSeqNo());
            stringRedisTemplate.delete(RedisKeyPrefix.DATA_CASE + dataCaseEntity.getCardNo() + "@" + dataCaseEntity.getCaseDate());
        }
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
                temp.setMoneyMsg(temp.getMoney()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getMoney().stripTrailingZeros()+""));
                temp.setBankAmtMsg(temp.getBankAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getBankAmt().stripTrailingZeros()+""));
                temp.setBalanceMsg(temp.getBalance()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getBalance().stripTrailingZeros()+""));
                temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getProRepayAmt().stripTrailingZeros()+""));
                temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt().stripTrailingZeros()+""));
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
        logger.info("开始组装查询条件");
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
                String[] idsTemp = new String[ids.length];
                for (int i=0;i<ids.length;i++){
                    try{
                        idsTemp[i] = Integer.parseInt(ids[i])+"";
                        dataCaseEntity.setIdFlag("1");
                    }catch (Exception e){
                        idsTemp[i] = "0";
                    }
                }

                dataCaseEntity.setIds(idsTemp);
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

        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("0") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+3];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="1";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            newStatuss[dataCaseEntity.getStatuss().length+2]="3";
            dataCaseEntity.setStatuss(newStatuss);
        }
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("1") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+2];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="0";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            dataCaseEntity.setStatuss(newStatuss);
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        logger.info("开始查询");
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.pageBatchBoundsCaseList(dataCaseEntity);
            logger.info("结束查询");
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                totalAmt = totalAmt.add(temp.getMoney());
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
        }else {
            logger.info("开始查询");
            list = dataCaseMapper.pageCaseMangeList(dataCaseEntity);
            logger.info("结束查询");
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
        logger.info("完成属性的拼接");
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
        logger.info("开始返回");
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
        if (bean.getStatus()!=null && bean.getStatus().equals(4)){
            bean.setReturnTime(new Date());
        }
        dataCaseMapper.updateStatus(bean);
    }

    @Override
    public void sendOdv(DataCaseEntity bean){
        DataCaseEntity temp = dataCaseMapper.findById(bean);
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
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setUrl("/send");
        operationLog.setUserIp("127.0.0.1");
        operationLog.setUserId(getUserInfo().getId());

        if (StringUtils.isEmpty(temp.getOdv())){
            operationLog.setRequestBody(getUserInfo().getUserName()+"把"+temp.getSeqNo()+"案件分配给"+user.getUserName());
        }else{
            SysUserEntity tempUser = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            operationLog.setRequestBody(getUserInfo().getUserName()+"把"+temp.getSeqNo()+"案件（从"+tempUser.getUserName()+"）分配给"+user.getUserName());
        }

        sysOperationLogMapper.insertRequest(operationLog);
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
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("0") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+3];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="1";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            newStatuss[dataCaseEntity.getStatuss().length+2]="3";
            dataCaseEntity.setStatuss(newStatuss);
        }
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("1") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+2];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="0";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            dataCaseEntity.setStatuss(newStatuss);
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.totalSendBatchBoundsCaseList(dataCaseEntity);
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
            list = dataCaseMapper.totalSendCaseList(dataCaseEntity);
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
        DataCaseEntity caseTemp = new DataCaseEntity();
        BeanUtils.copyProperties(dataCaseEntity,caseTemp);
        dataCaseEntity.setDistributeHistory(",分配给"+user.getUserName());
        dataCaseEntity.setIds(ids);
        dataCaseEntity.setDept(user==null?"":user.getDepartment());
        dataCaseMapper.sendOdvByProperty(dataCaseEntity);

        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setUrl("/send");
        operationLog.setUserIp("127.0.0.1");
        operationLog.setUserId(getUserInfo().getId());
        String seqNos = "";

        List<DataCaseEntity> tempList = dataCaseMapper.listSend(caseTemp);
        for (int i=0;i<tempList.size();i++){
            seqNos = seqNos+","+tempList.get(i).getSeqNo();
        }

        operationLog.setRequestBody(getUserInfo().getUserName()+"把"+(seqNos==null?"":seqNos.substring(1))+"案件分配给"+user.getUserName());

        sysOperationLogMapper.insertRequest(operationLog);

    }

    @Override
    public WebResponse querySendByProperty(DataCaseEntity dataCaseEntity){
        Integer totalCount=0;
        Integer enCount = 0;
        Integer unCount = 0;
        BigDecimal totalAmt = new BigDecimal(0);
        BigDecimal enAmt= new BigDecimal(0);
        BigDecimal unAmt= new BigDecimal(0);
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
            list = dataCaseMapper.autoQuery1(dataCaseEntity);
            for(int i=0;i<list.size();i++) {
                DataCaseEntity temp = list.get(i);
                totalCount = totalCount+1;
                totalAmt = totalAmt.add(temp.getMoney());
                if (StringUtils.isEmpty(temp.getOdv())){
                    unCount = unCount+1;
                    unAmt = unAmt.add(temp.getMoney());
                }else{
                    enCount = enCount+1;
                    enAmt = enAmt.add(temp.getMoney());
                }
            }
        }else {
            list = dataCaseMapper.autoQuery2(dataCaseEntity);
            for(int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                totalCount = totalCount+1;
                totalAmt = totalAmt.add(temp.getMoney());
                if (StringUtils.isEmpty(temp.getOdv())){
                    unCount = unCount+1;
                    unAmt = unAmt.add(temp.getMoney());
                }else{
                    enCount = enCount+1;
                    enAmt = enAmt.add(temp.getMoney());
                }
            }
        }
        DataCaseSendQuery dataCaseSendQuery = new DataCaseSendQuery();
        dataCaseSendQuery.setTotalCount(totalCount);
        dataCaseSendQuery.setEnCount(enCount);
        dataCaseSendQuery.setUnCount(unCount);
        dataCaseSendQuery.setTotalAmt("￥"+FmtMicrometer.fmtMicrometer(totalAmt.stripTrailingZeros().toPlainString()));
        dataCaseSendQuery.setEnAmt("￥"+FmtMicrometer.fmtMicrometer(enAmt.stripTrailingZeros().toPlainString()));
        dataCaseSendQuery.setUnAmt("￥"+FmtMicrometer.fmtMicrometer(unAmt.stripTrailingZeros().toPlainString()));

        return WebResponse.success(dataCaseSendQuery);

    }

    @Override
    public WebResponse autoSendByPropertyResult(DataCaseEntity dataCaseEntity){
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
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("0") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+3];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="1";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            newStatuss[dataCaseEntity.getStatuss().length+2]="3";
            dataCaseEntity.setStatuss(newStatuss);
        }
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("1") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+2];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="0";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            dataCaseEntity.setStatuss(newStatuss);
        }
        Integer[] sendTypes = dataCaseEntity.getSendType();
        String sendModule = null;
        for (int i=0;i<sendTypes.length;i++){
            if (sendTypes[i]!=1){
                sendModule = "notodv";
            }else{
                sendModule = "";
                break;
            }
        }
        dataCaseEntity.setSendModule(sendModule);
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.autoQuery1(dataCaseEntity);

        }else {
            list = dataCaseMapper.autoQuery2(dataCaseEntity);

        }
        for (int i=0;i<list.size();i++){
            DataCaseEntity tempCase = list.get(i);
            tempCase.setPercent("1");
            list.set(i,tempCase);
        }
        Map percentMap = new HashMap();
        String[] sendOdvs = null;
        List<String> strList = Arrays.asList(dataCaseEntity.getSendOdvs());
        List<String> odvList = new ArrayList<String>(strList);//转换为ArrayLsit
        String[] odvPercents = dataCaseEntity.getOdvPercent();
        Boolean percentFlag = false;
        for (int i=0;i<odvPercents.length;i++){
            if (odvPercents[i]!=null && !odvPercents[i].trim().equals("")  && !odvPercents[i].trim().equals("0")){
                percentFlag = true;
                break;
            }
        }
        double totalPercent = 0;
        if (percentFlag) {
            for (int i = 0; i < odvList.size(); i++) {
                String odvPercent = odvPercents[i];
                if (odvPercent == null || odvPercent.equals("") || odvPercent.equals("0")) {
                    odvList.remove(i);
                } else {
                    percentMap.put(odvList.get(i),odvPercents[i]);
                    totalPercent = totalPercent + Double.parseDouble(odvPercents[i]);
                }
            }
            sendOdvs = new String[odvList.size()];
            sendOdvs = odvList.toArray(sendOdvs);
        }else{
            sendOdvs = dataCaseEntity.getSendOdvs();
            for (int i=0;i<sendOdvs.length;i++){
                percentMap.put(sendOdvs[i],1);
                totalPercent = totalPercent+1;
            }
        }

        int sendType=0;
        for (int i=0;i<sendTypes.length;i++){
            if (sendTypes[i]==3){
                sendType = 3;
            }
        }

        List<ShowSendCase> odvShowList = new ArrayList<ShowSendCase>();
        Map<String, List<DataCaseEntity>> map = new HashMap<>();
        if (dataCaseEntity.getMathType()==1 ||dataCaseEntity.getMathType()==3){
            SendCaseModule sendCaseModule= new SendCaseModule();
            map = sendCaseModule.authSend(list,sendOdvs,percentMap,sendType,dataCaseEntity.getMathType(),totalPercent);
        }else{
            map = this.authSend(list,sendOdvs,percentMap,sendType,dataCaseEntity.getMathType());
        }

        for (int i=0;i<dataCaseEntity.getSendOdvs().length;i++){
            String thisOdv = dataCaseEntity.getSendOdvs()[i];
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ thisOdv, SysUserEntity.class);
            ShowSendCase showCase = new ShowSendCase();
            showCase.setOdv(user==null?"":user.getUserName());
            showCase.setPercent(StringUtils.isEmpty(dataCaseEntity.getOdvPercent()[i])?"":dataCaseEntity.getOdvPercent()[i]+"%");
            List<DataCaseEntity> odvCaseList = map.get(thisOdv);
            showCase.setCaseNum(odvCaseList==null?"0":odvCaseList.size()+"");
            BigDecimal caseAmt = new BigDecimal(0);
            if (odvCaseList==null){
                caseAmt = caseAmt.add(new BigDecimal(0));
            }else {
                for (int m = 0; m < odvCaseList.size(); m++) {
                    caseAmt = caseAmt.add(odvCaseList.get(m).getMoney());
                }
            }
            showCase.setCaseAmt("￥"+FmtMicrometer.fmtMicrometer(caseAmt.stripTrailingZeros().toPlainString()));
            odvShowList.add(showCase);
        }

        return WebResponse.success(odvShowList);
    }

    @Override
    public void autoSendByProperty(DataCaseEntity dataCaseEntity){
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
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("0") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+3];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="1";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            newStatuss[dataCaseEntity.getStatuss().length+2]="3";
            dataCaseEntity.setStatuss(newStatuss);
        }
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("1") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+2];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="0";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            dataCaseEntity.setStatuss(newStatuss);
        }
        Integer[] sendTypes = dataCaseEntity.getSendType();
        double totalPercent = 0;
        String sendModule = null;
        for (int i=0;i<sendTypes.length;i++){
            if (sendTypes[i]!=1){
                sendModule = "notodv";
            }else{
                sendModule = "";
                break;
            }
        }
        dataCaseEntity.setSendModule(sendModule);
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.autoQuery1(dataCaseEntity);

        }else {
            list = dataCaseMapper.autoQuery2(dataCaseEntity);

        }
        for (int i=0;i<list.size();i++){
            DataCaseEntity tempCase = list.get(i);
            tempCase.setPercent("1");
            list.set(i,tempCase);
        }
        Map percentMap = new HashMap();
        String[] sendOdvs = null;
        List<String> strList = Arrays.asList(dataCaseEntity.getSendOdvs());
        List<String> odvList = new ArrayList<String>(strList);//转换为ArrayLsit
        String[] odvPercents = dataCaseEntity.getOdvPercent();
        Boolean percentFlag = false;
        for (int i=0;i<dataCaseEntity.getSendOdvs().length;i++){
            if (dataCaseEntity.getSendOdvs()[i]!=null && !dataCaseEntity.getSendOdvs()[i].trim().equals("")  && !dataCaseEntity.getSendOdvs()[i].trim().equals("0")){
                percentFlag = true;
                break;
            }
        }
        if (percentFlag) {
            for (int i = 0; i < odvList.size(); i++) {
                String odvPercent = odvPercents[i];
                if (odvPercent == null || odvPercents.equals("") || odvPercents.equals("0")) {
                    odvList.remove(i);
                } else {
                    percentMap.put(odvList.get(i),odvPercents[i]);
                    totalPercent = totalPercent + Double.parseDouble(odvPercents[i]);
                }
            }
            sendOdvs = new String[odvList.size()];
            sendOdvs = odvList.toArray(sendOdvs);
        }else{
            sendOdvs = dataCaseEntity.getSendOdvs();
            for (int i=0;i<sendOdvs.length;i++){
                percentMap.put(sendOdvs[i],1);
                totalPercent = totalPercent+1;
            }
        }

        int sendType=0;
        for (int i=0;i<sendTypes.length;i++){
            if (sendTypes[i]==3){
                sendType = 3;
            }
        }

        Map<String, List<DataCaseEntity>> map = new HashMap<>();
        if (dataCaseEntity.getMathType()==1 ||dataCaseEntity.getMathType()==3){
            SendCaseModule sendCaseModule= new SendCaseModule();
            map = sendCaseModule.authSend(list,sendOdvs,percentMap,sendType,dataCaseEntity.getMathType(),totalPercent);
        }else{
            map = this.authSend(list,sendOdvs,percentMap,sendType,dataCaseEntity.getMathType());
        }
        for(Map.Entry<String, List<DataCaseEntity>> entry : map.entrySet()){
            List<DataCaseEntity> odvCaseList = entry.getValue();
            for(int i=0;i<odvCaseList.size();i++){
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odvCaseList.get(i).getOdv(), SysUserEntity.class);
                odvCaseList.get(i).setDept(user.getDepartment());
                odvCaseList.get(i).setDistributeHistory(list.get(i).getSeqNo()+"案件分配给"+user.getUserName());
                dataCaseMapper.sendOdv(odvCaseList.get(i));
                SysOperationLogEntity operationLog = new SysOperationLogEntity();
                operationLog.setUrl("/send");
                operationLog.setUserIp("127.0.0.1");
                operationLog.setUserId(getUserInfo().getId());


                if (StringUtils.isEmpty(list.get(i).getAccount())){//此时的account肩负之前的odv责任
                    operationLog.setRequestBody(getUserInfo().getUserName()+"把"+list.get(i).getSeqNo()+"案件分配给"+user.getUserName());
                }else{
                    SysUserEntity tempUser = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ list.get(i).getAccount(), SysUserEntity.class);
                    operationLog.setRequestBody(getUserInfo().getUserName()+"把"+list.get(i).getSeqNo()+"案件（从"+tempUser.getUserName()+"）分配给"+user.getUserName());
                }

                sysOperationLogMapper.insertRequest(operationLog);
            }

        }
        String[] ids = new String[list.size()];
        for (int i=0;i<list.size();i++){
            ids[i] = list.get(i).getId()+"";
        }
        dataCaseEntity.setIds(ids);
        if (dataCaseEntity.getCleanTimes()!=null && dataCaseEntity.getCleanTimes()==1){
            dataCaseMapper.cleanTimes(dataCaseEntity);
        }
        if (dataCaseEntity.getCleanCollect()!=null && dataCaseEntity.getCleanCollect()==1){
            dataCaseMapper.cleanCollect(dataCaseEntity);
        }

    }

    public Map<String, List<DataCaseEntity>> authSend(List<DataCaseEntity> list,String[] odvs,Map percentMap,Integer sendType,Integer authType){
        Map map = new HashMap();
        List<DataCaseEntity> firstList = new ArrayList<DataCaseEntity>();
        for (int i=0;i<odvs.length;i++){
            DataCaseEntity firstCase = new DataCaseEntity();
            firstCase.setOdv(odvs[i]);
            firstCase.setMoney(new BigDecimal(0));
            firstCase.setPercent(percentMap.get(odvs[i])==null?"1":percentMap.get(odvs[i]).toString());
            firstList.add(firstCase);
        }

        Collections.sort(firstList);
        for (int j = 0; j < firstList.size(); j++) {
            odvs[j] = firstList.get(j).getOdv();
        }

        //先按照分配比例把催收员按照比例从大到小配列一次
        if (authType==1){
            for (int i=0;i<list.size();i++){
                DataCaseEntity temp = list.get(i);
                int num = i%odvs.length;
                List<DataCaseEntity> tempList = null;

                if (sendType==3 && odvs[num]!=null && odvs[num].equals(temp.getOdv())){
                    if (num==odvs.length-1){
                        String tempOdv = odvs[num];
                        odvs[num] = odvs[0];
                        odvs[0] = tempOdv;
                    }else{
                        String tempOdv = odvs[num];
                        odvs[num] = odvs[num+1];
                        odvs[num+1] = tempOdv;
                    }
                    if(map.get(odvs[num])==null){
                        tempList = new ArrayList<DataCaseEntity>();
                    }else {
                        tempList = (List<DataCaseEntity>)map.get(odvs[num]);
                    }
                    tempList.add(temp);
                    map.put(odvs[num], tempList);
                }else {
                    if(map.get(odvs[num])==null){
                        tempList = new ArrayList<DataCaseEntity>();
                    }else {
                        tempList = (List<DataCaseEntity>)map.get(odvs[num]);
                    }
                    tempList.add(temp);
                    map.put(odvs[num], tempList);
                }
                temp.setAccount(temp.getOdv());
                temp.setOdv(odvs[num]);
            }
        }else{
            Collections.sort(list);
            List<DataCaseEntity> orderList = new ArrayList<DataCaseEntity>();
            for (int i=0;i<list.size();i++) {
                DataCaseEntity temp = list.get(i);
                orderList.clear();
                //int num = i%odvs.length;
                int num =1;
                List<DataCaseEntity> tempList = null;

                if (sendType==3 && odvs[num]!=null && odvs[num].equals(temp.getOdv())){
                    if (num==odvs.length-1){
                        String tempOdv = odvs[num];
                        odvs[num] = odvs[0];
                        odvs[0] = tempOdv;
                    }else{
                        String tempOdv = odvs[num];
                        odvs[num] = odvs[num+1];
                        odvs[num+1] = tempOdv;
                    }
                    temp.setAccount(temp.getOdv());
                    temp.setOdv(odvs[num]);

                    if(map.get(odvs[num])==null){
                        tempList = new ArrayList<DataCaseEntity>();
                    }else {
                        tempList = (List<DataCaseEntity>)map.get(odvs[num]);
                    }
                    tempList.add(temp);
                    map.put(odvs[num], tempList);
                    BigDecimal totalTemp = new BigDecimal(0);
                    for (int m=0;m<tempList.size();m++){
                        totalTemp = totalTemp.add(tempList.get(m).getMoney());
                    }
                    DataCaseEntity orderCase = new DataCaseEntity();
                    orderCase.setOdv(odvs[num]);
                    orderCase.setMoney(totalTemp);
                    orderCase.setPercent(percentMap.get(odvs[num])==null?"1":percentMap.get(odvs[num]).toString());
                    orderList.add(orderCase);
                    for (int m=0;m<odvs.length;m++){
                        boolean juage = true;
                        for (int j = 0; j < orderList.size(); j++) {
                           if (odvs[m].equals(orderList.get(j).getOdv())){
                               juage =  false;
                               break;
                           }
                        }
                        if (juage){
                            DataCaseEntity orderCase2 = new DataCaseEntity();
                            orderCase2.setOdv(odvs[m]);
                            List<DataCaseEntity> compareList  = null;
                            if(map.get(odvs[m])==null){
                                compareList = new ArrayList<DataCaseEntity>();
                            }else {
                                compareList = (List<DataCaseEntity>)map.get(odvs[m]);
                            }
                            BigDecimal compareAmt = new BigDecimal(0);
                            for (int n=0;n<compareList.size();n++){
                                compareAmt = compareAmt.add(compareList.get(n).getMoney());
                            }
                            orderCase2.setMoney(compareAmt);
                            orderCase2.setPercent(percentMap.get(odvs[m])==null?"1":percentMap.get(odvs[m]).toString());
                            orderList.add(orderCase2);
                        }
                    }
                    //if (num == odvs.length-1) {
                        Collections.sort(orderList);
                        for (int j = 0; j < orderList.size(); j++) {
                            odvs[j] = orderList.get(j).getOdv();
                        }
                    //}
                }else {
                    temp.setAccount(temp.getOdv());
                    temp.setOdv(odvs[num]);
                    if(map.get(odvs[num])==null){
                        tempList = new ArrayList<DataCaseEntity>();
                    }else {
                        tempList = (List<DataCaseEntity>)map.get(odvs[num]);
                    }
                    tempList.add(temp);
                    map.put(odvs[num], tempList);
                    BigDecimal totalTemp = new BigDecimal(0);
                    for (int m=0;m<tempList.size();m++){
                        totalTemp = totalTemp.add(tempList.get(m).getMoney());
                    }
                    DataCaseEntity orderCase = new DataCaseEntity();
                    orderCase.setOdv(odvs[num]);
                    orderCase.setMoney(totalTemp);
                    orderCase.setPercent(percentMap.get(odvs[num])==null?"1":percentMap.get(odvs[num]).toString());
                    orderList.add(orderCase);
                    for (int m=0;m<odvs.length;m++){
                        boolean juage = true;
                        for (int j = 0; j < orderList.size(); j++) {
                            if (odvs[m].equals(orderList.get(j).getOdv())){
                                juage =  false;
                                break;
                            }
                        }
                        if (juage){
                            DataCaseEntity orderCase2 = new DataCaseEntity();
                            orderCase2.setOdv(odvs[m]);
                            List<DataCaseEntity> compareList  = null;
                            if(map.get(odvs[m])==null){
                                compareList = new ArrayList<DataCaseEntity>();
                            }else {
                                compareList = (List<DataCaseEntity>)map.get(odvs[m]);
                            }
                            BigDecimal compareAmt = new BigDecimal(0);
                            for (int n=0;n<compareList.size();n++){
                                compareAmt = compareAmt.add(compareList.get(n).getMoney());
                            }
                            orderCase2.setMoney(compareAmt);
                            orderCase2.setPercent(percentMap.get(odvs[m])==null?"1":percentMap.get(odvs[m]).toString());
                            orderList.add(orderCase2);
                        }
                    }
                    //if (num == odvs.length-1) {
                        Collections.sort(orderList);
                        for (int j=0;j<orderList.size();j++){
                            odvs[j] = orderList.get(j).getOdv();
                        }
                    //}
                }


            }
        }

        return map;
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
        /*int[] caseIdArray = new int[list.size()];
        for (int i=0;i<list.size();i++){
            caseIdArray[i] = list.get(i).getId();
        }
        Map collectMap = new HashMap();
        if (caseIdArray.length>0){
            DataCollectionEntity dataCollectionEntity1 = new DataCollectionEntity();
            dataCollectionEntity1.setIds(caseIdArray);
            dataCollectionMapper.saveBatchCollect(dataCollectionEntity1);
            List<DataCollectionEntity> collectList = dataCollectionMapper.showCollectTime();
            dataCollectionMapper.deletBatchCollect(dataCollectionEntity1);
            for (int i=0;i<collectList.size();i++){
                DataCollectionEntity tempCollect = collectList.get(i);
                collectMap.put(tempCollect.getCaseId(),tempCollect);
            }
        }*/
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
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

        for(DataCaseEntity entity : dataCaseEntities) {
            dataCaseMapper.updateBySeqNo(entity);
        }

    }

    @Transactional
    @Override
    public void saveCaseList(List<DataCaseEntity> dataCaseEntities,String batchNo) {
        try {
            List<DataCaseTelEntity> telEntityList = Lists.newArrayList();

            //修改批次信息
            final DataBatchEntity dataBatchEntity = new DataBatchEntity();
            dataBatchEntity.setBatchNo(batchNo);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            dataBatchEntity.setUploadTime(sdf.format(new Date()));
            dataBatchEntity.setTotalAmt(BigDecimal.ZERO);
            dataBatchEntity.setUserCount(dataCaseEntities.size());
            ExecutorService executor = Executors.newFixedThreadPool(20);
            for (DataCaseEntity entity : dataCaseEntities) {
                entity.setBatchNo(batchNo);
                DataBatchEntity batchEntity =  RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH+dataBatchEntity.getBatchNo(),DataBatchEntity.class);
                entity.setClient(batchEntity ==null ?"":batchEntity.getClient());
                if(entity.getCollectionArea() != null && entity.getCollectionArea().getId() != null) {
                    SysDictionaryEntity collectAreaEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH + entity.getCollectionArea().getId(), SysDictionaryEntity.class);
                    entity.setCollectArea(collectAreaEntity == null ? "" : entity.getCollectionArea().getId() + "");
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getAccountAge())){
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getAccountAge(),SysDictionaryEntity.class);
                    entity.setAccountAge(sysDictionaryEntity.getId()+"");
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getCollectionType())){
                    SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getCollectionType(),SysDictionaryEntity.class);
                    entity.setCollectionType(sysDictionaryEntity.getId()+"");
                }

                entity.setMoney(entity.getMoney()==null?new BigDecimal(0):entity.getMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                entity.setBalance(entity.getBalance()==null?new BigDecimal(0):entity.getBalance().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                entity.setRate(entity.getRate()==null?new BigDecimal(0):entity.getRate().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                try{
                    BigDecimal bMval = new BigDecimal(entity.getMVal()==null?"0":entity.getMVal());
                }catch (Exception e){
                }
                entity.setEnRepayAmt(entity.getEnRepayAmt()==null?new BigDecimal(0):entity.getEnRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                entity.setBankAmt(entity.getBankAmt()==null?new BigDecimal(0):entity.getBankAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                entity.setProRepayAmt(entity.getProRepayAmt()==null?new BigDecimal(0):entity.getProRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                entity.setCommissionMoney(entity.getCommissionMoney()==null?new BigDecimal(0):entity.getCommissionMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                entity.setLastRepayMoney(entity.getLastRepayMoney()==null?new BigDecimal(0):entity.getLastRepayMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                entity.setOutstandingAmount(entity.getOutstandingAmount()==null?new BigDecimal(0):entity.getOutstandingAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                dataCaseMapper.saveCase(entity);

                BigDecimal tmp = dataBatchEntity.getTotalAmt()==null?new BigDecimal(0):dataBatchEntity.getTotalAmt();
                dataBatchEntity.setTotalAmt(tmp.add(entity.getMoney()));
                stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + entity.getSeqNo(), JSONObject.toJSONString(entity));
                stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + entity.getCardNo()+"@"+entity.getCaseDate(), JSONObject.toJSONString(entity));


                if (StringUtils.notEmpty(entity.getContactName1()) || StringUtils.notEmpty(entity.getContactMobile1())) {
                    DataCaseTelEntity dataCaseTelEntity1 = new DataCaseTelEntity();
                    dataCaseTelEntity1.setCaseId(entity.getId());
                    dataCaseTelEntity1.setName(entity.getContactName1());
                    dataCaseTelEntity1.setIdentNo(entity.getContactIdentNo1());
                    dataCaseTelEntity1.setRelation(entity.getContactRelation1());
                    dataCaseTelEntity1.setTel(entity.getContactMobile1());
                    dataCaseTelEntity1.setTelStatusMsg("未知");
                    telEntityList.add(dataCaseTelEntity1);
                }
                if (StringUtils.notEmpty(entity.getContactName2()) || StringUtils.notEmpty(entity.getContactMobile2())) {
                    DataCaseTelEntity dataCaseTelEntity2 = new DataCaseTelEntity();
                    dataCaseTelEntity2.setCaseId(entity.getId());
                    dataCaseTelEntity2.setName(entity.getContactName2());
                    dataCaseTelEntity2.setIdentNo(entity.getContactIdentNo2());
                    dataCaseTelEntity2.setRelation(entity.getContactRelation2());
                    dataCaseTelEntity2.setTel(entity.getContactMobile2());
                    dataCaseTelEntity2.setTelStatusMsg("未知");
                    telEntityList.add(dataCaseTelEntity2);
                }
                if (StringUtils.notEmpty(entity.getContactName3()) || StringUtils.notEmpty(entity.getContactMobile3())) {
                    DataCaseTelEntity dataCaseTelEntity3 = new DataCaseTelEntity();
                    dataCaseTelEntity3.setCaseId(entity.getId());
                    dataCaseTelEntity3.setName(entity.getContactName3());
                    dataCaseTelEntity3.setIdentNo(entity.getContactIdentNo3());
                    dataCaseTelEntity3.setRelation(entity.getContactRelation3());
                    dataCaseTelEntity3.setTel(entity.getContactMobile3());
                    dataCaseTelEntity3.setTelStatusMsg("未知");
                    telEntityList.add(dataCaseTelEntity3);
                }
                if (StringUtils.notEmpty(entity.getContactName4()) || StringUtils.notEmpty(entity.getContactMobile4())) {
                    DataCaseTelEntity dataCaseTelEntity4 = new DataCaseTelEntity();
                    dataCaseTelEntity4.setCaseId(entity.getId());
                    dataCaseTelEntity4.setName(entity.getContactName4());
                    dataCaseTelEntity4.setIdentNo(entity.getContactIdentNo4());
                    dataCaseTelEntity4.setRelation(entity.getContactRelation4());
                    dataCaseTelEntity4.setTel(entity.getContactMobile4());
                    dataCaseTelEntity4.setTelStatusMsg("未知");
                    telEntityList.add(dataCaseTelEntity4);
                }
                if (StringUtils.notEmpty(entity.getContactName5()) || StringUtils.notEmpty(entity.getContactMobile5())) {
                    DataCaseTelEntity dataCaseTelEntity5 = new DataCaseTelEntity();
                    dataCaseTelEntity5.setCaseId(entity.getId());
                    dataCaseTelEntity5.setName(entity.getContactName5());
                    dataCaseTelEntity5.setIdentNo(entity.getContactIdentNo5());
                    dataCaseTelEntity5.setRelation(entity.getContactRelation5());
                    dataCaseTelEntity5.setTel(entity.getContactMobile5());
                    dataCaseTelEntity5.setTelStatusMsg("未知");
                    telEntityList.add(dataCaseTelEntity5);
                }
                if (StringUtils.notEmpty(entity.getContactName6()) || StringUtils.notEmpty(entity.getContactMobile6())) {
                    DataCaseTelEntity dataCaseTelEntity6 = new DataCaseTelEntity();
                    dataCaseTelEntity6.setCaseId(entity.getId());
                    dataCaseTelEntity6.setName(entity.getContactName6());
                    dataCaseTelEntity6.setIdentNo(entity.getContactIdentNo6());
                    dataCaseTelEntity6.setRelation(entity.getContactRelation6());
                    dataCaseTelEntity6.setTel(entity.getContactMobile6());
                    dataCaseTelEntity6.setTelStatusMsg("未知");
                    telEntityList.add(dataCaseTelEntity6);
                }


                if (telEntityList.size() >= 100) {
                    dataCaseTelMapper.insertBatchTel(telEntityList);
                    telEntityList.clear();
                }

            }
            executor.shutdown();
            while (true) {
                if (executor.isTerminated()) {
                    break;
                }
                try {
                    Thread.sleep(100);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            logger.info("保存完毕，开始处理批次");
            if (telEntityList.size() > 0) {
                dataCaseTelMapper.insertBatchTel(telEntityList);
                telEntityList.clear();
            }

            dataBatchMapper.updateUploadTimeByBatchNo(dataBatchEntity);
            logger.info("处理批次完毕");
        }catch (Exception e){
            logger.info(e.getCause().getMessage());
            e.printStackTrace();
            throw new CustomerException(500,"后台异常，请检查数据后后再试");
        }
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
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("0") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+3];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="1";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            newStatuss[dataCaseEntity.getStatuss().length+2]="3";
            dataCaseEntity.setStatuss(newStatuss);
        }
        if (dataCaseEntity.getStatuss()!=null && Arrays.asList(dataCaseEntity.getStatuss()).contains("1") ) {
            String[] newStatuss = new String[dataCaseEntity.getStatuss().length+2];
            for (int i=0;i<dataCaseEntity.getStatuss().length;i++){
                newStatuss[i] = dataCaseEntity.getStatuss()[i];
            }
            newStatuss[dataCaseEntity.getStatuss().length]="0";
            newStatuss[dataCaseEntity.getStatuss().length+1]="2";
            dataCaseEntity.setStatuss(newStatuss);
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
            curentuser.setDepartment(temp.getDepartment());
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


        logger.info("设置数据字典开始");
        if (dataCaseDetail.getOdv()!=null && dataCaseDetail.getOdv().equals("1")){
            dataCaseDetail.setCurrentuser(true);
        }else {
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(dataCaseDetail.getOdv()) && (!(curentuser.getId() + "").equals(dataCaseDetail.getOdv()))) {
                SysNewUserEntity sysNewUserEntity = new SysNewUserEntity();
                sysNewUserEntity.setId(Integer.parseInt(dataCaseDetail.getOdv()==null?"0":(dataCaseDetail.getOdv().equals("")?"0":dataCaseDetail.getOdv())));
                this.foreachData(dataCaseDetail, sysNewUserEntity, Integer.parseInt(curentuser.getId()+""));
            } else if (org.apache.commons.lang3.StringUtils.isNotEmpty(dataCaseDetail.getOdv()) && (curentuser.getId() + "").equals(dataCaseDetail.getOdv())) {
                dataCaseDetail.setCurrentuser(true);
            }
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
            dataCaseDetail.setEnRepayAmtMsg("0");
        }else{
            dataCaseDetail.setEnRepayAmtMsg("￥"+FmtMicrometer.fmtMicrometer(dataCaseDetail.getEnRepayAmt().stripTrailingZeros().toPlainString()));
        }
        if (dataCaseDetail.getBalance()==null || dataCaseDetail.getBalance().compareTo(new BigDecimal(0))==0){
            dataCaseDetail.setBalanceMsg("￥0");
        }else{
            dataCaseDetail.setBalanceMsg("￥"+FmtMicrometer.fmtMicrometer(dataCaseDetail.getBalance().stripTrailingZeros().toPlainString()));
        }
        if (dataCaseDetail.getMoney()==null || dataCaseDetail.getMoney().compareTo(new BigDecimal(0))==0){
            dataCaseDetail.setMoneyMsg("￥0");
        }else{
            dataCaseDetail.setMoneyMsg("￥"+FmtMicrometer.fmtMicrometer(dataCaseDetail.getMoney().stripTrailingZeros().toPlainString()));
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
            if (province==null){
                province = new SysDictionaryEntity();
                province.setName("");
                dataCaseDetail.setProvince(province);
            }else {
                dataCaseDetail.setProvince(province);
            }
        }

        SysDictionaryEntity city = dataCaseDetail.getCity();
        if (city==null){
            city = new SysDictionaryEntity();
            city.setName("");
            dataCaseDetail.setCity(city);
        }else{
            city = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + city.getId(), SysDictionaryEntity.class);
            if (city==null){
                city = new SysDictionaryEntity();
                city.setName("");
                dataCaseDetail.setCity(city);
            }else {
                dataCaseDetail.setCity(city);
            }
        }

        SysDictionaryEntity county = dataCaseDetail.getCounty();
        if (county==null){
            county = new SysDictionaryEntity();
            county.setName("");
            dataCaseDetail.setCounty(county);
        }else{
            county = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ county.getId(), SysDictionaryEntity.class);
            if (county==null){
                county = new SysDictionaryEntity();
                county.setName("");
                dataCaseDetail.setCounty(county);
            }else {
                dataCaseDetail.setCounty(county);
            }
        }
        logger.info("设置数据字典结束");
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
        logger.info("设置电话类型结束");
        DataCaseCommentEntity dataCaseCommentEntity = new DataCaseCommentEntity();
        dataCaseCommentEntity.setCaseId(bean.getId());
        List<DataCaseCommentEntity> commentList = dataCaseCommentMapper.findAll(dataCaseCommentEntity);
        for (int i=0;i<commentList.size();i++){
            DataCaseCommentEntity temp = commentList.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getCreatUser(), SysUserEntity.class);
            temp.setCreatUserName(user == null ? "" : user.getUserName());
            commentList.set(i,temp);
        }
        logger.info("设置备注结束");
        DataCaseEntity caseTemp = dataCaseMapper.findById(bean);
        List<CaseTimeAreaEntity> timeList = caseTimeAreaMapper.listAll();

        if (timeList.size()>0){
            CaseTimeAreaEntity caseTimeAreaEntity = timeList.get(0);
            if (caseTimeAreaEntity.getSeeFlag()==1) {
                caseTemp.setSeeFlag("1");
            }else{
                caseTemp.setSeeFlag("0");
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,caseTimeAreaEntity.getTimeArea()==null?0:caseTimeAreaEntity.getTimeArea());
            caseTemp.setReturnTime(cal.getTime());
        }
       /* if (caseTemp.getSeeFlag()!=null && caseTemp.getSeeFlag().equals("1") && curentuser.getDepartment()!=null && curentuser.getDepartment().equals("业务部")){
            dataCaseDetail.setSameBatchCaseList(new ArrayList<DataCaseEntity>());
        }else{
            List<DataCaseEntity> sameBatchCaseList = dataCaseMapper.findSameBatchCase(caseTemp);
            dataCaseDetail.setSameBatchCaseList(sameBatchCaseList);
        }*/


        dataCaseDetail.setDataCaseCommentEntityList(commentList);
        dataCaseDetail.setDataCaseTelEntityList(dataCaseTelEntityList);
        DataCaseEntity dataCaseEntity = new DataCaseEntity();
        dataCaseEntity.setId(bean.getId());
        List<DataCaseEntity> caseList = new ArrayList<DataCaseEntity>();
        caseList.add(dataCaseEntity);
        logger.info("结束");
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
            temp.setTypeMsg(temp.getType()==null?"":(map.get(Integer.parseInt(temp.getType()))==null?temp.getType():map.get(Integer.parseInt(temp.getType())).toString()));
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
            temp.setMoneyMsg(temp.getMoney()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getMoney().stripTrailingZeros()+""));
            temp.setBankAmtMsg(temp.getBankAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getBankAmt().stripTrailingZeros()+""));
            temp.setBalanceMsg(temp.getBalance()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getBalance().stripTrailingZeros()+""));
            temp.setProRepayAmtMsg(temp.getProRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getProRepayAmt().stripTrailingZeros()+""));
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"￥0": "￥"+FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt().stripTrailingZeros()+""));
            list.set(i,temp);
        }
        return list;
    }

    public List<DataCaseEntity> sameBatchCaseList(DataCaseEntity bean){
        DataCaseEntity caseTemp = dataCaseMapper.findById(bean);
        SysUserEntity curentuser = getUserInfo();
        if (curentuser!=null){
            SysNewUserEntity temp = sysUserMapper.getDataById(curentuser.getId());
            curentuser.setDepartment(temp.getDepartment());
        }
        List<CaseTimeAreaEntity> timeList = caseTimeAreaMapper.listAll();

        if (timeList.size()>0){
            CaseTimeAreaEntity caseTimeAreaEntity = timeList.get(0);
            if (caseTimeAreaEntity.getSeeFlag()==1) {
                caseTemp.setSeeFlag("1");
            }else{
                caseTemp.setSeeFlag("0");
            }
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DAY_OF_MONTH,caseTimeAreaEntity.getTimeArea()==null?0:-caseTimeAreaEntity.getTimeArea());
            caseTemp.setReturnTime(cal.getTime());
        }
        List<DataCaseEntity> list = dataCaseMapper.findSameBatchCase1(caseTemp);
        if (caseTemp.getSeeFlag()!=null && caseTemp.getSeeFlag().equals("1") && curentuser.getDepartment()!=null && (curentuser.getDepartment().indexOf("业务")>0)){

        }else{
            List<DataCaseEntity> list2 = dataCaseMapper.findSameBatchCase2(caseTemp);
            if (list2!=null && list2.size()>0){
                list.addAll(list2);
            }
        }
        if(list!=null) {
            for (int i = 0; i < list.size(); i++) {
                DataCaseEntity temp = list.get(i);
                SysDictionaryEntity accountAgeDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + temp.getAccountAge(), SysDictionaryEntity.class);
                temp.setAccountAge(accountAgeDic == null ? "" : accountAgeDic.getName());
                temp.setMoneyMsg(temp.getMoney() == null ? "￥0" : "￥" + FmtMicrometer.fmtMicrometer(temp.getMoney().stripTrailingZeros() + ""));
                temp.setBankAmtMsg(temp.getBankAmt() == null ? "￥0" : "￥" + FmtMicrometer.fmtMicrometer(temp.getBankAmt().stripTrailingZeros() + ""));
                temp.setBalanceMsg(temp.getBalance() == null ? "￥0" : "￥" + FmtMicrometer.fmtMicrometer(temp.getBalance().stripTrailingZeros() + ""));
                temp.setProRepayAmtMsg(temp.getProRepayAmt() == null ? "￥0" : "￥" + FmtMicrometer.fmtMicrometer(temp.getProRepayAmt().stripTrailingZeros() + ""));
                temp.setEnRepayAmtMsg(temp.getEnRepayAmt() == null ? "￥0" : "￥" + FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt().stripTrailingZeros() + ""));
                list.set(i, temp);
            }
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

        /*DataCaseContactsEntity queryContactsEntity = new DataCaseContactsEntity();
        queryContactsEntity.setCaseIdsSet(caseIdsSet);
        List<DataCaseContactsEntity> contacts = dataCaseContactsMapper.listByCaseIds(queryContactsEntity);
        Map<Integer, List<DataCaseContactsEntity>> contactMap = new HashMap<>();
        for(DataCaseContactsEntity entity : contacts) {
            if(!contactMap.containsKey(entity.getCaseId())) {
                contactMap.put(entity.getCaseId(), new ArrayList<>());
            }
            contactMap.get(entity.getCaseId()).add(entity);
        }*/

        /*DataCaseRemarkEntity queryRemarks = new DataCaseRemarkEntity();
        queryRemarks.setCaseIdsSet(caseIdsSet);
        List<DataCaseRemarkEntity> remarks = dataCaseRemarkMapper.listByCaseIds(queryRemarks);
        Map<Integer, List<DataCaseRemarkEntity>> remarkMap = new HashMap<>();
        for(DataCaseRemarkEntity entity : remarks) {
            if(!remarkMap.containsKey(entity.getCaseId())) {
                remarkMap.put(entity.getCaseId(), new ArrayList<>());
            }
            remarkMap.get(entity.getCaseId()).add(entity);
        }*/
        for(int i = 0; i < list.size(); i ++) {
            DataCaseEntity entity = list.get(i);
            if(entity.getCollectionUser() != null && entity.getCollectionUser().getId() != null && userMap.containsKey(entity.getCollectionUser().getId())) {
                list.get(i).getCollectionUser().setUserName(userMap.get(entity.getCollectionUser().getId()).getUserName());
            }
            /*if(contactMap.containsKey(entity.getId())) {
                list.get(i).setContacts(contactMap.get(entity.getId()));
            }
            if(remarkMap.containsKey(entity.getId())) {
                list.get(i).setCaseRemarks(remarkMap.get(entity.getId()));
            }*/
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


    public void doDataCase(List<DataCaseEntity> caseEntityList) {
        if(CollectionUtils.isEmpty(caseEntityList)){
            return ;
        }

            caseEntityList.forEach(caseEntity -> {
                if (caseEntity.getCollectionArea() != null && caseEntity.getCollectionArea().getId() != null) {
                    SysDictionaryEntity collectAreaEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH + caseEntity.getCollectionArea().getId(), SysDictionaryEntity.class);
                    caseEntity.setCollectArea(collectAreaEntity == null ? "" : caseEntity.getCollectionArea().getId() + "");
                }
                if (caseEntity.getCollectionUser() != null && caseEntity.getCollectionUser().getId() != null) {
                    /*SysNewUserEntity userEntity = new SysNewUserEntity();
                    userEntity.setRole("催收员");
                    List<SysNewUserEntity> userInfoEntity = sysUserService.getDataByRoleNameForList(userEntity);
                    Map collectUserMap = new HashMap();
                    for (int m = 0; m < userInfoEntity.size(); m++) {
                        SysNewUserEntity sysNewUserEntity = userInfoEntity.get(m);
                        collectUserMap.put(sysNewUserEntity.getId(), sysNewUserEntity);
                    }
                    if (collectUserMap.get(caseEntity.getCollectionUser().getId()) == null) {*/
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ caseEntity.getCollectionUser().getId(), SysUserEntity.class);
                    if (user==null) {
                        throw new IllegalArgumentException("催收员ID" + caseEntity.getCollectionUser().getId() + "不正确，请核实后再上传");
                    }
                   // }

                }
                if (org.apache.commons.lang3.StringUtils.isEmpty(caseEntity.getSeqNo()) && (org.apache.commons.lang3.StringUtils.isEmpty(caseEntity.getCardNo()) && org.apache.commons.lang3.StringUtils.isEmpty(caseEntity.getCaseDate()))) {
                    throw new IllegalArgumentException("姓名" + caseEntity.getName() + "的数据未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(caseEntity.getAccountAge())) {
                    SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + caseEntity.getAccountAge(), SysDictionaryEntity.class);
                    if (sysDictionaryEntity == null) {
                        throw new IllegalArgumentException("姓名" + caseEntity.getName() + "的数据逾期账龄值" + caseEntity.getCollectionType() + "不在枚举配置中，并检查excel的逾期账龄是否均填写正确");
                    } else {
                        caseEntity.setAccountAge(sysDictionaryEntity.getId() + "");
                    }
                }
                //语音 手机号 视频 留言
                if (org.apache.commons.lang3.StringUtils.isNotEmpty(caseEntity.getCollectionType())) {
                    SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + caseEntity.getCollectionType(), SysDictionaryEntity.class);
                    if (sysDictionaryEntity == null) {
                        throw new IllegalArgumentException("姓名" + caseEntity.getName() + "的数据行催收分类值" + caseEntity.getCollectionType() + "不在枚举配置中，并检查excel的催收分类是否均填写正确");
                    } else {
                        caseEntity.setCollectionType(sysDictionaryEntity.getId() + "");
                    }
                }

                //用户检测
                if (caseEntity.getCollectionUser() != null && caseEntity.getCollectionUser().getId() != null) {
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + caseEntity.getCollectionUser().getId(), SysUserEntity.class);
                    caseEntity.setDept(user == null ? "" : user.getDepartment());
                }

                caseEntity.setMoney(caseEntity.getMoney()==null?null:caseEntity.getMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                caseEntity.setBalance(caseEntity.getBalance()==null?null:caseEntity.getBalance().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                caseEntity.setRate(caseEntity.getRate()==null?null:caseEntity.getRate().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                try{
                    BigDecimal bMval = new BigDecimal(caseEntity.getMVal()==null?"0":caseEntity.getMVal());
                }catch (Exception e){
                    throw new IllegalArgumentException("个案序列号"+caseEntity.getSeqNo()+"的Mval格式不对，请修改后重新上传");
                }
                caseEntity.setEnRepayAmt(caseEntity.getEnRepayAmt()==null?null:caseEntity.getEnRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                caseEntity.setBankAmt(caseEntity.getBankAmt()==null?null:caseEntity.getBankAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                caseEntity.setProRepayAmt(caseEntity.getProRepayAmt()==null?null:caseEntity.getProRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                caseEntity.setCommissionMoney(caseEntity.getCommissionMoney()==null?null:caseEntity.getCommissionMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                caseEntity.setLastRepayMoney(caseEntity.getLastRepayMoney()==null?null:caseEntity.getLastRepayMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
                caseEntity.setOutstandingAmount(caseEntity.getOutstandingAmount()==null?null:caseEntity.getOutstandingAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN));

            });
        try {
            for (DataCaseEntity entity : caseEntityList) {
                dataCaseMapper.updateBySeqNo(entity);
            }
        }catch (Exception e){
            logger.info(e.getCause().getMessage());
            e.printStackTrace();
            throw new CustomerException(500,"后台异常，请检查数据后后再试");
        }

    }



}
