package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.DataOpLog;

import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
public interface DataOpLogMapper {

    public List<DataOpLog> listDataOpLog(DataOpLog dataOpLog);

    public List<DataOpLog> listAddressDataOpLog(DataOpLog dataOpLog);

    public void saveDataLog(DataOpLog dataOpLog);

    public void updateDataLog(DataOpLog dataOpLog);

    public void delDataLog(DataOpLog dataOpLog);

}
