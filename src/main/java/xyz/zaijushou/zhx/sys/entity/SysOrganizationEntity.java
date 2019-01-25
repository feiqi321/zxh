package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.TreeEntity;

import java.util.List;

/**
 * 组织机构实体类
 */
public class SysOrganizationEntity extends TreeEntity<SysOrganizationEntity> {

    /**
     * 机构名称
     */
    private String orgName;

    /**
     * 机构用户列表
     */
    private List<SysUserEntity> users;

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public List<SysUserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<SysUserEntity> users) {
        this.users = users;
    }
}
