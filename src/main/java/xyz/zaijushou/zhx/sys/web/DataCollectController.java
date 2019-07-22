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
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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

        WebResponse webResponse = dataCollectService.pageDataCollectExport(bean);
        PageInfo<DataCollectExportEntity> pageInfo = (PageInfo<DataCollectExportEntity>) webResponse.getData();
        ExcelUtils.exportExcel(pageInfo.getList(),
                caseCollectExports2.toArray(new ExcelCollectExportConstant.CaseCollectExport[caseCollectExports2.size()]),
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

        WebResponse webResponse = dataCollectService.totalDataCollect(bean);
       List<DataCollectExportEntity> list = (List<DataCollectExportEntity>) webResponse.getData();

        String fileName = "催记管理全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(list,
                caseCollectExports2.toArray(new ExcelCollectExportConstant.CaseCollectExport[caseCollectExports2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选", notes = "查询导出所选")
    @PostMapping("/dataCollect/selectDataCollectExport")
    public Object selectDataCollectExport(@RequestBody DataCollectionEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        /*int[] ids = new int[list.size()];
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            ids[i] = temp.getId();
        }*/
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

        WebResponse webResponse = dataCollectService.selectDataCollect(bean);

        String fileName = "催记管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List<DataCollectExportEntity> resultList = (List<DataCollectExportEntity>) webResponse.getData();
        ExcelUtils.exportExcel(resultList,
                caseCollectExports2.toArray(new ExcelCollectExportConstant.CaseCollectExport[caseCollectExports2.size()]),
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

        for (int i=0;i<dataCollectionEntities.size();i++){
            DataCollectionEntity temp = dataCollectionEntities.get(i);
            if (org.apache.commons.lang3.StringUtils.isEmpty(temp.getSeqno())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getCardNo()+"@"+temp.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getSeqno(),DataCaseEntity.class);
                if (dataCaseEntity!=null){

                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getResultId())) {
                SysDictionaryEntity resultDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + dataCollectionEntities.get(i).getResultId(), SysDictionaryEntity.class);
                if (resultDic == null) {
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行催收结果id" + dataCollectionEntities.get(i).getResultId() + "不在枚举配置中，并检查excel的催收结果id是否均填写正确");
                } else {
                    dataCollectionEntities.get(i).setResultId(resultDic.getId() + "");
                }
            }
            if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getTelType())) {
                SysDictionaryEntity telTypeDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + dataCollectionEntities.get(i).getTelType(), SysDictionaryEntity.class);
                if (telTypeDic == null) {
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行电话类型" + dataCollectionEntities.get(i).getTelType() + "不在枚举配置中，并检查excel的电话类型是否均填写正确");
                } else {
                    dataCollectionEntities.get(i).setTelType(telTypeDic.getId() + "");
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

    @ApiOperation(value = "案件详情催记", notes = "案件详情催记")
    @PostMapping("/dataCollect/detailCollect2")
    public Object detailCollect2(@RequestBody DataCollectionEntity bean) {

        WebResponse webResponse = dataCollectService.detailCollect2(bean);
        return webResponse;
    }

    @ApiOperation(value = "案件详情电话本案催记", notes = "案件详情电话本案催记")
    @PostMapping("/dataCollect/tel/detailTelCurentCollect")
    public Object detailTelCurentCollect(@RequestBody DataCollectionEntity bean) {

        WebResponse webResponse = dataCollectService.detailTelCurentCollect(bean);
        return webResponse;
    }

}
