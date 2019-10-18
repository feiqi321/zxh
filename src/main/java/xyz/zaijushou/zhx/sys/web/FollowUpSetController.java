package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.FollowUpData;
import xyz.zaijushou.zhx.sys.service.FollowUpSetService;

import javax.annotation.Resource;

/**
 * @author lsl
 * @version [1.0.0, 2019/10/16,21:13]
 */
@RestController
public class FollowUpSetController {

    @Resource
    private FollowUpSetService followUpSetService;

    @ApiOperation(value = "修改设置", notes = "修改设置")
    @PostMapping("/followUp/update")
    public Object update(@RequestBody FollowUpData followUpData) {
        followUpSetService.update(followUpData);
        return WebResponse.success();
    }

    @ApiOperation(value = "列表查询", notes = "列表查询")
    @PostMapping("/followUp/find")
    public Object find() {
        FollowUpData followUpData= followUpSetService.find();
        return WebResponse.success(followUpData);
    }
}
