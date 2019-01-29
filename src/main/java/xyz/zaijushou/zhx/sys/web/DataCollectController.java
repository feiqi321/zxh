package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectService;

import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
@Api("数据管理/催收管理")
@RestController
public class DataCollectController {

    @Autowired
    private DataCollectService dataCollectService;

    @ApiOperation(value = "刪除催收信息", notes = "刪除催收信息")
    @PostMapping("/dataCollect/delete")
    public Object delete(@RequestBody List<DataCollectionEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCollectionEntity bean = list.get(i);
            dataCollectService.delete(bean);
        }


        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataCollect/pageDataCollect")
    public Object pageDataCase(@RequestBody DataCollectionEntity bean) {

        List<DataCollectionEntity> list = dataCollectService.pageDataCollect(bean);
        return WebResponse.success(list);
    }

}
