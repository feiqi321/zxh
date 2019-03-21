package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SelectFilterEntity;
import xyz.zaijushou.zhx.sys.service.SelectFilterService;

import java.util.List;

/**
 * Created by looyer on 2019/3/21.
 */
@Api("查询条件控制")
@RestController
public class SelectFilterController {

    @Autowired
    private SelectFilterService selectFilterService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/saveSelectFilter")
    public Object saveSelectFilter(@RequestBody SelectFilterEntity bean) {

        selectFilterService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "查询", notes = "查询")
    @PostMapping("/selectByModule")
    public Object selectByModule(@RequestBody SelectFilterEntity bean) {

        SelectFilterEntity  result = selectFilterService.selectByModule(bean);
        return WebResponse.success(result);
    }

}
