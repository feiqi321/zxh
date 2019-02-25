package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

/**
 * Created by looyer on 2019/2/25.
 */
public interface ReduceService {

    public PageInfo<DataCollectionEntity> pageReduce(DataCollectionEntity bean);

    public void saveReduce(DataCollectionEntity bean);

    public void updateReduce(DataCollectionEntity bean);

    public DataCollectionEntity findById(DataCollectionEntity bean);

    public void updateStatus(DataCollectionEntity bean);
}