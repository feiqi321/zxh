package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysModuleConf;
import xyz.zaijushou.zhx.sys.service.SysModuleConfService;

/**
 * Created by looyer on 2019/2/14.
 */
@Api("模板配置")
@RestController
public class SysModuleConfController {

    @Autowired
    private SysModuleConfService sysModuleConfService;

    @ApiOperation(value = "模板配置-配置项", notes = "模板配置-配置项")
    @PostMapping("/sys/module/list")
    public Object findAllConf(@RequestBody SysModuleConf bean) {

        WebResponse webResponse = sysModuleConfService.findAllConf(bean);
        return webResponse;
    }

}
