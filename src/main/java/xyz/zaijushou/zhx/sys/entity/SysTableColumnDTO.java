package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,14:08]
 */
public class SysTableColumnDTO {
    private String tableid;

    private List<SysTableColumnEntity> columns;

    public SysTableColumnDTO() {
    }

    public SysTableColumnDTO(String tableid, List<SysTableColumnEntity> columns) {
        this.tableid = tableid;
        this.columns = columns;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public List<SysTableColumnEntity> getColumns() {
        return columns;
    }

    public void setColumns(List<SysTableColumnEntity> columns) {
        this.columns = columns;
    }
}
