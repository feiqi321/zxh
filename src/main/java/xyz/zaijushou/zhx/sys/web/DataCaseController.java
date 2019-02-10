package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelConstant;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.utils.ExcelUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/案件管理")
@RestController
public class DataCaseController {

    private static Logger logger = LoggerFactory.getLogger(DataCaseController.class);

    @Autowired
    private DataCaseService dataCaseService;

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

        List<DataCaseEntity> list = dataCaseService.pageCaseList(bean);
        return WebResponse.success(list);
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
        List<DataCaseEntity> caseList = ExcelUtils.importExcel(file, ExcelConstant.CaseTel.values(), DataCaseEntity.class);
        return WebResponse.success();
    }

}
