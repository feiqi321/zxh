package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.List;

/**
 * 角色实体类
 */
public class SysRoleEntity extends CommonEntity {

    /**
     * 权限名称
     */
    private String roleName;

    /**
     * 权限描述
     */
    private String roleDesc;

    /**
     * 角色权限标志
     */
    private String roleAuthSymbol;

    /**
     * 角色用户列表
     */
    private List<SysUserEntity> users;

    /**
     * 角色权限列表
     */
    private List<SysMenuEntity> menus;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public List<SysUserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<SysUserEntity> users) {
        this.users = users;
    }

    public List<SysMenuEntity> getMenus() {
        return menus;
    }

    public void setMenus(List<SysMenuEntity> menus) {
        this.menus = menus;
    }

    public String getRoleAuthSymbol() {
        return roleAuthSymbol;
    }

    public void setRoleAuthSymbol(String roleAuthSymbol) {
        this.roleAuthSymbol = roleAuthSymbol;
    }
}
