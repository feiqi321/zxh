package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/2/25.
 *
 */
@Mapper
public interface ReduceMapper {

    public void updateStatus(DataCollectionEntity bean);

    public DataCollectionEntity findById(DataCollectionEntity bean);


    public List<DataCollectionEntity> pageReduceApply(DataCollectionEntity bean);

    public List<DataCollectionEntity> pageReduceApplyExport(DataCollectionEntity bean);

    public List<DataCollectionEntity> totalExport(DataCollectionEntity bean);

    public void saveReduceApply(DataCollectionEntity bean);

    public void updateReduceApply(DataCollectionEntity bean);

    public void updateApplyStatus(DataCollectionEntity bean);

}
