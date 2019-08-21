package xyz.zaijushou.zhx.sys.entity;


import java.math.BigDecimal;

/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,14:08]
 */
public class SysTableColumnEntity {
    private String tableid;
    private String columnname;
    private BigDecimal columnwidth;
    private BigDecimal minwidth;
    private Integer userid;


    public SysTableColumnEntity() {
    }

    public SysTableColumnEntity(String tableid, String columnname, BigDecimal columnwidth, Integer userid) {
        this.tableid = tableid;
        this.columnname = columnname;
        this.columnwidth = columnwidth;
        this.userid = userid;
    }

    public BigDecimal getMinwidth() {
        return minwidth;
    }

    public void setMinwidth(BigDecimal minwidth) {
        this.minwidth = minwidth;
    }

    public String getTableid() {
        return tableid;
    }

    public void setTableid(String tableid) {
        this.tableid = tableid;
    }

    public String getColumnname() {
        return columnname;
    }

    public void setColumnname(String columnname) {
        this.columnname = columnname;
    }

    public BigDecimal getColumnwidth() {
        return columnwidth;
    }

    public void setColumnwidth(BigDecimal columnwidth) {
        this.columnwidth = columnwidth;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
