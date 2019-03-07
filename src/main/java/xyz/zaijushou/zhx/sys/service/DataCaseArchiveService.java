package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataCaseArchive;

import java.util.List;

/**
 * Created by looyer on 2019/3/7.
 */
public interface DataCaseArchiveService {

    public List<DataCaseArchive> listByCaseId(DataCaseArchive bean);

    public void save(DataCaseArchive bean);

}
