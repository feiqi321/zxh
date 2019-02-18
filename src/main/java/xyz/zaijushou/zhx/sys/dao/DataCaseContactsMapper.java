package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseContactsEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

@Mapper
public interface DataCaseContactsMapper {

    void deleteCaseContactsBatchByCaseIds(DataCaseContactsEntity contactsEntity);

    void insertCaseContactsBatch(DataCaseEntity caseEntity);
}
