package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Date;
import java.util.List;

/**
 * 用户实体类
 */
public class SysUserEntity extends CommonEntity {

    /**
     * 姓名
     */
    private String userName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 登录名
     */
    private String loginName;

    /**
     * 密码
     */
    private String password;

    /**
     * 角色列表
     */
    private List<SysRoleEntity> roles;

    /**
     * 生成的token
     */
    private String token;

    /**
     * 上次登录失败时间
     */
    private Date lastLoginFailTime;

    /**
     * 连续登录失败次数
     */
    private Integer loginFailTimes;

    private int loginNameCount;

    private List<Integer> idsList;

    private boolean sameBatch;

    public boolean isSameBatch() {
        return sameBatch;
    }

    public void setSameBatch(boolean sameBatch) {
        this.sameBatch = sameBatch;
    }

    public int getLoginNameCount() {
        return loginNameCount;
    }

    public void setLoginNameCount(int loginNameCount) {
        this.loginNameCount = loginNameCount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<SysRoleEntity> getRoles() {
        return roles;
    }

    public void setRoles(List<SysRoleEntity> roles) {
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Date getLastLoginFailTime() {
        return lastLoginFailTime;
    }

    public void setLastLoginFailTime(Date lastLoginFailTime) {
        this.lastLoginFailTime = lastLoginFailTime;
    }

    public Integer getLoginFailTimes() {
        return loginFailTimes;
    }

    public void setLoginFailTimes(Integer loginFailTimes) {
        this.loginFailTimes = loginFailTimes;
    }

    public List<Integer> getIdsList() {
        return idsList;
    }

    public void setIdsList(List<Integer> idsList) {
        this.idsList = idsList;
    }
}
