package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
public interface DataCollectService {

    public void delete(DataCollectionEntity bean);

    public WebResponse pageDataCollect(DataCollectionEntity bean);

    public WebResponse pageDataCollectExport(DataCollectionEntity bean);

    public WebResponse totalDataCollect(DataCollectionEntity bean);

    public WebResponse selectDataCollect(int[] ids);

    public WebResponse selectDataCollectExportByBatch(String[] batchs);

    public WebResponse selectDataCollectExportByCase(String[] caseIds);

}
