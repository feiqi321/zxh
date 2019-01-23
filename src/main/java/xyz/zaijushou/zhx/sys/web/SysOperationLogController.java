package xyz.zaijushou.zhx.sys.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operationLog")
public class SysOperationLogController {

    @Resource
    private SysOperationLogService sysOperationLogService;

    @PostMapping("/pageLogs")
    public Object pageLogs(SysOperationLogEntity operationLog) {
        return sysOperationLogService.pageLogs(operationLog);
    }

}
