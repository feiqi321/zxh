package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCollectionService {

    public void save(DataCollectionEntity dataCollectionEntity);

    public void update(DataCollectionEntity dataCollectionEntity);

    public void delete(DataCollectionEntity dataCollectionEntity);

    public List<DataCollectionEntity> pageDataCollectionList(DataCollectionEntity dataCollectionEntity);

    public List<DataCollectionEntity> pageMyCase(DataCollectionEntity dataCollectionEntity);

}
