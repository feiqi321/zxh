package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 角色实体类
 */
public class CollectionStatistic extends CommonEntity {
    @JSONField(format="HH:mm")//页面显示时间范围
    private Date dateStart ;
    @JSONField(format="HH:mm")
    private Date dateEnd;
    @JSONField(format="yyyy-MM-dd")
    private Date dateSearchStart;//时间搜索
    @JSONField(format="yyyy-MM-dd")
    private Date dateSearchEnd;
    private int comlun;//列数
    private String timeArea ; // 时间区域
    private int countConPhoneNum;//接通电话数
    private int countPhoneNum;//总通话数
    private int countCasePhoneNum;//通话涉及到的案件数


    //委托方
    private String client;

    private String batchNo;
    private String accountAge;//逾期账龄
    private int collectStatus ; // 催收状态
    private BigDecimal paidMoney;//已还款金额
    private BigDecimal commisionMoney;//委案金额
    private int sumCase;//案件量


    private String repayTime;//承诺还款时间
    private String repayTimeStart;//承诺还款时间
    private String repayTimeEnd;//承诺还款时间

    private String bankTime;//银行对账时间
    private String bankTimeStart;//银行对账时间
    private String bankTimeEnd;//银行对账时间

    private String expectTime;//预计退案日期
    private String expectTimeStart;//预计退案日期
    private String expectTimeEnd;//预计退案日期

    private String monthStart ; //月日期开始
    private String monthEnd ; //月日期结束

    private BigDecimal bankAmt;//銀行对账金额
    private BigDecimal repayAmt;//承諾还款金额-PTP
    private BigDecimal repaidAmt;//已还款金额的提成金额（M）
    private BigDecimal repaidBankAmt;//银行查账金额的提成金额（M）

    private BigDecimal lastPaidMoney ; //上月还款金额
    private BigDecimal lastBankAmt;//上月銀行对账金额
    private BigDecimal lastRepayAmt;//上月承諾还款金额-PTP
    private BigDecimal lastRepaidAmt;//上月已还款金额的提成金额（M）
    private BigDecimal lastRepaidBankAmt;//月银行查账金额的提成金额（M）

    private BigDecimal thisPaidMoney ; //上月还款金额
    private BigDecimal thisBankAmt;//上月銀行对账金额
    private BigDecimal thisRepayAmt;//上月承諾还款金额-PTP
    private BigDecimal thisRepaidAmt;//月已还款金额的提成金额（M）
    private BigDecimal thisRepaidBankAmt;//月银行查账金额的提成金额（M）

    PageInfo<DataCollectionEntity> list = new PageInfo<DataCollectionEntity>();

    public BigDecimal getLastPaidMoney() {
        return lastPaidMoney;
    }

    public void setLastPaidMoney(BigDecimal lastPaidMoney) {
        this.lastPaidMoney = lastPaidMoney;
    }

    public BigDecimal getLastBankAmt() {
        return lastBankAmt;
    }

    public void setLastBankAmt(BigDecimal lastBankAmt) {
        this.lastBankAmt = lastBankAmt;
    }

    public BigDecimal getLastRepayAmt() {
        return lastRepayAmt;
    }

    public void setLastRepayAmt(BigDecimal lastRepayAmt) {
        this.lastRepayAmt = lastRepayAmt;
    }

    public BigDecimal getLastRepaidAmt() {
        return lastRepaidAmt;
    }

    public void setLastRepaidAmt(BigDecimal lastRepaidAmt) {
        this.lastRepaidAmt = lastRepaidAmt;
    }

    public BigDecimal getThisPaidMoney() {
        return thisPaidMoney;
    }

    public void setThisPaidMoney(BigDecimal thisPaidMoney) {
        this.thisPaidMoney = thisPaidMoney;
    }

    public BigDecimal getThisBankAmt() {
        return thisBankAmt;
    }

    public void setThisBankAmt(BigDecimal thisBankAmt) {
        this.thisBankAmt = thisBankAmt;
    }

    public BigDecimal getThisRepayAmt() {
        return thisRepayAmt;
    }

    public void setThisRepayAmt(BigDecimal thisRepayAmt) {
        this.thisRepayAmt = thisRepayAmt;
    }

    public BigDecimal getThisRepaidAmt() {
        return thisRepaidAmt;
    }

    public void setThisRepaidAmt(BigDecimal thisRepaidAmt) {
        this.thisRepaidAmt = thisRepaidAmt;
    }

    public PageInfo<DataCollectionEntity> getList() {
        return list;
    }

    public void setList(PageInfo<DataCollectionEntity> list) {
        this.list = list;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public String getRepayTimeStart() {
        return repayTimeStart;
    }

    public void setRepayTimeStart(String repayTimeStart) {
        this.repayTimeStart = repayTimeStart;
    }

    public String getRepayTimeEnd() {
        return repayTimeEnd;
    }

    public void setRepayTimeEnd(String repayTimeEnd) {
        this.repayTimeEnd = repayTimeEnd;
    }

    public String getBankTime() {
        return bankTime;
    }

    public void setBankTime(String bankTime) {
        this.bankTime = bankTime;
    }

    public String getBankTimeStart() {
        return bankTimeStart;
    }

    public void setBankTimeStart(String bankTimeStart) {
        this.bankTimeStart = bankTimeStart;
    }

    public String getBankTimeEnd() {
        return bankTimeEnd;
    }

    public void setBankTimeEnd(String bankTimeEnd) {
        this.bankTimeEnd = bankTimeEnd;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getExpectTimeStart() {
        return expectTimeStart;
    }

    public void setExpectTimeStart(String expectTimeStart) {
        this.expectTimeStart = expectTimeStart;
    }

    public String getExpectTimeEnd() {
        return expectTimeEnd;
    }

    public void setExpectTimeEnd(String expectTimeEnd) {
        this.expectTimeEnd = expectTimeEnd;
    }

    public String getMonthStart() {
        return monthStart;
    }

    public void setMonthStart(String monthStart) {
        this.monthStart = monthStart;
    }

    public String getMonthEnd() {
        return monthEnd;
    }

    public void setMonthEnd(String monthEnd) {
        this.monthEnd = monthEnd;
    }

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateSearchStart() {
        return dateSearchStart;
    }

    public void setDateSearchStart(Date dateSearchStart) {
        this.dateSearchStart = dateSearchStart;
    }

    public Date getDateSearchEnd() {
        return dateSearchEnd;
    }

    public void setDateSearchEnd(Date dateSearchEnd) {
        this.dateSearchEnd = dateSearchEnd;
    }

    public int getComlun() {
        return comlun;
    }

    public void setComlun(int comlun) {
        this.comlun = comlun;
    }

    public String getTimeArea() {
        return timeArea;
    }

    public void setTimeArea(String timeArea) {
        this.timeArea = timeArea;
    }

    public int getCountConPhoneNum() {
        return countConPhoneNum;
    }

    public void setCountConPhoneNum(int countConPhoneNum) {
        this.countConPhoneNum = countConPhoneNum;
    }

    public int getCountPhoneNum() {
        return countPhoneNum;
    }

    public void setCountPhoneNum(int countPhoneNum) {
        this.countPhoneNum = countPhoneNum;
    }

    public int getCountCasePhoneNum() {
        return countCasePhoneNum;
    }

    public void setCountCasePhoneNum(int countCasePhoneNum) {
        this.countCasePhoneNum = countCasePhoneNum;
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

    public String getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(String accountAge) {
        this.accountAge = accountAge;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    public BigDecimal getCommisionMoney() {
        return commisionMoney;
    }

    public void setCommisionMoney(BigDecimal commisionMoney) {
        this.commisionMoney = commisionMoney;
    }

    public int getSumCase() {
        return sumCase;
    }

    public void setSumCase(int sumCase) {
        this.sumCase = sumCase;
    }

    public BigDecimal getBankAmt() {
        return bankAmt;
    }

    public void setBankAmt(BigDecimal bankAmt) {
        this.bankAmt = bankAmt;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public BigDecimal getRepaidAmt() {
        return repaidAmt;
    }

    public void setRepaidAmt(BigDecimal repaidAmt) {
        this.repaidAmt = repaidAmt;
    }

    public BigDecimal getRepaidBankAmt() {
        return repaidBankAmt;
    }

    public void setRepaidBankAmt(BigDecimal repaidBankAmt) {
        this.repaidBankAmt = repaidBankAmt;
    }

    public BigDecimal getLastRepaidBankAmt() {
        return lastRepaidBankAmt;
    }

    public void setLastRepaidBankAmt(BigDecimal lastRepaidBankAmt) {
        this.lastRepaidBankAmt = lastRepaidBankAmt;
    }

    public BigDecimal getThisRepaidBankAmt() {
        return thisRepaidBankAmt;
    }

    public void setThisRepaidBankAmt(BigDecimal thisRepaidBankAmt) {
        this.thisRepaidBankAmt = thisRepaidBankAmt;
    }
}
