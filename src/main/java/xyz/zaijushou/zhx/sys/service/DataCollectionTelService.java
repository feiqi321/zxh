package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestBody;
import xyz.zaijushou.zhx.sys.entity.*;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCollectionTelService {

    public void save(DataCollectionTelEntity bean);

    public PageInfo<StatisticReturn2> pageCollectionDay(CollectionStatistic dataCollectionEntity);

    public PageInfo<StatisticReturn> pageCollectionMonth(CollectionStatistic dataCollectionEntity);

    public PageInfo<CollectionStatistic> pageCollectionDayAction(CollectionStatistic dataCollectionEntity);

    public PageInfo<CollectionStatistic> pageCollectionTelInfo(CollectionStatistic dataCollectionEntity);

    List<CollectionDetailsDTO> pageDetails(CollectionStatistic bean);
}
