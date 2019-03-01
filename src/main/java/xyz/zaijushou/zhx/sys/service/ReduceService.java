package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/2/25.
 */
public interface ReduceService {

    public PageInfo<DataCollectionEntity> pageReduce(DataCollectionEntity bean);

    public List<DataCollectionEntity> listReduce(DataCollectionEntity bean);

    public void saveReduce(DataCollectionEntity bean);

    public void saveReduceInfo(List<DataCollectionEntity> bean);

    public void updateReduce(DataCollectionEntity bean);

    public DataCollectionEntity findById(DataCollectionEntity bean);

    public void updateStatus(DataCollectionEntity bean);

    public PageInfo<DataCollectionEntity> pageReduceApply(DataCollectionEntity bean);

    public void saveReduceApply(DataCollectionEntity bean);

    public DataCollectionEntity findApplyById(DataCollectionEntity bean);

    public void updateApplyStatus(DataCollectionEntity bean);
}
