package xyz.zaijushou.zhx.sys.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysTableColumnDTO;
import xyz.zaijushou.zhx.sys.service.TableColumnsWidthSettingsService;

import javax.annotation.Resource;

/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,13:26]
 * 表格列宽设置功能
 */
@RestController
@RequestMapping("/tableColumnsWidthSettings")
public class TableColumnsWidthSettingsController {
    @Resource
    private TableColumnsWidthSettingsService tableColumnsWidthSettingsService;

    @RequestMapping("/query")
    public Object findTableInformationByTableId(@RequestParam("tableid") String tableid) {
        return WebResponse.success(tableColumnsWidthSettingsService.findTableInformationByTableId(tableid));
    }

    @RequestMapping("/save")
    public Object addTableInformation(@RequestBody SysTableColumnDTO sysTableColumnDTO) {
        return tableColumnsWidthSettingsService.addTableInformation(sysTableColumnDTO);
    }
}
