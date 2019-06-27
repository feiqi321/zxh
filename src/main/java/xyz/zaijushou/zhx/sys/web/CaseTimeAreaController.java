package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CaseTimeAreaEntity;
import xyz.zaijushou.zhx.sys.service.CaseTimeAreaService;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("案件退案作用表")
@RestController
public class CaseTimeAreaController {

    @Autowired
    private CaseTimeAreaService caseTimeAreaService;

    @ApiOperation(value = "新增记录", notes = "新增记录")
    @PostMapping("/case/time/save")
    public Object save(@RequestBody CaseTimeAreaEntity bean) {

        caseTimeAreaService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改记录", notes = "修改记录")
    @PostMapping("/case/time/update")
    public Object update(@RequestBody CaseTimeAreaEntity bean) {

        caseTimeAreaService.update(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "查询记录", notes = "查询记录")
    @PostMapping("/case/time/all")
    public Object findAll() {
        WebResponse webResponse = caseTimeAreaService.findAll();
        return webResponse;

    }
}
