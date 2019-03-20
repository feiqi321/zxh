package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.ExcelRepayRecordConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseRepayRecordMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;
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
public class DataCaseRepayRecordServiceImpl implements DataCaseRepayRecordService {

    @Resource
    private DataCaseRepayRecordMapper dataCaseRepayRecordMapper;
    @Autowired
    private DataLogService dataLogService;
    @Resource
    private SysUserService sysUserService;//用户业务控制层
    @Resource
    private DataCaseMapper dataCaseMapper;


    @Override
    public PageInfo<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity) {
        if(!StringUtils.isEmpty(entity.getOrderBy())){
            entity.setOrderBy(ExcelRepayRecordConstant.RepayRecordSortEnum.getEnumByKey(entity.getOrderBy()).getValue());
        }
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordMapper.pageRepayRecordList(entity);
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
        Map<Integer, SysNewUserEntity> userMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            userMap = CollectionsUtils.listToMap(userList);
        }
        Map<String, SysDictionaryEntity> dictMap = new HashMap<>();
        if(!CollectionUtils.isEmpty(dictSet)) {
            List<SysDictionaryEntity> dictList = RedisUtils.scanEntityWithKeys(dictSet, SysDictionaryEntity.class);
            for(SysDictionaryEntity dict : dictList) {
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
                }
            }
            if(temp != null && temp.getDataCase() != null && StringUtils.isNotEmpty(temp.getDataCase().getOverdueBillTime())) {
                if(dictMap.get(temp.getDataCase().getOverdueBillTime()) != null) {
                    list.get(i).getDataCase().setOverdueBillTime(dictMap.get(temp.getDataCase().getOverdueBillTime()).getName());
                }
            }
            if(temp.getDataCase()==null){
                DataCaseEntity bean = new DataCaseEntity();
                bean.setMoneyMsg("");
                bean.setOverdueBalanceMsg("");
                bean.setRepayMoneyMsg("");
                temp.setDataCase(bean);
            }else{
                temp.getDataCase().setMoneyMsg(temp.getDataCase()==null?"":(temp.getDataCase().getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getMoney()+"")));
                temp.getDataCase().setOverdueBalanceMsg(temp.getDataCase()==null?"":(temp.getDataCase().getOverdueBalance()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getOverdueBalance()+"")));
                temp.getDataCase().setEnRepayAmtMsg(temp.getDataCase()==null?"":(temp.getDataCase().getEnRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getEnRepayAmt()+"")));
                temp.getDataCase().setCommissionMoneyMsg(temp.getDataCase()==null?"":(temp.getDataCase().getCommissionMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getCommissionMoney()+"")));
            }
            if (temp.getBankReconciliation()==null){
                DataCaseBankReconciliationEntity bean =  new DataCaseBankReconciliationEntity();
                temp.setBankReconciliation(bean);
            }else{
                temp.getBankReconciliation().setCpMoneyMsg(temp.getBankReconciliation()==null?"":(temp.getBankReconciliation().getCpMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getBankReconciliation().getCpMoney()+"")));
            }
            if (temp.getRepayType()==null){
                SysDictionaryEntity bean =  new SysDictionaryEntity();
                bean.setName("");
                temp.setRepayType(bean);
            }else{
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRepayType().getId(),SysDictionaryEntity.class);
                temp.getRepayType().setName(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
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
        return PageInfo.of(list);
    }

    @Override
    public void revoke(DataCaseRepayRecordEntity entity) {
        entity.setRecordStatus("1");
        dataCaseRepayRecordMapper.updateRecordStatus(entity);
        for(Integer id : entity.getIds()) {
            entity.setId(id);
            entity = dataCaseRepayRecordMapper.findById(entity);
            updateDataCaseBalance(entity);
        }

    }

    @Override
    public void save(DataCaseRepayRecordEntity entity) {
        dataCaseRepayRecordMapper.save(entity);
        updateDataCaseBalance(entity);
        DataOpLog log = new DataOpLog();
        log.setType("案件管理");
        log.setContext("已还款："+entity.getRepayMoney()+"，还款日期："+entity.getRepayUser()+"，备注： "+entity.getRemark());
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
    @Override
    public List<DataCaseRepayRecordEntity> listRepayRecord(DataCaseRepayRecordEntity repayRecordEntity) {
        return dataCaseRepayRecordMapper.listRepayRecord(repayRecordEntity);
    }

    @Override
    public void addList(List<DataCaseRepayRecordEntity> dataEntities) {
        if(CollectionUtils.isEmpty(dataEntities)) {
            return;
        }
        dataCaseRepayRecordMapper.addList(dataEntities);
        for(DataCaseRepayRecordEntity entity : dataEntities) {
            updateDataCaseBalance(entity);
        }
        for (int i=0;i<dataEntities.size();i++){
            DataCaseRepayRecordEntity entity = dataEntities.get(i);
            DataOpLog log = new DataOpLog();
            log.setType("案件管理");
            log.setContext("已还款："+entity.getRepayMoney()+"，还款日期："+entity.getRepayUser()+"，备注： "+entity.getRemark());
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(entity.getDataCase().getId()+"");
            dataLogService.saveDataLog(log);
        }
    }

    @Override
    public DataCaseRepayRecordEntity querySum(DataCaseRepayRecordEntity entity) {
        DataCaseRepayRecordEntity returnEntity = dataCaseRepayRecordMapper.queryCaseSum(entity);
        DataCaseRepayRecordEntity repaySumEntity = dataCaseRepayRecordMapper.queryRepaySum(entity);
        returnEntity.setRepayMoney(repaySumEntity.getRepayMoney());
        return returnEntity;
    }

    public void updateDataCaseBalance(DataCaseRepayRecordEntity record) {
        List<DataCaseRepayRecordEntity> records = dataCaseRepayRecordMapper.listBySeqNo(record);
        BigDecimal repayMoney = new BigDecimal(0);
        for(DataCaseRepayRecordEntity recordEntity : records) {
            repayMoney = repayMoney.add(recordEntity.getRepayMoney());
        }
        DataCaseEntity dataCaseEntity = record.getDataCase();
        dataCaseEntity.setEnRepayAmt(repayMoney);
        dataCaseMapper.updateRepayMoney(dataCaseEntity);

    }
}
