package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.entity.DataCase;
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

    public void save(DataCase bean){
        dataCaseMapper.saveCase(bean);
    }

    public void update(DataCase bean){
        dataCaseMapper.updateCase(bean);
    }

    public void deleteById(DataCase bean){
        dataCaseMapper.deleteById(bean.getId());
    }


    public List<DataCase> findAll(DataCase bean){
        List<DataCase> dataCases = dataCaseMapper.pageDataCase(bean);
        return dataCases;
    }

    public DataCase getDataById(DataCase bean){
        return dataCaseMapper.selectCaseById(bean.getId());
    }

}
