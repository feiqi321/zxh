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
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectExportEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.utils.ExcelUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
        ExcelUtils.exportExcel(list,
                ExcelCollectExportConstant.CaseCollectExport.values(),
                "催记管理全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
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
        List<DataCollectExportEntity> resultList = (List<DataCollectExportEntity>) webResponse.getData();
        ExcelUtils.exportExcel(resultList,
                ExcelCollectExportConstant.CaseCollectExport.values(),
                "催记管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "催收记录导入", notes = "催收记录导入")
    @PostMapping("/dataCollect/import")
    public Object dataCollectImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        List<DataCollectionEntity> dataCollectionEntities = ExcelUtils.importExcel(file, ExcelCollectConstant.CaseCollect.values(), DataCollectionEntity.class);
        WebResponse webResponse =fileManageService.batchCollect(dataCollectionEntities);
        return webResponse;
    }

}
