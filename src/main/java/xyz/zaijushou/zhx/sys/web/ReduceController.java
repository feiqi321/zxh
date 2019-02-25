package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.ReduceService;

/**
 * Created by looyer on 2019/2/25.
 */
@Api("减免管理")
@RestController
public class ReduceController {

    @Autowired
    private ReduceService reduceService;
    @Autowired
    private FileManageService fileManageService;

    @ApiOperation(value = "减免分页查询", notes = "减免分页查询")
    @PostMapping("/reduce/page/all")
    public Object pageDataCase(@RequestBody DataCollectionEntity bean) {
        PageInfo<DataCollectionEntity> list = reduceService.pageReduce(bean);
        return list;
    }

    @ApiOperation(value = "增加减免信息", notes = "增加减免信息")
    @PostMapping("/reduce/add")
    public Object saveReduce(@RequestBody DataCollectionEntity bean) {
        reduceService.saveReduce(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "修改减免信息", notes = "修改减免信息")
    @PostMapping("/reduce/update/info")
    public Object updateReduce(@RequestBody DataCollectionEntity bean) {
        reduceService.updateReduce(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "操作减免状态", notes = "操作减免状态")
    @PostMapping("/reduce/update/status")
    public Object updateStatus(@RequestBody DataCollectionEntity bean) {
        reduceService.updateStatus(bean);
        return WebResponse.success();
    }

    @ApiOperation(value = "查找减免详情", notes = "查找减免详情")
    @PostMapping("/reduce/select/id")
    public Object findById(@RequestBody DataCollectionEntity bean) {
        DataCollectionEntity entity = reduceService.findById(bean);
        return WebResponse.success(entity);
    }


}
