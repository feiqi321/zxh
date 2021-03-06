package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Api("枚举操作")
@RestController
@RequestMapping(value = "/sys/dictionary")
public class SysDictionaryController {

    @Resource
    private SysDictionaryService dictionaryService;

    @PostMapping("/loadByType")
    public Object loadByType() {
        Map<String,Object> dictionaryEntityList = dictionaryService.loadByType();
        return WebResponse.success(dictionaryEntityList);
    }

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

    @ApiOperation(value = "根据父级名称查询数据列表", notes = "根据父级名称查询数据列表")
    @PostMapping("/select/list/name")
    public Object listDataByName(@RequestBody SysDictionaryEntity dictionary) {
        List<SysDictionaryEntity> dictionaryEntityList = dictionaryService.listDataByName(dictionary);
        return WebResponse.success(dictionaryEntityList);
    }

    @ApiOperation(value = "根据父级id查询数据列表", notes = "根据父级id查询数据列表")
    @PostMapping("/select/list/pid")
    public Object listDataByPid(@RequestBody SysDictionaryEntity dictionary) {
        List<SysDictionaryEntity> dictionaryEntityList = dictionaryService.listDataByPid(dictionary);
        return WebResponse.success(dictionaryEntityList);
    }

    @ApiOperation(value = "根据名称模糊查询数据", notes = "根据名称查询数据")
    @PostMapping("/select/name")
    public Object listDataByDName(@RequestBody SysDictionaryEntity dictionary) {
        List<SysDictionaryEntity> dictionaryEntityList = dictionaryService.listDataByDName(dictionary);
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

    @PostMapping("/findAreaTableData")
    public Object findTableData(@RequestParam("id") Integer id) {
        List<SysDictionaryEntity> list = dictionaryService.findAreaTableData(id);
        return WebResponse.success(list);
    }

    @PostMapping("/addArea")
    public Object addArea(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        dictionaryService.addArea(sysDictionaryEntity);
        return WebResponse.success();
    }

    @PostMapping("/saveArea")
    public Object save(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        dictionaryService.updateArea(sysDictionaryEntity);
        return WebResponse.success();
    }

    @PostMapping("/deleteArea")
    public Object deleteArea(@RequestBody SysDictionaryEntity sysDictionaryEntity) {
        return dictionaryService.deleteArea(sysDictionaryEntity);
    }
}
