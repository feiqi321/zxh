package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionTelEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCollectionTelMapper {

    public void saveTel(DataCollectionTelEntity entity);

    public void deleteTel(DataCollectionTelEntity entity);

    public List<DataCollectionTelEntity> findAll(DataCollectionTelEntity entity);


    public int statisticsCollectionSum(CollectionStatistic bean);
    public int statisticsCollectionCon(CollectionStatistic bean);
    public int statisticsCollectionCase(CollectionStatistic bean);


}
