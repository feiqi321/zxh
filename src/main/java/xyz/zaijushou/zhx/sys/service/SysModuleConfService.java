package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysModuleConf;

/**
 * Created by looyer on 2019/2/14.
 */
public interface SysModuleConfService {

    public WebResponse findAllConf(SysModuleConf sysModuleConf);

}
