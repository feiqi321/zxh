package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataBatchMapper;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.service.DataBatchService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataBatchServiceImpl implements DataBatchService {

    @Resource
    private DataBatchMapper dataCaseMapper;

    public void save(DataBatchEntity bean){
        dataCaseMapper.saveBatch(bean);
    }

    public void update(DataBatchEntity bean){
        dataCaseMapper.updateBatch(bean);
    }

    public void deleteById(DataBatchEntity bean){
        dataCaseMapper.deleteById(bean.getId());
    }


    public List<DataBatchEntity> findAll(DataBatchEntity bean){
        List<DataBatchEntity> dataCaseEntities = dataCaseMapper.pageDataBatch(bean);
        return dataCaseEntities;
    }

    public DataBatchEntity getDataById(DataBatchEntity bean){
        return dataCaseMapper.selectBatchById(bean.getId());
    }

}
