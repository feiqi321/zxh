package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.CollectionDetailsDTO;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionTelEntity;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCollectionTelMapper {

    public void saveTel(DataCollectionTelEntity entity);

    public void deleteTel(DataCollectionTelEntity entity);

    public void updateCollect(DataCollectionTelEntity entity);

    DataCollectionTelEntity findAll(DataCollectionTelEntity entity);


    public List<CollectionStatistic> statisticsCollectionSum(CollectionStatistic bean);

    public List<CollectionStatistic> statisticsCollectionCon(CollectionStatistic bean);

    public List<CollectionStatistic> statisticsCollectionCase(CollectionStatistic bean);

    public List<StatisticReturn> pageCollectionDay(CollectionStatistic entity);

    public List<StatisticReturn> pageCollectionMonth(CollectionStatistic entity);

    public List<CollectionStatistic> pageCollectionDayAction(CollectionStatistic entity);

    public List<CollectionStatistic> countCollectionDayAction(CollectionStatistic entity);

    public List<CollectionStatistic> pageCollectionTelInfo(CollectionStatistic entity);

    List<CollectionDetailsDTO> pageDetails(CollectionStatistic bean);
}
