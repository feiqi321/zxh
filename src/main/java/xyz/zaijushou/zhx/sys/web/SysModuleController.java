package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelArchiveConstant;
import xyz.zaijushou.zhx.sys.entity.DataArchiveEntity;
import xyz.zaijushou.zhx.sys.entity.SysModule;
import xyz.zaijushou.zhx.sys.service.DataArchiveService;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.SysModuleService;
import xyz.zaijushou.zhx.utils.ExcelUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("系统管理/模板设置")
@RestController
public class SysModuleController {

    @Autowired
    private SysModuleService sysModuleService;

    @ApiOperation(value = "新增模板", notes = "新增模板")
    @PostMapping("/sys/module/save")
    public Object save(@RequestBody SysModule bean) {

        sysModuleService.saveModule(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "保存内容", notes = "保存内容")
    @PostMapping("/sys/module/update")
    public Object update(@RequestBody SysModule bean) {

        sysModuleService.saveContext(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "刪除模板", notes = "刪除模板")
    @PostMapping("/sys/module/delete")
    public Object delete(@RequestBody  SysModule bean) {
        sysModuleService.deleteById(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "查询模板", notes = "查询模板")
    @PostMapping("/sys/module/list")
    public Object list(@RequestBody SysModule bean) {

        List list  = sysModuleService.findAll();
        return WebResponse.success(list);

    }

    @ApiOperation(value = "查询模板详情", notes = "查询模板详情")
    @PostMapping("/sys/module/detail")
    public Object detail(@RequestBody SysModule bean) {

        SysModule sysModule  = sysModuleService.selectModuleById(bean);
        return WebResponse.success(sysModule);

    }

}
