package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

/**
 * 角色实体类
 */
public class SysRoleEntity extends CommonEntity {

    /**
     * 权限名称
     */
    private String roleName;

    /**
     * 权限标志
     */
    private String roleSymbol;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleSymbol() {
        return roleSymbol;
    }

    public void setRoleSymbol(String roleSymbol) {
        this.roleSymbol = roleSymbol;
    }
}
