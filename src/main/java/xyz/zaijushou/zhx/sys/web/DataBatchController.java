package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.service.DataBatchService;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/案件导入")
@RestController
public class DataBatchController {

    @Autowired
    private DataBatchService dataCaseService;

    @ApiOperation(value = "新增批次", notes = "新增批次")
    @PostMapping("/dataBatch/save")
    public Object save(@RequestBody DataBatchEntity dataBatchEntity) {

        dataCaseService.save(dataBatchEntity);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改批次", notes = "修改批次")
    @PostMapping("/dataBatch/update")
    public Object update(@RequestBody DataBatchEntity dataBatchEntity) {

        dataCaseService.update(dataBatchEntity);

        return WebResponse.success();

    }

    @ApiOperation(value = "退案", notes = "退案")
    @PostMapping("/dataBatch/returnCase")
    public Object returnCase(@RequestBody List<DataBatchEntity> list) {
        for (int i=0;i<list.size();i++){
            DataBatchEntity dataBatchEntity = list.get(i);
            dataCaseService.returnCase(dataBatchEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "刪除批次", notes = "刪除批次")
    @PostMapping("/dataBatch/delete")
    public Object delete(@RequestBody List<DataBatchEntity> list) {
        for (int i=0;i<list.size();i++){
            DataBatchEntity dataBatchEntity = list.get(i);
            dataCaseService.deleteById(dataBatchEntity);
        }


        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataBatch/pageDataCase")
    public Object pageDataCase(@RequestBody DataBatchEntity dataBatchEntity) {

        List<DataBatchEntity> list = dataCaseService.pageDataBatch(dataBatchEntity);
        return WebResponse.success(list);

    }

}
