package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseAddressEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCaseAddressMapper {

    public void saveAddress(DataCaseAddressEntity entity);

    public void deleteAddress(DataCaseAddressEntity entity);

    public List<DataCaseAddressEntity> findAll(DataCaseAddressEntity entity);

}
