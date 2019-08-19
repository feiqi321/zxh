package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CaseOpLog;
import xyz.zaijushou.zhx.sys.service.CaseOpLogService;

/**
 * Created by looyer on 2019/8/17.
 */
@Api("案件日志表")
@RestController
public class CaseOpLogController {

    @Autowired
    private CaseOpLogService caseOpLogService;

    @ApiOperation(value = "查询记录", notes = "查询记录")
    @PostMapping("/list")
    public Object list(@RequestBody CaseOpLog caseOpLog) {
        WebResponse webResponse = caseOpLogService.pageCaseOpLog(caseOpLog);
        return webResponse;

    }

}
