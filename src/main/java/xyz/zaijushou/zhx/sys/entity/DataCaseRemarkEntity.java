package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

public class DataCaseRemarkEntity extends CommonEntity {

    private Integer caseId; //案件id

    //remark 在CommonEntity中已经声明

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }
}
