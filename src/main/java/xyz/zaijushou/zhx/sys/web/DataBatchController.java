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
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.service.DataBatchService;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    private SysOperationLogService sysOperationLogService;

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

    @ApiOperation(value = "恢复", notes = "恢复")
    @PostMapping("/dataBatch/recoverCase")
    public Object recoverCase(@RequestBody List<DataBatchEntity> list) {
        for (int i=0;i<list.size();i++){
            DataBatchEntity dataBatchEntity = list.get(i);

            dataCaseService.recoverCase(dataBatchEntity);
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

    @ApiOperation(value = "根据批次号查询委托方", notes = "根据批次号查询委托方")
    @PostMapping("/dataBatch/listClients")
    public Object listClients(@RequestBody DataBatchEntity dataBatchEntity) {

        WebResponse webResponse = dataCaseService.listClients(dataBatchEntity);
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

        List exportKeyList = new ArrayList();
        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelBatchExportConstant.BatchMemorizeConf batchExportConf = ExcelBatchExportConstant.BatchMemorizeConf.getEnumByKey(entry.getKey().toString());
                if (batchExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(batchExportConf.getAttr())) {
                    exportKeyList.add(batchExportConf.getAttr());
                }
                colMap.put(batchExportConf.getCol(), batchExportConf.getCol());
            }
        }

        ExcelBatchExportConstant.BatchMemorize batchMemorizes[]= ExcelBatchExportConstant.BatchMemorize.values();
        List<ExcelBatchExportConstant.BatchMemorize> batchMemorizes2 = new ArrayList<ExcelBatchExportConstant.BatchMemorize>();

        for (int i=0;i<batchMemorizes.length;i++){
            ExcelBatchExportConstant.BatchMemorize batchListTemp = batchMemorizes[i];
            if (colMap.get(batchListTemp.getCol())!=null){
                batchMemorizes2.add(batchListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);

        WebResponse webResponse = dataCaseService.pageDataBatchExport(bean);
        List<DataBatchEntity> list = (List<DataBatchEntity>) webResponse.getData();

        String fileName = "批次管理当前页导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(list,
                batchMemorizes2.toArray(new ExcelBatchExportConstant.BatchMemorize[batchMemorizes2.size()]),
                fileName  + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所有", notes = "查询导出所有")
    @PostMapping("/dataBatch/totalDataBatchExport")
    public Object totalDataBatchExport(@RequestBody DataBatchEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {

        List exportKeyList = new ArrayList();
        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelBatchExportConstant.BatchMemorizeConf batchExportConf = ExcelBatchExportConstant.BatchMemorizeConf.getEnumByKey(entry.getKey().toString());
                if (batchExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(batchExportConf.getAttr())) {
                    exportKeyList.add(batchExportConf.getAttr());
                }
                colMap.put(batchExportConf.getCol(), batchExportConf.getCol());
            }
        }

        ExcelBatchExportConstant.BatchMemorize batchMemorizes[]= ExcelBatchExportConstant.BatchMemorize.values();
        List<ExcelBatchExportConstant.BatchMemorize> batchMemorizes2 = new ArrayList<ExcelBatchExportConstant.BatchMemorize>();

        for (int i=0;i<batchMemorizes.length;i++){
            ExcelBatchExportConstant.BatchMemorize batchListTemp = batchMemorizes[i];
            if (colMap.get(batchListTemp.getCol())!=null){
                batchMemorizes2.add(batchListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);


        WebResponse webResponse = dataCaseService.totalDataBatch(bean);
        List<DataBatchEntity> list = (List<DataBatchEntity>) webResponse.getData();

        String fileName = "批次管理全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(list,
                batchMemorizes2.toArray(new ExcelBatchExportConstant.BatchMemorize[batchMemorizes2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选", notes = "查询导出所选")
    @PostMapping("/dataBatch/selectDataBatchExport")
    public Object selectDataBatchExport(@RequestBody DataBatchEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {


        List exportKeyList = new ArrayList();
        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelBatchExportConstant.BatchMemorizeConf batchExportConf = ExcelBatchExportConstant.BatchMemorizeConf.getEnumByKey(entry.getKey().toString());
                if (batchExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(batchExportConf.getAttr())) {
                    exportKeyList.add(batchExportConf.getAttr());
                }
                colMap.put(batchExportConf.getCol(), batchExportConf.getCol());
            }
        }

        ExcelBatchExportConstant.BatchMemorize batchMemorizes[]= ExcelBatchExportConstant.BatchMemorize.values();
        List<ExcelBatchExportConstant.BatchMemorize> batchMemorizes2 = new ArrayList<ExcelBatchExportConstant.BatchMemorize>();

        for (int i=0;i<batchMemorizes.length;i++){
            ExcelBatchExportConstant.BatchMemorize batchListTemp = batchMemorizes[i];
            if (colMap.get(batchListTemp.getCol())!=null){
                batchMemorizes2.add(batchListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);


        WebResponse webResponse = dataCaseService.selectDataBatch(bean);
        List<DataBatchEntity> resultList = (List<DataBatchEntity>) webResponse.getData();

        String fileName = "批次管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(resultList,
                batchMemorizes2.toArray(new ExcelBatchExportConstant.BatchMemorize[batchMemorizes2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "查询导出所选批次的催收记录", notes = "查询导出所选批次的催收记录")
    @PostMapping("/dataBatch/selectDataCollectExportByBatch")
    public Object selectDataCollectExportByBatch(@RequestBody DataBatchEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {

        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelCollectExportConstant.CaseCollectExportConf caseCollectExportConf = ExcelCollectExportConstant.CaseCollectExportConf.getEnumByKey(entry.getKey().toString());
                if (caseCollectExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(caseCollectExportConf.getAttr())) {
                    exportKeyList.add(caseCollectExportConf.getAttr());
                }
                colMap.put(caseCollectExportConf.getCol(), caseCollectExportConf.getCol());
            }
        }

        ExcelCollectExportConstant.CaseCollectExport caseCollectExports[]= ExcelCollectExportConstant.CaseCollectExport.values();
        List<ExcelCollectExportConstant.CaseCollectExport> caseCollectExports2 = new ArrayList<ExcelCollectExportConstant.CaseCollectExport>();

        for (int i=0;i<caseCollectExports.length;i++){
            ExcelCollectExportConstant.CaseCollectExport caseCollectListTemp = caseCollectExports[i];
            if (colMap.get(caseCollectListTemp.getCol())!=null){
                caseCollectExports2.add(caseCollectListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);

        WebResponse webResponse = dataCollectService.selectDataCollectExportByBatch(bean);
        List<DataCollectionEntity> resultList = (List<DataCollectionEntity>) webResponse.getData();

        String fileName = "批次管理催收记录选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(resultList,
                caseCollectExports2.toArray(new ExcelCollectExportConstant.CaseCollectExport[caseCollectExports2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }
}
