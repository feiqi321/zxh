package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataOpLog;

import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
public interface DataLogService {

    public void updateDataLog(DataOpLog log);

    public void delDataLog(DataOpLog log);

    public List<DataOpLog> listDataOpLog(DataOpLog log);

}
