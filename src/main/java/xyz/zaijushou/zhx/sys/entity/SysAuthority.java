package xyz.zaijushou.zhx.sys.entity;

/**
 * 权限实体类
 */
public class SysAuthority {

    /**
     * 权限名称
     */
    private String authorityName;

    /**
     * 权限标志
     */
    private String authoritySymbol;

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthoritySymbol() {
        return authoritySymbol;
    }

    public void setAuthoritySymbol(String authoritySymbol) {
        this.authoritySymbol = authoritySymbol;
    }
}
