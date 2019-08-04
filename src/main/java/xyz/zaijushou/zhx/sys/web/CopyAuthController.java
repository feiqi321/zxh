package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CopyAuth;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.service.CopyAuthService;

import java.util.List;

/**
 * Created by looyer on 2019/8/3.
 */
@Api("copy身份证号权限")
@RestController
public class CopyAuthController {

    @Autowired
    private CopyAuthService copyAuthService;

    @ApiOperation(value = "修改粘贴权限", notes = "修改粘贴权限")
    @PostMapping("/copy/update")
    public Object update(@RequestBody CopyAuth copyAuth) {

        copyAuthService.update(copyAuth);

        return WebResponse.success();

    }

    @ApiOperation(value = "列表查询", notes = "列表查询")
    @PostMapping("/copy/list")
    public Object list(@RequestBody CopyAuth copyAuth) {

        List<CopyAuth> list  = copyAuthService.list();
        return WebResponse.success(list);

    }


}
