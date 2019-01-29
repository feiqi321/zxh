package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCaseRepayMapper {

    public void saveRepay(DataCaseRepayEntity entity);

    public void deleteRepay(DataCaseRepayEntity entity);

    public List<DataCaseRepayEntity> findAll(DataCaseRepayEntity entity);

}
