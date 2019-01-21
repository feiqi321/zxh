package xyz.zaijushou.zhx.sys.entity;

/**
 * 角色-菜单关系（多对多）
 */
public class SysToRoleMenu {

    /**
     * 角色
     */
    private SysRoleEntity role;

    /**
     * 菜单
     */
    private SysMenuEntity menu;

    public SysRoleEntity getRole() {
        return role;
    }

    public void setRole(SysRoleEntity role) {
        this.role = role;
    }

    public SysMenuEntity getMenu() {
        return menu;
    }

    public void setMenu(SysMenuEntity menu) {
        this.menu = menu;
    }
}
