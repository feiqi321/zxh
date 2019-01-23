package xyz.zaijushou.zhx.sys.service;


import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;

import java.util.List;

public interface SysDictionaryService {
    /**
     * 插入数据信息
     * @param dictionary
     */
    void saveDataDictionary(SysDictionaryEntity dictionary);

    /**
     * 更新数据信息
     * @param dictionary
     */
    void updateDataDictionary(SysDictionaryEntity dictionary);

    /**
     * 查询数据集合
     * @param dictionaryId
     * @return
     */
    List<SysDictionaryEntity> getDataList(Integer dictionaryId, String name);

    /**
     *  根据Id查询数据
     * @param id
     * @return
     */
    SysDictionaryEntity getDataById(Integer id);

    /**
     * 通过Id删除数据信息
     * @param id
     */
    void deleteById(Integer id);
}

