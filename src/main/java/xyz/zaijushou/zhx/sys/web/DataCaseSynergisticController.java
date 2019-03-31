package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelSynergisticConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseSynergisticService;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.ExcelUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Api("协催管理/待处理协催、协催记录")
@RestController
@RequestMapping("/synergistic")
public class DataCaseSynergisticController {

    @Resource
    private DataCaseSynergisticService dataCaseSynergisticService;

    @Resource
    private SysUserService sysUserService;

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @PostMapping("/types")
    public Object types() {
        JSONArray typeList = new JSONArray();
        JSONObject typeAll = new JSONObject();
        typeAll.put("key", "");
        typeAll.put("typeName", "全部");
        typeList.add(typeAll);
        List<SysDictionaryEntity> types = dataCaseSynergisticService.listAllTypes();
        for(SysDictionaryEntity  dict : types) {
            JSONObject type = new JSONObject();
            type.put("key", dict.getId() + "");
            type.put("typeName", dict.getName());
            typeList.add(type);
        }
        return WebResponse.success(typeList);
    }

    @PostMapping("/typesWithCount")
    public Object typesWithCount(@RequestBody DataCaseSynergisticEntity synergistic) {
        List<DataCaseSynergisticEntity> synergisticTypeCountNumLists =  dataCaseSynergisticService.countNum(synergistic);
        JSONArray typeList = new JSONArray();
        int total = 0;
        for(DataCaseSynergisticEntity  dict : synergisticTypeCountNumLists) {
            JSONObject type = new JSONObject();
            type.put("id", dict.getSynergisticType().getId());
            type.put("name", dict.getSynergisticType().getName());
            type.put("count", dict.getCountNum() == null ? 0 : dict.getCountNum());
            typeList.add(type);
            total += dict.getCountNum();
        }
        JSONObject typeAll = new JSONObject();
        typeAll.put("id", "");
        typeAll.put("name", "全部");
        typeAll.put("count", total);
        typeList.add(0, typeAll);
        return WebResponse.success(typeList);
    }

    @PostMapping("/list")
    public Object list(@RequestBody DataCaseSynergisticEntity synergistic) {
        PageInfo<DataCaseSynergisticEntity> pageInfo = dataCaseSynergisticService.pageSynergisticList(synergistic);
        return WebResponse.success(pageInfo);
    }

    @PostMapping("/approve")
    public Object approve(@RequestBody DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticService.approve(synergistic);
        return WebResponse.success();
    }

    @PostMapping("/finish")
    public Object finish(@RequestBody DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticService.finish(synergistic);
        return WebResponse.success();
    }

    @PostMapping("/queryDataExport")
    public Object queryDataExport(@RequestBody DataCaseSynergisticEntity synergistic, HttpServletResponse response) throws IOException {
        List<DataCaseSynergisticEntity> list = dataCaseSynergisticService.listSynergistic(synergistic);
        Set<String> userIdsSet = new HashSet<>();
        Set<String> clientSet = new HashSet<>();
        for(DataCaseSynergisticEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
            if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                clientSet.add(RedisKeyPrefix.SYS_DIC + entity.getDataCase().getClient());
            }
            SysDictionaryEntity contextEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getApplyContent(),SysDictionaryEntity.class);
            entity.setApplyContent(contextEntity==null?"":contextEntity.getName());
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for (DataCaseSynergisticEntity entity : list) {
                if (entity != null && entity.getDataCase() != null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    entity.getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }
        if(!CollectionUtils.isEmpty(clientSet)) {
            List<SysDictionaryEntity> clientList = RedisUtils.scanEntityWithKeys(clientSet, SysDictionaryEntity.class);
            Map<String, SysDictionaryEntity> clientMap = new HashMap<>();
            for(SysDictionaryEntity entity : clientList) {
                clientMap.put(entity.getId() + "", entity);
            }
            for (DataCaseSynergisticEntity entity : list) {
                if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                    if(clientMap.get(entity.getDataCase().getClient()) != null) {
                        entity.getDataCase().setClient(clientMap.get(entity.getDataCase().getClient()).getName());
                    }
                }
            }
        }

        String fileName = "导出协催查询结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(
                list,
                ExcelSynergisticConstant.SynergisticExport.values(),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/pageDataExport")
    public Object pageDataExport(@RequestBody DataCaseSynergisticEntity synergistic, HttpServletResponse response) throws IOException {
        List<DataCaseSynergisticEntity> list = dataCaseSynergisticService.pageSynergisticList(synergistic).getList();
        Set<String> userIdsSet = new HashSet<>();
        Set<String> clientSet = new HashSet<>();
        for(DataCaseSynergisticEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
            if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                clientSet.add(RedisKeyPrefix.SYS_DIC + entity.getDataCase().getClient());
            }

            SysDictionaryEntity contextEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getApplyContent(),SysDictionaryEntity.class);
            entity.setApplyContent(contextEntity==null?"":contextEntity.getName());
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for(int i = 0; i < list.size(); i ++) {
                DataCaseSynergisticEntity entity = list.get(i);
                if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    list.get(i).getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }
        if(!CollectionUtils.isEmpty(clientSet)) {
            List<SysDictionaryEntity> clientList = RedisUtils.scanEntityWithKeys(clientSet, SysDictionaryEntity.class);
            Map<String, SysDictionaryEntity> clientMap = new HashMap<>();
            for(SysDictionaryEntity entity : clientList) {
                clientMap.put(entity.getId() + "", entity);
            }
            for (DataCaseSynergisticEntity entity : list) {
                if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                    if(clientMap.get(entity.getDataCase().getClient()) != null) {
                        entity.getDataCase().setClient(clientMap.get(entity.getDataCase().getClient()).getName());
                    }
                }
            }
        }

        String fileName = "导出协催当前页结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        ExcelUtils.exportExcel(
                list,
                ExcelSynergisticConstant.SynergisticExport.values(),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("/selectDataExport")
    public Object selectDataExport(@RequestBody DataCaseSynergisticEntity synergistic, HttpServletResponse response) throws IOException {

        String fileName ="导出协催选中结果" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        if(synergistic == null || synergistic.getIds() == null || synergistic.getIds().length == 0) {
            ExcelUtils.exportExcel(
                    new ArrayList<>(),
                    ExcelSynergisticConstant.SynergisticExport.values(),
                    fileName + ".xlsx",
                    response
            );
            return null;
        }
        List<DataCaseSynergisticEntity> list = dataCaseSynergisticService.listSynergistic(synergistic);
        Set<String> userIdsSet = new HashSet<>();
        Set<String> clientSet = new HashSet<>();
        for(DataCaseSynergisticEntity entity : list) {
            if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                userIdsSet.add(RedisKeyPrefix.USER_INFO + entity.getDataCase().getCollectionUser().getId());
            }
            if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                clientSet.add(RedisKeyPrefix.SYS_DIC + entity.getDataCase().getClient());
            }

            SysDictionaryEntity contextEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getApplyContent(),SysDictionaryEntity.class);
            entity.setApplyContent(contextEntity==null?"":contextEntity.getName());
        }
        if(!CollectionUtils.isEmpty(userIdsSet)) {
            List<SysNewUserEntity> userList = RedisUtils.scanEntityWithKeys(userIdsSet, SysNewUserEntity.class);
            Map<Integer, SysNewUserEntity> userMap = CollectionsUtils.listToMap(userList);
            for(int i = 0; i < list.size(); i ++) {
                DataCaseSynergisticEntity entity = list.get(i);
                if(entity != null && entity.getDataCase()!= null && entity.getDataCase().getCollectionUser() != null && entity.getDataCase().getCollectionUser().getId() != null) {
                    list.get(i).getDataCase().setCollectionUser(userMap.get(entity.getDataCase().getCollectionUser().getId()));
                }
            }
        }
        if(!CollectionUtils.isEmpty(clientSet)) {
            List<SysDictionaryEntity> clientList = RedisUtils.scanEntityWithKeys(clientSet, SysDictionaryEntity.class);
            Map<String, SysDictionaryEntity> clientMap = new HashMap<>();
            for(SysDictionaryEntity entity : clientList) {
                clientMap.put(entity.getId() + "", entity);
            }
            for (DataCaseSynergisticEntity entity : list) {
                if(entity != null && entity.getDataCase() != null && StringUtils.isNotEmpty(entity.getDataCase().getClient())) {
                    if(clientMap.get(entity.getDataCase().getClient()) != null) {
                        entity.getDataCase().setClient(clientMap.get(entity.getDataCase().getClient()).getName());
                    }
                }
            }
        }
        ExcelUtils.exportExcel(
                list,
                ExcelSynergisticConstant.SynergisticExport.values(),
                fileName + ".xlsx",
                response
        );
        return null;
    }

    @PostMapping("finishedSynergisticImport")
    public Object finishedSynergisticImport(MultipartFile file) throws IOException {
        List<DataCaseSynergisticEntity> synergisticList = ExcelUtils.importExcel(file, ExcelSynergisticConstant.FinishedSynergisticImport.values(), DataCaseSynergisticEntity.class);
        if(CollectionUtils.isEmpty(synergisticList)) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "上传了空模板");
        }

        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        Map<Integer, Integer> idCountMap = new HashMap<>();
        Set<Integer> idsSet = new HashSet<>();
        Set<String> userNamesSet = new HashSet<>();
        for(int i = 0; i < synergisticList.size(); i ++) {
            DataCaseSynergisticEntity entity = synergisticList.get(i);
            if(entity.getId() == null) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "第" + (i + 2) + "行未填写ID，请填写后上传，并检查excel的ID是否均填写了");
            }
            idsSet.add(entity.getId());
            if(!idCountMap.containsKey(entity.getId())) {
                idCountMap.put(entity.getId(), 1);
            } else {
                idCountMap.put(entity.getId(), idCountMap.get(entity.getId()) + 1);
            }
            if(synergisticList.get(i).getSynergisticUser() != null && StringUtils.isNotEmpty(synergisticList.get(i).getSynergisticUser().getUserName()) ) {
                userNamesSet.add(synergisticList.get(i).getSynergisticUser().getUserName());
            }
        }
        for(Map.Entry<Integer, Integer> entry : idCountMap.entrySet()) {
            if(entry.getValue() > 1) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "ID:" + entry.getKey() + "重复，请检查后上传");
            }
        }
        DataCaseSynergisticEntity queryEntity = new DataCaseSynergisticEntity();
        queryEntity.setIdsSet(idsSet);
        List<DataCaseSynergisticEntity> existList = dataCaseSynergisticService.listByIdsSet(queryEntity);
        Map<Integer, DataCaseSynergisticEntity> existMap = CollectionsUtils.listToMap(existList);
        SysNewUserEntity queryUser = new SysNewUserEntity();
        queryUser.setNamesSet(userNamesSet);
        List<SysNewUserEntity> uploadUserList = sysUserService.listByNameSet(queryUser);
        Map<String, SysNewUserEntity> uploadUserMap = new HashMap<>();
        for(SysNewUserEntity user : uploadUserList) {
            uploadUserMap.put(user.getUserName(), user);
        }
        for (DataCaseSynergisticEntity entity : synergisticList) {
            if (!existMap.containsKey(entity.getId())) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "ID: " + entity.getId() + " 有误，请检查后上传");
            }
            if (entity.getSynergisticTime() == null) {
                entity.setSynergisticTime(new Date());
            }
            if (entity.getSynergisticUser() == null || entity.getSynergisticUser().getUserName() == null || !uploadUserMap.containsKey(entity.getSynergisticUser().getUserName())) {
                SysNewUserEntity user = new SysNewUserEntity();
                user.setId(JwtTokenUtil.tokenData().getInteger("userId"));
                entity.setSynergisticUser(user);
            } else {
                entity.setSynergisticUser(uploadUserMap.get(entity.getSynergisticUser().getUserName()));
            }
            entity.setApplyStatus("1");
            entity.setFinishStatus("1");
        }
        for(DataCaseSynergisticEntity entity : synergisticList) {
            dataCaseSynergisticService.updateInfo(entity);
        }

        return WebResponse.success();
    }

    @PostMapping("synergisticRecordImport")
    public Object synergisticRecordImport(MultipartFile file) throws IOException {
        List<DataCaseSynergisticEntity> synergisticList = ExcelUtils.importExcel(file, ExcelSynergisticConstant.SynergisticRecordImport.values(), DataCaseSynergisticEntity.class);
        if(CollectionUtils.isEmpty(synergisticList)) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "上传了空模板");
        }
//        Map<Integer, Integer> idCountMap = new HashMap<>();
//        Set<Integer> idsSet = new HashSet<>();

        String fileName = file.getOriginalFilename();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysOperationLogEntity operationLog = new SysOperationLogEntity();
        operationLog.setRequestBody(fileName);
        operationLog.setUserId(userId);
        sysOperationLogService.insertRequest(operationLog);

        Set<String> redisKeySet = new HashSet<>();
        Map<String, Integer> redisKeyCountMap = new HashMap<>();
        Set<String> userNamesSet = new HashSet<>();
        for(int i = 0; i < synergisticList.size(); i ++) {
            DataCaseSynergisticEntity entity = synergisticList.get(i);
            if(entity == null || entity.getDataCase() == null || StringUtils.isEmpty(entity.getDataCase().getCardNo()) || entity.getDataCase().getCaseDate() == null) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "第" + (i + 2) + "行未填写完整的卡号和委案日期，请填写后上传，并检查excel的卡号和委案日期是否均填写了");
            }
            String redisKey = RedisKeyPrefix.DATA_CASE + entity.getDataCase().getCardNo() + "@" + entity.getDataCase().getCaseDate();
            redisKeySet.add(redisKey);
            if(!redisKeyCountMap.containsKey(redisKey)) {
                redisKeyCountMap.put(redisKey, 1);
            } else {
                redisKeyCountMap.put(redisKey, redisKeyCountMap.get(redisKey) + 1);
            }
            if(synergisticList.get(i).getSynergisticUser() != null && StringUtils.isNotEmpty(synergisticList.get(i).getSynergisticUser().getUserName()) ) {
                userNamesSet.add(synergisticList.get(i).getSynergisticUser().getUserName());
            }

            if (StringUtils.isNotEmpty(entity.getApplyContent())){
                SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getApplyContent(),SysDictionaryEntity.class);
                if (sysDictionaryEntity==null){
                    return WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (i + 2) + "行申请内容值"+entity.getApplyContent()+"不在枚举配置中，并检查excel的申请内容是否均填写正确");
                }else{
                    synergisticList.get(i).setApplyContent(sysDictionaryEntity.getId()+"");
                }
            }

        }
        for(Map.Entry<String, Integer> entry : redisKeyCountMap.entrySet()) {
            if(entry.getValue() > 1) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "卡号@日期：" + entry.getKey() + "重复，请检查后上传");
            }
        }
//        DataCaseSynergisticEntity queryEntity = new DataCaseSynergisticEntity();
//        queryEntity.setIdsSet(idsSet);
//        List<DataCaseSynergisticEntity> existList = dataCaseSynergisticService.listByIdsSet(queryEntity);
        List<DataCaseEntity> existCaseList = RedisUtils.scanEntityWithKeys(redisKeySet, DataCaseEntity.class);
        Map<String, DataCaseEntity> existMap = new HashMap<>();
        for(DataCaseEntity entity : existCaseList) {
            existMap.put(entity.getCardNo() + "@" + entity.getCaseDate(), entity);
        }

        SysNewUserEntity queryUser = new SysNewUserEntity();
        queryUser.setNamesSet(userNamesSet);
        List<SysNewUserEntity> uploadUserList = sysUserService.listByNameSet(queryUser);
        Map<String, SysNewUserEntity> uploadUserMap = new HashMap<>();
        for(SysNewUserEntity user : uploadUserList) {
            uploadUserMap.put(user.getUserName(), user);
        }
        for (DataCaseSynergisticEntity entity : synergisticList) {
            String redisKey = entity.getDataCase().getCardNo() + "@" + entity.getDataCase().getCaseDate();
            if (!existMap.containsKey(redisKey)) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "卡号@日期：" + redisKey + " 有误，找不到对应的案件，请检查后上传");
            }
            entity.setDataCase(existMap.get(redisKey));
            if (entity.getSynergisticTime() == null) {
                entity.setSynergisticTime(new Date());
            }
            if (entity.getSynergisticUser() == null || entity.getSynergisticUser().getUserName() == null || !uploadUserMap.containsKey(entity.getSynergisticUser().getUserName())) {
                SysNewUserEntity user = new SysNewUserEntity();
                user.setId(JwtTokenUtil.tokenData().getInteger("userId"));
                entity.setSynergisticUser(user);
            } else {
                entity.setSynergisticUser(uploadUserMap.get(entity.getSynergisticUser().getUserName()));
            }

            entity.setApplyStatus("1");
            entity.setFinishStatus("1");
        }
        for(DataCaseSynergisticEntity entity : synergisticList) {
            dataCaseSynergisticService.updateInfoByCaseId(entity);
        }

        return WebResponse.success();
    }


    @PostMapping("/saveApply")
    public Object saveApply(@RequestBody DataCaseSynergyDetailEntity bean) {
        dataCaseSynergisticService.saveApply(bean);
        return WebResponse.success();
    }

    @PostMapping("/saveResult")
    public Object saveResult(@RequestBody DataCaseSynergyDetailEntity bean){
        dataCaseSynergisticService.saveResult(bean);
        return WebResponse.success();
    }

}
