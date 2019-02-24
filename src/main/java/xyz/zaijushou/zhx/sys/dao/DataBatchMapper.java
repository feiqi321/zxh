package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Mapper
public interface DataBatchMapper {

    public void saveBatch(DataBatchEntity bean);

    public void updateBatch(DataBatchEntity bean);

    public void returnCase(DataBatchEntity bean);

    public void deleteById(Integer id);

    public List<DataBatchEntity> pageDataBatch(DataBatchEntity bean);

    public List<DataBatchEntity> totalDataBatch(DataBatchEntity bean);

    public List<DataBatchEntity> selectDataBatch(int[] ids);

    public int countDataBatch(DataBatchEntity bean);

    public DataBatchEntity selectBatchById(Integer id);

    public List<DataBatchEntity> listAllDataBatch(DataBatchEntity bean);

    public List<DataBatchEntity> selectBatchNo(DataBatchEntity bean);

}
