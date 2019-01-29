package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
@Mapper
public interface DataCollectionMapper {

    public void deleteCollect(DataCollectionEntity entity);

    public List<DataCollectionEntity> pageDataCollect(DataCollectionEntity bean);

}
