package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataArchiveEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Mapper
public interface DataArchiveMapper {

    public int saveArchive(DataArchiveEntity bean);

    public int saveBatchArchive(List<DataArchiveEntity> list);

    public void updateArchive(DataArchiveEntity bean);

    public void deleteById(Integer id);

    public List<DataArchiveEntity> pageDataArchive(DataArchiveEntity bean);

    public int countDataArchive(DataArchiveEntity bean);

    public DataArchiveEntity selectById(DataArchiveEntity bean);

    public DataArchiveEntity selectByIdentNo(DataArchiveEntity bean);

    public List<DataArchiveEntity> selectAll();

}
