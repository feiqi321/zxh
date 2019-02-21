package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

public class Letter extends CommonEntity {
    private Integer id;
    //案件id
    private Integer caseId;
    //卡号
    private String cardNo;

    private String identNo;

    private String caseDate;
    //地址
    private String address;
    //姓名
    private String name;

    private String relation;

    private String addressType;
    //协催结果
    private String synergyResult;
    //申请内容
    private String applyContext;

    private String synergyDate;

    private String synergyer;

    private Integer deleteFlag;

    private String createDate;
    //催收区域
    private String collectArea;
    //委托方
    private String client;
    //批次编号
    private String batchNo;
    //协催类型
    private  Integer synergyType;
    //案件状态
    private String caseStatus;
    //委案金额
    private BigDecimal caseAmt;
    //委案金额 开始
    private BigDecimal caseAmtStart;
    //委案金额 结束
    private BigDecimal caseAmtEnd;
    //催收状态
    private String collectStatus;
    //催收员
    private String odv;

    private BigDecimal repayAmt;
    //信函次数
    private int times;
    //信函模板
    private String module;
    //信函状态(0待发 1确认申请  2撤销)
    private String status;
    //个案序列号
    private String seqno;
    //申请时间
    private String applyDate;

    private String applyDateStart;

    private String applyDateEnd;

    private String applyer;

    private String relationer;

    public String getApplyer() {
        return applyer;
    }

    public void setApplyer(String applyer) {
        this.applyer = applyer;
    }

    public String getRelationer() {
        return relationer;
    }

    public void setRelationer(String relationer) {
        this.relationer = relationer;
    }

    public String getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(String applyDate) {
        this.applyDate = applyDate;
    }

    public String getApplyDateStart() {
        return applyDateStart;
    }

    public void setApplyDateStart(String applyDateStart) {
        this.applyDateStart = applyDateStart;
    }

    public String getApplyDateEnd() {
        return applyDateEnd;
    }

    public void setApplyDateEnd(String applyDateEnd) {
        this.applyDateEnd = applyDateEnd;
    }

    public BigDecimal getCaseAmtStart() {
        return caseAmtStart;
    }

    public void setCaseAmtStart(BigDecimal caseAmtStart) {
        this.caseAmtStart = caseAmtStart;
    }

    public BigDecimal getCaseAmtEnd() {
        return caseAmtEnd;
    }

    public void setCaseAmtEnd(BigDecimal caseAmtEnd) {
        this.caseAmtEnd = caseAmtEnd;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollectArea() {
        return collectArea;
    }

    public void setCollectArea(String collectArea) {
        this.collectArea = collectArea;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getSynergyType() {
        return synergyType;
    }

    public void setSynergyType(Integer synergyType) {
        this.synergyType = synergyType;
    }

    public String getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(String caseStatus) {
        this.caseStatus = caseStatus;
    }

    public BigDecimal getCaseAmt() {
        return caseAmt;
    }

    public void setCaseAmt(BigDecimal caseAmt) {
        this.caseAmt = caseAmt;
    }

    public String getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(String collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getOdv() {
        return odv;
    }

    public void setOdv(String odv) {
        this.odv = odv;
    }


    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getSynergyResult() {
        return synergyResult;
    }

    public void setSynergyResult(String synergyResult) {
        this.synergyResult = synergyResult;
    }

    public String getApplyContext() {
        return applyContext;
    }

    public void setApplyContext(String applyContext) {
        this.applyContext = applyContext;
    }

    public String getSynergyDate() {
        return synergyDate;
    }

    public void setSynergyDate(String synergyDate) {
        this.synergyDate = synergyDate;
    }

    public String getSynergyer() {
        return synergyer;
    }

    public void setSynergyer(String synergyer) {
        this.synergyer = synergyer;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

}