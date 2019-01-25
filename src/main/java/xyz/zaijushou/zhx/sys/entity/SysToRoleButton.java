package xyz.zaijushou.zhx.sys.entity;

/**
 * 角色-菜单关系（多对多）
 */
public class SysToRoleButton {

    /**
     * 角色
     */
    private SysRoleEntity role;

    /**
     * 按钮
     */
    private SysButtonEntity button;

    public SysRoleEntity getRole() {
        return role;
    }

    public void setRole(SysRoleEntity role) {
        this.role = role;
    }

    public SysButtonEntity getButton() {
        return button;
    }

    public void setButton(SysButtonEntity button) {
        this.button = button;
    }
}
