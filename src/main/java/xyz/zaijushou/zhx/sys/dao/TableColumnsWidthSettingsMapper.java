package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.SysTableColumnEntity;

import java.util.List;

/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,13:36]
 */
public interface TableColumnsWidthSettingsMapper {
    /**
     * 保存修改宽度后的表格
     * @param sysTableColumnEntity sysTableColumnEntity
     */
    void addTableInformation(SysTableColumnEntity sysTableColumnEntity);
    /**
     * 查表格宽度
     * @param tableid tableid
     * @param userid userid
     * @return 结果
     */
    List<SysTableColumnEntity> findTableInformationByTableId(String tableid,Integer userid);

    /**
     * 删除表格
     * @param tableid tableid
     * @param userid userid
     */
    void removeOldTableInformation(String tableid, Integer userid);

}
