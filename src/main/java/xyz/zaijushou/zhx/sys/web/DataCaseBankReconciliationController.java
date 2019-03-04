package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelBankReconciliationConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.RepayTypeEnum;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.DataCaseBankReconciliationEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseBankReconciliationService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.RedisUtils;

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

    @PostMapping("/cancel")
    public Object cancel(@RequestBody DataCaseBankReconciliationEntity entity) {
        if(entity == null || entity.getIds() == null || entity.getIds().length == 0) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请输入ids");
        }
        dataCaseBankReconciliationService.cancel(entity);
        return WebResponse.success();
    }

    //todo 三种导出 导入
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
                "导出银行对账选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }
    //todo caseArea

}
