package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.DataArchiveAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataArchiveRemarkEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
public interface DataArchiveRemarkMapper {

    public void saveRemark(DataArchiveRemarkEntity entity);

    public void deleteRemark(DataArchiveRemarkEntity entity);

    public List<DataArchiveRemarkEntity> findAll(DataArchiveRemarkEntity entity);

}
