package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelBankReconciliationConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.RepayTypeEnum;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.dao.DataCaseRemarkMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseBankReconciliationService;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
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
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationService.listBankReconciliation(bankReconciliationEntity);
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
                ExcelBankReconciliationConstant.BankReconciliationExport.values(),
                "导出银行对账查询结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/pageDataExport")
    public Object pageDataExport(@RequestBody DataCaseBankReconciliationEntity bankReconciliationEntity, HttpServletResponse response) throws IOException {
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationService.pageDataList(bankReconciliationEntity).getList();
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
                ExcelBankReconciliationConstant.BankReconciliationExport.values(),
                "导出银行对账当前页结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/selectDataExport")
    public Object selectDataExport(@RequestBody DataCaseBankReconciliationEntity bankReconciliationEntity, HttpServletResponse response) throws IOException {
        if(bankReconciliationEntity == null || bankReconciliationEntity.getIds() == null || bankReconciliationEntity.getIds().length == 0) {
            ExcelUtils.exportExcel(
                    new ArrayList<>(),
                    ExcelBankReconciliationConstant.BankReconciliationExport.values(),
                    "导出银行对账选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                    response
            );
            return null;
        }
        List<DataCaseBankReconciliationEntity> list = dataCaseBankReconciliationService.listBankReconciliation(bankReconciliationEntity);
        ExcelUtils.exportExcel(
                list,

                ExcelBankReconciliationConstant.BankReconciliationExport.values(),
                "导出银行对账选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
   }
    //todo caseArea

    @ApiOperation(value = "导入CP", notes = "导入CP")
    @PostMapping("/import")
    public Object dataCaseNewCaseImport(MultipartFile file) throws IOException {
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
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
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
