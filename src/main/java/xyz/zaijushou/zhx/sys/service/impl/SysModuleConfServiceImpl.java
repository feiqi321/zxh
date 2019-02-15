package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.SysModuleConfMapper;
import xyz.zaijushou.zhx.sys.entity.SysModuleConf;
import xyz.zaijushou.zhx.sys.service.SysModuleConfService;

import javax.annotation.Resource;

/**
 * Created by looyer on 2019/2/14.
 */
@Service
public class SysModuleConfServiceImpl implements SysModuleConfService {
    @Resource
    private SysModuleConfMapper sysModuleConfMapper;

    public WebResponse findAllConf(SysModuleConf sysModuleConf){

        return WebResponse.success(sysModuleConfMapper.findAllConf(sysModuleConf));
    }

}
