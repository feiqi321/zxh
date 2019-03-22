package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.CollectionReturnEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseBankReconciliationEntity;

import java.util.List;

public interface DataCaseBankReconciliationService {
    PageInfo<DataCaseBankReconciliationEntity> pageDataList(DataCaseBankReconciliationEntity entity);

    void cancel(DataCaseBankReconciliationEntity entity);

    List<DataCaseBankReconciliationEntity> listBankReconciliation(DataCaseBankReconciliationEntity bankReconciliationEntity);

    void addList(List<DataCaseBankReconciliationEntity> dataEntities);

    List<DataCaseBankReconciliationEntity> listByCaseId(DataCaseBankReconciliationEntity bean);

    void saveBank(DataCaseBankReconciliationEntity bean);
}
