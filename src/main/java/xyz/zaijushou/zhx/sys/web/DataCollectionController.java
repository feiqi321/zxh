package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CollectionReturnEntity;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
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

        CollectionReturnEntity list = dataCollectionService.pageMyCase(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "催收管理-单日电催量", notes = "催收管理-单日电催量")
    @PostMapping("/dataCollection/statistics/day")
    public Object statisticsCollectionDay(@RequestBody CollectionStatistic bean) {

        List<CollectionStatistic> list = dataCollectionService.statisticsCollectionDay(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "催收管理-催收状况", notes = "催收管理-催收状况")
    @PostMapping("/dataCollection/statistics/state")
    public Object statisticsCollectionState(@RequestBody CollectionStatistic bean) {

        List<CollectionStatistic> list = dataCollectionService.statisticsCollectionState(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "催收管理-批次分类", notes = "催收管理-批次分类")
    @PostMapping("/dataCollection/statistics/batch")
    public Object statisticsCollectionBatch(@RequestBody CollectionStatistic bean) {

        List<CollectionStatistic> list = dataCollectionService.statisticsCollectionBatch(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "催收管理-我的还款统计", notes = "催收管理-我的还款统计")
    @PostMapping("/dataCollection/statistics/pay")
    public Object statisticsCollectionPay(@RequestBody CollectionStatistic bean) {

        List<CollectionStatistic> list = dataCollectionService.statisticsCollectionPay(bean);

        return WebResponse.success(list);

    }



}
