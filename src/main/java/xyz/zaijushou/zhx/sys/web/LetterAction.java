package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelInterestConstant;
import xyz.zaijushou.zhx.constant.ExcelLetterConstant;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.LegalEntity;
import xyz.zaijushou.zhx.sys.entity.Letter;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.LetterService;
import xyz.zaijushou.zhx.utils.ExcelUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by looyer on 2019/2/21.
 */
@Api("信函")
@RestController
public class LetterAction {

    @Autowired
    private LetterService letterService;
    @Autowired
    private FileManageService fileManageService;

    @ApiOperation(value = "新增信函", notes = "新增信函")
    @PostMapping("/letter/addLetter")
    public Object addLetter(@RequestBody Letter bean) {

        letterService.addLetter(bean);
        return WebResponse.success();

    }

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/letter/pageDataLetter")
    public Object pageDataLetter(@RequestBody Letter bean) {

        WebResponse webResponse = letterService.pageDataLetter(bean);
        return webResponse;

    }
    @ApiOperation(value = "同意协催", notes = "同意协催")
    @PostMapping("/letter/confirmSynergy")
    public Object confirmSynergy(@RequestBody List<Letter> list) {
        for (int i=0;i<list.size();i++){
            Letter bean = list.get(i);
            letterService.confirmSynergy(bean);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "撤銷信函", notes = "撤銷信函")
    @PostMapping("/letter/cancelLetter")
    public Object cancelLetter(@RequestBody List<Letter> list) {
        for (int i=0;i<list.size();i++){
            Letter bean = list.get(i);
            letterService.cancelLetter(bean);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "批量确认信函", notes = "批量确认信函")
    @PostMapping("/letter/confirmLetter")
    public Object confirmLetter(@RequestBody List<Letter> list) {
        for (int i=0;i<list.size();i++){
            Letter bean = list.get(i);
            letterService.confirmLetter(bean);
        }

        return WebResponse.success();

    }

    @ApiOperation(value = "信函记录导入", notes = "信函记录导入")
    @PostMapping("/letter/import")
    public Object letterImport(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        List<Letter> letterEntities = ExcelUtils.importExcel(file, ExcelLetterConstant.Letter.values(), Letter.class);
        WebResponse webResponse =fileManageService.batchLetter(letterEntities);
        return webResponse;
    }

    @ApiOperation(value = "详情的信函", notes = "详情的信函")
    @PostMapping("/letter/getLetterListByCaseId")
    public Object getLetterListByCaseId(@RequestBody Letter bean) {

        WebResponse webResponse = letterService.findByCaseId(bean);
        return webResponse;

    }
}
