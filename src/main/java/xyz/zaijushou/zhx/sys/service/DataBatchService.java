package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataBatchService {

    public void save(DataBatchEntity bean);

    public void update(DataBatchEntity bean);

    public void returnCase(DataBatchEntity bean);

    public void recoverCase(DataBatchEntity bean);

    public void deleteById(DataBatchEntity bean);

    public WebResponse pageDataBatch(DataBatchEntity bean);

    public WebResponse totalDataBatch(DataBatchEntity bean);

    public WebResponse selectDataBatch(int[] ids);

    public DataBatchEntity getDataById(DataBatchEntity bean);

    public WebResponse selectBatchNo(DataBatchEntity bean);

    public WebResponse pageDataBatchExport(DataBatchEntity bean);

}
