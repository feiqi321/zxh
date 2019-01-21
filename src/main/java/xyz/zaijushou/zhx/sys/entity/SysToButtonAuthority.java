package xyz.zaijushou.zhx.sys.entity;

/**
 * 按钮-权限关系（多对多）
 */
public class SysToButtonAuthority {

    /**
     * 角色
     */
    private SysButtonEntity button;

    /**
     * 权限
     */
    private SysAuthorityEntity authority;

    public SysButtonEntity getButton() {
        return button;
    }

    public void setButton(SysButtonEntity button) {
        this.button = button;
    }

    public SysAuthorityEntity getAuthority() {
        return authority;
    }

    public void setAuthority(SysAuthorityEntity authority) {
        this.authority = authority;
    }
}
