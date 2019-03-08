package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CollectionReturnEntity;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCollectionService {

    public void save(DataCollectionEntity dataCollectionEntity);

    public void detailSave(DataCollectionEntity dataCollectionEntity);

    public void update(DataCollectionEntity dataCollectionEntity);

    public void updateCollection(DataCollectionEntity dataCollectionEntity);

    public void delete(DataCollectionEntity dataCollectionEntity);

    public List<DataCollectionEntity> pageDataCollectionList(DataCollectionEntity dataCollectionEntity);

    public WebResponse pageMyCase(DataCollectionEntity dataCollectionEntity);

    public List<DataCollectionEntity> listCaseBatchIdNo(DataCollectionEntity dataCollectionEntity);

    public List<CollectionStatistic> statisticsCollectionDay(CollectionStatistic dataCollectionEntity);

    public WebResponse pageStatisticsCollectionState(CollectionStatistic dataCollectionEntity);

    public WebResponse pageStatisticsCollectionBatch(CollectionStatistic dataCollectionEntity);

    public WebResponse pageStatisticsCollectionPay(CollectionStatistic dataCollectionEntity);
}
