package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCollectionTelService {

    public PageInfo<CollectionStatistic> pageCollectionDay(CollectionStatistic dataCollectionEntity);

    public PageInfo<CollectionStatistic> pageCollectionMonth(CollectionStatistic dataCollectionEntity);

    public PageInfo<CollectionStatistic> pageCollectionDayAction(CollectionStatistic dataCollectionEntity);
}
