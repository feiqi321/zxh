package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogTypeEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.TemplateUtils;

import javax.annotation.Resource;
import java.util.*;

@RestController
@RequestMapping("/operationLog")
public class SysOperationLogController {

    private static Logger logger = LoggerFactory.getLogger(SysOperationLogController.class);

    @Resource
    private SysOperationLogService sysOperationLogService;

    @Resource
    private SysUserService sysUserService;

    @PostMapping("/pageLogs")
    public Object pageLogs(@RequestBody SysOperationLogEntity operationLog) {
        if(operationLog != null && operationLog.getUserId() == null && StringUtils.isNoneBlank(operationLog.getUserName())) {
            SysUserEntity user = new SysUserEntity();
            user.setUserName(operationLog.getUserName());
            List<SysUserEntity> users = sysUserService.listUsers(user);
            if(CollectionUtils.isEmpty(users)) {
                return WebResponse.success(new PageInfo<>());
            }
            List<Integer> userIds = new ArrayList<>();
            for(SysUserEntity userEntity : users) {
                userIds.add(userEntity.getId());
            }
            operationLog.setUserIds(userIds);
        }
        List<SysOperationLogTypeEntity> typeList = sysOperationLogService.listLogType(new SysOperationLogTypeEntity());
        if(operationLog != null && StringUtils.isBlank(operationLog.getUrl())) {

            if(CollectionUtils.isEmpty(typeList)) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "尚未配置日志类型，请联系管理员！");
            }
            List<String> urls = new ArrayList<>();
            for(SysOperationLogTypeEntity type : typeList) {
                urls.add(type.getUrl());
            }
            operationLog.setQueryUrls(urls);
        }
        Map<String, SysOperationLogTypeEntity> typeMap = new HashMap<>();
        for(SysOperationLogTypeEntity type : typeList) {
            typeMap.put(type.getUrl(), type);
        }
        PageInfo<SysOperationLogEntity> pageInfo = sysOperationLogService.pageLogs(operationLog);
        List<SysOperationLogEntity> logList = pageInfo.getList();
        if(CollectionUtils.isEmpty(logList)) {
            return WebResponse.success(new PageInfo<>());
        }
        Set<String> idSet = new HashSet<>();
        for(int i = 0; i < logList.size(); i ++) {
            idSet.add(RedisKeyPrefix.USER_INFO + logList.get(i).getUserId());
        }
        Map<Integer, SysUserEntity> userMap = CollectionsUtils.listToMap(RedisUtils.scanEntityWithKeys(idSet, SysUserEntity.class));
        for(int i = 0; i < logList.size(); i ++) {
            if(userMap.get(logList.get(i).getUserId()) != null) {
                logList.get(i).setUserName(userMap.get(logList.get(i).getUserId()).getUserName());
            }
        }
        for(int i = 0; i < logList.size(); i ++) {
            if(logList.get(i).getUrl().equals("/import")){
                logList.get(i).setLogContent(logList.get(i).getRequestBody());
            }if(logList.get(i).getUrl().equals("/send")){
                logList.get(i).setLogContent(logList.get(i).getRequestBody());
            }else {
                if (typeMap.get(logList.get(i).getUrl()) != null && StringUtils.isNotBlank(typeMap.get(logList.get(i).getUrl()).getLogTemplate())) {
                    String template = typeMap.get(logList.get(i).getUrl()).getLogTemplate();
                    try {
                        String result = TemplateUtils.templateReplace(template, logList.get(i));
                        logList.get(i).setLogContent(result);
                    } catch (Exception e) {
                        logger.error("日志模板解析错误：{}", e);
                    }
                }
            }

        }

//        CollectionsUtils.userInfoSet(logList);
        pageInfo.setList(logList);
        return WebResponse.success(pageInfo);
    }

    @PostMapping("/logType")
    public Object logType() {
        List<SysOperationLogTypeEntity> list = sysOperationLogService.listLogType(new SysOperationLogTypeEntity());
        SysOperationLogTypeEntity specialType = new SysOperationLogTypeEntity();
        specialType.setKey("");
        specialType.setUrl("");
        specialType.setValue("全部");
        specialType.setLogType("全部");
        list.add(0, specialType);
        return WebResponse.success(list);
    }


}
