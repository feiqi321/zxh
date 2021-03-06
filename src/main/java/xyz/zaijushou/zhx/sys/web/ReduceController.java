package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map.Entry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelReduceConstant;
import xyz.zaijushou.zhx.constant.ExcelReduceExportConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.ReduceService;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by looyer on 2019/2/25.
 */
@Api("减免管理")
@RestController
public class ReduceController {
    private static Logger logger = LoggerFactory.getLogger(ReduceController.class);
    @Value(value = "${detailFile_path}")
    private String detailFile;
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
        DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+bean.getSeqno(),DataCaseEntity.class);
        if (dataCaseEntity!=null){
            bean.setCaseId(dataCaseEntity.getId());
        }else{
            return WebResponse.error("500","个案序列号:"+bean.getSeqno()+"不存在");
        }
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
        for (int i=0;i<bean.size();i++){
            DataCollectionEntity temp = bean.get(i);
            DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getSeqno(),DataCaseEntity.class);
            if (dataCaseEntity!=null){
                temp.setCaseId(dataCaseEntity.getId());
                bean.set(i,temp);
            }else{
               return WebResponse.error("500","个案序列号:"+temp.getSeqno()+"不存在");
            }
        }


        reduceService.saveReduceInfo(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "减免管理导出", notes = "减免管理导出")
    @PostMapping("/reduce/dataExport")
    public Object pageDataBatchExport(@RequestBody DataCollectionEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        List<DataCollectionEntity> list = new ArrayList<DataCollectionEntity>();
        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Entry entry = (Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelReduceExportConstant.ReduceConfList reduceConfList = ExcelReduceExportConstant.ReduceConfList.getEnumByKey(entry.getKey().toString());
                if (reduceConfList!=null && StringUtils.notEmpty(reduceConfList.getAttr())) {
                    exportKeyList.add(reduceConfList.getAttr());
                }
                colMap.put(reduceConfList.getCol(), reduceConfList.getCol());
            }
        }

        ExcelReduceExportConstant.ReduceList reduceList[]= ExcelReduceExportConstant.ReduceList.values();
        List<ExcelReduceExportConstant.ReduceList> reduceList2 = new ArrayList<ExcelReduceExportConstant.ReduceList>();

        for (int i=0;i<reduceList.length;i++){
            ExcelReduceExportConstant.ReduceList reduceListTemp = reduceList[i];
            if (colMap.get(reduceListTemp.getCol())!=null){
                reduceList2.add(reduceListTemp);
            }

        }

        bean.setExportKeyList(exportKeyList);
        if (bean.getsType() == 0){//导出全部
            list = reduceService.totalExport(bean);
        }else {//导出当前页
            PageInfo<DataCollectionEntity> pageInfo = reduceService.pageReduceExport(bean);
            list = pageInfo.getList();
        }
        if (StringUtils.isEmpty(list)){
            list = new ArrayList<DataCollectionEntity>();
        }

        ExcelUtils.exportExcel(list,
                reduceList2.toArray(new ExcelReduceExportConstant.ReduceList[reduceList2.size()]),
                "减免管理导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "增加减免申请信息", notes = "增加减免申请信息")
    @PostMapping("/reduce/apply/add")
    public Object saveReduceApply(@RequestBody DataCollectionEntity bean) {
        reduceService.saveReduceApply(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "修改减免申请信息", notes = "修改减免申请信息")
    @PostMapping("/reduce/apply/update")
    public Object updateReduceApply(@RequestBody DataCollectionEntity bean) {
        reduceService.saveReduceApply(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "操作减免申请状态", notes = "操作减免申请状态")
    @PostMapping("/reduce/apply/update/status")
    public Object updateApplyStatus(@RequestBody DataCollectionEntity bean) {
        reduceService.updateApplyStatus(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "查找减免申请详情", notes = "查找减免申请详情")
    @PostMapping("/reduce/apply/select/id")
    public Object findApplyById(@RequestBody DataCollectionEntity bean) {
        DataCollectionEntity entity = reduceService.findApplyById(bean);
        return WebResponse.success(entity);
    }

    @ApiOperation(value = "减免申请分页查询", notes = "减免申请分页查询")
    @PostMapping("/reduce/apply/page")
    public Object pageDataApply(@RequestBody DataCollectionEntity bean) {
        PageInfo<DataCollectionEntity> list = reduceService.pageReduceApply(bean);
        return WebResponse.success(list);
    }

    //保存附件信息
    @PostMapping("/reduce/save/import")
    public Object reduceImport(MultipartFile file, DataCollectionEntity bean ) throws IOException {
        String fileName = file.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
        String fileUuid = UUID.randomUUID().toString().replaceAll("-", "")+"."+suffix;
        file.transferTo(new File(detailFile+fileUuid));
        bean.setFileName(fileName);
        bean.setFileUuid(fileUuid);
        reduceService.saveReduceApply(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "删除附件信息", notes = "删除附件信息")
    @PostMapping("/reduce/file/delete")
    public Object deleteFile(@RequestBody ReduceFileList bean ) throws IOException {

        reduceService.delete(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "查询附件信息", notes = "查询附件信息")
    @PostMapping("/reduce/file/list")
    public Object listFile(@RequestBody ReduceFileList bean ) throws IOException {

        return WebResponse.success( reduceService.listFile(bean));
    }
}
