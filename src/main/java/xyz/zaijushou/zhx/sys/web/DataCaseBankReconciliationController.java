package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.dao.DataCaseRemarkMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseBankReconciliationService;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/bankReconciliation")
public class DataCaseBankReconciliationController {

    @Resource
    private DataCaseBankReconciliationService dataCaseBankReconciliationService;

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @Resource
    private DataCaseService dataCaseService;

    @PostMapping("/listRepayType")
    public Object listRepayType() {
        JSONArray typeList = new JSONArray();
        JSONObject typeAll = new JSONObject();
        typeAll.put("code", "");
        typeAll.put("typeName", "全部");
        typeList.add(typeAll);
        for(RepayTypeEnum value : RepayTypeEnum.values()) {
            JSONObject type = new JSONObject();
            type.put("code", value.getCode());
            type.put("typeName", value.getTypeName());
            typeList.add(type);
        }
        return WebResponse.success(typeList);
    }

    @PostMapping("/list")
    public Object list(@RequestBody DataCaseBankReconciliationEntity entity) {
        PageInfo<DataCaseBankReconciliationEntity> pageInfo = dataCaseBankReconciliationService.pageDataList(entity);
        return WebResponse.success(pageInfo);
    }

    @PostMapping("/listByCaseId")
    public Object listByCaseId(@RequestBody DataCaseBankReconciliationEntity entity) {
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationService.listByCaseId(entity);
        return WebResponse.success(list);
    }

    @PostMapping("/saveBank")
    public Object saveBank(@RequestBody DataCaseBankReconciliationEntity entity) {
        dataCaseBankReconciliationService.saveBank(entity);
        return WebResponse.success();
    }

    @PostMapping("/cancel")
    public Object cancel(@RequestBody DataCaseBankReconciliationEntity entity) {
        if(entity == null || entity.getIds() == null || entity.getIds().length == 0) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请输入ids");
        }
        dataCaseBankReconciliationService.cancel(entity);
        return WebResponse.success();
    }

    @PostMapping("/queryDataExport")
    public Object queryDataExport(@RequestBody DataCaseBankReconciliationEntity bankReconciliationEntity, HttpServletResponse response) throws IOException {

        String fileName = "导出银行对账查询结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List exportKeyList = new ArrayList();
        Iterator iter = bankReconciliationEntity.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelBankReconciliationConstant.BankReconciliationExportConf bankReconciliationExportConf = ExcelBankReconciliationConstant.BankReconciliationExportConf.getEnumByKey(entry.getKey().toString());
                if (bankReconciliationExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(bankReconciliationExportConf.getAttr())) {
                    exportKeyList.add(bankReconciliationExportConf.getAttr());
                }
                colMap.put(bankReconciliationExportConf.getCol(), bankReconciliationExportConf.getCol());
            }
        }

        ExcelBankReconciliationConstant.BankReconciliationExport bankReconciliationExports[]= ExcelBankReconciliationConstant.BankReconciliationExport.values();
        List<ExcelBankReconciliationConstant.BankReconciliationExport> bankReconciliationExports2 = new ArrayList<ExcelBankReconciliationConstant.BankReconciliationExport>();

        for (int i=0;i<bankReconciliationExports.length;i++){
            ExcelBankReconciliationConstant.BankReconciliationExport bankListTemp = bankReconciliationExports[i];
            if (colMap.get(bankListTemp.getCol())!=null){
                bankReconciliationExports2.add(bankListTemp);
            }

        }
        bankReconciliationEntity.setExportKeyList(exportKeyList);

        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationService.totalExport(bankReconciliationEntity);
        Set<String> userIdsSet = new HashSet<>();
        for(DataCaseBankReconciliationEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for(int i = 0; i < list.size(); i ++) {
                DataCaseBankReconciliationEntity entity = list.get(i);
                if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    list.get(i).getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }

        ExcelUtils.exportExcel(
                list,
                bankReconciliationExports2.toArray(new ExcelBankReconciliationConstant.BankReconciliationExport[bankReconciliationExports2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/pageDataExport")
    public Object pageDataExport(@RequestBody DataCaseBankReconciliationEntity bankReconciliationEntity, HttpServletResponse response) throws IOException {

        String fileName = "导出银行对账当前页结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List exportKeyList = new ArrayList();
        Iterator iter = bankReconciliationEntity.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelBankReconciliationConstant.BankReconciliationExportConf bankReconciliationExportConf = ExcelBankReconciliationConstant.BankReconciliationExportConf.getEnumByKey(entry.getKey().toString());
                if (bankReconciliationExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(bankReconciliationExportConf.getAttr())) {
                    exportKeyList.add(bankReconciliationExportConf.getAttr());
                }
                colMap.put(bankReconciliationExportConf.getCol(), bankReconciliationExportConf.getCol());
            }
        }

        ExcelBankReconciliationConstant.BankReconciliationExport bankReconciliationExports[]= ExcelBankReconciliationConstant.BankReconciliationExport.values();
        List<ExcelBankReconciliationConstant.BankReconciliationExport> bankReconciliationExports2 = new ArrayList<ExcelBankReconciliationConstant.BankReconciliationExport>();

        for (int i=0;i<bankReconciliationExports.length;i++){
            ExcelBankReconciliationConstant.BankReconciliationExport bankListTemp = bankReconciliationExports[i];
            if (colMap.get(bankListTemp.getCol())!=null){
                bankReconciliationExports2.add(bankListTemp);
            }

        }
        bankReconciliationEntity.setExportKeyList(exportKeyList);

        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationService.pageDataListExport(bankReconciliationEntity).getList();
        Set<String> userIdsSet = new HashSet<>();
        for(DataCaseBankReconciliationEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for(int i = 0; i < list.size(); i ++) {
                DataCaseBankReconciliationEntity entity = list.get(i);
                if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    list.get(i).getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }

        ExcelUtils.exportExcel(
                list,
                bankReconciliationExports2.toArray(new ExcelBankReconciliationConstant.BankReconciliationExport[bankReconciliationExports2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/selectDataExport")
    public Object selectDataExport(@RequestBody DataCaseBankReconciliationEntity bankReconciliationEntity, HttpServletResponse response) throws IOException {
        List exportKeyList = new ArrayList();
        Iterator iter = bankReconciliationEntity.getExportConf().entrySet().iterator(); // 获得map的Iterator
        Map colMap = new HashMap();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            if ((Boolean) entry.getValue()){
                ExcelBankReconciliationConstant.BankReconciliationExportConf bankReconciliationExportConf = ExcelBankReconciliationConstant.BankReconciliationExportConf.getEnumByKey(entry.getKey().toString());
                if (bankReconciliationExportConf!=null && xyz.zaijushou.zhx.utils.StringUtils.notEmpty(bankReconciliationExportConf.getAttr())) {
                    exportKeyList.add(bankReconciliationExportConf.getAttr());
                }
                colMap.put(bankReconciliationExportConf.getCol(), bankReconciliationExportConf.getCol());
            }
        }

        ExcelBankReconciliationConstant.BankReconciliationExport bankReconciliationExports[]= ExcelBankReconciliationConstant.BankReconciliationExport.values();
        List<ExcelBankReconciliationConstant.BankReconciliationExport> bankReconciliationExports2 = new ArrayList<ExcelBankReconciliationConstant.BankReconciliationExport>();

        for (int i=0;i<bankReconciliationExports.length;i++){
            ExcelBankReconciliationConstant.BankReconciliationExport bankListTemp = bankReconciliationExports[i];
            if (colMap.get(bankListTemp.getCol())!=null){
                bankReconciliationExports2.add(bankListTemp);
            }

        }
        bankReconciliationEntity.setExportKeyList(exportKeyList);

        if(bankReconciliationEntity == null || bankReconciliationEntity.getIds() == null || bankReconciliationEntity.getIds().length == 0) {
            String fileName = "导出银行对账选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
            Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
            SysOperationLogEntity operationLog = new SysOperationLogEntity();
            operationLog.setRequestBody(fileName);
            operationLog.setUserId(userId);
            sysOperationLogService.insertRequest(operationLog);
            ExcelUtils.exportExcel(
                    new ArrayList<>(),
                    bankReconciliationExports2.toArray(new ExcelBankReconciliationConstant.BankReconciliationExport[bankReconciliationExports2.size()]),
                    fileName + ".xlsx",
                    response
            );
            return null;
        }
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationService.totalExport(bankReconciliationEntity);
        String fileName = "导出银行对账选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);
        ExcelUtils.exportExcel(
                list,
                bankReconciliationExports2.toArray(new ExcelBankReconciliationConstant.BankReconciliationExport[bankReconciliationExports2.size()]),
                fileName + ".xlsx",
                response
        );
        return null;
   }
    //todo caseArea

    @ApiOperation(value = "导入CP", notes = "导入CP")
    @PostMapping("/import")
    public Object dataCaseNewCaseImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        List<DataCaseBankReconciliationEntity> dataEntities = ExcelUtils.importExcel(file, ExcelBankReconciliationConstant.BankReconciliationImport.values(), DataCaseBankReconciliationEntity.class);;
        if(dataEntities.size() == 0) {
            return WebResponse.success("添加0条数据");
        }
        Map<String, Integer> countMap = new HashMap<>();
        Set<String> seqNoSet = new HashSet<>();
        for(int i = 0; i < dataEntities.size(); i ++) {
            if(dataEntities.get(i).getDataCase() == null || StringUtils.isEmpty(dataEntities.get(i).getDataCase().getSeqNo())) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行未填写个案序列号，请填写后上传，并检查excel的个案序列号是否均填写了");
            }
            seqNoSet.add(dataEntities.get(i).getDataCase().getSeqNo());
            countMap.put(dataEntities.get(i).getDataCase().getSeqNo(), countMap.get(dataEntities.get(i).getDataCase().getSeqNo()) == null ? 1 : countMap.get(dataEntities.get(i).getDataCase().getSeqNo()) + 1);
        }
        StringBuilder builder = new StringBuilder();
        for(Map.Entry<String, Integer> entry : countMap.entrySet()) {
            if(entry.getValue() > 1) {
                builder.append(entry.getKey()).append(" ");
            }
        }
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(builder.toString())) {
            return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "存在重复的个案序列号：" + builder.toString());
        }
        DataCaseEntity queryEntity = new DataCaseEntity();
        queryEntity.setSeqNoSet(seqNoSet);
        List<DataCaseEntity> existCaseList = dataCaseService.listBySeqNoSet(queryEntity);
        Map<String, DataCaseEntity> existCaseMap = new HashMap<>();
        for(DataCaseEntity entity : existCaseList) {
            existCaseMap.put(entity.getSeqNo(), entity);
        }
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        for (DataCaseBankReconciliationEntity entity : dataEntities) {
            if (!existCaseMap.containsKey(entity.getDataCase().getSeqNo())) {
                return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "个案序列号:" + entity.getDataCase().getSeqNo() + "不存在，请确认后重新上传");
            }
            entity.setDataCase(existCaseMap.get(entity.getDataCase().getSeqNo()));
            entity.setCreateUser(user);
            entity.setUpdateUser(user);
        }
        dataCaseBankReconciliationService.addList(dataEntities);
        return WebResponse.success();
    }
}
