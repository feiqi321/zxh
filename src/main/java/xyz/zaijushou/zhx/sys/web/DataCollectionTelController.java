package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;
import xyz.zaijushou.zhx.sys.service.DataCollectionTelService;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("统计报表")
@RestController
@RequestMapping(value = "/statistics/collection")
public class DataCollectionTelController {

    @Autowired
    private DataCollectionTelService dataCollectionTelService;

    @ApiOperation(value = "电催员电催单日统计", notes = "电催员电催单日统计")
    @PostMapping("/day")
    public Object getCollectionDay(@RequestBody CollectionStatistic bean) {

        PageInfo<StatisticReturn> list = dataCollectionTelService.pageCollectionDay(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "电催员电催月度统计", notes = "电催员电催月度统计")
    @PostMapping("/month")
    public Object getCollectionMonth(@RequestBody CollectionStatistic bean) {

        PageInfo<StatisticReturn> list = dataCollectionTelService.pageCollectionMonth(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "电催员每日动作统计", notes = "电催员每日动作统计")
    @PostMapping("/day/action")
    public Object getCollectionDayAction(@RequestBody CollectionStatistic bean) {

        PageInfo<CollectionStatistic> list = dataCollectionTelService.pageCollectionDayAction(bean);
        return WebResponse.success(list);
    }

}
