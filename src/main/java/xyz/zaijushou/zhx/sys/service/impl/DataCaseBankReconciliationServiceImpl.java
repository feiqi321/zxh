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
        List<DataCaseBankReconciliationEntity> pageData = dataCaseBankReconciliationMapper.pageData(entity);
        return PageInfo.of(pageData);
    }
}
