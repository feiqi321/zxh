package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色实体类
 */
public class CollectionStatistic extends CommonEntity {
    @JSONField(format="yyyy-MM-dd HH:mm")//页面显示时间范围
    private Date dateStart ;
    @JSONField(format="yyyy-MM-dd HH:mm")
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

    private String timeArea1 ; // 时间区域
    private int countConPhoneNum1;//接通电话数
    private int countPhoneNum1;//总通话数
    private int countCasePhoneNum1;//通话涉及到的案件数

    private String timeArea2 ; // 时间区域
    private int countConPhoneNum2;//接通电话数
    private int countPhoneNum2;//总通话数
    private int countCasePhoneNum2;//通话涉及到的案件数

    private String timeArea3 ; // 时间区域
    private int countConPhoneNum3;//接通电话数
    private int countPhoneNum3;//总通话数
    private int countCasePhoneNum3;//通话涉及到的案件数

    private int sumConPhoneNum;//接通电话数
    private int sumPhoneNum;//总通话数
    private int sumCasePhoneNum;//通话涉及到的案件数

    private int sType;
    private String dept;
    private String odv;
    private String[] odvs;
    private String odvFlag;
    private List<String> odvAttr;//催收员数组
    private String area;//催收区域

    //委托方
    private String client;
    private String[] clients;
    private String clientFlag;

    private String batchNo;
    private String[] batchNos;
    private String batchFlag;
    private String accountAge;//逾期账龄
    private String collectStatusMsg;//催收状态中文显示
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

    private BigDecimal bankAmtC;//銀行对账金额-CP
    private BigDecimal repayAmtP;//承諾还款金额-PTP
    private BigDecimal repaidAmt;//已还款金额的提成金额（M）
    private BigDecimal repaidBankAmt;//银行查账金额的提成金额（M）

    private BigDecimal lastPaidMoney ; //上月还款金额
    private BigDecimal lastBankAmt;//上月銀行对账金额
    private BigDecimal lastRepayAmt;//上月承諾还款金额-PTP
    private BigDecimal lastRepaidAmt;//上月已还款金额的提成金额（M）
    private BigDecimal lastRepaidBankAmt;//月银行查账金额的提成金额（M）

    private BigDecimal thisPaidMoney ; //月还款金额
    private BigDecimal thisBankAmt;//月銀行对账金额
    private BigDecimal thisRepayAmt;//月承諾还款金额-PTP
    private BigDecimal thisRepaidAmt;//月已还款金额的提成金额（M）
    private BigDecimal thisRepaidBankAmt;//月银行查账金额的提成金额（M）

    private String opeType ;//0-还款记录，1-CP记录，2-PTP记录
    //1-114查询无效,2-DX1,3-DX2,4-DX3,5-DX4,6-承诺还款,7-可联本人,8-可联村委,
    // 9-可联第三人,10-可联家人,11-空号错号,12-网搜无效,13-无人接听,14-无效电话
    private int countSearchNo;
    private int countDX1;
    private int countDX2;
    private int countDX3;
    private int countDX4;
    private int countRepay;
    private int countConSelf;
    private int countConVillage;
    private int countConThird;
    private int countConFamily;

    private int countDeadNumber;
    private int countSearchInvalid;
    private int countNoAnswer;
    private int countInvalidCall;

    private String collectionResult;
    private int countResult;
    //对象姓名
    private String  targetName;

    private String content;

    private String connectionType;

    private String phone;

    private String phoneTime;

    List<DataCollectionEntity> list = new ArrayList<DataCollectionEntity>();

    public String getCollectStatusMsg() {
        return collectStatusMsg;
    }

    public void setCollectStatusMsg(String collectStatusMsg) {
        this.collectStatusMsg = collectStatusMsg;
    }

    public int getSumConPhoneNum() {
        return sumConPhoneNum;
    }

    public void setSumConPhoneNum(int sumConPhoneNum) {
        this.sumConPhoneNum = sumConPhoneNum;
    }

    public int getSumPhoneNum() {
        return sumPhoneNum;
    }

    public void setSumPhoneNum(int sumPhoneNum) {
        this.sumPhoneNum = sumPhoneNum;
    }

    public int getSumCasePhoneNum() {
        return sumCasePhoneNum;
    }

    public void setSumCasePhoneNum(int sumCasePhoneNum) {
        this.sumCasePhoneNum = sumCasePhoneNum;
    }

    public String getTimeArea1() {
        return timeArea1;
    }

    public void setTimeArea1(String timeArea1) {
        this.timeArea1 = timeArea1;
    }

    public int getCountConPhoneNum1() {
        return countConPhoneNum1;
    }

    public void setCountConPhoneNum1(int countConPhoneNum1) {
        this.countConPhoneNum1 = countConPhoneNum1;
    }

    public int getCountPhoneNum1() {
        return countPhoneNum1;
    }

    public void setCountPhoneNum1(int countPhoneNum1) {
        this.countPhoneNum1 = countPhoneNum1;
    }

    public int getCountCasePhoneNum1() {
        return countCasePhoneNum1;
    }

    public void setCountCasePhoneNum1(int countCasePhoneNum1) {
        this.countCasePhoneNum1 = countCasePhoneNum1;
    }

    public String getTimeArea2() {
        return timeArea2;
    }

    public void setTimeArea2(String timeArea2) {
        this.timeArea2 = timeArea2;
    }

    public int getCountConPhoneNum2() {
        return countConPhoneNum2;
    }

    public void setCountConPhoneNum2(int countConPhoneNum2) {
        this.countConPhoneNum2 = countConPhoneNum2;
    }

    public int getCountPhoneNum2() {
        return countPhoneNum2;
    }

    public void setCountPhoneNum2(int countPhoneNum2) {
        this.countPhoneNum2 = countPhoneNum2;
    }

    public int getCountCasePhoneNum2() {
        return countCasePhoneNum2;
    }

    public void setCountCasePhoneNum2(int countCasePhoneNum2) {
        this.countCasePhoneNum2 = countCasePhoneNum2;
    }

    public String getTimeArea3() {
        return timeArea3;
    }

    public void setTimeArea3(String timeArea3) {
        this.timeArea3 = timeArea3;
    }

    public int getCountConPhoneNum3() {
        return countConPhoneNum3;
    }

    public void setCountConPhoneNum3(int countConPhoneNum3) {
        this.countConPhoneNum3 = countConPhoneNum3;
    }

    public int getCountPhoneNum3() {
        return countPhoneNum3;
    }

    public void setCountPhoneNum3(int countPhoneNum3) {
        this.countPhoneNum3 = countPhoneNum3;
    }

    public int getCountCasePhoneNum3() {
        return countCasePhoneNum3;
    }

    public void setCountCasePhoneNum3(int countCasePhoneNum3) {
        this.countCasePhoneNum3 = countCasePhoneNum3;
    }

    public String[] getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(String[] batchNos) {
        this.batchNos = batchNos;
    }

    public String getBatchFlag() {
        return batchFlag;
    }

    public void setBatchFlag(String batchFlag) {
        this.batchFlag = batchFlag;
    }

    public String[] getOdvs() {
        return odvs;
    }

    public void setOdvs(String[] odvs) {
        this.odvs = odvs;
    }

    public String getOdvFlag() {
        return odvFlag;
    }

    public void setOdvFlag(String odvFlag) {
        this.odvFlag = odvFlag;
    }

    public String[] getClients() {
        return clients;
    }

    public void setClients(String[] clients) {
        this.clients = clients;
    }

    public String getClientFlag() {
        return clientFlag;
    }

    public void setClientFlag(String clientFlag) {
        this.clientFlag = clientFlag;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhoneTime() {
        return phoneTime;
    }

    public void setPhoneTime(String phoneTime) {
        this.phoneTime = phoneTime;
    }

    public String getCollectionResult() {
        return collectionResult;
    }

    public void setCollectionResult(String collectionResult) {
        this.collectionResult = collectionResult;
    }

    public int getCountResult() {
        return countResult;
    }

    public void setCountResult(int countResult) {
        this.countResult = countResult;
    }

    public int getCountSearchNo() {
        return countSearchNo;
    }

    public void setCountSearchNo(int countSearchNo) {
        this.countSearchNo = countSearchNo;
    }

    public int getCountDX1() {
        return countDX1;
    }

    public void setCountDX1(int countDX1) {
        this.countDX1 = countDX1;
    }

    public int getCountDX2() {
        return countDX2;
    }

    public void setCountDX2(int countDX2) {
        this.countDX2 = countDX2;
    }

    public int getCountDX3() {
        return countDX3;
    }

    public void setCountDX3(int countDX3) {
        this.countDX3 = countDX3;
    }

    public int getCountDX4() {
        return countDX4;
    }

    public void setCountDX4(int countDX4) {
        this.countDX4 = countDX4;
    }

    public int getCountRepay() {
        return countRepay;
    }

    public void setCountRepay(int countRepay) {
        this.countRepay = countRepay;
    }

    public int getCountConSelf() {
        return countConSelf;
    }

    public void setCountConSelf(int countConSelf) {
        this.countConSelf = countConSelf;
    }

    public int getCountConVillage() {
        return countConVillage;
    }

    public void setCountConVillage(int countConVillage) {
        this.countConVillage = countConVillage;
    }

    public int getCountConThird() {
        return countConThird;
    }

    public void setCountConThird(int countConThird) {
        this.countConThird = countConThird;
    }

    public int getCountConFamily() {
        return countConFamily;
    }

    public void setCountConFamily(int countConFamily) {
        this.countConFamily = countConFamily;
    }

    public int getCountDeadNumber() {
        return countDeadNumber;
    }

    public void setCountDeadNumber(int countDeadNumber) {
        this.countDeadNumber = countDeadNumber;
    }

    public int getCountSearchInvalid() {
        return countSearchInvalid;
    }

    public void setCountSearchInvalid(int countSearchInvalid) {
        this.countSearchInvalid = countSearchInvalid;
    }

    public int getCountNoAnswer() {
        return countNoAnswer;
    }

    public void setCountNoAnswer(int countNoAnswer) {
        this.countNoAnswer = countNoAnswer;
    }

    public int getCountInvalidCall() {
        return countInvalidCall;
    }

    public void setCountInvalidCall(int countInvalidCall) {
        this.countInvalidCall = countInvalidCall;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public List<String> getOdvAttr() {
        return odvAttr;
    }

    public void setOdvAttr(List<String> odvAttr) {
        this.odvAttr = odvAttr;
    }

    public String getOdv() {
        return odv;
    }

    public void setOdv(String odv) {
        this.odv = odv;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public int getsType() {
        return sType;
    }

    public void setsType(int sType) {
        this.sType = sType;
    }

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

    public List<DataCollectionEntity> getList() {
        return list;
    }

    public void setList(List<DataCollectionEntity> list) {
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

    public BigDecimal getBankAmtC() {
        return bankAmtC;
    }

    public void setBankAmtC(BigDecimal bankAmtC) {
        this.bankAmtC = bankAmtC;
    }

    public BigDecimal getRepayAmtP() {
        return repayAmtP;
    }

    public void setRepayAmtP(BigDecimal repayAmtP) {
        this.repayAmtP = repayAmtP;
    }

    public String getOpeType() {
        return opeType;
    }

    public void setOpeType(String opeType) {
        this.opeType = opeType;
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
