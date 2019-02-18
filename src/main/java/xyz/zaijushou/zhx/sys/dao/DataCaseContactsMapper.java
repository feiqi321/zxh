package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseContactsEntity;

@Mapper
public interface DataCaseContactsMapper {

    void saveCaseContacts(DataCaseContactsEntity contactsEntity);

    void deleteCaseContactsByCaseId(DataCaseContactsEntity contactsEntity);

}
