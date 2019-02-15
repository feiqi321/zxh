package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

public class Letter extends CommonEntity {
    private Integer id;

    private Integer caseId;

    private String cardNo;

    private String identNo;

    private String caseDate;

    private String address;

    private String name;

    private String relation;

    private String addressType;

    private String synergyResult;

    private String applyContext;

    private String synergyDate;

    private String synergyer;

    private Integer deleteFlag;

    private String createDate;

    private String collectArea;

    private String client;

    private String batchNo;
    //协催类型
    private  Integer synergyType;

    private String caseStatus;
    //委案金额
    private BigDecimal caseAmt;

    private String collectStatus;
    //催收员
    private String odv;

    private String applyStartDate;

    private String applyEndDate;

    private BigDecimal repayAmt;

    private int times;

    private String module;

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

    public String getApplyStartDate() {
        return applyStartDate;
    }

    public void setApplyStartDate(String applyStartDate) {
        this.applyStartDate = applyStartDate;
    }

    public String getApplyEndDate() {
        return applyEndDate;
    }

    public void setApplyEndDate(String applyEndDate) {
        this.applyEndDate = applyEndDate;
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