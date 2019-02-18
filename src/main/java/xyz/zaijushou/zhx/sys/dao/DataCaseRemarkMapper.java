package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseRemarkEntity;

@Mapper
public interface DataCaseRemarkMapper {

    void saveCaseRemark(DataCaseRemarkEntity remarkEntity);

    void deleteCaseRemarkByCaseId(DataCaseRemarkEntity remarkEntity);

}
