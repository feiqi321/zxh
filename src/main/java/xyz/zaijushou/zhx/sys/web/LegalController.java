package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.LegalEntity;
import xyz.zaijushou.zhx.sys.entity.LegalFee;
import xyz.zaijushou.zhx.sys.entity.LegalHandle;
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
        legalEntity.setLegalStatus(0);
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
        return webResponse;

    }

    @ApiOperation(value = "案件详情对应的诉讼", notes = "案件详情对应的诉讼")
    @PostMapping("/legal/listLegal")
    public Object listLegal(@RequestBody LegalEntity legalEntity) {

        WebResponse webResponse = legalService.listLegal(legalEntity);
        return webResponse;

    }

    @ApiOperation(value = "我的诉讼案件分頁查询", notes = "我的诉讼案件分頁查询")
    @PostMapping("/legal/pageMyDataCase")
    public Object pageMyDataCase(@RequestBody LegalEntity legalEntity) {

        WebResponse webResponse = legalService.pageMyDataLegal(legalEntity);
        return webResponse;

    }

    @ApiOperation(value = "审核", notes = "审核")
    @PostMapping("/legal/check")
    public Object check(@RequestBody LegalEntity legalEntity) {
        legalService.checkLegal(legalEntity);
        return WebResponse.success();

    }

    @ApiOperation(value = "查看详情", notes = "查看详情")
    @PostMapping("/legal/detail")
    public Object detail(@RequestBody LegalEntity legalEntity) {
        WebResponse webResponse = legalService.detail(legalEntity);
        return webResponse;

    }

    @ApiOperation(value = "新增办理信息", notes = "新增办理信息")
    @PostMapping("/legal/saveHandle")
    public Object saveHandle(@RequestBody LegalHandle bean) {

        legalService.saveLegalHandle(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改办理信息", notes = "修改办理信息")
    @PostMapping("/legal/updateHandle")
    public Object update(@RequestBody LegalHandle bean) {

        legalService.updateLegalHandle(bean);

        return WebResponse.success();

    }


    @ApiOperation(value = "刪除办理信息", notes = "刪除办理信息")
    @PostMapping("/legal/deleteHandle")
    public Object delete(@RequestBody LegalHandle bean) {
        legalService.deleteLegalHandle(bean);
        return WebResponse.success();
    }


    @ApiOperation(value = "新增费用信息", notes = "新增诉讼信息")
    @PostMapping("/legal/saveFee")
    public Object save(@RequestBody LegalFee bean) {

        legalService.saveLegalFee(bean);

        return WebResponse.success();

    }

    @ApiOperation(value = "修改费用信息", notes = "修改费用信息")
    @PostMapping("/legal/updateFee")
    public Object update(@RequestBody LegalFee bean) {

        legalService.updateLegalFee(bean);

        return WebResponse.success();

    }


    @ApiOperation(value = "刪除费用信息", notes = "刪除费用信息")
    @PostMapping("/legal/deleteFee")
    public Object delete(@RequestBody LegalFee bean) {
        legalService.deleteLegalFee(bean);
        return WebResponse.success();

    }
}
