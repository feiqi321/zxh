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
import xyz.zaijushou.zhx.constant.ExcelBatchConstant;
import xyz.zaijushou.zhx.constant.ExcelBatchExportConstant;
import xyz.zaijushou.zhx.constant.ExcelCollectConstant;
import xyz.zaijushou.zhx.constant.ExcelCollectExportConstant;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.DataBatchService;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/案件导入")
@RestController
public class DataBatchController {

    @Autowired
    private DataBatchService dataCaseService;
    @Autowired
    private DataCaseService caseService;
    @Autowired
    private DataCollectService dataCollectService;

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
        String[] batchNos = new String[list.size()];
        for(int i=0;i<list.size();i++){
            DataBatchEntity temp = list.get(i);
            batchNos[i]=temp.getBatchNo();
        }
        List<DataCaseEntity> tempList = caseService.listByBatchNos(batchNos);
        if (tempList.size()>0){
            return WebResponse.error("500","此批次下有关联案件,不能删除!");
        }

        for (int i=0;i<list.size();i++){
            DataBatchEntity dataBatchEntity = list.get(i);
            dataCaseService.deleteById(dataBatchEntity);
        }


        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataBatch/pageDataCase")
    public Object pageDataCase(@RequestBody DataBatchEntity dataBatchEntity) {

        WebResponse webResponse = dataCaseService.pageDataBatch(dataBatchEntity);
        return webResponse;

    }


    @ApiOperation(value = "批次编号下拉查询", notes = "批次编号下拉查询")
    @PostMapping("/dataBatch/selectBatchNo")
    public Object selectBatchNo(@RequestBody DataBatchEntity dataBatchEntity) {

        WebResponse webResponse = dataCaseService.selectBatchNo(dataBatchEntity);
        return webResponse;

    }


    @ApiOperation(value = "分頁查询导出", notes = "分頁查询导出")
    @PostMapping("/dataBatch/pageDataBatchExport")
    public Object pageDataBatchExport(@RequestBody DataBatchEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {

        WebResponse webResponse = dataCaseService.pageDataBatchExport(bean);
        List<DataBatchEntity> list = (List<DataBatchEntity>) webResponse.getData();
        ExcelUtils.exportExcel(list,
                ExcelBatchExportConstant.BatchMemorize.values(),
                "批次管理当前页导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所有", notes = "查询导出所有")
    @PostMapping("/dataBatch/totalDataBatchExport")
    public Object totalDataBatchExport(@RequestBody DataBatchEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {

        WebResponse webResponse = dataCaseService.totalDataBatch(bean);
        List<DataBatchEntity> list = (List<DataBatchEntity>) webResponse.getData();
        ExcelUtils.exportExcel(list,
                ExcelBatchExportConstant.BatchMemorize.values(),
                "批次管理全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选", notes = "查询导出所选")
    @PostMapping("/dataBatch/selectDataBatchExport")
    public Object selectDataBatchExport(@RequestBody List<DataBatchEntity> list, HttpServletResponse response) throws IOException, InvalidFormatException {

        int [] ids = new int[list.size()];
        for(int i=0;i<list.size();i++){
            DataBatchEntity dataBatchEntity = list.get(i);
            ids[i] = dataBatchEntity.getId();
        }
        WebResponse webResponse = dataCaseService.selectDataBatch(ids);
        List<DataBatchEntity> resultList = (List<DataBatchEntity>) webResponse.getData();
        ExcelUtils.exportExcel(resultList,
                ExcelBatchExportConstant.BatchMemorize.values(),
                "批次管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "查询导出所选批次的催收记录", notes = "查询导出所选批次的催收记录")
    @PostMapping("/dataBatch/selectDataCollectExportByBatch")
    public Object selectDataCollectExportByBatch(@RequestBody List<DataBatchEntity> list, HttpServletResponse response) throws IOException, InvalidFormatException {
        String[] batchs = new String[list.size()];
        for(int i=0;i<list.size();i++){
            DataBatchEntity temp = list.get(i);
            batchs[i]=temp.getBatchNo();
        }
        WebResponse webResponse = dataCollectService.selectDataCollectExportByBatch(batchs);
        List<DataCollectionEntity> resultList = (List<DataCollectionEntity>) webResponse.getData();
        ExcelUtils.exportExcel(resultList,
                ExcelCollectExportConstant.CaseCollectExport.values(),
                "批次管理催收记录选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }
}
