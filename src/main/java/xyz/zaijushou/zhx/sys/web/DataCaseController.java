package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseService;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/案件导入")
@RestController
public class DataCaseController {

    @Autowired
    private DataCaseService dataCaseService;

    @ApiOperation(value = "新增批次", notes = "新增批次")
    @PostMapping("/dataCase/save")
    public Object save(@RequestBody DataCaseEntity dataCaseEntity) {

        dataCaseService.save(dataCaseEntity);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改批次", notes = "修改批次")
    @PostMapping("/dataCase/update")
    public Object update(@RequestBody DataCaseEntity dataCaseEntity) {

        dataCaseService.update(dataCaseEntity);

        return WebResponse.success();

    }

    @ApiOperation(value = "刪除批次", notes = "刪除批次")
    @PostMapping("/dataCase/update")
    public Object delete(@RequestBody DataCaseEntity dataCaseEntity) {

        dataCaseService.deleteById(dataCaseEntity);

        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataCase/pageDataCase")
    public Object pageDataCase(@RequestBody DataCaseEntity dataCaseEntity) {

        List<DataCaseEntity> list = dataCaseService.findAll(dataCaseEntity);
        return WebResponse.success(list);

    }

}
