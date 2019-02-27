package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataReduceEntity;

import java.util.List;

/**
 * Created by looyer on 2019/2/25.
 *
 */
@Mapper
public interface ReduceMapper {

    public void saveReduce(DataCollectionEntity bean);

    public void updateReduce(DataCollectionEntity bean);

    public void updateStatus(DataCollectionEntity bean);

    public DataCollectionEntity findById(DataCollectionEntity bean);

    public List<DataCollectionEntity> pageReduce(DataCollectionEntity bean);

    public void saveReduceApply(DataReduceEntity bean);

    public void updateReduceApply(DataReduceEntity bean);

    public void updateApplyStatus(DataReduceEntity bean);

    public DataReduceEntity findApplyById(DataReduceEntity bean);
}
