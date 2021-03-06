package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.ExcelBankReconciliationConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCaseBankReconciliationMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseRemarkMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseRepayRecordMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseBankReconciliationService;
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
public class DataCaseBankReconciliationServiceImpl implements DataCaseBankReconciliationService {

    @Resource
    private DataCaseBankReconciliationMapper dataCaseBankReconciliationMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层
    @Autowired
    private DataLogService dataLogService;
    @Resource
    private DataCaseMapper dataCaseMapper;
    @Resource
    private DataCaseRepayRecordMapper dateCaseRepayRecord;

    @Override
    public PageInfo<DataCaseBankReconciliationEntity> pageDataList(DataCaseBankReconciliationEntity entity) {
        if(entity != null && entity.getDataCase() != null && entity.getDataCase().getCollectStatus() == 0) {
            entity.getDataCase().setCollectStatusMsg("");
        }
        if(!StringUtils.isEmpty(entity.getOrderBy())){
            entity.setOrderBy(ExcelBankReconciliationConstant.BankReconciliationSortEnum.getEnumByKey(entity.getOrderBy()).getValue());
        }else{
            entity.setOrderBy("b.submit_time");
            entity.setSort("desc");
        }
        List<DataCaseBankReconciliationEntity> pageData = combineInfo(dataCaseBankReconciliationMapper.pageData(entity));
        for (int i=0;i<pageData.size();i++){
            DataCaseBankReconciliationEntity temp = pageData.get(i);

            if (temp.getRepayType()!=null && temp.getRepayType().equals("10")){
                temp.setRepayType("代扣卡");
            }else if (temp.getRepayType()!=null && temp.getRepayType().equals("20")){
                temp.setRepayType("对公还款");
            }

            temp.getDataCase().setMoneyMsg(temp.getDataCase().getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getMoney()+""));
            temp.getDataCase().setRepayMoneyMsg(temp.getDataCase().getRepayMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getRepayMoney()+""));
            temp.setCpMoneyMsg(temp.getCpMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getCpMoney()+""));
            temp.getDataCase().setEnRepayAmtMsg(temp.getDataCase().getEnRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getEnRepayAmt()+""));
            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRemark(),SysDictionaryEntity.class);
            temp.setRemark(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());
            pageData.set(i,temp);
        }
        return PageInfo.of(pageData);
    }

    @Override
    public PageInfo<DataCaseBankReconciliationEntity> pageDataListExport(DataCaseBankReconciliationEntity entity) {
        if(entity != null && entity.getDataCase() != null && entity.getDataCase().getCollectStatus() == 0) {
            entity.getDataCase().setCollectStatusMsg("");
        }
        if(!StringUtils.isEmpty(entity.getOrderBy())){
            entity.setOrderBy(ExcelBankReconciliationConstant.BankReconciliationSortEnum.getEnumByKey(entity.getOrderBy()).getValue());
        }
        List<DataCaseBankReconciliationEntity> pageData = combineInfo(dataCaseBankReconciliationMapper.pageDataListExport(entity));
        for (int i=0;i<pageData.size();i++){
            DataCaseBankReconciliationEntity temp = pageData.get(i);

            if (temp.getRepayType()!=null && temp.getRepayType().equals("10")){
                temp.setRepayType("代扣卡");
            }else if (temp.getRepayType()!=null && temp.getRepayType().equals("20")){
                temp.setRepayType("对公还款");
            }

            temp.getDataCase().setMoneyMsg(entity.getDataCase().getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getMoney().stripTrailingZeros()+""));
            temp.getDataCase().setRepayMoneyMsg(entity.getDataCase().getRepayMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getRepayMoney().stripTrailingZeros()+""));
            temp.setCpMoneyMsg(temp.getCpMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getCpMoney().stripTrailingZeros()+""));
            temp.getDataCase().setEnRepayAmtMsg(entity.getDataCase().getEnRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getEnRepayAmt().stripTrailingZeros()+""));
            pageData.set(i,temp);
        }
        return PageInfo.of(pageData);
    }

    @Override
    public void cancel(DataCaseBankReconciliationEntity entity) {
        entity.setStatus("1");
        dataCaseBankReconciliationMapper.updateStatus(entity);
        for(Integer id : entity.getIds()) {
            DataCaseBankReconciliationEntity cp = new DataCaseBankReconciliationEntity();
            cp.setId(id);
            cp = dataCaseBankReconciliationMapper.findById(cp);
            updateDataCaseBalance(cp);
        }
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationMapper.listBankReconciliation(entity);
        for(int i=0;i<list.size();i++)
        {
            DataCaseBankReconciliationEntity  temp = list.get(i);
            DataOpLog log = new DataOpLog();
            log.setType("CP管理 ");
            log.setContext("[作废CP]待确认还款："+temp.getCpMoney()+"，待确认还款日期："+temp.getCpDate()+" ");
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(temp.getDataCase().getId()+"");
            dataLogService.saveDataLog(log);
        }

    }
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }
    @Override
    public List<DataCaseBankReconciliationEntity> listBankReconciliation(DataCaseBankReconciliationEntity bankReconciliationEntity) {
        if(bankReconciliationEntity != null && bankReconciliationEntity.getDataCase() != null && bankReconciliationEntity.getDataCase().getCollectStatus() == 0) {
            bankReconciliationEntity.getDataCase().setCollectStatusMsg("");
        }
        return combineInfo(dataCaseBankReconciliationMapper.listBankReconciliation(bankReconciliationEntity));
    }

    @Override
    public List<DataCaseBankReconciliationEntity> totalExport(DataCaseBankReconciliationEntity bankReconciliationEntity) {
        if(bankReconciliationEntity != null && bankReconciliationEntity.getDataCase() != null && bankReconciliationEntity.getDataCase().getCollectStatus() == 0) {
            bankReconciliationEntity.getDataCase().setCollectStatusMsg("");
        }
        return combineInfo(dataCaseBankReconciliationMapper.totalExport(bankReconciliationEntity));
    }

    @Override
    public void addList(List<DataCaseBankReconciliationEntity> dataEntities) {
        if(CollectionUtils.isEmpty(dataEntities)) {
            return;
        }
        dataCaseBankReconciliationMapper.addList(dataEntities);
        for(DataCaseBankReconciliationEntity entity : dataEntities) {
            updateDataCaseBalance(entity);
        }
        for (int i=0;i<dataEntities.size();i++){
            DataCaseBankReconciliationEntity temp = dataEntities.get(i);
            DataOpLog log = new DataOpLog();
            log.setType("CP管理");
            log.setContext("[新增CP]还款金额："+temp.getCpMoney()+"，还款时间："+temp.getCpDate()+" ");
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(temp.getDataCase().getId()+"");
            dataLogService.saveDataLog(log);
        }
    }

    public void updateDataCaseBalance(DataCaseBankReconciliationEntity cpEntity) {
        DataCaseBankReconciliationEntity queryEntity = dataCaseBankReconciliationMapper.findLatestCpByCaseId(cpEntity);
        if(queryEntity == null) {
            queryEntity = new DataCaseBankReconciliationEntity();
            queryEntity.setDataCase(cpEntity.getDataCase());
            queryEntity.setCpMoney(new BigDecimal(0));
        }
        DataCaseEntity dataCaseEntity = queryEntity.getDataCase();
        if (dataCaseEntity!=null) {
            dataCaseEntity.setBankAmt(queryEntity.getCpMoney());
            dataCaseMapper.updateCpMoney(dataCaseEntity);
        }

    }

    private List<DataCaseBankReconciliationEntity> combineInfo(List<DataCaseBankReconciliationEntity> list) {
        Set<String> userIdsSet = new HashSet<>();
        Set<String> dictSet = new HashSet<>();
        Set<Integer> caseIdsSet = new HashSet<>();
        for(DataCaseBankReconciliationEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }

            if(entity != null && entity.getDataCase() != null && entity.getDataCase().getId() != null) {
                caseIdsSet.add(entity.getDataCase().getId());
            }
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for (DataCaseBankReconciliationEntity entity : list) {
                if (entity != null && entity.getDataCase() != null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    entity.getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }

        for (DataCaseBankReconciliationEntity entity : list) {

            if (entity != null && entity.getSubmitUser() != null && entity.getSubmitUser().getId()!=null){
                SysNewUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ entity.getSubmitUser().getId(), SysNewUserEntity.class);
                entity.setSubmitUser(userTemp);
            }

            if (entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getOdv())){
                SysNewUserEntity odvTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ entity.getDataCase().getOdv(), SysNewUserEntity.class);
                entity.getDataCase().setOdv(odvTemp==null?"":odvTemp.getUserName());
            }

            if (entity != null && entity.getRepayType() != null){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getRepayType(),SysDictionaryEntity.class);
                entity.setRepayType(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getClient(),SysDictionaryEntity.class);
                entity.getDataCase().setClient(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getAccountAge())) {
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getAccountAge(),SysDictionaryEntity.class);
                entity.getDataCase().setAccountAge(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            }
            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getRemark(),SysDictionaryEntity.class);
            entity.setRemark(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            try{
                if (entity.getDataCase()!=null && entity.getDataCase().getMMoney()!=null){
                    entity.getDataCase().setMMoney(entity.getDataCase().getMMoney().multiply(new BigDecimal(entity.getDataCase().getmVal()==null?"0":entity.getDataCase().getmVal())));
                }

            }catch(Exception e){
                entity.getDataCase().setMMoney(new BigDecimal(0));
            }
           /* if(entity != null && entity.getDataCase() != null && entity.getDataCase().getProvince()!=null && entity.getDataCase().getProvince().getId()!=null) {
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


    public List<DataCaseBankReconciliationEntity> listByCaseId(DataCaseBankReconciliationEntity bean){
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationMapper.listByCaseId(bean);
        DataCaseRepayRecordEntity record = new DataCaseRepayRecordEntity();
        record.setDataCase(bean.getDataCase());
        List<DataCaseRepayRecordEntity> repayList = dateCaseRepayRecord.listByCaseId(record);
        for (int i=0;i<list.size();i++){
            DataCaseBankReconciliationEntity temp = list.get(i);
            temp.setCpMoneyMsg(temp.getCpMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getCpMoney().stripTrailingZeros()+""));
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRepayType(),SysDictionaryEntity.class);
            temp.setRepayTypeMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRemark(),SysDictionaryEntity.class);
            temp.setRemark(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());
            temp.setConfirmMoneyMsg("￥0");
            list.set(i,temp);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for (int i=0;i<repayList.size();i++){
            DataCaseRepayRecordEntity temp = repayList.get(i);
            DataCaseBankReconciliationEntity temp1 = new DataCaseBankReconciliationEntity();
            temp1.setConfirmMoneyMsg(temp.getRepayMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getRepayMoney().stripTrailingZeros()+""));
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRepayType(),SysDictionaryEntity.class);
            temp1.setRepayTypeMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRemark(),SysDictionaryEntity.class);
            temp1.setRemark(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());
            temp1.setRepayUser(temp.getRepayUser());
            temp1.setRepayDate(temp.getRepayDate()==null?"":sdf.format(temp.getRepayDate()));
            temp1.setConfirmDate(temp.getConfirmTime()==null?"":sdf.format(temp.getConfirmTime()));
            list.add(temp1);
        }

        return list;
    }


    public void saveBank(DataCaseBankReconciliationEntity bean){

        SysUserEntity sysUserEntity = getUserInfo();
        SysNewUserEntity submitUser = new SysNewUserEntity();
        submitUser.setId(sysUserEntity.getId());
        bean.setSubmitUser(submitUser);
        bean.setCreateUser(sysUserEntity);
        bean.setUpdateUser(sysUserEntity);
        dataCaseBankReconciliationMapper.saveBank(bean);

    }
}
