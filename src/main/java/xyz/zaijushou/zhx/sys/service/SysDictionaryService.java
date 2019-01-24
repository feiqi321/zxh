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
     * @param dictionary
     * @return
     */
    List<SysDictionaryEntity> getDataList(SysDictionaryEntity dictionary);

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
}

