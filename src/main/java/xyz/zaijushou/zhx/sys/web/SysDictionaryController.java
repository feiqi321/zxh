package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
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
    public Object insertData(@RequestBody SysDictionaryEntity dictionary) {
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
    @GetMapping("/select/list")
    public Object getDataList(@RequestParam Integer dictionaryId,@RequestParam String name) {
        List<SysDictionaryEntity> dictionaryEntityList = dictionaryService.getDataList(dictionaryId,name);
        return WebResponse.success(dictionaryEntityList);
    }

    @ApiOperation(value = "查询指定数据", notes = "查询指定数据")
    @GetMapping("/select/id")
    public Object getDataById(@RequestParam Integer id) {
        SysDictionaryEntity dictionaryEntity = dictionaryService.getDataById(id);
        return WebResponse.success(dictionaryEntity);
    }

    @ApiOperation(value = "删除指定数据", notes = "删除指定数据")
    @PostMapping("/delete/id")
    public Object deleteById(@RequestParam Integer id) {
        dictionaryService.deleteById(id);
        return WebResponse.success(id);
    }
}
