package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.Notice;

/**
 * Created by looyer on 2019/6/27.
 */
public interface PointService {

    public WebResponse myCount();

    public void save(Notice notice);

    public void update(Notice notice);

    public void delete(Notice notice);

    public WebResponse pageList(Notice notice);

    public WebResponse personPageList();

}
