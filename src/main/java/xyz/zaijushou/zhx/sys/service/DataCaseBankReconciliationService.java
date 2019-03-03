package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.DataCaseBankReconciliationEntity;

public interface DataCaseBankReconciliationService {
    PageInfo<DataCaseBankReconciliationEntity> pageDataList(DataCaseBankReconciliationEntity entity);
}
