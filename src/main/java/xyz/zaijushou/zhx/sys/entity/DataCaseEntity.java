package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCaseEntity extends CommonEntity {
    private String client;
    private String batchNo;
    private BigDecimal money;
    private BigDecimal balance;
    private String odv;
    private BigDecimal rate;
    private String realReturnTime;
    private String mVal;
    private String important;
    private String summary;
    private String distributeHistory;
    private BigDecimal enRepayAmt;
    private BigDecimal bankAmt;
    private String proRepayDate;
    private String distributeTime;
    private String identNo;
    private String seqNo;
    private String cardNo;
    private int overDays;
    private int reportStatus;
    private String bank;
    private int reduceStatus;
    private String account;
    private String archiveNo;
    private String name;
    private String realRepayDate;
    private String collectHand;
    private String license;
    private int distributeStatus;
    private String repayDate;
    private String vin;
    private int collectStatus;
    private String accountAge;
    private String color;
    private String dept;
    private String remark;
    private int status;
    //最后一次跟进时间(催收时间)
    private String collectDate;
    //地区
    private String area;
    //委案日期
    private String caseDate;
    //案件类型
    private String caseType;


    List<DataCaseAddressEntity> addressList = new ArrayList<DataCaseAddressEntity>();
    List<DataCaseTelEntity> telList = new ArrayList<DataCaseTelEntity>();
    List<DataCaseCommentEntity> commentList = new ArrayList<DataCaseCommentEntity>();
    List<DataCaseInterestEntity> interestList = new ArrayList<DataCaseInterestEntity>();
    List<DataCaseRepayEntity> repayList = new ArrayList<DataCaseRepayEntity>();

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOdv() {
        return odv;
    }

    public void setOdv(String odv) {
        this.odv = odv;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public String getRealReturnTime() {
        return realReturnTime;
    }

    public void setRealReturnTime(String realReturnTime) {
        this.realReturnTime = realReturnTime;
    }

    public String getmVal() {
        return mVal;
    }

    public void setmVal(String mVal) {
        this.mVal = mVal;
    }

    public String getImportant() {
        return important;
    }

    public void setImportant(String important) {
        this.important = important;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDistributeHistory() {
        return distributeHistory;
    }

    public void setDistributeHistory(String distributeHistory) {
        this.distributeHistory = distributeHistory;
    }

    public BigDecimal getEnRepayAmt() {
        return enRepayAmt;
    }

    public void setEnRepayAmt(BigDecimal enRepayAmt) {
        this.enRepayAmt = enRepayAmt;
    }

    public BigDecimal getBankAmt() {
        return bankAmt;
    }

    public void setBankAmt(BigDecimal bankAmt) {
        this.bankAmt = bankAmt;
    }

    public String getProRepayDate() {
        return proRepayDate;
    }

    public void setProRepayDate(String proRepayDate) {
        this.proRepayDate = proRepayDate;
    }

    public String getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(String distributeTime) {
        this.distributeTime = distributeTime;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public int getOverDays() {
        return overDays;
    }

    public void setOverDays(int overDays) {
        this.overDays = overDays;
    }

    public int getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(int reportStatus) {
        this.reportStatus = reportStatus;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public int getReduceStatus() {
        return reduceStatus;
    }

    public void setReduceStatus(int reduceStatus) {
        this.reduceStatus = reduceStatus;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getArchiveNo() {
        return archiveNo;
    }

    public void setArchiveNo(String archiveNo) {
        this.archiveNo = archiveNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealRepayDate() {
        return realRepayDate;
    }

    public void setRealRepayDate(String realRepayDate) {
        this.realRepayDate = realRepayDate;
    }

    public String getCollectHand() {
        return collectHand;
    }

    public void setCollectHand(String collectHand) {
        this.collectHand = collectHand;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public int getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(int distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(String accountAge) {
        this.accountAge = accountAge;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataCaseAddressEntity> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<DataCaseAddressEntity> addressList) {
        this.addressList = addressList;
    }

    public List<DataCaseTelEntity> getTelList() {
        return telList;
    }

    public void setTelList(List<DataCaseTelEntity> telList) {
        this.telList = telList;
    }

    public List<DataCaseCommentEntity> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<DataCaseCommentEntity> commentList) {
        this.commentList = commentList;
    }

    public List<DataCaseInterestEntity> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<DataCaseInterestEntity> interestList) {
        this.interestList = interestList;
    }

    public List<DataCaseRepayEntity> getRepayList() {
        return repayList;
    }

    public void setRepayList(List<DataCaseRepayEntity> repayList) {
        this.repayList = repayList;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }
}
