package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCaseBankReconciliationMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseBankReconciliationEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseBankReconciliationService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataCaseBankReconciliationServiceImpl implements DataCaseBankReconciliationService {

    @Resource
    private DataCaseBankReconciliationMapper dataCaseBankReconciliationMapper;

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
    }
}
