package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCaseServiceImpl implements DataCaseService {

    @Resource
    private DataCaseMapper dataCaseMapper;

    public void save(DataCaseEntity bean){
        dataCaseMapper.saveCase(bean);
    }

    public void update(DataCaseEntity bean){
        dataCaseMapper.updateCase(bean);
    }

    public void deleteById(DataCaseEntity bean){
        dataCaseMapper.deleteById(bean.getId());
    }


    public List<DataCaseEntity> findAll(DataCaseEntity bean){
        List<DataCaseEntity> dataCaseEntities = dataCaseMapper.pageDataCase(bean);
        return dataCaseEntities;
    }

    public DataCaseEntity getDataById(DataCaseEntity bean){
        return dataCaseMapper.selectCaseById(bean.getId());
    }

}
