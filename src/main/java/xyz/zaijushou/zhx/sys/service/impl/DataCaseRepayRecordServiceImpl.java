package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.ExcelRepayRecordConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseRemarkMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseRepayRecordMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Transactional(rollbackFor = Exception.class)
public class DataCaseRepayRecordServiceImpl implements DataCaseRepayRecordService {
    private static Logger logger = LoggerFactory.getLogger(DataCaseRepayRecordServiceImpl.class);
    @Resource
    private DataCaseRepayRecordMapper dataCaseRepayRecordMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层
    @Resource
    private DataCaseMapper dataCaseMapper;
    @Resource
    private DataCaseRemarkMapper dataCaseRemarkMapper;
    @Resource
    private DataCollectionService dataCollectionService;
    @Autowired
    private DataLogService dataLogService;


    @Override
    public PageInfo<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity) {
        if(!StringUtils.isEmpty(entity.getOrderBy())){
            entity.setOrderBy(ExcelRepayRecordConstant.RepayRecordSortEnum.getEnumByKey(entity.getOrderBy()).getValue());
        }else{
           entity.setOrderBy("r.repay_date");
           entity.setSort("desc");
           entity.setOrderBy1("r.confirm_time");
           entity.setSort1("desc");
        }
        logger.info("开始查询");
        List<DataCaseRepayRecordEntity> list = combineInfo(dataCaseRepayRecordMapper.pageRepayRecordList(entity));
        logger.info("结束查询");
        Set<String> userIdsSet = new HashSet<>();
        Set<String> dictSet = new HashSet<>();
        for(DataCaseRepayRecordEntity record : list) {
            if(entity != null && entity.getCollectUser()!= null && entity.getCollectUser().getId() != null ) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getCollectUser().getId());
            }
            if(entity != null && entity.getConfirmUser()!= null && entity.getConfirmUser().getId() != null ) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getConfirmUser().getId());
            }
            if(record != null && record.getDataCase() != null && StringUtils.isNotEmpty(record.getDataCase().getClient())) {
                dictSet.add(RedisKeyPrefix.SYS_DIC + record.getDataCase().getClient());
            }
            if(record != null && record.getDataCase() != null && StringUtils.isNotEmpty(record.getDataCase().getOverdueBillTime())) {
                dictSet.add(RedisKeyPrefix.SYS_DIC + record.getDataCase().getOverdueBillTime());
            }

        }
        logger.info("数据字典结束");
        Map<Integer, SysNewUserEntity> userMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            userMap = CollectionsUtils.listToMap(userList);
        }
        Map<String, SysDictionaryEntity> dictMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(dictSet)) {
            List<SysDictionaryEntity> dictList = RedisUtils.scanEntityWithKeys(dictSet, SysDictionaryEntity.class);
            for(SysDictionaryEntity dict : dictList) {
                if (dict ==null){
                    continue;
                }
                dictMap.put(dict.getId() + "", dict);
            }
        }
        for (int i=0;i<list.size();i++){
            DataCaseRepayRecordEntity temp = list.get(i);
            if(temp != null && temp.getCollectUser()!= null && temp.getCollectUser().getId() != null && userMap.get(temp.getCollectUser().getId()) != null) {
                list.get(i).setCollectUser(userMap.get(temp.getCollectUser().getId()));
            }
            if(temp != null && temp.getConfirmUser()!= null && temp.getConfirmUser().getId() != null && userMap.get(temp.getConfirmUser().getId()) != null) {
                list.get(i).setConfirmUser(userMap.get(temp.getConfirmUser().getId()));
            }
            if(temp != null && temp.getDataCase() != null && StringUtils.isNotEmpty(temp.getDataCase().getClient())) {
                if(dictMap.get(temp.getDataCase().getClient()) != null) {
                    list.get(i).getDataCase().setClient(dictMap.get(temp.getDataCase().getClient()).getName());
                }else{
                    list.get(i).getDataCase().setClient("");
                }
            }
            if(temp != null && temp.getDataCase() != null && StringUtils.isNotEmpty(temp.getDataCase().getOverdueBillTime())) {
                if(dictMap.get(temp.getDataCase().getOverdueBillTime()) != null) {
                    list.get(i).getDataCase().setOverdueBillTime(dictMap.get(temp.getDataCase().getOverdueBillTime()).getName());
                }
            }
            if(temp != null && temp.getDataCase() != null && StringUtils.isNotEmpty(temp.getDataCase().getAccountAge())) {
                if(dictMap.get(temp.getDataCase().getAccountAge()) != null) {
                    list.get(i).getDataCase().setAccountAge(dictMap.get(temp.getDataCase().getAccountAge()).getName());
                }else{
                    list.get(i).getDataCase().setAccountAge("");
                }
            }
            logger.info("金钱开始");
            if(temp.getDataCase()==null){
                DataCaseEntity bean = new DataCaseEntity();
                bean.setMoneyMsg("");
                bean.setOverdueBalanceMsg("");
                bean.setRepayMoneyMsg("");
                temp.setDataCase(bean);
            }else{
                temp.getDataCase().setCommissionMoney((temp.getDataCase().getmVal()==null?new BigDecimal(0):new BigDecimal(temp.getDataCase().getmVal())).multiply(temp.getRepayMoney()==null?new BigDecimal(0):temp.getRepayMoney()));
                temp.getDataCase().setMoneyMsg(temp.getDataCase()==null?"":(temp.getDataCase().getMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getMoney().stripTrailingZeros()+"")));
                temp.getDataCase().setOverdueBalanceMsg(temp.getDataCase()==null?"":(temp.getDataCase().getOverdueBalance()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getOverdueBalance().stripTrailingZeros()+"")));
                temp.getDataCase().setEnRepayAmtMsg(temp.getDataCase()==null?"":(temp.getDataCase().getEnRepayAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getEnRepayAmt().stripTrailingZeros()+"")));
                temp.getDataCase().setCommissionMoneyMsg(temp.getDataCase()==null?"":(temp.getDataCase().getCommissionMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getCommissionMoney().stripTrailingZeros()+"")));
                temp.getDataCase().setBalanceMsg(temp.getDataCase()==null?"":(temp.getDataCase().getBalance()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getBalance().stripTrailingZeros()+"")));
                temp.setRepayMoneyMsg(temp.getRepayMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getRepayMoney().stripTrailingZeros()+""));
                SysDictionaryEntity client = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getDataCase().getClient(), SysDictionaryEntity.class);
                temp.getDataCase().setClient(client==null?"":client.getName());
            }
            logger.info("金钱结束");
            if (temp.getBankReconciliation()==null){
                DataCaseBankReconciliationEntity bean =  new DataCaseBankReconciliationEntity();
                temp.setBankReconciliation(bean);
            }else{
                temp.getBankReconciliation().setCpMoneyMsg(temp.getBankReconciliation()==null?"￥0":(temp.getBankReconciliation().getCpMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getBankReconciliation().getCpMoney().stripTrailingZeros()+"")));
            }
            if (temp.getRepayType()==null){
                SysDictionaryEntity bean =  new SysDictionaryEntity();
                bean.setName("");
                temp.setRepayType(bean);
            }else{
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRepayType().getId(),SysDictionaryEntity.class);
                temp.getRepayType().setName(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if (temp.getRemark()==null){
                temp.setRemark("");
            }else{
//                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRemark(),SysDictionaryEntity.class);
//                temp.setRemark(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if (temp.getCollectUser()==null){
                SysNewUserEntity bean =  new SysNewUserEntity();
                bean.setUserName("");
                temp.setCollectUser(bean);
            }else{
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getCollectUser().getId(), SysUserEntity.class);
                temp.getCollectUser().setUserName(user == null ? "" : user.getUserName());
            }

            list.set(i,temp);
        }
        logger.info("全部结束");
        return PageInfo.of(list);
    }

    @Override
    public void revoke(DataCaseRepayRecordEntity entity) {
        Integer cancelFlag = entity.getCancelFlag();
        entity.setRecordStatus("1");
        dataCaseRepayRecordMapper.updateRecordStatus(entity);
        for(Integer id : entity.getIds()) {
            entity.setId(id);
            entity = dataCaseRepayRecordMapper.findById(entity);
            entity.setCancelFlag(cancelFlag);
            updateDataCaseBalanceCancel(entity);
            DataOpLog log = new DataOpLog();
            log.setType("登帐");
            log.setContext("[撤销还款]还款金额:"+entity.getRepayMoney());
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(entity.getDataCase().getId()+"");
            dataLogService.saveDataLog(log);
            log.setContext("撤销还款恢复案件结清状态");
            dataLogService.saveDataLog(log);
        }

    }

    public void updateDataCaseBalanceCancel(DataCaseRepayRecordEntity record) {
        List<DataCaseRepayRecordEntity> records = dataCaseRepayRecordMapper.listBySeqNo(record);
        BigDecimal repayMoney = new BigDecimal(0);
        Integer lastId = 0;

        for(DataCaseRepayRecordEntity recordEntity : records) {
            repayMoney = repayMoney.add(recordEntity.getRepayMoney()==null?new BigDecimal(0):recordEntity.getRepayMoney());
            if(recordEntity.getId() > lastId) {
                lastId = recordEntity.getId();

            }
        }
        DataCaseRepayRecordEntity lastRecord = new DataCaseRepayRecordEntity();
        lastRecord.setId(lastId);
        lastRecord = dataCaseRepayRecordMapper.findById(lastRecord);
        DataCaseEntity dataCaseEntity = record.getDataCase();
        dataCaseEntity.setId(record.getDataCase().getId());
        //还款金额
        dataCaseEntity.setEnRepayAmt(repayMoney);
        //还款时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dataCaseEntity.setRepayDate(lastRecord==null?"":(lastRecord.getRepayDate()==null?"":sdf.format(lastRecord.getRepayDate())));
        //此处结清状态为导入时的状态
        dataCaseEntity.setSettleFlag(lastRecord==null?"":lastRecord.getSettleFlag());

        //根据seqNo 获取dataCase
        DataCaseEntity dataCaseEntity1 =dataCaseMapper.findById(new DataCaseEntity(){{setId(record.getDataCase().getId());}});
        if (dataCaseEntity1 != null) {
            //判读结清状态
            if (record.getCancelFlag()!=null && record.getCancelFlag()==1){
                dataCaseEntity.setSettleFlag("未结清");
                dataCaseEntity.setStatus(1);
            }else if(dataCaseEntity1!=null && "已结清".equals(dataCaseEntity1.getSettleFlag())){
                //本来是结清状态就不需要更新了
                dataCaseEntity.setSettleFlag(null);
            }else{
                dataCaseEntity.setSettleFlag("未结清");
                dataCaseEntity.setStatus(1);
            }

            dataCaseMapper.updateRepayMoney(dataCaseEntity);
            if (record !=null && record.getCollectUser()!=null && record.getCollectUser().getId()!=null) {
                this.royalti(dataCaseEntity.getId(), record.getCollectUser().getId());
            }else if ((record==null || record.getCollectUser()==null) &&  StringUtils.isNotEmpty(dataCaseEntity1.getOdv())){
                this.royalti(dataCaseEntity.getId(), Integer.parseInt(dataCaseEntity1.getOdv()));
            }else if(record!=null && record.getCollectUser()!=null && record.getCollectUser().getId()==null && StringUtils.isNotEmpty(dataCaseEntity1.getOdv())){
                this.royalti(dataCaseEntity.getId(), Integer.parseInt(dataCaseEntity1.getOdv()));
            }
        }

    }

    @Override
    public void save(DataCaseRepayRecordEntity entity) {
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysNewUserEntity newUser = new SysNewUserEntity();
        newUser.setId(userId);
        entity.setConfirmUser(newUser);
        dataCaseRepayRecordMapper.save(entity);
        updateDataCaseBalance(entity);
        DataOpLog log = new DataOpLog();
        log.setType("案件管理");
        log.setContext("已还款："+(entity.getRepayMoney()==null?"0":entity.getRepayMoney())+"，还款日期："+(entity.getRepayUser()==null?"":entity.getRepayUser())+"，备注： "+(entity.getRemark()==null?"":entity.getRemark()));
        log.setOper(getUserInfo().getId());
        log.setOperName(getUserInfo().getUserName());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.setOpTime(sdf.format(new Date()));
        log.setCaseId(entity.getDataCase().getId()+"");
        dataLogService.saveDataLog(log);
    }
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }

    public List<DataCaseRepayRecordEntity> showRepay(OdvPercentage entity){
        entity.setOdv(getUserInfo().getId());
        //获取前月的第一天
        Calendar   cal_1=Calendar.getInstance();//获取当前日期
        cal_1.set(Calendar.DAY_OF_MONTH,1);//设置为1号,当前日期既为本月第一天
        entity.setStarttime(cal_1.getTime());
        Calendar timeEnd = Calendar.getInstance();
        timeEnd.set(Calendar.DAY_OF_MONTH, timeEnd.getActualMaximum(Calendar.DAY_OF_MONTH));
        entity.setEndtime(timeEnd.getTime());
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordMapper.showRepay(entity);
        for (int i=0;i<list.size();i++){
            DataCaseRepayRecordEntity temp = list.get(i);
            temp.setRepayMoneyMsg(temp.getRepayMoney()==null?"0":temp.getRepayMoney().stripTrailingZeros().toPlainString());
            list.set(i,temp);
        }
        return list;
    }

    @Override
    public List<DataCaseRepayRecordEntity> listRepayRecord(DataCaseRepayRecordEntity repayRecordEntity) {
        return combineInfo(dataCaseRepayRecordMapper.listRepayRecord(repayRecordEntity));
    }

    @Override
    public List<DataCaseRepayRecordEntity> listRepayRecordSelectExport(DataCaseRepayRecordEntity repayRecordEntity) {
        return combineInfo(dataCaseRepayRecordMapper.listRepayRecordSelectExport(repayRecordEntity));
    }

    @Override
    public List<DataCaseRepayRecordEntity> listRepayRecordExport(DataCaseRepayRecordEntity repayRecordEntity) {
        return combineInfo(dataCaseRepayRecordMapper.listRepayRecordExport(repayRecordEntity));
    }

    @Override
    public void addList(List<DataCaseRepayRecordEntity> dataEntities) {
        if(CollectionUtils.isEmpty(dataEntities)) {
            return;
        }
        for (int i=0;i<dataEntities.size();i++){
            DataCaseRepayRecordEntity temp = dataEntities.get(i);
            dataCaseRepayRecordMapper.save(temp);
        }

        this.saveElse(dataEntities);
        //dataCaseRepayRecordMapper.addList(dataEntities);

     /*   for (int i=0;i<dataEntities.size();i++){
            DataCaseRepayRecordEntity entity = dataEntities.get(i);
            updateDataCaseBalance(entity);
            DataOpLog log = new DataOpLog();
            log.setType("案件管理");
            log.setContext("已还款："+(entity.getRepayMoney()==null?"0":entity.getRepayMoney())+"，还款日期："+(entity.getRepayUser()==null?"":entity.getRepayUser())+"，备注： "+(entity.getRemark()==null?"":entity.getRemark()));
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(entity.getDataCase().getId()+"");
            dataLogService.saveDataLog(log);
        }*/
    }
    @Async
    public void saveElse(List<DataCaseRepayRecordEntity> dataEntities){
        for (int i=0;i<dataEntities.size();i++){
            DataCaseRepayRecordEntity entity = dataEntities.get(i);
            updateDataCaseBalance(entity);
            DataOpLog log = new DataOpLog();
            log.setType("案件管理");
            log.setContext("已还款："+(entity.getRepayMoney()==null?"0":entity.getRepayMoney())+"，还款日期："+(entity.getRepayUser()==null?"":entity.getRepayUser())+"，备注： "+(entity.getRemark()==null?"":entity.getRemark()));
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(entity.getDataCase().getId()+"");
            dataLogService.saveDataLog(log);
        }
    }

    @Override
    public DataCaseRepayRecordEntity culateSum(DataCaseRepayRecordEntity entity) {
        DataCaseRepayRecordEntity returnEntity = dataCaseRepayRecordMapper.queryCaseSum(entity);
        DataCaseRepayRecordEntity repaySumEntity = dataCaseRepayRecordMapper.queryRepaySum(entity);
        returnEntity.setRepayMoneyMsg(repaySumEntity==null?"":(repaySumEntity.getRepayMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(repaySumEntity.getRepayMoney().stripTrailingZeros()+"")));
        returnEntity.getDataCase().setCommissionMoneyMsg(returnEntity.getDataCase()==null?"":(returnEntity.getDataCase().getCommissionMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(returnEntity.getDataCase().getCommissionMoney().stripTrailingZeros()+"")));
        return returnEntity;
    }


    public void updateDataCaseBalance(DataCaseRepayRecordEntity record) {
        List<DataCaseRepayRecordEntity> records = dataCaseRepayRecordMapper.listBySeqNo(record);
        BigDecimal repayMoney = new BigDecimal(0);
        Integer lastId = 0;
//        DataCaseRepayRecordEntity lastRecord = null;
        for(DataCaseRepayRecordEntity recordEntity : records) {
            repayMoney = repayMoney.add(recordEntity.getRepayMoney()==null?new BigDecimal(0):recordEntity.getRepayMoney());
            if(recordEntity.getId() > lastId) {
                lastId = recordEntity.getId();
//                lastRecord = recordEntity;
            }
        }
        DataCaseEntity dataCaseEntity = record.getDataCase();
        dataCaseEntity.setId(record.getDataCase().getId());
        //还款金额
        dataCaseEntity.setEnRepayAmt(repayMoney);
        //还款时间
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dataCaseEntity.setRepayDate(record.getRepayDate()==null?"":sdf.format(record.getRepayDate()));
        //此处结清状态为导入时的状态
        dataCaseEntity.setSettleFlag(record.getSettleFlag());
//        if(lastId != 0) {
//            dataCaseEntity.setSettleFlag(lastRecord.getSettleFlag());
//        }
        //获取dataCasede的seqNO
        String seqNo = record.getDataCase().getSeqNo();
        //根据seqNo 获取dataCase
        List<DataCaseEntity> dataCaseEntityList =dataCaseMapper.findSettleFlagBySeqNo(new DataCaseEntity(){{setSeqNo(seqNo);}});
        //判读结清状态
        if (dataCaseEntityList.size()>0){
            if("已结清".equals(dataCaseEntityList.get(0).getSettleFlag())){
            //本来是结清状态就不需要更新了
            dataCaseEntity.setSettleFlag(null);
            }else{
                if (dataCaseEntity.getSettleFlag()!=null && dataCaseEntity.getSettleFlag().equals("已结清")){

                }else {
                    dataCaseEntity.setSettleFlag("未结清");
                    dataCaseEntity.setStatus(dataCaseEntityList.get(0).getStatus());
                }
            }
        }

        dataCaseMapper.updateRepayMoney(dataCaseEntity);
        dataCaseEntity =dataCaseMapper.findById(new DataCaseEntity(){{setId(record.getDataCase().getId());}});
        if (record.getCollectUser()!=null && record.getCollectUser().getId()!=null) {
            this.royalti(dataCaseEntity.getId(), record.getCollectUser().getId());
        }else if (record.getCollectUser()==null &&  StringUtils.isNotEmpty(dataCaseEntity.getOdv())){
            this.royalti(dataCaseEntity.getId(), Integer.parseInt(dataCaseEntity.getOdv()));
        }else if(record.getCollectUser()!=null && record.getCollectUser().getId()==null && StringUtils.isNotEmpty(dataCaseEntity.getOdv())){
            this.royalti(dataCaseEntity.getId(), Integer.parseInt(dataCaseEntity.getOdv()));
        }
    }

    @Async
    public void royalti(Integer id,Integer userId){
        dataCollectionService.calRoyalti(id,userId);
        dataCollectionService.calRoyaltiManage(id,userId);
    }

    private List<DataCaseRepayRecordEntity> combineInfo(List<DataCaseRepayRecordEntity> list) {
        Set<String> userIdsSet = new HashSet<>();
        Set<Integer> caseIdsSet = new HashSet<>();
        for(DataCaseRepayRecordEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
            if(entity != null && entity.getCollectUser()!= null && entity.getCollectUser().getId() != null ) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getCollectUser().getId());
            }
            if(entity != null && entity.getConfirmUser()!= null && entity.getConfirmUser().getId() != null ) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getConfirmUser().getId());
            }

            if(entity != null && entity.getDataCase() != null && entity.getDataCase().getId() != null) {
                caseIdsSet.add(entity.getDataCase().getId());
            }
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for (DataCaseRepayRecordEntity entity : list) {
                if (entity != null && entity.getDataCase() != null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    entity.getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }

                if (entity != null && entity.getCollectUser()!= null && entity.getCollectUser().getId() != null ) {
                    entity.setCollectUser(userMap.get(entity.getCollectUser().getId()));
                }
                if (entity.getCollectUser()!=null){
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ entity.getCollectUser().getId(), SysUserEntity.class);
                    entity.getCollectUser().setDeptName(user.getDeptName());
                }
                if (entity.getCreateUser()!=null && entity.getCreateUser().getId()!=null){
                    SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ entity.getCreateUser().getId(), SysUserEntity.class);
                    entity.getCreateUser().setUserName(user.getUserName());
                }
                if (entity != null && entity.getConfirmUser()!= null && entity.getConfirmUser().getId() != null ) {
                    entity.setConfirmUser(userMap.get(entity.getConfirmUser().getId()));
                }
            }
        }

        DataCaseRemarkEntity queryRemarks = new DataCaseRemarkEntity();
        queryRemarks.setCaseIdsSet(caseIdsSet);

        for (DataCaseRepayRecordEntity entity : list) {

            if (entity.getRepayType()!=null && entity.getRepayType().getId()!=null){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getRepayType().getId(),SysDictionaryEntity.class);
                entity.setRepayType(sysDictionaryEntity);
            }
            /*if (StringUtils.isNotEmpty(entity.getRemark())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getRemark(),SysDictionaryEntity.class);
                entity.setRemark(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }*/
            if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getClient(),SysDictionaryEntity.class);
                entity.getDataCase().setClient(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getAccountAge())) {
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getAccountAge(),SysDictionaryEntity.class);
                entity.getDataCase().setAccountAge(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            try{
                if (entity.getDataCase()!=null && entity.getDataCase().getMMoney()!=null){
                    entity.getDataCase().setMMoney(entity.getDataCase().getMMoney().multiply(new BigDecimal(entity.getDataCase().getmVal()==null?"0":entity.getDataCase().getmVal())));
                }

            }catch(Exception e){
                entity.getDataCase().setMMoney(new BigDecimal(0));
            }
            /*if(entity != null && entity.getDataCase() != null && entity.getDataCase().getProvince()!=null && entity.getDataCase().getProvince().getId()!=null) {
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getProvince().getId(),SysDictionaryEntity.class);
                entity.getDataCase().setProvince(sysDictionaryEntity);
            }
            if(entity != null && entity.getDataCase() != null && entity.getDataCase().getCity()!=null && entity.getDataCase().getCity().getId()!=null) {
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getCity().getId(),SysDictionaryEntity.class);
                entity.getDataCase().setCity(sysDictionaryEntity);
            }
            if(entity != null && entity.getDataCase() != null && entity.getDataCase().getCounty()!=null && entity.getDataCase().getCounty().getId()!=null) {
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getCounty().getId(),SysDictionaryEntity.class);
                entity.getDataCase().setCounty(sysDictionaryEntity);
            }*/
        }

        return list;
    }

    @Override
    public DataCaseRepayRecordEntity queryOneRecord(Integer id) {
        return dataCaseRepayRecordMapper.queryOneRecord(id);
    }

    @Override
    public void updateRecord(DataCaseRepayRecordEntity entity) {
        dataCaseRepayRecordMapper.updateRecord(entity);
    }
}
