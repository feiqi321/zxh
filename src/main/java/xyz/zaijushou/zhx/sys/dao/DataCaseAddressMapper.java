package xyz.zaijushou.zhx.sys.dao;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCaseAddressMapper {

    public void saveAddress(DataCaseAddressEntity entity);

    public void saveBatchAddress(List<DataCaseAddressEntity> list);

    public void updateAddress(DataCaseAddressEntity entity);

    public void deleteAddress(DataCaseAddressEntity entity);

    public List<DataCaseAddressEntity> findAll(DataCaseAddressEntity entity);

    public void updateCaseAddressStatus(DataCaseAddressEntity bean);

    public void updateLetterCount(DataCaseAddressEntity bean);
}
