package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataBatchService {

    public void save(DataBatchEntity bean);

    public void update(DataBatchEntity bean);

    public void deleteById(DataBatchEntity bean);

    public List<DataBatchEntity> pageDataBatch(DataBatchEntity bean);

    public DataBatchEntity getDataById(DataBatchEntity bean);

}
