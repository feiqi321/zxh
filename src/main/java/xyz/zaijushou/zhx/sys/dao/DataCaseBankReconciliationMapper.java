package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseBankReconciliationEntity;

import java.util.List;

@Mapper
public interface DataCaseBankReconciliationMapper {
    List<DataCaseBankReconciliationEntity> pageData(DataCaseBankReconciliationEntity entity);
}