package xyz.zaijushou.zhx.sys.entity;

/**
 * 用户-角色关系（多对多）
 */
public class SysToUserRole {

    /**
     * 用户
     */
    private SysUserEntity user;

    /**
     * 角色
     */
    private SysRoleEntity role;

    public SysUserEntity getUser() {
        return user;
    }

    public void setUser(SysUserEntity user) {
        this.user = user;
    }

    public SysRoleEntity getRole() {
        return role;
    }

    public void setRole(SysRoleEntity role) {
        this.role = role;
    }
}
