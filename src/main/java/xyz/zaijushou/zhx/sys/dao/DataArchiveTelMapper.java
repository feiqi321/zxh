package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataArchiveAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataArchiveTelEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataArchiveTelMapper {

    public void saveTel(DataArchiveTelEntity entity);

    public void deleteTel(DataArchiveTelEntity entity);

    public List<DataArchiveTelEntity> findAll(DataArchiveTelEntity entity);

}
