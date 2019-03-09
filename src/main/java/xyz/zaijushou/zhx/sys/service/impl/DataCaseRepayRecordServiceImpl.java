package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.ExcelRepayRecordConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCaseRepayRecordMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DataCaseRepayRecordServiceImpl implements DataCaseRepayRecordService {

    @Resource
    private DataCaseRepayRecordMapper dataCaseRepayRecordMapper;
    @Autowired
    private DataLogService dataLogService;
    @Resource
    private SysUserService sysUserService;//用户业务控制层


    @Override
    public PageInfo<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity) {
        if(!StringUtils.isEmpty(entity.getOrderBy())){
            entity.setOrderBy(ExcelRepayRecordConstant.RepayRecordSortEnum.getEnumByKey(entity.getOrderBy()).getValue());
        }
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordMapper.pageRepayRecordList(entity);
        for (int i=0;i<list.size();i++){
            DataCaseRepayRecordEntity temp = list.get(i);
            if(temp.getDataCase()==null){
                DataCaseEntity bean = new DataCaseEntity();
                bean.setMoneyMsg("");
                bean.setOverdueBalanceMsg("");
                bean.setRepayMoneyMsg("");
                temp.setDataCase(bean);
            }else{
                temp.getDataCase().setMoneyMsg(temp.getDataCase()==null?"":(temp.getDataCase().getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getMoney()+"")));
                temp.getDataCase().setOverdueBalanceMsg(temp.getDataCase()==null?"":(temp.getDataCase().getOverdueBalance()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getOverdueBalance()+"")));
                temp.getDataCase().setRepayMoneyMsg(temp.getDataCase()==null?"":(temp.getDataCase().getRepayMoneyMsg()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getDataCase().getRepayMoneyMsg()+"")));
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
    }

    @Override
    public void save(DataCaseRepayRecordEntity entity) {
        dataCaseRepayRecordMapper.save(entity);
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
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutStatusById(user);
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


}
