package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysTableColumnDTO;


/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,13:34]
 */
public interface TableColumnsWidthSettingsService {
    Object findTableInformationByTableId(String tableid);

    Object addTableInformation(SysTableColumnDTO sysTableColumnDTO);
}
