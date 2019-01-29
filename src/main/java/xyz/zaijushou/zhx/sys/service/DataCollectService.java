package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
public interface DataCollectService {

    public void delete(DataCollectionEntity bean);

    public List<DataCollectionEntity> pageDataCollect(DataCollectionEntity bean);

}
