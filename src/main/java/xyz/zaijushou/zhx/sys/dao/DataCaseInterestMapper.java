package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseInterestEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCaseInterestMapper {

    public void saveInterest(DataCaseInterestEntity entity);

    public void deleteInterest(DataCaseInterestEntity entity);

    public List<DataCaseInterestEntity> findAll(DataCaseInterestEntity entity);

}
