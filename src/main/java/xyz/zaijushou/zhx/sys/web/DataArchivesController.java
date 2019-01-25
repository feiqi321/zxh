package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataArchiveEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.service.DataArchiveService;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Api("数据管理/档案管理")
@RestController
public class DataArchivesController {

    @Autowired
    private DataArchiveService dataArchiveService;

    @ApiOperation(value = "新增档案", notes = "新增档案")
    @PostMapping("/dataArchive/save")
    public Object save(@RequestBody DataArchiveEntity bean) {

        dataArchiveService.save(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改档案", notes = "修改档案")
    @PostMapping("/dataArchive/update")
    public Object update(@RequestBody DataArchiveEntity bean) {

        dataArchiveService.update(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "刪除档案", notes = "刪除档案")
    @PostMapping("/dataArchive/delete")
    public Object delete(@RequestBody DataArchiveEntity bean) {

        dataArchiveService.delete(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataArchive/pageDataArchive")
    public Object pageDataArchive(@RequestBody DataArchiveEntity bean) {

        List<DataArchiveEntity> list = dataArchiveService.pageDataArchiveList(bean);
        return WebResponse.success(list);

    }

}
