package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCaseEntity extends CommonEntity {
    private String batchNo; //批次编号 查询条件  列表
    private String client;//委托方 查询条件
    private BigDecimal money;//委案金额 查询条件    列表
    private BigDecimal moneyStart;//委案金额  查询条件
    private BigDecimal moneyEnd;//委案金额  查询条件
    private BigDecimal balance;//委案余额    列表
    //委案日期 查询条件   列表
    private String caseDate;
    private String caseDateStart;
    private String caseDateEnd;
    private String collectArea;//催收区域  查询条件  列表
    private String dept;  //部门 查询条件

    private String odv;//催收员 查询条件     列表
    private BigDecimal rate;
    private int status; //案件状态 查询条件
    private String accountAge;//逾期账龄 查询条件
    //地区 查询条件   列表
    private String area;
    private String color; //标色状态
    //案件类型  查询条件
    private String caseType;
    private String vin; //车架号 查询条件

    private String repayDate;//还款日期 查询条件
    private String repayDateStart;//还款日期
    private String repayDateEnd;//还款日期
    private int distributeStatus;//分配状态  查询条件
    private String license; //牌照号  查询条件
    private String  expectTime;// 预计退案日 查询条件   列表
    private String expectStartTime;
    private String expectEndTime;
    private String collectHand;//催收手别（0-90天） 查询条件
    private String realReturnTime;//实际退案日期 查询条件
    private String realReturnStartTime;
    private String realReturnEndTime;
    private String archiveNo; //档案号 查询条件
    private String name; //姓名 查询条件   列表
    private String account;//账号 查询条件

    private String bank; //开户行 查询条件
    //最后一次跟进时间(催收时间) 查询条件
    private String collectDate;
    private String collectStartDate;
    private String collectEndDate;
    private int overDays;//逾期天数 查询条件
    private String identNo;//证件号 查询条件    列表
    private String seqNo;//个案序列号 查询条件  列表
    private String cardNo;//卡号 查询条件
    private String collectInfo;//催收小结（催收表） 催收记录 查询条件
    private int collectStatus; //催收状态   列表
    private int collectTimes; // 跟进次数 列表
    private String distributeTime;//分配時間   列表

    private String mVal; //M值系数
    private String important;
    private String summary;//案件小結 列表
    private String distributeHistory; // 分配历史  列表
    private String comment;//评语

    private BigDecimal enRepayAmt;//已还金额  列表
    private BigDecimal bankAmt;//待銀行查账金额-CP 列表
    private String proRepayDate;//承诺还款时间-PTP   列表
    private String proRepayAmt;//承诺还款金额-PTP   列表

    private String realRepayDate;
    private String realRepayDateStart;
    private String realRepayDateEnd;

    private String repayStatus;//还款状态

    private int newCase;//是否新分配

    private int reduceStatus;//减免状态
    private int reportStatus;//报备状态

    private int userCount;//用户数
    private BigDecimal totalAmt; //总金额

    private int synergy; //协催状态 2 申请中  1 最终同意申请  3待协催 4撤销申请
    private String synergyDate; //协催申请时间
    private String synergyStartTime;//协催申请开始时间
    private String synergyEndTime;//协催申请结束时间
    private String synergyContext; //协催申请内容

    List<DataCaseAddressEntity> addressList = new ArrayList<DataCaseAddressEntity>();
    List<DataCaseTelEntity> telList = new ArrayList<DataCaseTelEntity>();
    List<DataCaseCommentEntity> commentList = new ArrayList<DataCaseCommentEntity>();
    List<DataCaseInterestEntity> interestList = new ArrayList<DataCaseInterestEntity>();
    List<DataCaseRepayEntity> repayList = new ArrayList<DataCaseRepayEntity>();


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

    public String getRealReturnStartTime() {
        return realReturnStartTime;
    }

    public void setRealReturnStartTime(String realReturnStartTime) {
        this.realReturnStartTime = realReturnStartTime;
    }

    public String getRealReturnEndTime() {
        return realReturnEndTime;
    }

    public void setRealReturnEndTime(String realReturnEndTime) {
        this.realReturnEndTime = realReturnEndTime;
    }

    public String getCollectStartDate() {
        return collectStartDate;
    }

    public void setCollectStartDate(String collectStartDate) {
        this.collectStartDate = collectStartDate;
    }

    public String getCollectEndDate() {
        return collectEndDate;
    }

    public void setCollectEndDate(String collectEndDate) {
        this.collectEndDate = collectEndDate;
    }

    public String getProRepayAmt() {
        return proRepayAmt;
    }

    public void setProRepayAmt(String proRepayAmt) {
        this.proRepayAmt = proRepayAmt;
    }

    public int getSynergy() {
        return synergy;
    }

    public void setSynergy(int synergy) {
        this.synergy = synergy;
    }

    public String getSynergyDate() {
        return synergyDate;
    }

    public void setSynergyDate(String synergyDate) {
        this.synergyDate = synergyDate;
    }

    public String getSynergyStartTime() {
        return synergyStartTime;
    }

    public void setSynergyStartTime(String synergyStartTime) {
        this.synergyStartTime = synergyStartTime;
    }

    public String getSynergyEndTime() {
        return synergyEndTime;
    }

    public void setSynergyEndTime(String synergyEndTime) {
        this.synergyEndTime = synergyEndTime;
    }

    public String getSynergyContext() {
        return synergyContext;
    }

    public void setSynergyContext(String synergyContext) {
        this.synergyContext = synergyContext;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public int getCollectTimes() {
        return collectTimes;
    }

    public void setCollectTimes(int collectTimes) {
        this.collectTimes = collectTimes;
    }

    public String getCollectArea() {
        return collectArea;
    }

    public void setCollectArea(String collectArea) {
        this.collectArea = collectArea;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }


    public String getRealRepayDateStart() {
        return realRepayDateStart;
    }

    public void setRealRepayDateStart(String realRepayDateStart) {
        this.realRepayDateStart = realRepayDateStart;
    }

    public String getRealRepayDateEnd() {
        return realRepayDateEnd;
    }

    public void setRealRepayDateEnd(String realRepayDateEnd) {
        this.realRepayDateEnd = realRepayDateEnd;
    }

    public BigDecimal getMoneyStart() {
        return moneyStart;
    }

    public void setMoneyStart(BigDecimal moneyStart) {
        this.moneyStart = moneyStart;
    }

    public BigDecimal getMoneyEnd() {
        return moneyEnd;
    }

    public void setMoneyEnd(BigDecimal moneyEnd) {
        this.moneyEnd = moneyEnd;
    }

    public String getRepayDateStart() {
        return repayDateStart;
    }

    public void setRepayDateStart(String repayDateStart) {
        this.repayDateStart = repayDateStart;
    }

    public String getRepayDateEnd() {
        return repayDateEnd;
    }

    public void setRepayDateEnd(String repayDateEnd) {
        this.repayDateEnd = repayDateEnd;
    }

    public String getCaseDateStart() {
        return caseDateStart;
    }

    public void setCaseDateStart(String caseDateStart) {
        this.caseDateStart = caseDateStart;
    }

    public String getCaseDateEnd() {
        return caseDateEnd;
    }

    public void setCaseDateEnd(String caseDateEnd) {
        this.caseDateEnd = caseDateEnd;
    }

    public String getCollectInfo() {
        return collectInfo;
    }

    public void setCollectInfo(String collectInfo) {
        this.collectInfo = collectInfo;
    }

    public int getNewCase() {
        return newCase;
    }

    public void setNewCase(int newCase) {
        this.newCase = newCase;
    }

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

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
