package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.*;

import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 *
 */
@Mapper
public interface DataCollectionMapper {

    public int saveCollection(DataCollectionEntity bean);

    public int saveBatchCollection(List<DataCollectionEntity> list);

    public int detailSave(DataCollectionEntity bean);

    public int detailUpdate(DataCollectionEntity bean);

    public int detailDel(DataCollectionEntity bean);

    public void updateCollection(DataCollectionEntity bean);

    public void deleteById(Integer id);

    public List<DataCollectionEntity> pageDataCollection(DataCollectionEntity bean);

    public void deleteCollect(DataCollectionEntity entity);

    public List<DataCollectionEntity> pageDataCollect(DataCollectionEntity bean);

    public List<DataCollectionEntity> pageMyCollect(DataCollectionEntity bean);

    public int countMyNewCollect(DataCollectionEntity bean);

    public List<DataCollectionEntity> listDataCollect(DataCollectionEntity bean);

    public void updateDataCollect(DataCollectionEntity bean);

    public List<DataCollectExportEntity> pageDataCollectExport(DataCollectionEntity bean);

    public List<DataCollectExportEntity> totalDataCollect(DataCollectionEntity bean);

    public List<DataCollectExportEntity> selectDataCollectByBatch(DataBatchEntity bean);

    public List<DataCollectExportEntity> selectDataCollectExportByCase(DataCaseEntity bean);


    public List<DataCollectExportEntity> selectDataCollect(DataCollectionEntity bean);

    public int countDataCollect(DataCollectionEntity bean);

    public List<CollectionStatistic> statisticsCollectionState(CollectionStatistic bean);

    public int countStatisticsCollectionState(CollectionStatistic bean);

    public List<CollectionStatistic> statisticsCollectionBatch(CollectionStatistic bean);

    public int countStatisticsCollectionBatch(CollectionStatistic bean);

    public List<DataCollectionEntity> pageStatisticsCollectionPay(CollectionStatistic bean);

    public int countStatisticsCollectionPay(CollectionStatistic bean);

    public CollectionStatistic statisticsCollectionPayM(CollectionStatistic bean);

    public void addColor(DataCollectionEntity dataCollectionEntity);

    public void addCollectStatus(DataCollectionEntity dataCollectionEntity);

    public List<DataCollectionEntity> selectAllByCaseId(DataCollectionEntity dataCollectionEntity);

    public List<DataCollectionEntity> detailCollect1(DataCollectionEntity bean);

    public List<DataCollectionEntity> detailCollect2(DataCollectionEntity bean);

    public List<DataCollectionEntity> detailCollect3(DataCollectionEntity bean);

    public List<DataCollectionEntity> detailCollect4(DataCollectionEntity bean);

    public List<DataCollectionEntity> detailTelCurentCollect1(DataCollectionEntity bean);

    public List<DataCollectionEntity> detailTelCurentCollect2(DataCollectionEntity bean);

    public DataCollectionEntity findById(DataCollectionEntity bean);
}
