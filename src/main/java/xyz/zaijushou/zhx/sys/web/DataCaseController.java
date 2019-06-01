package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.functions.Rows;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.*;
import xyz.zaijushou.zhx.utils.ExcelCaseUtils;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
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
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysDictionaryService dictionaryService;
    @Autowired
    private SysOperationLogService sysOperationLogService;
    @Autowired
    private DataLogService dataLogService;

    @Resource
    private ExcelUtils excelUtils;

    @Resource
    private DataCaseMapper dataCaseMapper;

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

    @ApiOperation(value = "修改自定义信息", notes = "修改自定义信息")
    @PostMapping("/dataCase/updateRemak")
    public Object updateRemark(@RequestBody DataCaseEntity bean) {

        dataCaseService.updateRemark(bean);

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
        WebResponse webResponse = WebResponse.buildResponse();
        try {
            logger.info("进入执行");
            webResponse = dataCaseService.pageCaseListOnly(bean);
            logger.info("跳出执行");
        }catch (Exception e){
            webResponse.setCode("500");
            webResponse.setMsg(e.getMessage());
        }

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


    @ApiOperation(value = "添加评语", notes = "添加评语")
    @PostMapping("/dataCase/addComment")
    public Object addComment(@RequestBody List<DataCaseEntity> list) {
        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            dataCaseService.addComment(dataCaseEntity);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "添加警告", notes = "添加警告")
    @PostMapping("/dataCase/addWarning")
    public Object addWarning(@RequestBody DataCaseEntity bean) {
            dataCaseService.addWarning(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "下一条案件", notes = "下一条案件")
    @PostMapping("/dataCase/nextCase")
    public Object nextCase(@RequestBody DataCaseEntity bean) {
        DataCaseEntity temp = dataCaseService.nextCase(bean);
        if (temp ==null){
            return WebResponse.error("500","已经是最后一条了");
        }
        return WebResponse.success(temp);

    }

    @ApiOperation(value = "上一条案件", notes = "上一条案件")
    @PostMapping("/dataCase/lastCase")
    public Object lastCase(@RequestBody DataCaseEntity bean) {
        DataCaseEntity temp = dataCaseService.lastCase(bean);
        if (temp ==null){
            return WebResponse.error("500","已经是第一条了");
        }

        return WebResponse.success(temp);

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
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        logger.info(fileName);
        List<DataCaseTelEntity> caseList = ExcelUtils.importExcel(file, ExcelTelConstant.CaseTel.values(), DataCaseTelEntity.class);
        SysDictionaryEntity dictionary = new SysDictionaryEntity();
        dictionary.setName("电话类型");
        List<SysDictionaryEntity> dictionaryEntityList = dictionaryService.listDataByName(dictionary);
        Map dicMap = new HashMap();
        for (int m=0;m<dictionaryEntityList.size();m++){
            SysDictionaryEntity temp = dictionaryEntityList.get(m);
            dicMap.put(temp.getName(),temp.getId());
        }
        for (int i=0;i<caseList.size();i++){
            DataCaseTelEntity dataCaseTelEntity = caseList.get(i);
            if (StringUtils.isEmpty(dataCaseTelEntity.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getCardNo()+"@"+dataCaseTelEntity.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
                }else{
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
                }
            }
            if (dicMap.get(caseList.get(i).getType())==null){
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行电话类型不在枚举配置中，请核对后再上传");
            }else{
                caseList.get(i).setType(dicMap.get(caseList.get(i).getType())+"");
            }
            caseList.set(i,dataCaseTelEntity);
        }

        WebResponse webResponse =fileManageService.batchCaseTel(caseList);
        return webResponse;
    }

    @ApiOperation(value = "案件地址导入", notes = "案件地址导入")
    @PostMapping("/dataCase/address/import")
    public Object dataCaseAddressImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        logger.info(fileName);
        List<DataCaseAddressEntity> addressList = ExcelUtils.importExcel(file, ExcelAddressConstant.CaseAddress.values(), DataCaseAddressEntity.class);
        WebResponse webResponse =fileManageService.batchCaseAddress(addressList);
        return webResponse;
    }

    @ApiOperation(value = "案件利息导入", notes = "案件利息导入")
    @PostMapping("/dataCase/interest/import")
    public Object dataCaseInterestImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        logger.info(fileName);
        List<DataCaseInterestEntity> interestList = ExcelUtils.importExcel(file, ExcelInterestConstant.CaseInterest.values(), DataCaseInterestEntity.class);
        WebResponse webResponse =fileManageService.batchCaseInterest(interestList);
        return webResponse;
    }

    @ApiOperation(value = "案件评语导入", notes = "案件评语导入")
    @PostMapping("/dataCase/comment/import")
    public Object dataCaseCommentImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        logger.info(fileName);
        List<DataCaseEntity> dataCaseEntities = ExcelUtils.importExcel(file, ExcelInterestConstant.CaseColor.values(), DataCaseEntity.class);
        WebResponse webResponse =fileManageService.batchCaseComment(dataCaseEntities);
        return webResponse;
    }

    @ApiOperation(value = "导入更新案件", notes = "导入更新案件")
    @PostMapping("/dataCase/updateCase/import")
    public Object dataCaseUpdateCaseImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        logger.info(fileName);
        InputStream inputStream = file.getInputStream();
        Workbook workbook = null;
        if(StringUtils.isNotEmpty(fileName) && fileName.length() >= 5 && ".xlsx".equals(fileName.substring(fileName.length() - 5))) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new HSSFWorkbook(inputStream);
        }
        int cols = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
        Row row = workbook.getSheetAt(0).getRow(0);
        String modelType = "";
        for (int i=0;i<cols;i++){
            Cell cell = row.getCell(i);
            if (cell!=null && cell.getStringCellValue()!=null && cell.getStringCellValue().equals("配偶姓名")){
                modelType = "chedai";
            }else if(cell!=null && cell.getStringCellValue()!=null){
                modelType = "biaozhun";
            }
        }
        List<DataCaseEntity> dataCaseEntities;
        if(modelType.equals("biaozhun")) {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.StandardCase.values(), DataCaseEntity.class);
        } else {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.CardLoanCase.values(), DataCaseEntity.class);
        }
        if(dataCaseEntities.size() == 0) {
            return WebResponse.success("更新0条数据");
        }
        for(int i = 0; i < dataCaseEntities.size(); i ++) {

            if(dataCaseEntities.get(i).getCollectionArea() != null && dataCaseEntities.get(i).getCollectionArea().getId() != null) {
                SysDictionaryEntity collectAreaEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH + dataCaseEntities.get(i).getCollectionArea().getId(), SysDictionaryEntity.class);
                dataCaseEntities.get(i).setCollectArea(collectAreaEntity == null ? "" : dataCaseEntities.get(i).getCollectionArea().getId() + "");
            }
            if(dataCaseEntities.get(i).getCollectionUser()!=null && dataCaseEntities.get(i).getCollectionUser().getId()!=null) {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataCaseEntities.get(i).getCollectionUser().getId(), SysUserEntity.class);
                if (user==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行催收员id不正确，请核实后再上传");
                }

            }
            if(StringUtils.isEmpty(dataCaseEntities.get(i).getSeqNo()) && (StringUtils.isEmpty(dataCaseEntities.get(i).getCardNo()) &&  StringUtils.isEmpty(dataCaseEntities.get(i).getCaseDate()))) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
            }
            if (StringUtils.isNotEmpty(dataCaseEntities.get(i).getAccountAge())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getAccountAge(),SysDictionaryEntity.class);
                if (sysDictionaryEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行逾期账龄值"+dataCaseEntities.get(i).getCollectionType()+"不在枚举配置中，并检查excel的逾期账龄是否均填写正确");
                }else{
                    dataCaseEntities.get(i).setAccountAge(sysDictionaryEntity.getId()+"");
                }
            }
            //语音 手机号 视频 留言
            if (StringUtils.isNotEmpty(dataCaseEntities.get(i).getCollectionType())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getCollectionType(),SysDictionaryEntity.class);
                if (sysDictionaryEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行催收分类值"+dataCaseEntities.get(i).getCollectionType()+"不在枚举配置中，并检查excel的催收分类是否均填写正确");
                }else{
                    dataCaseEntities.get(i).setCollectionType(sysDictionaryEntity.getId()+"");
                }
            }
            if (dataCaseEntities.get(i).getProvince()!=null && StringUtils.isNotEmpty(dataCaseEntities.get(i).getProvince().getName())){
                SysDictionaryEntity provicneDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getProvince().getName(),SysDictionaryEntity.class);
                if (provicneDic==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行省"+dataCaseEntities.get(i).getProvince().getName()+"不在枚举配置中，并检查excel的省是否均填写正确");
                }else{
                    dataCaseEntities.get(i).getProvince().setId(provicneDic.getId());
                }
            }
            if (dataCaseEntities.get(i).getCity()!=null && StringUtils.isNotEmpty(dataCaseEntities.get(i).getCity().getName())){
                SysDictionaryEntity cityDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getCity().getName(),SysDictionaryEntity.class);
                if (cityDic==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行市"+dataCaseEntities.get(i).getCity().getName()+"不在枚举配置中，并检查excel的市是否均填写正确");
                }else{
                    dataCaseEntities.get(i).getCity().setId(cityDic.getId());
                }
            }
            if (dataCaseEntities.get(i).getCounty()!=null && StringUtils.isNotEmpty(dataCaseEntities.get(i).getCounty().getName())){
                SysDictionaryEntity countyDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getCounty().getName(),SysDictionaryEntity.class);
                if (countyDic==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行县"+dataCaseEntities.get(i).getCounty().getName()+"不在枚举配置中，并检查excel的县是否均填写正确");
                }else{
                    dataCaseEntities.get(i).getCounty().setId(countyDic.getId());
                }
            }

            if (StringUtils.isNotEmpty(dataCaseEntities.get(i).getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntities.get(i).getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity==null) {
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行案件不存在，请核实后重新上传");
                }else{
                    dataCaseEntities.get(i).setId(dataCaseEntity.getId());
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntities.get(i).getCardNo()+"@"+dataCaseEntities.get(i).getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行案件不存在，请核实后重新上传");
                }else{
                    dataCaseEntities.get(i).setId(dataCaseEntity.getId());
                }
            }
            if(dataCaseEntities.get(i).getCollectionUser()!=null && dataCaseEntities.get(i).getCollectionUser().getId()!=null) {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + dataCaseEntities.get(i).getCollectionUser().getId(), SysUserEntity.class);
                dataCaseEntities.get(i).setDept(user == null ? "" : user.getDepartment());
            }

        }
        StringBuffer sucessStr = new StringBuffer("导入成功，总计导入行数为:"+dataCaseEntities.size());

        dataCaseService.updateCaseList(dataCaseEntities);
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse.setCode("100");
        webResponse.setMsg(sucessStr.toString());
        return WebResponse.success();
    }

    private void doDataCase(List<DataCaseEntity> caseEntityList){
        if(CollectionUtils.isEmpty(caseEntityList)){
            return ;
        }
        caseEntityList.forEach(caseEntity-> {
            if (caseEntity.getCollectionArea() != null && caseEntity.getCollectionArea().getId() != null) {
                SysDictionaryEntity collectAreaEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH + caseEntity.getCollectionArea().getId(), SysDictionaryEntity.class);
                caseEntity.setCollectArea(collectAreaEntity == null ? "" : caseEntity.getCollectionArea().getId() + "");
            }
            if (caseEntity.getCollectionUser() != null && caseEntity.getCollectionUser().getId() != null) {
                SysNewUserEntity userEntity = new SysNewUserEntity();
                userEntity.setRole("催收员");
                List<SysNewUserEntity> userInfoEntity = sysUserService.getDataByRoleNameForList(userEntity);
                Map collectUserMap = new HashMap();
                for (int m = 0; m < userInfoEntity.size(); m++) {
                    SysNewUserEntity sysNewUserEntity = userInfoEntity.get(m);
                    collectUserMap.put(sysNewUserEntity.getId(), sysNewUserEntity);
                }
                if (collectUserMap.get(caseEntity.getCollectionUser().getId()) == null) {
                    throw new IllegalArgumentException("姓名" + caseEntity.getName() + "的数据催收员ID不正确，请核实后再上传");
                }

            }
            if (StringUtils.isEmpty(caseEntity.getSeqNo()) && (StringUtils.isEmpty(caseEntity.getCardNo()) && StringUtils.isEmpty(caseEntity.getCaseDate()))) {
                throw new IllegalArgumentException("姓名" + caseEntity.getName() + "的数据未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号或者卡号和委案日期是否均填写了");
            }
            if (StringUtils.isNotEmpty(caseEntity.getAccountAge())) {
                SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + caseEntity.getAccountAge(), SysDictionaryEntity.class);
                if (sysDictionaryEntity == null) {
                    throw new IllegalArgumentException("姓名" + caseEntity.getName() + "的数据逾期账龄值" + caseEntity.getCollectionType() + "不在枚举配置中，并检查excel的逾期账龄是否均填写正确");
                } else {
                    caseEntity.setAccountAge(sysDictionaryEntity.getId() + "");
                }
            }
            //语音 手机号 视频 留言
            if (StringUtils.isNotEmpty(caseEntity.getCollectionType())) {
                SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + caseEntity.getCollectionType(), SysDictionaryEntity.class);
                if (sysDictionaryEntity == null) {
                    throw new IllegalArgumentException("姓名" + caseEntity.getName() +"的数据行催收分类值" + caseEntity.getCollectionType() + "不在枚举配置中，并检查excel的催收分类是否均填写正确");
                } else {
                    caseEntity.setCollectionType(sysDictionaryEntity.getId() + "");
                }
            }

            //用户检测
            if (caseEntity.getCollectionUser() != null && caseEntity.getCollectionUser().getId() != null) {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + caseEntity.getCollectionUser().getId(), SysUserEntity.class);
                caseEntity.setDept(user == null ? "" : user.getDepartment());
            }
            //省份检测
            if (caseEntity.getProvince() != null && !xyz.zaijushou.zhx.utils.StringUtils.isEmpty(caseEntity.getProvince().getName())) {
                SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + caseEntity.getProvince().getName(), SysDictionaryEntity.class);
                if (sysDictionaryEntity != null) {
                    caseEntity.getProvince().setId(sysDictionaryEntity.getId());

                    if (caseEntity.getCity() != null && !xyz.zaijushou.zhx.utils.StringUtils.isEmpty(caseEntity.getCity().getName())) {
                        SysDictionaryEntity sysDictionaryEntity2 = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + caseEntity.getCity().getName(), SysDictionaryEntity.class);
                        if (sysDictionaryEntity2 != null && sysDictionaryEntity2.getParent() != null && sysDictionaryEntity2.getParent().getId().equals(sysDictionaryEntity.getId())) {
                            caseEntity.getCity().setId(sysDictionaryEntity2.getId());

                            if (caseEntity.getCounty() != null && !xyz.zaijushou.zhx.utils.StringUtils.isEmpty(caseEntity.getCounty().getName())) {
                                SysDictionaryEntity sysDictionaryEntity3 = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + caseEntity.getCounty().getName(), SysDictionaryEntity.class);
                                if (sysDictionaryEntity3 != null && sysDictionaryEntity3.getParent() != null && sysDictionaryEntity3.getParent().getId().equals(sysDictionaryEntity2.getId())) {

                                    caseEntity.getCounty().setId(sysDictionaryEntity3.getId());
                                }
                            }

                        }

                    }
                }
            }



        });


        dataCaseMapper.updateBatchBySeqNo(caseEntityList);

    }

    @ApiOperation(value = "导入新案件或追加案件", notes = "导入新案件或追加案件")
    @PostMapping("/dataCase/newCase/import")
    public Object dataCaseNewCaseImport(MultipartFile file, DataBatchEntity batch) throws IOException {
        if(batch == null || StringUtils.isEmpty(batch.getBatchNo())) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请上传批次号");
        }
        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        logger.info(fileName);
        InputStream inputStream = file.getInputStream();
        Workbook workbook = null;
        if(StringUtils.isNotEmpty(fileName) && fileName.length() >= 5 && ".xlsx".equals(fileName.substring(fileName.length() - 5))) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new HSSFWorkbook(inputStream);
        }
        int cols = workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells();
        Row row = workbook.getSheetAt(0).getRow(0);
        String modelType = "";
        for (int i=0;i<cols;i++){
            Cell cell = row.getCell(i);
            if (cell!=null && cell.getStringCellValue()!=null && cell.getStringCellValue().equals("配偶姓名")){
                modelType = "chedai";
            }else if(cell!=null && cell.getStringCellValue()!=null){
                modelType = "biaozhun";
            }
        }
        List<DataCaseEntity> dataCaseEntities;
        if(modelType.equals("biaozhun")) {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.StandardCase.values(), DataCaseEntity.class);
        } else {
            dataCaseEntities = ExcelUtils.importExcel(file, ExcelCaseConstant.CardLoanCase.values(), DataCaseEntity.class);
        }
        if(dataCaseEntities.size() == 0) {
            return WebResponse.success("添加0条数据");
        }
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            DataBatchEntity batchEntity =  RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH+batch.getBatchNo(),DataBatchEntity.class);
            dataCaseEntities.get(i).setClient(batchEntity ==null ?"":batchEntity.getClient());
            if(dataCaseEntities.get(i).getCollectionArea() != null && dataCaseEntities.get(i).getCollectionArea().getId() != null) {
                SysDictionaryEntity collectAreaEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH + dataCaseEntities.get(i).getCollectionArea().getId(), SysDictionaryEntity.class);
                dataCaseEntities.get(i).setCollectArea(collectAreaEntity == null ? "" : dataCaseEntities.get(i).getCollectionArea().getId() + "");
            }
            if(dataCaseEntities.get(i).getCollectionUser()!=null && dataCaseEntities.get(i).getCollectionUser().getId()!=null) {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataCaseEntities.get(i).getCollectionUser().getId(), SysUserEntity.class);
                if (user==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行催收员id不正确，请核实后再上传");
                }

            }
            if(StringUtils.isEmpty(dataCaseEntities.get(i).getSeqNo()) && (StringUtils.isEmpty(dataCaseEntities.get(i).getCardNo()) &&  StringUtils.isEmpty(dataCaseEntities.get(i).getCaseDate()))) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号是否均填写了");
            }
            if (StringUtils.isNotEmpty(dataCaseEntities.get(i).getAccountAge())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getAccountAge(),SysDictionaryEntity.class);
                if (sysDictionaryEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行逾期账龄值"+dataCaseEntities.get(i).getCollectionType()+"不在枚举配置中，并检查excel的逾期账龄是否均填写正确");
                }else{
                    dataCaseEntities.get(i).setAccountAge(sysDictionaryEntity.getId()+"");
                }
            }
            if (StringUtils.isNotEmpty(dataCaseEntities.get(i).getCollectionType())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getCollectionType(),SysDictionaryEntity.class);
                if (sysDictionaryEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行催收分类值"+dataCaseEntities.get(i).getCollectionType()+"不在枚举配置中，并检查excel的催收分类是否均填写正确");
                }else{
                    dataCaseEntities.get(i).setCollectionType(sysDictionaryEntity.getId()+"");
                }
            }

            if (StringUtils.isNotEmpty(dataCaseEntities.get(i).getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntities.get(i).getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null) {
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行案件已存在，请修改后重新上传");
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntities.get(i).getCardNo()+"@"+dataCaseEntities.get(i).getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行案件已存在，请修改后重新上传");
                }
            }

            if(dataCaseEntities.get(i).getCollectionUser()!=null && dataCaseEntities.get(i).getCollectionUser().getId()!=null) {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + dataCaseEntities.get(i).getCollectionUser().getId(), SysUserEntity.class);
                dataCaseEntities.get(i).setDept(user == null ? "" : user.getDepartment());
            }
            if (dataCaseEntities.get(i).getProvince()!=null && StringUtils.isNotEmpty(dataCaseEntities.get(i).getProvince().getName())){
                SysDictionaryEntity provicneDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getProvince().getName(),SysDictionaryEntity.class);
                if (provicneDic==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行省"+dataCaseEntities.get(i).getProvince().getName()+"不在枚举配置中，并检查excel的省是否均填写正确");
                }else{
                    dataCaseEntities.get(i).getProvince().setId(provicneDic.getId());
                }
            }
            if (dataCaseEntities.get(i).getCity()!=null && StringUtils.isNotEmpty(dataCaseEntities.get(i).getCity().getName())){
                SysDictionaryEntity cityDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getCity().getName(),SysDictionaryEntity.class);
                if (cityDic==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行市"+dataCaseEntities.get(i).getCity().getName()+"不在枚举配置中，并检查excel的市是否均填写正确");
                }else{
                    dataCaseEntities.get(i).getCity().setId(cityDic.getId());
                }
            }
            if (dataCaseEntities.get(i).getCounty()!=null && StringUtils.isNotEmpty(dataCaseEntities.get(i).getCounty().getName())){
                SysDictionaryEntity countyDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+dataCaseEntities.get(i).getCounty().getName(),SysDictionaryEntity.class);
                if (countyDic==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行县"+dataCaseEntities.get(i).getCounty().getName()+"不在枚举配置中，并检查excel的县是否均填写正确");
                }else{
                    dataCaseEntities.get(i).getCounty().setId(countyDic.getId());
                }
            }

            dataCaseEntities.get(i).setBatchNo(batch.getBatchNo());

        }
        StringBuffer sucessStr = new StringBuffer("导入成功，总计导入行数为:"+dataCaseEntities.size());

        dataCaseService.saveCaseList(dataCaseEntities,batch.getBatchNo());
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse.setCode("100");
        webResponse.setMsg(sucessStr.toString());
        return WebResponse.success();
    }

    @ApiOperation(value = "数据模块-案件管理查询导出", notes = "数据模块-案件管理查询导出")
    @PostMapping("/dataCase/pageCaseListExport")
    public Object pageCaseListExport(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException,Exception{
        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelCaseConstant.CaseExportCaseConf caseExportCaseConf = ExcelCaseConstant.CaseExportCaseConf.getEnumByKey(entry.getKey().toString());
                if (caseExportCaseConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(caseExportCaseConf.getAttr())) {
                    exportKeyList.add(caseExportCaseConf.getAttr());
                }
                colMap.put(caseExportCaseConf.getCol(), caseExportCaseConf.getCol());
            }
        }

        ExcelCaseConstant.CaseExportCase caseExportCases[]= ExcelCaseConstant.CaseExportCase.values();
        List<ExcelCaseConstant.CaseExportCase> caseExportCases2 = new ArrayList<ExcelCaseConstant.CaseExportCase>();

        for (int i=0;i<caseExportCases.length;i++){
            ExcelCaseConstant.CaseExportCase caseListTemp = caseExportCases[i];
            if (colMap.get(caseListTemp.getCol())!=null){
                caseExportCases2.add(caseListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);
        List<DataCaseEntity> list = dataCaseService.pageCaseListExport(bean);
        String fileName = "案件管理当前页导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        ExcelUtils.exportExcel(list,
                caseExportCases2.toArray(new ExcelCaseConstant.CaseExportCase[caseExportCases2.size()]),
                fileName+ ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出当前页", notes = "查询导出当前页")
    @PostMapping("/dataCase/pageDataBatchExport")
    public Object pageDataBatchExport(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException,Exception {
        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelCaseConstant.CaseExportCaseConf caseExportCaseConf = ExcelCaseConstant.CaseExportCaseConf.getEnumByKey(entry.getKey().toString());
                if (caseExportCaseConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(caseExportCaseConf.getAttr())) {
                    exportKeyList.add(caseExportCaseConf.getAttr());
                }
                colMap.put(caseExportCaseConf.getCol(), caseExportCaseConf.getCol());
            }
        }

        ExcelCaseConstant.CaseExportCase caseExportCases[]= ExcelCaseConstant.CaseExportCase.values();
        List<ExcelCaseConstant.CaseExportCase> caseExportCases2 = new ArrayList<ExcelCaseConstant.CaseExportCase>();

        for (int i=0;i<caseExportCases.length;i++){
            ExcelCaseConstant.CaseExportCase caseListTemp = caseExportCases[i];
            if (colMap.get(caseListTemp.getCol())!=null){
                caseExportCases2.add(caseListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);
        WebResponse result = dataCaseService.pageCaseList(bean);

        String fileName = "案件管理当前页导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List<DataCaseEntity> list = ((CaseResponse)result.getData()).getPageInfo().getList();
        ExcelCaseUtils.exportExcel(list,
                        caseExportCases2.toArray(new ExcelCaseConstant.CaseExportCase[caseExportCases2.size()]),
                fileName+ ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所有", notes = "查询导出所有")
    @PostMapping("/dataCase/totalDataBatchExport")
    public Object totalDataBatchExport(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelCaseConstant.CaseExportCaseConf caseExportCaseConf = ExcelCaseConstant.CaseExportCaseConf.getEnumByKey(entry.getKey().toString());
                if (caseExportCaseConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(caseExportCaseConf.getAttr())) {
                    exportKeyList.add(caseExportCaseConf.getAttr());
                }
                colMap.put(caseExportCaseConf.getCol(), caseExportCaseConf.getCol());
            }
        }

        ExcelCaseConstant.CaseExportCase caseExportCases[]= ExcelCaseConstant.CaseExportCase.values();
        List<ExcelCaseConstant.CaseExportCase> caseExportCases2 = new ArrayList<ExcelCaseConstant.CaseExportCase>();

        for (int i=0;i<caseExportCases.length;i++){
            ExcelCaseConstant.CaseExportCase caseListTemp = caseExportCases[i];
            if (colMap.get(caseListTemp.getCol())!=null){
                caseExportCases2.add(caseListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);
        logger.info("导出查询开始");
        List<DataCaseEntity> list = dataCaseService.totalCaseListExport(bean);
        logger.info("导出查询结束");
        String fileName = "案件管理全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        ExcelCaseUtils.exportExcel(list,
                caseExportCases2.toArray(new ExcelCaseConstant.CaseExportCase[caseExportCases2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选", notes = "查询导出所选")
    @PostMapping("/dataCase/selectDataCaseExport")
    public Object selectDataCaseExport(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelCaseConstant.CaseExportCaseConf caseExportCaseConf = ExcelCaseConstant.CaseExportCaseConf.getEnumByKey(entry.getKey().toString());
                if (caseExportCaseConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(caseExportCaseConf.getAttr())) {
                    exportKeyList.add(caseExportCaseConf.getAttr());
                }
                colMap.put(caseExportCaseConf.getCol(), caseExportCaseConf.getCol());
            }
        }

        ExcelCaseConstant.CaseExportCase caseExportCases[]= ExcelCaseConstant.CaseExportCase.values();
        List<ExcelCaseConstant.CaseExportCase> caseExportCases2 = new ArrayList<ExcelCaseConstant.CaseExportCase>();

        for (int i=0;i<caseExportCases.length;i++){
            ExcelCaseConstant.CaseExportCase caseListTemp = caseExportCases[i];
            if (colMap.get(caseListTemp.getCol())!=null){
                caseExportCases2.add(caseListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);

        List<DataCaseEntity> resultList = dataCaseService.selectCaseListExport(bean);
        String fileName = "案件管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        ExcelCaseUtils.exportExcel(resultList,
                caseExportCases2.toArray(new ExcelCaseConstant.CaseExportCase[caseExportCases2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出所选操作记录", notes = "查询导出所选")
    @PostMapping("/dataCase/selectOpExport")
    public Object selectOpExport(@RequestBody DataOpLog bean, HttpServletResponse response) throws IOException, InvalidFormatException {

        List<DataOpLog> resultList = dataLogService.listDataOpLogByCaseId(bean);
        String fileName = "所选案件操作记录导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        ExcelUtils.exportExcel(resultList,
                ExcelOpConstant.LogMemorize.values(),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @ApiOperation(value = "查询导出批次案件", notes = "查询导出批次案件")
    @PostMapping("/dataCase/selectDataCaseExportByBatch")
    public Object selectDataCaseExportByBatch(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelCaseConstant.CaseExportCaseConf caseExportCaseConf = ExcelCaseConstant.CaseExportCaseConf.getEnumByKey(entry.getKey().toString());
                if (caseExportCaseConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(caseExportCaseConf.getAttr())) {
                    exportKeyList.add(caseExportCaseConf.getAttr());
                }
                colMap.put(caseExportCaseConf.getCol(), caseExportCaseConf.getCol());
            }
        }

        ExcelCaseConstant.CaseExportCase caseExportCases[]= ExcelCaseConstant.CaseExportCase.values();
        List<ExcelCaseConstant.CaseExportCase> caseExportCases2 = new ArrayList<ExcelCaseConstant.CaseExportCase>();

        for (int i=0;i<caseExportCases.length;i++){
            ExcelCaseConstant.CaseExportCase caseListTemp = caseExportCases[i];
            if (colMap.get(caseListTemp.getCol())!=null){
                caseExportCases2.add(caseListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);


        List<DataCaseEntity> resultList = dataCaseService.selectDataCaseExportByBatch(bean);
        String fileName = "案件管理选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        ExcelCaseUtils.exportExcel(resultList,
                caseExportCases2.toArray(new ExcelCaseConstant.CaseExportCase[caseExportCases2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }



    @ApiOperation(value = "查询导出所选案件的催收记录", notes = "查询导出所选案件的催收记录")
    @PostMapping("/dataCase/selectDataCollectExportByBatch")
    public Object selectDataCollectExportByCase(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {

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

        WebResponse webResponse = dataCollectService.selectDataCollectExportByCase(bean);
        List<DataCollectionEntity> resultList = (List<DataCollectionEntity>) webResponse.getData();

        String fileName = "案件管理催收记录选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
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

    @ApiOperation(value = "查询导出所选案件的电话", notes = "查询导出所选案件的电话")
    @PostMapping("/dataCase/selectDataCaseTel")
    public Object selectDataCaseTel(@RequestBody DataCaseEntity bean, HttpServletResponse response) throws IOException, InvalidFormatException {
        List exportKeyList = new ArrayList();

        Iterator iter = bean.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelCaseTelConstant.CaseTelConf caseTelExportCaseConf = ExcelCaseTelConstant.CaseTelConf.getEnumByKey(entry.getKey().toString());
                if (caseTelExportCaseConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(caseTelExportCaseConf.getAttr())) {
                    exportKeyList.add(caseTelExportCaseConf.getAttr());
                }
                colMap.put(caseTelExportCaseConf.getCol(), caseTelExportCaseConf.getCol());
            }
        }

        ExcelCaseTelConstant.CaseTel caseTelExportCases[]= ExcelCaseTelConstant.CaseTel.values();
        List<ExcelCaseTelConstant.CaseTel> caseTelExportCases2 = new ArrayList<ExcelCaseTelConstant.CaseTel>();

        for (int i=0;i<caseTelExportCases.length;i++){
            ExcelCaseTelConstant.CaseTel caseListTemp = caseTelExportCases[i];
            if (colMap.get(caseListTemp.getCol())!=null){
                caseTelExportCases2.add(caseListTemp);
            }
        }

        bean.setExportKeyList(exportKeyList);


        List<DataCaseTelExport> resultList = dataCaseService.selectCaseTelListExport(bean);

        String fileName = "案件管理电话选择导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(resultList,
                caseTelExportCases2.toArray(new ExcelCaseTelConstant.CaseTel[caseTelExportCases2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "查询案件明细", notes = "查询案件明细")
    @PostMapping("/dataCase/detail")
    public Object detail(@RequestBody DataCaseEntity bean) {
        logger.info("进入到详情");
        DataCaseDetail detail = dataCaseService.detail(bean);
        logger.info("出详情");
        return WebResponse.success(detail);

    }

    @ApiOperation(value = "修改案件电话状态", notes = "修改案件电话状态")
    @PostMapping("/tel/updateStatus")
    public Object updateTelStatus(@RequestBody List<DataCaseTelEntity> list) {
        for(int i=0;i< list.size();i++){
            DataCaseTelEntity bean = list.get(i);
            dataCaseService.updateCaseTelStatus(bean);
        }

        return WebResponse.success();
    }

    @ApiOperation(value = "查询案件详情电话列表", notes = "查询案件详情电话列表")
    @PostMapping("/dataCase/detail/telList")
    public Object telList(@RequestBody DataCaseEntity bean) {
        List<DataCaseTelEntity> list = dataCaseService.findTelListByCaseId(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "查询案件详情地址列表", notes = "查询案件详情地址列表")
    @PostMapping("/dataCase/detail/addressList")
    public Object addressList(@RequestBody DataCaseEntity bean) {
        List<DataCaseAddressEntity> list = dataCaseService.findAddressListByCaseId(bean);

        return WebResponse.success(list);

    }

    @ApiOperation(value = "更新案件电话", notes = "更新案件电话")
    @PostMapping("/dataCase/saveCaseTel")
    public Object updateCaseTel(@RequestBody DataCaseTelEntity bean) {
        DataCaseTelEntity dataCaseTelEntity = dataCaseService.saveCaseTel(bean);
        return WebResponse.success(dataCaseTelEntity);
    }

    @ApiOperation(value = "批量新增案件电话", notes = "批量新增案件电话")
    @PostMapping("/dataCase/addBatchCaseTel")
    public Object addBatchCaseTel(@RequestBody DataCaseTelEntity bean) {
        if (StringUtils.isEmpty(bean.getRemark())){
            WebResponse webResponse = WebResponse.buildResponse();
            webResponse.setMsg("内容不能为空");
            webResponse.setCode("500");
            return webResponse;

        }
        dataCaseService.addBatchCaseTel(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "删除案件电话", notes = "删除案件电话")
    @PostMapping("/dataCase/delCaseTel")
    public Object delCaseTel(@RequestBody DataCaseTelEntity bean) {
        dataCaseService.delCaseTel(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "同步共债电话", notes = "删除案件电话")
    @PostMapping("/dataCase/synchroSameTel")
    public Object synchroSameTel(@RequestBody DataCaseEntity bean) {
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse = dataCaseService.synchroSameTel(bean);
        return webResponse;
    }

    @ApiOperation(value = "查询同批次共债案件", notes = "查询同批次共债案件")
    @PostMapping("/dataCase/sameBatchCaseList")
    public Object sameBatchCaseList(@RequestBody DataCaseEntity bean) {
        return WebResponse.success(dataCaseService.sameBatchCaseList(bean));
    }

    @ApiOperation(value = "查询共债案件", notes = "查询共债案件")
    @PostMapping("/dataCase/sameCaseList")
    public Object sameCaseList(@RequestBody DataCaseEntity bean) {
        return WebResponse.success(dataCaseService.sameCaseList(bean));
    }

    @ApiOperation(value = "更新案件地址", notes = "更新案件地址")
    @PostMapping("/dataCase/saveCaseAddress")
    public Object saveCaseAddress(@RequestBody DataCaseAddressEntity bean) {
        dataCaseService.saveCaseAddress(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "修改案件地址状态", notes = "修改案件地址状态")
    @PostMapping("/address/updateStatus")
    public Object updateAddressStatus(@RequestBody List<DataCaseAddressEntity> list) {
        for(int i=0;i< list.size();i++){
            DataCaseAddressEntity bean = list.get(i);
            dataCaseService.updateCaseAddressStatus(bean);
        }

        return WebResponse.success();
    }

    @ApiOperation(value = "删除案件地址", notes = "删除案件地址")
    @PostMapping("/dataCase/delCaseAddress")
    public Object delCaseAddress(@RequestBody DataCaseAddressEntity bean) {
        dataCaseService.delCaseAddress(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "个案序列号模糊查询", notes = "个案序列号模糊查询")
    @PostMapping("/dataCase/pageSeqNos")
    public Object pageSeqNos(@RequestBody DataCaseEntity dataCaseEntity) {
        PageInfo<DataCaseEntity> pageInfo = dataCaseService.pageSeqNos(dataCaseEntity);
        return WebResponse.success(pageInfo);
    }

    @ApiOperation(value = "查询案件评语", notes = "查询案件评语")
    @PostMapping("/dataCase/listComment")
    public Object listComment(@RequestBody DataCaseEntity dataCaseEntity) {
        List<DataCaseCommentEntity> list = dataCaseService.listComment(dataCaseEntity);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "根据评语id查询评语", notes = "查询案件评语")
    @PostMapping("/dataCase/detailComment")
    public Object detailComment(@RequestBody DataCaseCommentEntity bean) {
        DataCaseCommentEntity dataCaseCommentEntity = dataCaseService.detailComment(bean);
        return WebResponse.success(dataCaseCommentEntity);
    }

    @ApiOperation(value = "根据评语id修改评语", notes = "根据评语id修改评语")
    @PostMapping("/dataCase/updateComment")
    public Object updateComment(@RequestBody DataCaseCommentEntity bean) {
        dataCaseService.updateComment(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "根据评语id删除评语", notes = "根据评语id删除评语")
    @PostMapping("/dataCase/delComment")
    public Object delComment(@RequestBody DataCaseCommentEntity bean) {
        dataCaseService.delComment(bean);
        return WebResponse.success();
    }


    @ApiOperation(value = "查询利息更新", notes = "查询利息更新")
    @PostMapping("/dataCase/listInterest")
    public Object listInterest(@RequestBody DataCaseEntity dataCaseEntity) {
        List<DataCaseInterestEntity> list = dataCaseService.listInterest(dataCaseEntity);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "查询协催", notes = "查询协催")
    @PostMapping("/dataCase/listSynergy")
    public Object listSynergy(@RequestBody DataCaseEntity dataCaseEntity) {
        List<DataCaseEntity> list = dataCaseService.listSynergy(dataCaseEntity);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "修改催收小结", notes = "修改催收小结")
    @PostMapping("/dataCase/updateCollectInfo")
    public Object updateCollectInfo(@RequestBody DataCaseEntity dataCaseEntity) {
        dataCaseService.updateCollectInfo(dataCaseEntity);
        return WebResponse.success();
    }



}
