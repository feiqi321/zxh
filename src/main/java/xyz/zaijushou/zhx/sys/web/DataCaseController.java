package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.utils.ExcelUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/案件管理")
@RestController
public class DataCaseController {

    private static Logger logger = LoggerFactory.getLogger(DataCaseController.class);

    @Autowired
    private DataCaseService dataCaseService;
    @Autowired
    private FileManageService fileManageService;
    @Autowired
    private DataCollectService dataCollectService;

    @ApiOperation(value = "新增案件", notes = "新增案件")
    @PostMapping("/dataCase/save")
    public Object save(@RequestBody DataCaseEntity bean) {

        dataCaseService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改案件", notes = "修改案件")
    @PostMapping("/dataCase/update")
    public Object update(@RequestBody DataCaseEntity bean) {

        dataCaseService.update(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "刪除案件", notes = "刪除案件")
    @PostMapping("/dataCase/delete")
    public Object delete(@RequestBody List<DataCaseEntity> list) {
        for(int i=0;i<list.size();i++){
            DataCaseEntity bean = list.get(i);
            dataCaseService.delete(bean);
        }


        return WebResponse.success();

    }

    @ApiOperation(value = "数据模块-案件管理分頁查询", notes = "数据模块-案件管理分頁查询")
    @PostMapping("/dataCase/pageCaseList")
    public Object pageCaseList(@RequestBody DataCaseEntity bean) {

        WebResponse webResponse = dataCaseService.pageCaseList(bean);
        return webResponse;
    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataCase/pageDataCase")
    public Object pageDataCase(@RequestBody DataCaseEntity bean) {

        List<DataCaseEntity> list = dataCaseService.pageDataCaseList(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "催收管理-分頁查询", notes = "催收管理-分頁查询")
    @PostMapping("/dataCase/case/pageCaseInfo")
    public Object pageCaseInfo(@RequestBody DataCaseEntity bean) {

        List<DataCaseEntity> list = dataCaseService.pageCaseInfoList(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "催收管理-统计", notes = "催收管理-统计")
    @PostMapping("/dataCase/case/sumCaseMoney")
    public Object sumCaseMoney(@RequestBody DataCaseEntity bean) {

        //List<DataCaseEntity> list = dataCaseService.pageCaseInfoList(bean);
        dataCaseService.sumCaseMoney(bean);

        return WebResponse.success();
    }

    @ApiOperation(value = "分配业务员", notes = "分配业务员")
    @PostMapping("/dataCase/sendOdv")
    public Object sendOdv(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.sendOdv(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "按照查询分配业务员", notes = "按照查询分配业务员")
    @PostMapping("/dataCase/sendOdvByProperty")
    public Object sendOdvByProperty(@RequestBody DataCaseEntity bean) {
        dataCaseService.sendOdvByProperty(bean);
        return WebResponse.success();

    }
    //未退案0/正常1/暂停2/关档3/退档4/全部5
    @ApiOperation(value = "修改案件状态", notes = "修改案件状态")
    @PostMapping("/dataCase/updateStatus")
    public Object updateStatus(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.updateStatus(dataCaseEntity);
        }

        return WebResponse.success();

    }


    @ApiOperation(value = "添加评语", notes = "修改案件状态")
    @PostMapping("/dataCase/addComment")
    public Object addComment(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addComment(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "案件标色", notes = "案件标色")
    @PostMapping("/dataCase/addColor")
    public Object addColor(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addColor(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "修改重要等级", notes = "修改重要等级")
    @PostMapping("/dataCase/addImportant")
    public Object addImportant(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addImportant(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "修改催收状态", notes = "修改催收状态")
    @PostMapping("/dataCase/addCollectStatus")
    public Object addCollectStatus(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addCollectStatus(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "修改催收区域", notes = "修改催收区域")
    @PostMapping("/dataCase/addCollectArea")
    public Object addCollectArea(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addCollectArea(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "修改 M 值系数", notes = "修改 M 值系数")
    @PostMapping("/dataCase/addMValue")
    public Object addMValue(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addMValue(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "申请协催", notes = "申请协催")
    @PostMapping("/dataCase/addSynergy")
    public Object addSynergy(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addSynergy(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "协催状态修改", notes = "协催状态修改")
    @PostMapping("/synergy/updateSynergy")
    public Object updateSynergy(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.updateSynergy(dataCaseEntity);
        }

        return WebResponse.success();

    }


    @ApiOperation(value = "催收管理-主管协催-分頁查询", notes = "催收管理-主管协催-分頁查询")
    @PostMapping("/synergy/synergy/pageSynergyInfo")
    public Object pageSynergyInfo(@RequestBody DataCaseEntity bean) {

        WebResponse webResponse = dataCaseService.pageSynergyInfo(bean);
        return webResponse;
    }


    @ApiOperation(value = "催收管理-来电查询-分頁查询", notes = "催收管理-来电查询-分頁查询")
    @PostMapping("/synergy/synergy/pageCaseTel")
    public Object pageCaseTel(@RequestBody DataCaseEntity bean) {

        WebResponse webResponse = dataCaseService.pageCaseTel(bean);
        return webResponse;
    }


    @ApiOperation(value = "案件电话导入", notes = "案件电话导入")
    @PostMapping("/dataCase/tel/import")
    public Object dataCaseTelImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        logger.info(fileName);
        List<DataCaseTelEntity> caseList = ExcelUtils.importExcel(file, ExcelTelConstant.CaseTel.values(), DataCaseTelEntity.class);
        WebResponse webResponse =fileManageService.batchCaseTel(caseList);
        return webResponse;
    }

    @ApiOperation(value = "案件地址导入", notes = "案件地址导入")
    @PostMapping("/dataCase/address/import")
    public Object dataCaseAddressImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        logger.info(fileName);
        List<DataCaseAddressEntity> addressList = ExcelUtils.importExcel(file, ExcelAddressConstant.CaseAddress.values(), DataCaseAddressEntity.class);
        WebResponse webResponse =fileManageService.batchCaseAddress(addressList);
        return webResponse;
    }

    @ApiOperation(value = "案件利息导入", notes = "案件利息导入")
    @PostMapping("/dataCase/interest/import")
    public Object dataCaseInterestImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        logger.info(fileName);
        List<DataCaseInterestEntity> interestList = ExcelUtils.importExcel(file, ExcelInterestConstant.CaseInterest.values(), DataCaseInterestEntity.class);
        WebResponse webResponse =fileManageService.batchCaseInterest(interestList);
        return webResponse;
    }

    @ApiOperation(value = "案件评语导入", notes = "案件评语导入")
    @PostMapping("/dataCase/comment/import")
    public Object dataCaseCommentImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        logger.info(fileName);
        List<DataCaseEntity> dataCaseEntities = ExcelUtils.importExcel(file, ExcelInterestConstant.CaseInterest.values(), DataCaseEntity.class);
        WebResponse webResponse =fileManageService.batchCaseComment(dataCaseEntities);
        return webResponse;
    }

    @ApiOperation(value = "导入更新案件", notes = "导入更新案件")
    @PostMapping("/dataCase/updateCase/import")
    public Object dataCaseUpdateCaseImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        logger.info(fileName);
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        int cols = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
        List<DataCaseEntity> dataCaseEntities;
        if(Math.abs(ExcelCaseConstant.StandardCase.values().length - cols) <= Math.abs(ExcelCaseConstant.CardLoanCase.values().length - cols)) {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.StandardCase.values(), DataCaseEntity.class);
        } else {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.CardLoanCase.values(), DataCaseEntity.class);
            for(int i = 0; i < dataCaseEntities.size(); i ++) {
                DataCaseEntity entity = dataCaseEntities.get(i);
                if(entity != null && !CollectionUtils.isEmpty(entity.getContacts()) && entity.getContacts().get(0) != null) {
                    dataCaseEntities.get(i).getContacts().get(0).setRelation("配偶");
                }
//                if(entity != null && !CollectionUtils.isEmpty(entity.getContacts()) && entity.getContacts().size() >= 2 && entity.getContacts().get(1) != null) {
//                    dataCaseEntities.get(i).getContacts().get(1).setRelation("担保人");
//                }
            }
        }
        if(dataCaseEntities.size() == 0) {
            return WebResponse.success("更新0条数据");
        }
        Set<String> seqNoSet = new HashSet<>();
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            if(StringUtils.isEmpty(dataCaseEntities.get(i).getSeqNo())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号，请填写后上传，并检查excel的个案序列号是否均填写了");
            }
            seqNoSet.add(dataCaseEntities.get(i).getSeqNo());
        }
        DataCaseEntity queryEntity = new DataCaseEntity();
        queryEntity.setSeqNoSet(seqNoSet);
        List<DataCaseEntity> existCaseList = dataCaseService.listBySeqNoSet(queryEntity);
        Map<String, DataCaseEntity> existCaseMap = new HashMap<>();
        for(DataCaseEntity entity : existCaseList) {
            existCaseMap.put(entity.getSeqNo(), entity);
        }
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            DataCaseEntity entity = dataCaseEntities.get(i);
            if(!existCaseMap.containsKey(entity.getSeqNo())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "个案序列号:" + entity.getSeqNo() + "不存在，请修改后重新上传");
            }
            dataCaseEntities.get(i).setId(existCaseMap.get(entity.getSeqNo()).getId());
        }
        dataCaseService.updateCaseList(dataCaseEntities);
        return WebResponse.success();
    }

    @ApiOperation(value = "导入新案件或追加案件", notes = "导入新案件或追加案件")
    @PostMapping("/dataCase/newCase/import")
    public Object dataCaseNewCaseImport(MultipartFile file, DataBatchEntity batch) throws IOException {
        if(batch == null || StringUtils.isEmpty(batch.getBatchNo())) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请上传批次号");
        }
        String fileName = file.getOriginalFilename();
        logger.info(fileName);
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        int cols = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
        List<DataCaseEntity> dataCaseEntities;
        if(Math.abs(ExcelCaseConstant.StandardCase.values().length - cols) <= Math.abs(ExcelCaseConstant.CardLoanCase.values().length - cols)) {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.StandardCase.values(), DataCaseEntity.class);
        } else {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.CardLoanCase.values(), DataCaseEntity.class);
            for(int i = 0; i < dataCaseEntities.size(); i ++) {
                DataCaseEntity entity = dataCaseEntities.get(i);
                if(entity != null && !CollectionUtils.isEmpty(entity.getContacts()) && entity.getContacts().get(0) != null) {
                    dataCaseEntities.get(i).getContacts().get(0).setRelation("配偶");
                }
//                if(entity != null && !CollectionUtils.isEmpty(entity.getContacts()) && entity.getContacts().size() >= 2 && entity.getContacts().get(1) != null) {
//                    dataCaseEntities.get(i).getContacts().get(1).setRelation("担保人");
//                }
            }
        }
        if(dataCaseEntities.size() == 0) {
            return WebResponse.success("添加0条数据");
        }
        Map<String, Integer> countMap = new HashMap<>();
        Set<String> seqNoSet = new HashSet<>();
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            if(StringUtils.isEmpty(dataCaseEntities.get(i).getSeqNo())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号，请填写后上传，并检查excel的个案序列号是否均填写了");
            }
            seqNoSet.add(dataCaseEntities.get(i).getSeqNo());
            countMap.put(dataCaseEntities.get(i).getSeqNo(), countMap.get(dataCaseEntities.get(i).getSeqNo()) == null ? 1 : countMap.get(dataCaseEntities.get(i).getSeqNo()) + 1);
        }
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if(entry.getValue() > 1) {
                builder.append(entry.getKey()).append(" ");
            }
        }
        if(StringUtils.isNoneEmpty(builder.toString())) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "存在重复的个案序列号：" + builder.toString());
        }
        DataCaseEntity queryEntity = new DataCaseEntity();
        queryEntity.setSeqNoSet(seqNoSet);
        List<DataCaseEntity> existCaseList = dataCaseService.listBySeqNoSet(queryEntity);
        Map<String, DataCaseEntity> existCaseMap = new HashMap<>();
        for(DataCaseEntity entity : existCaseList) {
            existCaseMap.put(entity.getSeqNo(), entity);
        }
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            DataCaseEntity entity = dataCaseEntities.get(i);
            if(existCaseMap.containsKey(entity.getSeqNo())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "个案序列号:" + entity.getSeqNo() + "已存在，请确认后重新上传");
            }
            dataCaseEntities.get(i).setBatchNo(batch.getBatchNo());
        }
        dataCaseService.saveCaseList(dataCaseEntities);
        return WebResponse.success();
    }

    @ApiOperation(value = "数据模块-案件管理查询导出", notes = "数据模块-案件管理查询导出")
    @PostMapping("/dataCase/pageCaseListExport")
    public Object pageCaseListExport(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException{

        List<DataCaseEntity> list = dataCaseService.pageCaseListExport(bean);
        ExcelUtils.exportExcel(list,
                ExcelBatchConstant.BatchMemorize.values(),
                "案件管理当前页导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "查询导出所有", notes = "查询导出所有")
    @PostMapping("/dataCase/totalDataBatchExport")
    public Object totalDataBatchExport(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {

        List<DataCaseEntity> list = dataCaseService.totalCaseListExport(bean);
        ExcelUtils.exportExcel(list,
                ExcelBatchConstant.BatchMemorize.values(),
                "案件管理全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选", notes = "查询导出所选")
    @PostMapping("/dataCase/selectDataCaseExport")
    public Object selectDataCaseExport(@RequestBody List<DataCaseEntity> list, HttpServletResponse response) throws IOException, InvalidFormatException {
        int[] ids = new int[list.size()];
        for(int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            ids[i] = temp.getId();
        }
        List<DataCaseEntity> resultList = dataCaseService.selectCaseListExport(ids);
        ExcelUtils.exportExcel(resultList,
                ExcelBatchConstant.BatchMemorize.values(),
                "案件管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "查询导出所选案件的催收记录", notes = "查询导出所选案件的催收记录")
    @PostMapping("/dataCase/selectDataCollectExportByBatch")
    public Object selectDataCollectExportByCase(@RequestBody List<DataCaseEntity> list, HttpServletResponse response) throws IOException, InvalidFormatException {
        String[] caseIds = new String[list.size()];
        for(int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            caseIds[i] = temp.getId()+"";
        }
        WebResponse webResponse = dataCollectService.selectDataCollectExportByCase(caseIds);
        List<DataCollectionEntity> resultList = (List<DataCollectionEntity>) webResponse.getData();
        ExcelUtils.exportExcel(resultList,
                ExcelCollectExportConstant.CollectMemorize.values(),
                "案件管理催收记录选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选案件的电话", notes = "查询导出所选案件的电话")
    @PostMapping("/dataCase/selectDataCaseTel")
    public Object selectDataCaseTel(@RequestBody List<DataCaseEntity> list, HttpServletResponse response) throws IOException, InvalidFormatException {

        int[] ids = new int[list.size()];
        for(int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            ids[i] = temp.getId();
        }
        List<DataCaseTelExport> resultList = dataCaseService.selectCaseTelListExport(ids);
        ExcelUtils.exportExcel(resultList,
                ExcelCollectExportConstant.CollectMemorize.values(),
                "案件管理电话选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

}
