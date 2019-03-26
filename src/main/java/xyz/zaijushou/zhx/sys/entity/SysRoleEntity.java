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
     * 角色菜单列表
     */
    private List<SysMenuEntity> menus;

    /**
     * 角色权限列表
     */
    private List<SysAuthorityEntity> authorities;

    /**
     * 角色按钮列表
     */
    private List<SysButtonEntity> buttons;

    private Integer dataAuth;

    private Integer busiAuth;

    public Integer getBusiAuth() {
        return busiAuth;
    }

    public void setBusiAuth(Integer busiAuth) {
        this.busiAuth = busiAuth;
    }

    public Integer getDataAuth() {
        return dataAuth;
    }

    public void setDataAuth(Integer dataAuth) {
        this.dataAuth = dataAuth;
    }

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

    public List<SysButtonEntity> getButtons() {
        return buttons;
    }

    public void setButtons(List<SysButtonEntity> buttons) {
        this.buttons = buttons;
    }

    public List<SysAuthorityEntity> getAuthorities() {
        return authorities;
    }

    public void setAuthorities(List<SysAuthorityEntity> authorities) {
        this.authorities = authorities;
    }
}
