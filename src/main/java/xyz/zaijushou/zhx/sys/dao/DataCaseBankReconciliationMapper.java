package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseBankReconciliationEntity;

import java.util.List;

@Mapper
public interface DataCaseBankReconciliationMapper {
    List<DataCaseBankReconciliationEntity> pageData(DataCaseBankReconciliationEntity entity);

    List<DataCaseBankReconciliationEntity> pageDataListExport(DataCaseBankReconciliationEntity entity);

    void updateStatus(DataCaseBankReconciliationEntity entity);

    List<DataCaseBankReconciliationEntity> listBankReconciliation(DataCaseBankReconciliationEntity bankReconciliationEntity);

    List<DataCaseBankReconciliationEntity> totalExport(DataCaseBankReconciliationEntity bankReconciliationEntity);

    void addList(List<DataCaseBankReconciliationEntity> dataEntities);

    DataCaseBankReconciliationEntity findLatestCpByCaseId(DataCaseBankReconciliationEntity cpEntity);

    DataCaseBankReconciliationEntity findById(DataCaseBankReconciliationEntity cp);

    List<DataCaseBankReconciliationEntity> listByCaseId(DataCaseBankReconciliationEntity entity);

    void saveBank(DataCaseBankReconciliationEntity entity);
}
