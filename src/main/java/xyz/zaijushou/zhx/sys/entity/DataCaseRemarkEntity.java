package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Set;

public class DataCaseRemarkEntity extends CommonEntity {

    private Integer caseId; //案件id

    //remark 在CommonEntity中已经声明

    private Set<Integer> caseIdsSet;

    private String caseIdsSetFlag;

    public String getCaseIdsSetFlag() {
        return caseIdsSetFlag;
    }

    public void setCaseIdsSetFlag(String caseIdsSetFlag) {
        this.caseIdsSetFlag = caseIdsSetFlag;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Set<Integer> getCaseIdsSet() {
        return caseIdsSet;
    }

    public void setCaseIdsSet(Set<Integer> caseIdsSet) {
        this.caseIdsSet = caseIdsSet;
    }
}
