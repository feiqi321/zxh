package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysTableColumnDTO;


/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,13:34]
 */
public interface TableColumnsWidthSettingsService {
    /**
     * 查表格宽度
     * @param tableid tableid
     * @return 结果
     */
    Object findTableInformationByTableId(String tableid);

    /**
     * 保存修改宽度后的表格
     * @param sysTableColumnDTO sysTableColumnDTO
     * @return 结果
     */
    Object addTableInformation(SysTableColumnDTO sysTableColumnDTO);
}
