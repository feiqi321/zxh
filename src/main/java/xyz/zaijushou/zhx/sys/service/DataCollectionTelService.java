package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCollectionTelService {

    public PageInfo<StatisticReturn> pageCollectionDay(CollectionStatistic dataCollectionEntity);

    public PageInfo<StatisticReturn> pageCollectionMonth(CollectionStatistic dataCollectionEntity);

    public PageInfo<CollectionStatistic> pageCollectionDayAction(CollectionStatistic dataCollectionEntity);

    public PageInfo<CollectionStatistic> pageCollectionTelInfo(CollectionStatistic dataCollectionEntity);
}
