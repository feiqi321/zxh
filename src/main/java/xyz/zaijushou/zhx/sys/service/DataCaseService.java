package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCaseService {

    public void save(DataCaseEntity bean);

    public void update(DataCaseEntity bean);

    public void deleteById(DataCaseEntity bean);

    public List<DataCaseEntity> findAll(DataCaseEntity bean);

    public DataCaseEntity getDataById(DataCaseEntity bean);

}
