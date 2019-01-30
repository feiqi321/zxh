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
    private DataBatchMapper dataBatchMapper;

    public void save(DataBatchEntity bean){
        dataBatchMapper.saveBatch(bean);
    }

    public void update(DataBatchEntity bean){
        dataBatchMapper.updateBatch(bean);
    }

    public void deleteById(DataBatchEntity bean){
        dataBatchMapper.deleteById(bean.getId());
    }


    public List<DataBatchEntity> pageDataBatch(DataBatchEntity bean){
        List<DataBatchEntity> dataCaseEntities = dataBatchMapper.pageDataBatch(bean);
        return dataCaseEntities;
    }

    public DataBatchEntity getDataById(DataBatchEntity bean){
        return dataBatchMapper.selectBatchById(bean.getId());
    }

}
