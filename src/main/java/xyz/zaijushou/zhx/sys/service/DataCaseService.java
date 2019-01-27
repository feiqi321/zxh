package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCaseService {

    public void save(DataCaseEntity dataCaseEntity);

    public void update(DataCaseEntity dataCaseEntity);

    public void delete(DataCaseEntity dataCaseEntity);

    public List<DataCaseEntity> pageDataCaseList(DataCaseEntity dataCaseEntity);

}
