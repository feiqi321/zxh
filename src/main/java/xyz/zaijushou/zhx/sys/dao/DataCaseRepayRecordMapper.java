package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;

import java.util.List;

@Mapper
public interface DataCaseRepayRecordMapper {
    List<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity);
}
