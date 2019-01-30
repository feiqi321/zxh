package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseService;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/案件管理")
@RestController
public class DataCaseController {

    @Autowired
    private DataCaseService dataCaseService;

    @ApiOperation(value = "新增案件", notes = "新增案件")
    @PostMapping("/dataCase/save")
    public Object save(@RequestBody DataCaseEntity bean) {

        dataCaseService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改案件", notes = "修改案件")
    @PostMapping("/dataCase/update")
    public Object update(@RequestBody DataCaseEntity bean) {

        dataCaseService.update(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "刪除案件", notes = "刪除案件")
    @PostMapping("/dataCase/delete")
    public Object delete(@RequestBody List<DataCaseEntity> list) {
        for(int i=0;i<list.size();i++){
            DataCaseEntity bean = list.get(i);
            dataCaseService.delete(bean);
        }


        return WebResponse.success();

    }

    @ApiOperation(value = "数据模块-案件管理分頁查询", notes = "数据模块-案件管理分頁查询")
    @PostMapping("/dataCase/pageCaseList")
    public Object pageCaseList(@RequestBody DataCaseEntity bean) {

        List<DataCaseEntity> list = dataCaseService.pageCaseList(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataCase/pageDataCase")
    public Object pageDataCase(@RequestBody DataCaseEntity bean) {

        List<DataCaseEntity> list = dataCaseService.pageDataCaseList(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "催收管理-分頁查询", notes = "催收管理-分頁查询")
    @PostMapping("/dataCase/case/pageCaseInfo")
    public Object pageCaseInfo(@RequestBody DataCaseEntity bean) {

        List<DataCaseEntity> list = dataCaseService.pageCaseInfoList(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "催收管理-统计", notes = "催收管理-统计")
    @PostMapping("/dataCase/case/sumCaseMoney")
    public Object sumCaseMoney(@RequestBody DataCaseEntity bean) {

        //List<DataCaseEntity> list = dataCaseService.pageCaseInfoList(bean);
        dataCaseService.sumCaseMoney(bean);

        return WebResponse.success();
    }
}
