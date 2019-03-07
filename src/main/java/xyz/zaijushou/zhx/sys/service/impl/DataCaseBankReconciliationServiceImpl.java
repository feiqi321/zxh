package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.sys.dao.DataCaseBankReconciliationMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseBankReconciliationService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DataCaseBankReconciliationServiceImpl implements DataCaseBankReconciliationService {

    @Resource
    private DataCaseBankReconciliationMapper dataCaseBankReconciliationMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层
    @Autowired
    private DataLogService dataLogService;

    @Override
    public PageInfo<DataCaseBankReconciliationEntity> pageDataList(DataCaseBankReconciliationEntity entity) {
        if(entity != null && entity.getDataCase() != null && entity.getDataCase().getCollectStatus() == 0) {
            entity.getDataCase().setCollectStatusMsg("");
        }
        List<DataCaseBankReconciliationEntity> pageData = dataCaseBankReconciliationMapper.pageData(entity);
        return PageInfo.of(pageData);
    }

    @Override
    public void cancel(DataCaseBankReconciliationEntity entity) {
        entity.setStatus("1");
        dataCaseBankReconciliationMapper.updateStatus(entity);
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
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutStatusById(user);
    }
    @Override
    public List<DataCaseBankReconciliationEntity> listBankReconciliation(DataCaseBankReconciliationEntity bankReconciliationEntity) {
        if(bankReconciliationEntity != null && bankReconciliationEntity.getDataCase() != null && bankReconciliationEntity.getDataCase().getCollectStatus() == 0) {
            bankReconciliationEntity.getDataCase().setCollectStatusMsg("");
        }
        return dataCaseBankReconciliationMapper.listBankReconciliation(bankReconciliationEntity);
    }

    @Override
    public void addList(List<DataCaseBankReconciliationEntity> dataEntities) {
        if(CollectionUtils.isEmpty(dataEntities)) {
            return;
        }
        dataCaseBankReconciliationMapper.addList(dataEntities);
        for (int i=0;i<dataEntities.size();i++){
            DataCaseBankReconciliationEntity temp = dataEntities.get(i);
            DataOpLog log = new DataOpLog();
            log.setType("CP管理 ");
            log.setContext("[新增CP]还款金额："+temp.getCpMoney()+"，还款时间："+temp.getCpDate()+" ");
            log.setOper(getUserInfo().getId());
            log.setOperName(getUserInfo().getUserName());
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            log.setOpTime(sdf.format(new Date()));
            log.setCaseId(temp.getDataCase().getId()+"");
            dataLogService.saveDataLog(log);
        }
    }

}
