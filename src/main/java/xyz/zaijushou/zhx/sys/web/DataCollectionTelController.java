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
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectionTelService;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
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
    @Autowired
    private SysOperationLogService sysOperationLogService;

    @ApiOperation(value = "电催员电催单日统计", notes = "电催员电催单日统计")
    @PostMapping("/day")
    public Object getCollectionDay(@RequestBody CollectionStatistic bean) {
        if(StringUtils.isEmpty(bean.getDateSearchStart()) || StringUtils.isEmpty(bean.getDateSearchEnd())){
            return WebResponse.error("500","请输入查询的时间段");
        }
        PageInfo<StatisticReturn> list = dataCollectionTelService.pageCollectionDay(bean);
        return WebResponse.success(list);
    }


    @ApiOperation(value = "电催员电催单日统计导出", notes = "电催员电催单日统计导出")
    @PostMapping("/day/export")
    public Object collectionDayExport(@RequestBody CollectionStatistic bean, HttpServletResponse response)throws IOException, InvalidFormatException {
        if(StringUtils.notEmpty(bean.getDateSearchStart()) && StringUtils.notEmpty(bean.getDateSearchEnd())){
            PageInfo<StatisticReturn> pageInfo = dataCollectionTelService.pageCollectionDay(bean);
            List<StatisticReturn>  list = pageInfo.getList();
            //导出
            String fileName = new String(("day-export").getBytes(), "ISO8859_1");
            String realname = fileName + "-"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
            SysOperationLogEntity operationLog = new SysOperationLogEntity();
            operationLog.setRequestBody(realname);
            operationLog.setUserId(userId);
            sysOperationLogService.insertRequest(operationLog);
            ExcelUtils.exportTempleteDay(response,list,realname);
        }

        return null;
    }

    @ApiOperation(value = "电催员电催月度统计", notes = "电催员电催月度统计")
    @PostMapping("/month")
    public Object getCollectionMonth(@RequestBody CollectionStatistic bean) {
        if(StringUtils.isEmpty(bean.getMonthStart()) || StringUtils.isEmpty(bean.getMonthEnd())){
            return WebResponse.error("500","请输入查询的时间段");
        }
        PageInfo<StatisticReturn> list = dataCollectionTelService.pageCollectionMonth(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "电催员电催月统计导出", notes = "电催员电催月统计导出")
    @PostMapping("/month/export")
    public Object collectionMonthExport(@RequestBody CollectionStatistic bean, HttpServletResponse response)throws IOException, InvalidFormatException {
        List<CollectionStatistic> colList = new ArrayList<CollectionStatistic>();
        if(StringUtils.isEmpty(bean.getMonthStart()) || StringUtils.isEmpty(bean.getMonthEnd())){
            return WebResponse.error("500","请输入查询的时间段");
        }
        if(StringUtils.notEmpty(bean.getMonthStart()) && StringUtils.notEmpty(bean.getMonthEnd())){
            PageInfo<StatisticReturn> pageInfo = dataCollectionTelService.pageCollectionMonth(bean);
            List<StatisticReturn>  list = pageInfo.getList();
           //导出
            String fileName = new String(("day-export").getBytes(), "ISO8859_1");
            String realname = fileName + "-"+ new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
            SysOperationLogEntity operationLog = new SysOperationLogEntity();
            operationLog.setRequestBody(realname);
            operationLog.setUserId(userId);
            sysOperationLogService.insertRequest(operationLog);
            ExcelUtils.exportTempleteMonth(response,list,realname);
        }

//        ExcelUtils.exportExcel(colList,
//                ExcelMonthExportConstant.CollecionList.values(),
//                "month-export-" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
//                response
//        );
        return null;
    }

    @ApiOperation(value = "电催员每日动作统计", notes = "电催员每日动作统计")
    @PostMapping("/day/action")
    public Object getCollectionDayAction(@RequestBody CollectionStatistic bean) {
        if(StringUtils.isEmpty(bean.getDateSearchStart()) || StringUtils.isEmpty(bean.getDateSearchEnd())){
            return WebResponse.error("500","请输入查询的时间段");
        }
        PageInfo<CollectionStatistic> list = dataCollectionTelService.pageCollectionDayAction(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "电催员每日动作统计导出", notes = "电催员每日动作统计导出")
    @PostMapping("/day/action/export")
    public Object collectionDayActionExport(@RequestBody CollectionStatistic bean, HttpServletResponse response)throws IOException, InvalidFormatException {
        List<CollectionStatistic>  list = new ArrayList<CollectionStatistic>();

        if(StringUtils.notEmpty(bean.getDateSearchStart()) && StringUtils.notEmpty(bean.getDateSearchEnd())){
            PageInfo<CollectionStatistic> pageInfo = dataCollectionTelService.pageCollectionDayAction(bean);
            list = pageInfo.getList();
        }


        String realname = "每日动作统计导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(realname);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(list,
                ExcelDayActionExportConstant.CollecionList.values(),
                realname + ".xlsx",
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
