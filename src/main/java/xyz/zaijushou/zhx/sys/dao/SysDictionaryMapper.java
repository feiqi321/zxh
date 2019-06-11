package xyz.zaijushou.zhx.sys.dao;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergisticEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;

import java.util.List;


@Mapper
public interface SysDictionaryMapper {
    void saveDataDictionary(SysDictionaryEntity dictionary);

    void saveDataAfter(SysDictionaryEntity dictionary);

    void updateDataDictionary(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> getDataList(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> loadByType();

    List<SysDictionaryEntity> listDataByName(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> listDataByDName(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> getDataById(Integer id);

    List<SysDictionaryEntity> getDataByParentId(Integer parentId);

    void deleteById(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> getCollectionDataByDicId(Integer id);
}
