package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;
import xyz.zaijushou.zhx.sys.entity.OdvPercentage;

import java.util.List;

@Mapper
public interface DataCaseRepayRecordMapper {
    List<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity);

    void updateRecordStatus(DataCaseRepayRecordEntity entity);

    void save(DataCaseRepayRecordEntity entity);

    List<DataCaseRepayRecordEntity> listRepayRecord(DataCaseRepayRecordEntity repayRecordEntity);

    List<DataCaseRepayRecordEntity> showRepay(OdvPercentage entity);

    List<DataCaseRepayRecordEntity> listRepayRecordSelectExport(DataCaseRepayRecordEntity repayRecordEntity);

    List<DataCaseRepayRecordEntity> listRepayRecordExport(DataCaseRepayRecordEntity repayRecordEntity);

    void addList(List<DataCaseRepayRecordEntity> dataEntities);

    DataCaseRepayRecordEntity queryCaseSum(DataCaseRepayRecordEntity entity);

    DataCaseRepayRecordEntity queryRepaySum(DataCaseRepayRecordEntity entity);

    List<DataCaseRepayRecordEntity> listBySeqNo(DataCaseRepayRecordEntity record);

    List<DataCaseRepayRecordEntity> listByCaseId(DataCaseRepayRecordEntity record);

    DataCaseRepayRecordEntity findById(DataCaseRepayRecordEntity entity);
}
