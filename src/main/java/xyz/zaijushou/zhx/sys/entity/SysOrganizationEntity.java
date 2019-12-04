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
     * 机构性质
     */
    private String orgNature;

    /**
     * 法人
     */
    private String legalPerson;

    /**
     * 员工人数
     */
    private Integer userNum;

    /**
     * 地址
     */
    private String orgAddress;

    /**
     * 主营行业
     */
    private String mainIndustry;

    /**
     * 机构用户列表
     */
    private List<SysUserEntity> users;

    /**
     * 查询标识
     */
    private int typeFlag;

    private Integer parentId;

    private Integer tempChildrenSize = 0;

    /**
     * 0 初始状态
     * 1 待累加到父节点
     * 2 已累加到父节点
     */
    private Integer tempStatus = 0;

    public int getTypeFlag() {
        return typeFlag;
    }

    public void setTypeFlag(int typeFlag) {
        this.typeFlag = typeFlag;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }


    public String getOrgNature() {
        return orgNature;
    }

    public void setOrgNature(String orgNature) {
        this.orgNature = orgNature;
    }

    public String getLegalPerson() {
        return legalPerson;
    }

    public void setLegalPerson(String legalPerson) {
        this.legalPerson = legalPerson;
    }

    public Integer getUserNum() {
        return userNum;
    }

    public void setUserNum(Integer userNum) {
        this.userNum = userNum;
    }

    public String getOrgAddress() {
        return orgAddress;
    }

    public void setOrgAddress(String orgAddress) {
        this.orgAddress = orgAddress;
    }

    public String getMainIndustry() {
        return mainIndustry;
    }

    public void setMainIndustry(String mainIndustry) {
        this.mainIndustry = mainIndustry;
    }

    public List<SysUserEntity> getUsers() {
        return users;
    }

    public void setUsers(List<SysUserEntity> users) {
        this.users = users;
    }

    public Integer getParentId() {
        return this.parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getTempChildrenSize() {
        return this.tempChildrenSize;
    }

    public void setTempChildrenSize(Integer tempChildrenSize) {
        this.tempChildrenSize = tempChildrenSize;
    }

    public Integer getTempStatus() {
        return this.tempStatus;
    }

    public void setTempStatus(Integer tempStatus) {
        this.tempStatus = tempStatus;
    }


}
