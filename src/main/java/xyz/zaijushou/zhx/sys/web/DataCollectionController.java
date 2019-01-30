package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/催收管理")
@RestController
public class DataCollectionController {

    @Autowired
    private DataCollectionService dataCollectionService;

    @ApiOperation(value = "新增催收", notes = "新增催收")
    @PostMapping("/dataCollection/save")
    public Object save(@RequestBody DataCollectionEntity bean) {

        dataCollectionService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改催收", notes = "修改催收")
    @PostMapping("/dataCollection/update")
    public Object update(@RequestBody DataCollectionEntity bean) {

        dataCollectionService.update(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "刪除催收", notes = "刪除催收")
    @PostMapping("/dataCollection/delete")
    public Object delete(@RequestBody  List<DataCollectionEntity> list) {
        for(int i=0;i<list.size();i++){
            DataCollectionEntity bean = list.get(i);
            dataCollectionService.delete(bean);
        }


        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataCollection/pageDataCollection")
    public Object pageDataCollection(@RequestBody DataCollectionEntity bean) {

        List<DataCollectionEntity> list = dataCollectionService.pageDataCollectionList(bean);
        return WebResponse.success(list);

    }

    @ApiOperation(value = "催收管理-我的案件分頁查询", notes = "催收管理-我的案件分頁查询")
    @PostMapping("/dataCollection/pageMyCase")
    public Object pageMyCase(@RequestBody DataCollectionEntity bean) {

        List<DataCollectionEntity> list = dataCollectionService.pageMyCase(bean);
        return WebResponse.success(list);

    }

}
