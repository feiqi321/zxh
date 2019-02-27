package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.SynergisticTypeEnum;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergisticEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseSynergisticService;
import xyz.zaijushou.zhx.utils.ExcelUtils;

import javax.annotation.Resource;
import java.util.List;

@Api("协催管理/待处理协催、协催记录")
@RestController
@RequestMapping("/synergistic")
public class DataCaseSynergisticController {

    @Resource
    private DataCaseSynergisticService dataCaseSynergisticService;

    @PostMapping("/types")
    public Object types() {
        JSONArray typeList = new JSONArray();
        JSONObject typeAll = new JSONObject();
        typeAll.put("key", "");
        typeAll.put("typeName", "全部");
        typeList.add(typeAll);
        for(SynergisticTypeEnum value : SynergisticTypeEnum.values()) {
            JSONObject type = new JSONObject();
            type.put("key", value.getKey());
            type.put("typeName", value.getTypeName());
            typeList.add(type);
        }
        return WebResponse.success(typeList);
    }

    @PostMapping("/list")
    public Object list(@RequestBody DataCaseSynergisticEntity synergistic) {
        PageInfo<DataCaseSynergisticEntity> pageInfo = dataCaseSynergisticService.pageSynergisticList(synergistic);
        return WebResponse.success(pageInfo);
    }

    @PostMapping("/approve")
    public Object approve(@RequestBody DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticService.approve(synergistic);
        return WebResponse.success();
    }

    @PostMapping("/finish")
    public Object finish(@RequestBody DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticService.finish(synergistic);
        return WebResponse.success();
    }

    @PostMapping("/queryDataExport")
    public Object queryDataExport(@RequestBody DataCaseSynergisticEntity synergistic) {
        List<DataCaseSynergisticEntity> list = dataCaseSynergisticService.listSynergistic(synergistic);
        //todo 导出字段定义
//        ExcelUtils.exportExcel(
//
//        );
        return null;
    }

    @PostMapping("finishedSynergisticImport")
    public Object finishedSynergisticImport(MultipartFile file) {
        //todo 导入文件处理
        return WebResponse.success();
    }

    @PostMapping("synergisticRecordImport")
    public Object synergisticRecordImport(MultipartFile file) {
        //todo 导入文件处理
        return WebResponse.success();
    }

}
