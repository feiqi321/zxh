package xyz.zaijushou.zhx.sys.entity;

/**
 * SysConfig
 */
public class SysConfig {
    private Integer cfgid;
    private String cfgtitle;
    private String cfgvalue;
    private String cfgcomment;

    public Integer getCfgid() {
        return this.cfgid;
    }

    public void setCfgid(Integer cfgid) {
        this.cfgid = cfgid;
    }

    public String getCfgtitle() {
        return this.cfgtitle;
    }

    public void setCfgtitle(String cfgtitle) {
        this.cfgtitle = cfgtitle;
    }

    public String getCfgvalue() {
        return this.cfgvalue;
    }

    public void setCfgvalue(String cfgvalue) {
        this.cfgvalue = cfgvalue;
    }

    public String getCfgcomment() {
        return this.cfgcomment;
    }

    public void setCfgcomment(String cfgcomment) {
        this.cfgcomment = cfgcomment;
    }
}