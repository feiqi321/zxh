package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseContactsEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.util.List;

@Mapper
public interface DataCaseContactsMapper {

    void deleteCaseContactsBatchByCaseIds(DataCaseContactsEntity contactsEntity);

    void insertCaseContactsBatch(DataCaseEntity caseEntity);

    List<DataCaseContactsEntity> listByCaseIds(DataCaseContactsEntity queryContactsEntity);
}
