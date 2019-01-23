package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;

import java.util.List;


@Mapper
public interface SysDictionaryMapper {
    void insertDataDictionary(SysDictionaryEntity dictionary);
    void updateDataDictionary(SysDictionaryEntity dictionary);
    List<SysDictionaryEntity> getDataList(@Param("dictionaryId") Integer dictionaryId, @Param("name") String name);
    SysDictionaryEntity getDataById(Integer id);
    void deleteById(Integer id);
}
