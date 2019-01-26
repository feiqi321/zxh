package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;

import javax.annotation.Resource;
import java.util.List;

@Api("枚举操作")
@RestController
@RequestMapping(value = "/sys/dictionary")
public class SysDictionaryController {

    @Resource
    private SysDictionaryService dictionaryService;

    @ApiOperation(value = "增加数据信息", notes = "增加数据信息")
    @PostMapping("/insert")
    public Object saveData(@RequestBody SysDictionaryEntity[] dictionary) {
        dictionaryService.saveDataDictionary(dictionary);
        return WebResponse.success(dictionary);
    }

    @ApiOperation(value = "修改数据信息", notes = "修改数据信息")
    @PostMapping("/update")
    public Object updateData(@RequestBody SysDictionaryEntity dictionary) {
        dictionaryService.updateDataDictionary(dictionary);
        return WebResponse.success(dictionary);
    }

    @ApiOperation(value = "查询数据列表", notes = "查询数据列表")
    @PostMapping("/select/list")
    public Object getDataList(@RequestBody SysDictionaryEntity dictionary) {
        List<SysDictionaryEntity> dictionaryEntityList = dictionaryService.getDataList(dictionary);
        return WebResponse.success(dictionaryEntityList);
    }

    @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
    @PostMapping("/select/id")
    public Object getDataById(@RequestBody SysDictionaryEntity dictionary) {
        SysDictionaryEntity dictionaryEntity = dictionaryService.getDataById(dictionary);
        return WebResponse.success(dictionaryEntity);
    }

    @ApiOperation(value = "删除指定数据", notes = "删除指定数据")
    @PostMapping("/delete/id")
    public Object deleteById(@RequestBody SysDictionaryEntity dictionary) {
        dictionaryService.deleteById(dictionary);
        return WebResponse.success(dictionary);
    }
}
