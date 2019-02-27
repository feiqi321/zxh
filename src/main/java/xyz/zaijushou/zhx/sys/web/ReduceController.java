package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelReduceConstant;
import xyz.zaijushou.zhx.constant.ExcelReduceExportConstant;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataReduceEntity;
import xyz.zaijushou.zhx.sys.service.ReduceService;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/2/25.
 */
@Api("减免管理")
@RestController
public class ReduceController {
    private static Logger logger = LoggerFactory.getLogger(ReduceController.class);

    @Autowired
    private ReduceService reduceService;

    @ApiOperation(value = "减免分页查询", notes = "减免分页查询")
    @PostMapping("/reduce/page/all")
    public Object pageDataCase(@RequestBody DataCollectionEntity bean) {
        PageInfo<DataCollectionEntity> list = reduceService.pageReduce(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "增加减免信息", notes = "增加减免信息")
    @PostMapping("/reduce/add")
    public Object saveReduce(@RequestBody DataCollectionEntity bean) {
        reduceService.saveReduce(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "修改减免信息", notes = "修改减免信息")
    @PostMapping("/reduce/update/info")
    public Object updateReduce(@RequestBody DataCollectionEntity bean) {
        reduceService.updateReduce(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "操作减免状态", notes = "操作减免状态")
    @PostMapping("/reduce/update/status")
    public Object updateStatus(@RequestBody DataCollectionEntity bean) {
        reduceService.updateStatus(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "查找减免详情", notes = "查找减免详情")
    @PostMapping("/reduce/select/id")
    public Object findById(@RequestBody DataCollectionEntity bean) {
        DataCollectionEntity entity = reduceService.findById(bean);
        return WebResponse.success(entity);
    }

    @ApiOperation(value = "减免管理导入", notes = "减免管理导入")
    @PostMapping("/reduce/import")
    public Object reduceInfoImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        logger.info(fileName);
        List<DataCollectionEntity> bean = ExcelUtils.importExcel(file, ExcelReduceConstant.ReduceInfo.values(), DataCollectionEntity.class);
        reduceService.saveReduceInfo(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "减免管理导出", notes = "减免管理导出")
    @PostMapping("/reduce/dataExport")
    public Object pageDataBatchExport(@RequestBody DataCollectionEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        List<DataCollectionEntity> list = new ArrayList<DataCollectionEntity>();
        if (bean.getsType() == 0){//导出全部
            list = reduceService.listReduce(bean);
        }else {//导出当前页
            PageInfo<DataCollectionEntity> pageInfo = reduceService.pageReduce(bean);
            list = pageInfo.getList();
        }
        if (StringUtils.isEmpty(list)){
            return null;
        }
        ExcelUtils.exportExcel(list,
                ExcelReduceExportConstant.ReduceList.values(),
                "减免管理导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "增加减免申请信息", notes = "增加减免申请信息")
    @PostMapping("/reduce/apply/add")
    public Object saveReduceApply(@RequestBody DataReduceEntity bean) {
        reduceService.saveReduceApply(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "修改减免申请信息", notes = "修改减免申请信息")
    @PostMapping("/reduce/apply/update")
    public Object updateReduceApply(@RequestBody DataReduceEntity bean) {
        reduceService.saveReduceApply(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "操作减免申请状态", notes = "操作减免申请状态")
    @PostMapping("/reduce/apply/update/status")
    public Object updateApplyStatus(@RequestBody DataReduceEntity bean) {
        reduceService.updateApplyStatus(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "查找减免申请详情", notes = "查找减免申请详情")
    @PostMapping("/reduce/apply/select/id")
    public Object findApplyById(@RequestBody DataReduceEntity bean) {
        DataReduceEntity entity = reduceService.findApplyById(bean);
        return WebResponse.success(entity);
    }
}
