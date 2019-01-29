package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/1/29.
 */
public class DataCollectionEntity extends CommonEntity {
    //案件id
    private String caseId;
    //个案序列号
    private String seqno;
    //标色状态
    private String color;
    //委案日期
    private String bailDate;
    //委案日期 开始时间
    private String bailStartDate;
    //委案日期 结束时间
    private String bailEndDate;
    //预计退案日
    private String expectTime;
    //预计退案日  开始时间
    private String expectStartTime;
    //预计退案日  结束时间
    private String expectEndTime;
    //催收区域
    private String area;
    //部门
    private String dept;
    //催收员
    private String odv;
    //催收措施
    private String measure;
    //委托方
    private String client;
    //催收时间
    private String collectTime;
    //催收时间  开始时间
    private String collectStartTime;
    //催收时间  结束
    private String collectEndTime;
    //对象姓名
    private String  targetName;
    //与案人关系
    private String relation;
    //电话类型
    private String telType;
    //电话号码
    private String mobile;
    //催收结果
    private String result;
    //催收记录
    private String collectInfo;
    //谈判方式
    private String method;
    //承诺日期
    private String repayTime;
    //承诺还款金额
    private BigDecimal repayAmt;
    //减免金额
    private BigDecimal reduceAmt;
    //催收状态
    private int collectStatus;
    //减免状态
    private int reduceStatus;
    //案件状态
    private int caseStatus;

    private String batchNo;

    private String identNo;

    private String cardNo;

    public String getBailStartDate() {
        return bailStartDate;
    }

    public void setBailStartDate(String bailStartDate) {
        this.bailStartDate = bailStartDate;
    }

    public String getBailEndDate() {
        return bailEndDate;
    }

    public void setBailEndDate(String bailEndDate) {
        this.bailEndDate = bailEndDate;
    }

    public String getExpectStartTime() {
        return expectStartTime;
    }

    public void setExpectStartTime(String expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public String getExpectEndTime() {
        return expectEndTime;
    }

    public void setExpectEndTime(String expectEndTime) {
        this.expectEndTime = expectEndTime;
    }

    public String getCollectStartTime() {
        return collectStartTime;
    }

    public void setCollectStartTime(String collectStartTime) {
        this.collectStartTime = collectStartTime;
    }

    public String getCollectEndTime() {
        return collectEndTime;
    }

    public void setCollectEndTime(String collectEndTime) {
        this.collectEndTime = collectEndTime;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }

    public int getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(int caseStatus) {
        this.caseStatus = caseStatus;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBailDate() {
        return bailDate;
    }

    public void setBailDate(String bailDate) {
        this.bailDate = bailDate;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getOdv() {
        return odv;
    }

    public void setOdv(String odv) {
        this.odv = odv;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCollectInfo() {
        return collectInfo;
    }

    public void setCollectInfo(String collectInfo) {
        this.collectInfo = collectInfo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public BigDecimal getReduceAmt() {
        return reduceAmt;
    }

    public void setReduceAmt(BigDecimal reduceAmt) {
        this.reduceAmt = reduceAmt;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public int getReduceStatus() {
        return reduceStatus;
    }

    public void setReduceStatus(int reduceStatus) {
        this.reduceStatus = reduceStatus;
    }
}
