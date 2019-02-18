package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Set;

public class DataCaseContactsEntity extends CommonEntity {

    private Integer caseId; //案件id

    private String name;    //联系人姓名

    private String identNo; //联系人证件号

    private String identType;   //联系人证件类型

    private String relation;    //联系人关系

    private String homeTel; //联系人家庭电话

    private String unitTel; //联系人单位电话

    private String mobile;  //联系人手机

    private String address; //联系人地址

    private String unit;    //联系人单位

    private Integer sort;

    private Set<Integer> caseIdsSet;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(String identType) {
        this.identType = identType;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getHomeTel() {
        return homeTel;
    }

    public void setHomeTel(String homeTel) {
        this.homeTel = homeTel;
    }

    public String getUnitTel() {
        return unitTel;
    }

    public void setUnitTel(String unitTel) {
        this.unitTel = unitTel;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Set<Integer> getCaseIdsSet() {
        return caseIdsSet;
    }

    public void setCaseIdsSet(Set<Integer> caseIdsSet) {
        this.caseIdsSet = caseIdsSet;
    }
}
