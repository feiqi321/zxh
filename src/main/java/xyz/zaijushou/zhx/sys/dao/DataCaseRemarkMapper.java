package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRemarkEntity;

import java.util.List;

@Mapper
public interface DataCaseRemarkMapper {
    void deleteCaseRemarkBatchByCaseIds(DataCaseRemarkEntity remarkEntity);

    void insertCaseRemarkBatch(DataCaseEntity caseEntity);

    List<DataCaseRemarkEntity> listByCaseIds(DataCaseRemarkEntity queryRemarks);
}
