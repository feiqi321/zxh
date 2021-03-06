package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author lsl
 * @version [1.0.0, 2019/7/17,15:13]
 */

public class SysNewUserDataForm extends CommonEntity {
    private int[] ids;

    private String[] idStrs;
    /**
     * 姓名
     */
    private String userName;

    private Integer accountStatus;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 员工号
     */
    private String number;

    /**
     * 部门
     */
    private String department;

    private String[] departmens;

    private String role;

    private String[] roles;

    /**
     * 员工角色list
     */
    private List<SysRoleEntity> roleList = new ArrayList<SysRoleEntity>();

    /**
     * 办公电话
     */
    private String officePhone;

    /**
     * 入职日期
     */
    @JSONField(format="yyyy-MM-dd")
    private Date joinTime;
    //下组日期
    @JSONField(format="yyyy-MM-dd")
    private Date actualTime;
    /**
     * 离职日期
     */
    @JSONField(format="yyyy-MM-dd")
    private Date leaveTime;

    private String loginName;
    private int enable;
    private String enableMsg;
    private String password;
    private int status;
    private String sex;

    private String color;
    private String departId ;//部门Id


    private int job ;//职位Id
    private String position ;//职位名称

    /**
     * 旧密码，用户自行修改密码，需确认密码
     */
    private String oldPassword;

    /**
     * 上次登录失败时间
     */
    private Date lastLoginFailTime;

    /**
     * 连续登录失败次数
     */
    private Integer loginFailTimes;

    private int loginNameCount;

    private String orderBy;

    private String sort;

    private Set<String> namesSet;

    private Set<String> departIdsSet;
    private BigDecimal repay_amt;
    private BigDecimal percentage;

    public BigDecimal getRepay_amt() {
        return repay_amt;
    }

    public void setRepay_amt(BigDecimal repay_amt) {
        this.repay_amt = repay_amt;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String[] getDepartmens() {
        return departmens;
    }

    public void setDepartmens(String[] departmens) {
        this.departmens = departmens;
    }

    public String[] getRoles() {
        return roles;
    }

    public void setRoles(String[] roles) {
        this.roles = roles;
    }

    public String[] getIdStrs() {
        return idStrs;
    }

    public void setIdStrs(String[] idStrs) {
        this.idStrs = idStrs;
    }

    public Integer getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Integer accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getActualTime() {
        return actualTime;
    }

    public void setActualTime(Date actualTime) {
        this.actualTime = actualTime;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getEnableMsg() {
        return enableMsg;
    }

    public void setEnableMsg(String enableMsg) {
        this.enableMsg = enableMsg;
    }

    public int getLoginNameCount() {
        return loginNameCount;
    }

    public void setLoginNameCount(int loginNameCount) {
        this.loginNameCount = loginNameCount;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<SysRoleEntity> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<SysRoleEntity> roleList) {
        this.roleList = roleList;
    }

    public int getJob() {
        return job;
    }

    public void setJob(int job) {
        this.job = job;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getDepartId() {
        return departId;
    }

    public void setDepartId(String departId) {
        this.departId = departId;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
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

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }


    public String getOfficePhone() {
        return officePhone;
    }

    public void setOfficePhone(String officePhone) {
        this.officePhone = officePhone;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public int getEnable() {
        return enable;
    }

    public void setEnable(int enable) {
        this.enable = enable;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
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

    public Set<String> getNamesSet() {
        return namesSet;
    }

    public void setNamesSet(Set<String> namesSet) {
        this.namesSet = namesSet;
    }

    public Set<String> getDepartIdsSet() {
        return departIdsSet;
    }

    public void setDepartIdsSet(Set<String> departIdsSet) {
        this.departIdsSet = departIdsSet;
    }
}
