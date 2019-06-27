package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CaseTimeAreaEntity;

/**
 * Created by looyer on 2019/1/25.
 */
public interface CaseTimeAreaService {

    public void save(CaseTimeAreaEntity bean);

    public void update(CaseTimeAreaEntity bean);

    public WebResponse findAll();

}
