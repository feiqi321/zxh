package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 * Created by looyer on 2019/1/25.
 */
@Mapper
public interface DataCollectionMapper {

    public int saveCollection(DataCollectionEntity bean);

    public void updateCollection(DataCollectionEntity bean);

    public void deleteById(Integer id);

    public List<DataCollectionEntity> pageDataCollection(DataCollectionEntity bean);

    public void deleteCollect(DataCollectionEntity entity);

    public List<DataCollectionEntity> pageDataCollect(DataCollectionEntity bean);

    public List<CollectionStatistic> statisticsCollectionState(CollectionStatistic bean);
    public List<CollectionStatistic> statisticsCollectionBatch(CollectionStatistic bean);
    public List<DataCollectionEntity> pageStatisticsCollectionPay(CollectionStatistic bean);
    public CollectionStatistic statisticsCollectionPayLastM(CollectionStatistic bean);
    public CollectionStatistic statisticsCollectionPayThisM(CollectionStatistic bean);
}
