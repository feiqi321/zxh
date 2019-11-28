package xyz.zaijushou.zhx.sys.service;

import java.util.List;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.OdvPercentage;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCollectionService {

    public void save(DataCollectionEntity dataCollectionEntity) throws Exception;

    public void detailSave(DataCollectionEntity dataCollectionEntity);

    public void detailDel(Integer[] ids);

    public void update(DataCollectionEntity dataCollectionEntity);

    public List<DataCollectionEntity> pageDataCollectionList(DataCollectionEntity dataCollectionEntity);

    public WebResponse pageMyCase(DataCollectionEntity dataCollectionEntity) throws Exception;

    public List<DataCollectionEntity> listCaseBatchIdNo(DataCollectionEntity dataCollectionEntity);

    public List<CollectionStatistic> statisticsCollectionDay(CollectionStatistic dataCollectionEntity);

    public WebResponse statisticsCollectionState(CollectionStatistic dataCollectionEntity);

    public WebResponse statisticsCollectionBatch(CollectionStatistic dataCollectionEntity);

    public WebResponse statisticsCollectionPay(CollectionStatistic dataCollectionEntity) throws Exception;

    public WebResponse loadDataOdv();

    public WebResponse showOdv(OdvPercentage bean);

    public WebResponse loadDataManage();

    public void calRoyalti(Integer caseId,Integer userId);

    public void calRoyaltiManage(Integer caseId,Integer userId);

}
