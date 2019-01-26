package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.DataArchiveAddressEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
public interface DataArchiveAddressMapper {

    public void saveAddress(DataArchiveAddressEntity entity);

    public void deleteAddress(DataArchiveAddressEntity entity);

    public List<DataArchiveAddressEntity> findAll(DataArchiveAddressEntity entity);

}
