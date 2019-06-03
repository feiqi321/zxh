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
    private DataCaseRemarkMapper dataCaseRemarkMapper;

    @Override
    public PageInfo<DataCaseBankReconciliationEntity> pageDataList(DataCaseBankReconciliationEntity entity) {
        if(entity != null && entity.getDataCase() != null && entity.getDataCase().getCollectStatus() == 0) {
            entity.getDataCase().setCollectStatusMsg("");
        }
        if(!StringUtils.isEmpty(entity.getOrderBy())){
            entity.setOrderBy(ExcelBankReconciliationConstant.BankReconciliationSortEnum.getEnumByKey(entity.getOrderBy()).getValue());
        }
        List<DataCaseBankReconciliationEntity> pageData = combineInfo(dataCaseBankReconciliationMapper.pageData(entity));
        for (int i=0;i<pageData.size();i++){
            DataCaseBankReconciliationEntity temp = pageData.get(i);

            if (temp.getRepayType()!=null && temp.getRepayType().equals("10")){
                temp.setRepayType("代扣卡");
            }else if (temp.getRepayType()!=null && temp.getRepayType().equals("20")){
                temp.setRepayType("对公还款");
            }

            temp.getDataCase().setMoneyMsg(entity.getDataCase().getMoney()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getMoney()+""));
            temp.getDataCase().setRepayMoneyMsg(entity.getDataCase().getRepayMoney()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getRepayMoney()+""));
            temp.setCpMoneyMsg(temp.getCpMoney()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(temp.getCpMoney()+""));
            temp.getDataCase().setEnRepayAmtMsg(entity.getDataCase().getEnRepayAmt()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getEnRepayAmt()+""));
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

            temp.getDataCase().setMoneyMsg(entity.getDataCase().getMoney()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getMoney().stripTrailingZeros()+""));
            temp.getDataCase().setRepayMoneyMsg(entity.getDataCase().getRepayMoney()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getRepayMoney().stripTrailingZeros()+""));
            temp.setCpMoneyMsg(temp.getCpMoney()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(temp.getCpMoney().stripTrailingZeros()+""));
            temp.getDataCase().setEnRepayAmtMsg(entity.getDataCase().getEnRepayAmt()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getEnRepayAmt().stripTrailingZeros()+""));
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
        dataCaseEntity.setBankAmt(queryEntity.getCpMoney());
        dataCaseMapper.updateCpMoney(dataCaseEntity);

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
            if(entity != null && entity.getDataCase() != null && entity.getDataCase().getProvince()!=null && entity.getDataCase().getProvince().getId()!=null) {
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
            }
        }
        return list;
    }


    public List<DataCaseBankReconciliationEntity> listByCaseId(DataCaseBankReconciliationEntity bean){
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationMapper.listByCaseId(bean);
        for (int i=0;i<list.size();i++){
            DataCaseBankReconciliationEntity temp = list.get(i);
            temp.setCpMoneyMsg(temp.getCpMoney()==null?"": "￥0"+ FmtMicrometer.fmtMicrometer(temp.getCpMoney().stripTrailingZeros()+""));
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRepayType(),SysDictionaryEntity.class);
            temp.setRepayType(sysDictionaryEntity.getName());
            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRemark(),SysDictionaryEntity.class);
            temp.setRemark(sysDictionaryEntity2.getName());
            temp.setConfirmMoneyMsg("￥0");
            list.set(i,temp);
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
