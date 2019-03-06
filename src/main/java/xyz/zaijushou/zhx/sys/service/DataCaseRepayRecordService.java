package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;

import java.util.List;

public interface DataCaseRepayRecordService {
    PageInfo<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity);

    void revoke(DataCaseRepayRecordEntity entity);

    void save(DataCaseRepayRecordEntity entity);

    List<DataCaseRepayRecordEntity> listRepayRecord(DataCaseRepayRecordEntity repayRecordEntity);

    void addList(List<DataCaseRepayRecordEntity> dataEntities);
}
