package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/operationLog")
public class SysOperationLogController {

    @Resource
    private SysOperationLogService sysOperationLogService;

    @PostMapping("/pageLogs")
    public Object pageLogs(@RequestBody SysOperationLogEntity operationLog) {
        return WebResponse.success(sysOperationLogService.pageLogs(operationLog));
    }

    @PostMapping("/logType")
    public Object logType() {
        JSONArray types = new JSONArray();
        for(int i = 0; i < 5; i ++) {
            JSONObject type = new JSONObject();
            type.put("key", "key" + i);
            type.put("value", "value" + i);
            types.add(type);
        }
        return WebResponse.success(types);
    }


}
