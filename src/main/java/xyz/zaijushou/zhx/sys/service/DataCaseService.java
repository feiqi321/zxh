package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataCase;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCaseService {

    public void save(DataCase bean);

    public void update(DataCase bean);

    public void deleteById(DataCase bean);

    public List<DataCase> findAll(DataCase bean);

    public DataCase getDataById(DataCase bean);

}
