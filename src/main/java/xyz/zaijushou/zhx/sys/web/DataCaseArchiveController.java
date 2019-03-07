package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseArchive;
import xyz.zaijushou.zhx.sys.service.DataCaseArchiveService;

import java.util.List;

/**
 * Created by looyer on 2019/3/7.
 */
@Api("案件详情/案人数据")
@RestController
public class DataCaseArchiveController {

    @Autowired
    private DataCaseArchiveService dataCaseArchiveService;

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/detai/archive/save")
    public Object save(@RequestBody DataCaseArchive bean) {

        dataCaseArchiveService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "查询", notes = "查询")
    @PostMapping("/detai/archive/list")
    public Object list(@RequestBody DataCaseArchive bean) {

        List<DataCaseArchive> list = dataCaseArchiveService.listByCaseId(bean);

        return WebResponse.success(list);

    }

}
