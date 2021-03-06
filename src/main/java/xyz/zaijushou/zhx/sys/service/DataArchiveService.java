package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataArchiveEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataArchiveService {

    public void save(DataArchiveEntity dataArchiveEntity);

    public void update(DataArchiveEntity dataArchiveEntity);

    public void delete(DataArchiveEntity dataArchiveEntity);

    public WebResponse pageDataArchiveList(DataArchiveEntity dataArchiveEntity);

    public WebResponse detail(DataArchiveEntity dataArchiveEntity);

    public WebResponse findAll();

}
