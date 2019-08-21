package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.SysTableColumnEntity;

import java.util.List;

/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,13:36]
 */
public interface TableColumnsWidthSettingsMapper {

    void addTableInformation(SysTableColumnEntity sysTableColumnEntity);

    List<SysTableColumnEntity> findTableInformationByTableId(String tableid,Integer userid);

    void removeOldTableInformation(String tableid, Integer userid);

}
