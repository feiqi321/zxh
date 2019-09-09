package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;
import xyz.zaijushou.zhx.sys.entity.OdvPercentage;

import java.util.List;

public interface DataCaseRepayRecordService {
    PageInfo<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity);

    List<DataCaseRepayRecordEntity> showRepay(OdvPercentage entity);

    void revoke(DataCaseRepayRecordEntity entity);

    void save(DataCaseRepayRecordEntity entity);

    List<DataCaseRepayRecordEntity> listRepayRecord(DataCaseRepayRecordEntity repayRecordEntity);

    List<DataCaseRepayRecordEntity> listRepayRecordSelectExport(DataCaseRepayRecordEntity repayRecordEntity);

    public List<DataCaseRepayRecordEntity> listRepayRecordExport(DataCaseRepayRecordEntity repayRecordEntity);

    void addList(List<DataCaseRepayRecordEntity> dataEntities);

    DataCaseRepayRecordEntity culateSum(DataCaseRepayRecordEntity entity);

    DataCaseRepayRecordEntity queryOneRecord(Integer id);

    void updateRecord(DataCaseRepayRecordEntity entity);
}
