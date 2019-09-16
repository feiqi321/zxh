package xyz.zaijushou.zhx.sys.service;


import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;

import java.util.List;
import java.util.Map;

public interface SysDictionaryService {
    /**
     * 插入数据信息
     * @param dictionarys
     */
    void saveDataDictionary(SysDictionaryEntity[] dictionarys);

    /**
     * 更新数据信息
     * @param dictionary
     */
    void updateDataDictionary(SysDictionaryEntity dictionary);

    /**
     * 查询数据集合
     * @param dictionary
     * @return
     */
    List<SysDictionaryEntity> getDataList(SysDictionaryEntity dictionary);

    /**
     * 查询数据集合
     * @return
     */
    Map<String,Object> loadByType();


    /**
     * 根据名称获取数据组
     * @param dictionary
     * @return
     */
    List<SysDictionaryEntity> listDataByName(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> listDataByPid(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> listDataByDName(SysDictionaryEntity dictionary);

    /**
     *  根据Id查询数据
     * @param dictionary
     * @return
     */
    SysDictionaryEntity getDataById(SysDictionaryEntity dictionary);

    /**
     * 通过Id删除数据信息
     * @param dictionary
     */
    void deleteById(SysDictionaryEntity dictionary);

    List<SysDictionaryEntity> findAreaTableData(Integer id);

    void addArea(SysDictionaryEntity sysDictionaryEntity);

    void updateArea(SysDictionaryEntity sysDictionaryEntity);

    Object deleteArea(SysDictionaryEntity sysDictionaryEntity);
}

