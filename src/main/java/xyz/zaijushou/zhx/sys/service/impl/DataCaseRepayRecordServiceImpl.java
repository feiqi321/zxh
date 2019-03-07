package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.ExcelRepayRecordConstant;
import xyz.zaijushou.zhx.sys.dao.DataCaseRepayRecordMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;
import xyz.zaijushou.zhx.sys.entity.DataOpLog;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

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
