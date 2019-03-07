package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataOpLog;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.FileManageService;

import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
@Api("案件详情/操作日志")
@RestController
public class DataLogController {

    @Autowired
    private DataLogService dataLogService;

    @ApiOperation(value = "刪除日志信息", notes = "刪除日志信息")
    @PostMapping("/dataLog/delete")
    public Object delete(@RequestBody DataOpLog log) {

        dataLogService.delDataLog(log);
        return WebResponse.success();

    }

    @ApiOperation(value = "新增日志信息", notes = "新增日志信息")
    @PostMapping("/dataLog/add")
    public Object add(@RequestBody DataOpLog log) {

        dataLogService.saveDataLog(log);
        return WebResponse.success();

    }

    @ApiOperation(value = "修改日志信息", notes = "修改日志信息")
    @PostMapping("/dataLog/update")
    public Object update(@RequestBody DataOpLog log) {

        dataLogService.updateDataLog(log);
        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataLog/pageDataLog")
    public Object pageDataLog(@RequestBody DataOpLog log) {

        List<DataOpLog> list = dataLogService.listDataOpLog(log);
        return WebResponse.success(list);
    }

}
