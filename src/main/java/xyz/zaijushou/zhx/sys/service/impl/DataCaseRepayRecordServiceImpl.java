package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCaseRepayRecordMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataCaseRepayRecordServiceImpl implements DataCaseRepayRecordService {

    @Resource
    private DataCaseRepayRecordMapper dataCaseRepayRecordMapper;

    @Override
    public PageInfo<DataCaseRepayRecordEntity> pageRepayRecordList(DataCaseRepayRecordEntity entity) {
        List<DataCaseRepayRecordEntity> list = dataCaseRepayRecordMapper.pageRepayRecordList(entity);
        return PageInfo.of(list);
    }

    @Override
    public void revoke(DataCaseRepayRecordEntity entity) {
        entity.setRecordStatus("1");
        dataCaseRepayRecordMapper.updateRecordStatus(entity);
    }

    @Override
    public void save(DataCaseRepayRecordEntity entity) {
        dataCaseRepayRecordMapper.save(entity);
    }

    @Override
    public List<DataCaseRepayRecordEntity> listRepayRecord(DataCaseRepayRecordEntity repayRecordEntity) {
        return dataCaseRepayRecordMapper.listRepayRecord(repayRecordEntity);
    }


}
