package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
public class DataReduceEntity extends CommonEntity {
    //案件id
    private String caseId;

    private String payer;
    private String relation;
    private String contactWay;
    private String sex;
    private String age;
    private int visitFlag;
    private int joinFlag;
    private String connectFlag;
    private BigDecimal enRepayAmt;
    private BigDecimal repayAmt;
    private String repayTime;
    private String reduceReason;
    private String reduceData;
    private String remark;

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getContactWay() {
        return contactWay;
    }

    public void setContactWay(String contactWay) {
        this.contactWay = contactWay;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public int getVisitFlag() {
        return visitFlag;
    }

    public void setVisitFlag(int visitFlag) {
        this.visitFlag = visitFlag;
    }

    public int getJoinFlag() {
        return joinFlag;
    }

    public void setJoinFlag(int joinFlag) {
        this.joinFlag = joinFlag;
    }

    public String getConnectFlag() {
        return connectFlag;
    }

    public void setConnectFlag(String connectFlag) {
        this.connectFlag = connectFlag;
    }

    public BigDecimal getEnRepayAmt() {
        return enRepayAmt;
    }

    public void setEnRepayAmt(BigDecimal enRepayAmt) {
        this.enRepayAmt = enRepayAmt;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public String getReduceReason() {
        return reduceReason;
    }

    public void setReduceReason(String reduceReason) {
        this.reduceReason = reduceReason;
    }


    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getReduceData() {
        return reduceData;
    }

    public void setReduceData(String reduceData) {
        this.reduceData = reduceData;
    }
}
