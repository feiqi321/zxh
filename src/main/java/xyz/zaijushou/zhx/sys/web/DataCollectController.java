package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelCollectConstant;
import xyz.zaijushou.zhx.constant.ExcelCollectExportConstant;
import xyz.zaijushou.zhx.constant.ExcelInterestConstant;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by looyer on 2019/1/29.
 */
@Api("数据管理/催收管理")
@RestController
public class DataCollectController {

    @Autowired
    private DataCollectService dataCollectService;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private SysDictionaryService dictionaryService;

    @Autowired
    private SysOperationLogService sysOperationLogService;

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

        WebResponse webResponse = dataCollectService.pageDataCollect(bean);
        return webResponse;
    }

    @ApiOperation(value = "分頁查询导出", notes = "分頁查询导出")
    @PostMapping("/dataCollect/pageDataCollectExport")
    public Object pageDataCollectExport(@RequestBody DataCollectionEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        WebResponse webResponse = dataCollectService.pageDataCollectExport(bean);
        PageInfo<DataCollectExportEntity> pageInfo = (PageInfo<DataCollectExportEntity>) webResponse.getData();
        ExcelUtils.exportExcel(pageInfo.getList(),
                ExcelCollectExportConstant.CaseCollectExport.values(),
                "催记管理当前页导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "根据案件id查询协催", notes = "根据案件id查询协催")
    @PostMapping("/dataCollect/selectAllByCaseId")
    public Object selectAllByCaseId(@RequestBody DataCollectionEntity bean) throws IOException, InvalidFormatException {
        WebResponse webResponse = dataCollectService.selectAllByCaseId(bean);
        return webResponse;
    }

    @ApiOperation(value = "查询导出所有", notes = "查询导出所有")
    @PostMapping("/dataCollect/totalDataCollectExport")
    public Object totalDataCollectExport(@RequestBody DataCollectionEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {

        WebResponse webResponse = dataCollectService.totalDataCollect(bean);
       List<DataCollectExportEntity> list = (List<DataCollectExportEntity>) webResponse.getData();

        String fileName = "催记管理全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(list,
                ExcelCollectExportConstant.CaseCollectExport.values(),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选", notes = "查询导出所选")
    @PostMapping("/dataCollect/selectDataCollectExport")
    public Object selectDataCollectExport(@RequestBody List<DataCollectionEntity> list, HttpServletResponse response) throws IOException, InvalidFormatException {
        int[] ids = new int[list.size()];
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            ids[i] = temp.getId();
        }
        WebResponse webResponse = dataCollectService.selectDataCollect(ids);

        String fileName = "催记管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List<DataCollectExportEntity> resultList = (List<DataCollectExportEntity>) webResponse.getData();
        ExcelUtils.exportExcel(resultList,
                ExcelCollectExportConstant.CaseCollectExport.values(),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "催收记录导入", notes = "催收记录导入")
    @PostMapping("/dataCollect/import")
    public Object dataCollectImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List<DataCollectionEntity> dataCollectionEntities = ExcelUtils.importExcel(file, ExcelCollectConstant.CaseCollect.values(), DataCollectionEntity.class);

        SysDictionaryEntity dictionary = new SysDictionaryEntity();
        dictionary.setName("催收结果");
        List<SysDictionaryEntity> dictionaryEntityList = dictionaryService.listDataByName(dictionary);
        Map dicMap = new HashMap();
        for (int m=0;m<dictionaryEntityList.size();m++){
            SysDictionaryEntity temp = dictionaryEntityList.get(m);
            dicMap.put(temp.getId(),temp);
        }

        SysDictionaryEntity telDic = new SysDictionaryEntity();
        telDic.setName("电话类型");
        List<SysDictionaryEntity> dicTelList = dictionaryService.listDataByName(telDic);
        Map dicTelMap = new HashMap();
        for (int m=0;m<dicTelList.size();m++){
            SysDictionaryEntity temp = dicTelList.get(m);
            dicTelMap.put(temp.getName(),temp);
        }
        for (int i=0;i<dataCollectionEntities.size();i++){
            if (StringUtils.notEmpty(dataCollectionEntities.get(i).getResultId())) {
                if (dataCollectionEntities.get(i).getResultId() != null && dicMap.get(Integer.parseInt(dataCollectionEntities.get(i).getResultId())) == null) {
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行催收结果id不正确，请核实后再上传");
                }
            }
            if (StringUtils.notEmpty(dataCollectionEntities.get(i).getTelType())) {
                if (dicTelMap.get(dataCollectionEntities.get(i).getTelType()) == null) {
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行电话类型不正确，请核实后再上传");
                } else {
                    dataCollectionEntities.get(i).setTelType(((SysDictionaryEntity) dicTelMap.get(dataCollectionEntities.get(i).getTelType())).getId() + "");
                }
            }
        }

        WebResponse webResponse =fileManageService.batchCollect(dataCollectionEntities);
        return webResponse;
    }

    @ApiOperation(value = "案件详情催记", notes = "案件详情催记")
    @PostMapping("/dataCollect/detailCollect")
    public Object detailCollect(@RequestBody DataCollectionEntity bean) {

        WebResponse webResponse = dataCollectService.detailCollect(bean);
        return webResponse;
    }

    @ApiOperation(value = "案件详情电话本案催记", notes = "案件详情电话本案催记")
    @PostMapping("/dataCollect/tel/detailTelCurentCollect")
    public Object detailTelCurentCollect(@RequestBody DataCollectionEntity bean) {

        WebResponse webResponse = dataCollectService.detailTelCurentCollect(bean);
        return webResponse;
    }

}
