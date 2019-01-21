package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

/**
 * 权限实体类
 */
public class SysAuthorityEntity extends CommonEntity {

    /**
     * 权限标志
     */
    private String authoritySymbol;

    /**
     * 权限名称
     */
    private String authorityName;

    /**
     * 权限说明
     */
    private String authorityDesc;

    public String getAuthoritySymbol() {
        return authoritySymbol;
    }

    public void setAuthoritySymbol(String authoritySymbol) {
        this.authoritySymbol = authoritySymbol;
    }

    public String getAuthorityName() {
        return authorityName;
    }

    public void setAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    public String getAuthorityDesc() {
        return authorityDesc;
    }

    public void setAuthorityDesc(String authorityDesc) {
        this.authorityDesc = authorityDesc;
    }
}
