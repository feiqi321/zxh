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
    private String visitFlag;
    private String joinFlag;
    private String connectFlag;
    private BigDecimal enRepayAmt;
    private BigDecimal repayAmt;
    private String repayTime;
    private String reduceReason;
    private String reduceData;
    private String remark;

    //减免有效时间
    private String reduceValidTime;
    private String reduceValidTimeStart;
    private String reduceValidTimeEnd;

    //个案序列号
    private String seqno;

    //批复还款金额
    private BigDecimal approveRepayAmt;

    //减免结果
    private String reduceResult;

    // 减免标识（0-减免结果有效，1-减免无效（删除）,2-已撤销）
    private String reduceFlag;

    //0-待审核，1-已审核，2-已完成
    private String reduceStatus;

    public String getReduceValidTime() {
        return reduceValidTime;
    }

    public void setReduceValidTime(String reduceValidTime) {
        this.reduceValidTime = reduceValidTime;
    }

    public String getReduceValidTimeStart() {
        return reduceValidTimeStart;
    }

    public void setReduceValidTimeStart(String reduceValidTimeStart) {
        this.reduceValidTimeStart = reduceValidTimeStart;
    }

    public String getReduceValidTimeEnd() {
        return reduceValidTimeEnd;
    }

    public void setReduceValidTimeEnd(String reduceValidTimeEnd) {
        this.reduceValidTimeEnd = reduceValidTimeEnd;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public BigDecimal getApproveRepayAmt() {
        return approveRepayAmt;
    }

    public void setApproveRepayAmt(BigDecimal approveRepayAmt) {
        this.approveRepayAmt = approveRepayAmt;
    }

    public String getReduceResult() {
        return reduceResult;
    }

    public void setReduceResult(String reduceResult) {
        this.reduceResult = reduceResult;
    }

    public String getReduceFlag() {
        return reduceFlag;
    }

    public void setReduceFlag(String reduceFlag) {
        this.reduceFlag = reduceFlag;
    }

    public String getReduceStatus() {
        return reduceStatus;
    }

    public void setReduceStatus(String reduceStatus) {
        this.reduceStatus = reduceStatus;
    }

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

    public String getVisitFlag() {
        return visitFlag;
    }

    public void setVisitFlag(String visitFlag) {
        this.visitFlag = visitFlag;
    }

    public String getJoinFlag() {
        return joinFlag;
    }

    public void setJoinFlag(String joinFlag) {
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
