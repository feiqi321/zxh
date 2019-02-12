package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.LegalEntity;
import xyz.zaijushou.zhx.sys.service.LegalService;

import java.util.List;

/**
 * Created by looyer on 2019/2/12.
 */
@Api("诉讼案件")
@RestController
public class LegalController {

    @Autowired
    private LegalService legalService;

    @ApiOperation(value = "新增诉讼", notes = "新增诉讼")
    @PostMapping("/legal/save")
    public Object save(@RequestBody LegalEntity legalEntity) {

        legalService.saveLegal(legalEntity);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改诉讼", notes = "修改诉讼")
    @PostMapping("/legal/update")
    public Object update(@RequestBody LegalEntity legalEntity) {

        legalService.updateLegal(legalEntity);

        return WebResponse.success();

    }


    @ApiOperation(value = "刪除诉讼", notes = "刪除诉讼")
    @PostMapping("/legal/delete")
    public Object delete(@RequestBody LegalEntity legalEntity) {
            legalService.deleteLegal(legalEntity);
        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/legal/pageDataCase")
    public Object pageDataCase(@RequestBody LegalEntity legalEntity) {

        WebResponse webResponse = legalService.pageDataLegal(legalEntity);
        return WebResponse.success(webResponse);

    }

}
