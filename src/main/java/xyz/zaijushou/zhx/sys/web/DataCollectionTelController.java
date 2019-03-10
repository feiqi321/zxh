package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelDayActionExportConstant;
import xyz.zaijushou.zhx.constant.ExcelDayExportConstant;
import xyz.zaijushou.zhx.constant.ExcelMonthExportConstant;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;
import xyz.zaijushou.zhx.sys.service.DataCollectionTelService;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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


    @ApiOperation(value = "电催员电催单日统计导出", notes = "电催员电催单日统计导出")
    @PostMapping("/day/export")
    public Object collectionDayExport(@RequestBody CollectionStatistic bean, HttpServletResponse response)throws IOException, InvalidFormatException {

        PageInfo<StatisticReturn> pageInfo = dataCollectionTelService.pageCollectionDay(bean);
        List<StatisticReturn>  list = pageInfo.getList();
        if (StringUtils.isEmpty(list)){
            return null;
        }
        List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
        for (StatisticReturn info : list){
            CollectionStatistic col = new CollectionStatistic();
            col.setOdv(info.getOdv());
            col.setTimeArea1(info.getList().get(0).getArea());
            col.setCountPhoneNum1(info.getList().get(0).getCountPhoneNum());
            col.setCountConPhoneNum1(info.getList().get(0).getCountConPhoneNum());
            col.setCountCasePhoneNum1(info.getList().get(0).getCountCasePhoneNum());

            col.setTimeArea2(info.getList().get(1).getArea());
            col.setCountPhoneNum2(info.getList().get(1).getCountPhoneNum());
            col.setCountConPhoneNum2(info.getList().get(1).getCountConPhoneNum());
            col.setCountCasePhoneNum2(info.getList().get(1).getCountCasePhoneNum());

            col.setTimeArea3(info.getList().get(2).getArea());
            col.setCountPhoneNum3(info.getList().get(2).getCountPhoneNum());
            col.setCountConPhoneNum3(info.getList().get(2).getCountConPhoneNum());
            col.setCountCasePhoneNum3(info.getList().get(2).getCountCasePhoneNum());

            col.setSumConPhoneNum(info.getSumConPhoneNum());
            col.setSumPhoneNum(info.getSumPhoneNum());
            col.setSumCasePhoneNum(info.getSumCasePhoneNum());
            colList.add(col);
        }
        ExcelUtils.exportExcel(colList,
                ExcelDayExportConstant.CollecionList.values(),
                "day-export-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "电催员电催月度统计", notes = "电催员电催月度统计")
    @PostMapping("/month")
    public Object getCollectionMonth(@RequestBody CollectionStatistic bean) {

        PageInfo<StatisticReturn> list = dataCollectionTelService.pageCollectionMonth(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "电催员电催月统计导出", notes = "电催员电催月统计导出")
    @PostMapping("/month/export")
    public Object collectionMonthExport(@RequestBody CollectionStatistic bean, HttpServletResponse response)throws IOException, InvalidFormatException {

        PageInfo<StatisticReturn> pageInfo = dataCollectionTelService.pageCollectionMonth(bean);
        List<StatisticReturn>  list = pageInfo.getList();
        if (StringUtils.isEmpty(list)){
            return null;
        }
        List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
        for (StatisticReturn info : list){
            for (CollectionStatistic sta : info.getList()){
                CollectionStatistic col = new CollectionStatistic();
                col.setOdv(info.getOdv());
                col.setTimeArea(sta.getArea());
                col.setCountConPhoneNum(sta.getCountConPhoneNum());
                col.setCountPhoneNum(sta.getCountPhoneNum());
                col.setCountCasePhoneNum(sta.getCountCasePhoneNum());
                colList.add(col);
            }

        }
        ExcelUtils.exportExcel(colList,
                ExcelMonthExportConstant.CollecionList.values(),
                "month-export-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "电催员每日动作统计", notes = "电催员每日动作统计")
    @PostMapping("/day/action")
    public Object getCollectionDayAction(@RequestBody CollectionStatistic bean) {

        PageInfo<CollectionStatistic> list = dataCollectionTelService.pageCollectionDayAction(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "电催员每日动作统计导出", notes = "电催员每日动作统计导出")
    @PostMapping("/day/action/export")
    public Object collectionDayActionExport(@RequestBody CollectionStatistic bean, HttpServletResponse response)throws IOException, InvalidFormatException {

        PageInfo<CollectionStatistic> pageInfo = dataCollectionTelService.pageCollectionDayAction(bean);
        List<CollectionStatistic>  list = pageInfo.getList();
        if (StringUtils.isEmpty(list)){
            return null;
        }
        ExcelUtils.exportExcel(list,
                ExcelDayActionExportConstant.CollecionList.values(),
                "每日动作统计导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "统计-查询催收员电催详情", notes = "统计-查询催收员电催详情")
    @PostMapping("/tel/info")
    public Object getCollectionTelInfo(@RequestBody CollectionStatistic bean) {

        PageInfo<CollectionStatistic> list = dataCollectionTelService.pageCollectionTelInfo(bean);
        return WebResponse.success(list);
    }

}
