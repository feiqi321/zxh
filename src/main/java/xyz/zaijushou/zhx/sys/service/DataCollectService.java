package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
public interface DataCollectService {

    public void delete(DataCollectionEntity bean);

    public WebResponse pageDataCollect(DataCollectionEntity bean);

    public WebResponse selectAllByCaseId(DataCollectionEntity bean);

    public WebResponse pageDataCollectExport(DataCollectionEntity bean);

    public WebResponse totalDataCollect(DataCollectionEntity bean);

    public WebResponse selectDataCollect(DataCollectionEntity bean);

    public WebResponse selectDataCollectExportByBatch(DataBatchEntity bean);

    public WebResponse selectDataCollectExportByCase(DataCaseEntity bean);

    public WebResponse detailCollect(DataCollectionEntity bean);

    public WebResponse detailTelCurentCollect(DataCollectionEntity bean);

}
