package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
public class DataCollectionEntity extends CommonEntity {
    //案件id
    private String caseId;

    private String name;
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
    private String expectTimeStart;
    private String expectTimeEnd;
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
    //查询类型：1-部门，0-个人
    private int sType;
    //对象姓名
    private String  targetName;
    //所属部门
    private String  department;
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

    //承诺还款金额
    private BigDecimal repayAmt;
    //待銀行对账金额
    private BigDecimal bankAmt;
    //银行对账时间
    private String bankTime;
    //减免金额
    private BigDecimal reduceAmt;
    //催收状态
    private int collectStatus;

    private String collectStatusMsg;
//    //减免状态
   private int reduceStatus;

   private String reduceStatusMsg;
    //案件状态
    private int caseStatus;

    private String batchNo;

    private String identNo;

    private String cardNo;

    private String accountAge;//逾期账龄

    private int overDays;//逾期天数
    private int newCase;//逾期天数

    private String collectionType ; //催收类别
    private String caseType ; //案件类别
    private String repayStatus ; //还款状态

    //委案日期
    private String caseDate;
    private String caseDateStart;
    private String caseDateEnd;

    //下次跟进日期
    private String nextFollDate;
    private String nextFollDateStart;
    private String nextFollDateEnd;

    //最后跟进日期
    private String lastFollDate;
    private String lastFollDateStart;
    private String lastFollDateEnd;

    //跟进次数
    private String countFollow;
    private String countFollowStart;
    private String countFollowEnd;

    //自定义信息
    private String remark;
    //电话查询-电话
    private String telPhone;
    //电话查询-姓名
    private String telName;
    //催记查询-电话/地址
    private String collectPhoneAddr;
    //催记查询-催记内容
    private String collectMeasure;
    //提醒
    private String remind;
    //上次通话时间
    private String lastPhoneTime;
    //闲置天数
    private String leaveDays;

    //预计还款时间
    //承诺日期
    private String repayTime;
    private String repayTimeStart;
    private String repayTimeEnd;

    private BigDecimal money;//委案金额
    private BigDecimal moneyStart;//委案金额
    private BigDecimal moneyEnd;//委案金额
    private BigDecimal balance;//委案余额
    private BigDecimal enRepayAmt;//已还金额

    private String archiveNo;//档案号

    private String payName;//还款人
    private String payMethod; // 还款方式
    private String confimName;//确认人
    private String confimTime;//确认时间

    private String account;//账号
    private String contractDate;//联系时间
    private String resultId;//催收结果id

    List<DataCollectionTelEntity> collTelList = new ArrayList<DataCollectionTelEntity>();

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getContractDate() {
        return contractDate;
    }

    public void setContractDate(String contractDate) {
        this.contractDate = contractDate;
    }

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public int getsType() {
        return sType;
    }

    public void setsType(int sType) {
        this.sType = sType;
    }

    public String getCollectStatusMsg() {
        return collectStatusMsg;
    }

    public void setCollectStatusMsg(String collectStatusMsg) {
        this.collectStatusMsg = collectStatusMsg;
    }

    public int getReduceStatus() {
        return reduceStatus;
    }

    public void setReduceStatus(int reduceStatus) {
        this.reduceStatus = reduceStatus;
    }

    public String getReduceStatusMsg() {
        return reduceStatusMsg;
    }

    public void setReduceStatusMsg(String reduceStatusMsg) {
        this.reduceStatusMsg = reduceStatusMsg;
    }

    public String getNextFollDate() {
        return nextFollDate;
    }

    public void setNextFollDate(String nextFollDate) {
        this.nextFollDate = nextFollDate;
    }

    public String getNextFollDateStart() {
        return nextFollDateStart;
    }

    public void setNextFollDateStart(String nextFollDateStart) {
        this.nextFollDateStart = nextFollDateStart;
    }

    public String getNextFollDateEnd() {
        return nextFollDateEnd;
    }

    public void setNextFollDateEnd(String nextFollDateEnd) {
        this.nextFollDateEnd = nextFollDateEnd;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getConfimName() {
        return confimName;
    }

    public void setConfimName(String confimName) {
        this.confimName = confimName;
    }

    public String getConfimTime() {
        return confimTime;
    }

    public void setConfimTime(String confimTime) {
        this.confimTime = confimTime;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getBankTime() {
        return bankTime;
    }

    public void setBankTime(String bankTime) {
        this.bankTime = bankTime;
    }

    public BigDecimal getBankAmt() {
        return bankAmt;
    }

    public void setBankAmt(BigDecimal bankAmt) {
        this.bankAmt = bankAmt;
    }

    public BigDecimal getEnRepayAmt() {
        return enRepayAmt;
    }

    public void setEnRepayAmt(BigDecimal enRepayAmt) {
        this.enRepayAmt = enRepayAmt;
    }


    public String getArchiveNo() {
        return archiveNo;
    }

    public void setArchiveNo(String archiveNo) {
        this.archiveNo = archiveNo;
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

    public String getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(String accountAge) {
        this.accountAge = accountAge;
    }

    public int getOverDays() {
        return overDays;
    }

    public void setOverDays(int overDays) {
        this.overDays = overDays;
    }

    public int getNewCase() {
        return newCase;
    }

    public void setNewCase(int newCase) {
        this.newCase = newCase;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

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


    public List<DataCollectionTelEntity> getCollTelList() {
        return collTelList;
    }

    public void setCollTelList(List<DataCollectionTelEntity> collTelList) {
        this.collTelList = collTelList;
    }

    public String getLastFollDate() {
        return lastFollDate;
    }

    public void setLastFollDate(String lastFollDate) {
        this.lastFollDate = lastFollDate;
    }

    public String getLastFollDateStart() {
        return lastFollDateStart;
    }

    public void setLastFollDateStart(String lastFollDateStart) {
        this.lastFollDateStart = lastFollDateStart;
    }

    public String getLastFollDateEnd() {
        return lastFollDateEnd;
    }

    public void setLastFollDateEnd(String lastFollDateEnd) {
        this.lastFollDateEnd = lastFollDateEnd;
    }

    public String getCountFollow() {
        return countFollow;
    }

    public void setCountFollow(String countFollow) {
        this.countFollow = countFollow;
    }

    public String getCountFollowStart() {
        return countFollowStart;
    }

    public void setCountFollowStart(String countFollowStart) {
        this.countFollowStart = countFollowStart;
    }

    public String getCountFollowEnd() {
        return countFollowEnd;
    }

    public void setCountFollowEnd(String countFollowEnd) {
        this.countFollowEnd = countFollowEnd;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getTelName() {
        return telName;
    }

    public void setTelName(String telName) {
        this.telName = telName;
    }

    public String getCollectPhoneAddr() {
        return collectPhoneAddr;
    }

    public void setCollectPhoneAddr(String collectPhoneAddr) {
        this.collectPhoneAddr = collectPhoneAddr;
    }

    public String getCollectMeasure() {
        return collectMeasure;
    }

    public void setCollectMeasure(String collectMeasure) {
        this.collectMeasure = collectMeasure;
    }

    public String getRemind() {
        return remind;
    }

    public void setRemind(String remind) {
        this.remind = remind;
    }

    public String getLastPhoneTime() {
        return lastPhoneTime;
    }

    public void setLastPhoneTime(String lastPhoneTime) {
        this.lastPhoneTime = lastPhoneTime;
    }

    public String getLeaveDays() {
        return leaveDays;
    }

    public void setLeaveDays(String leaveDays) {
        this.leaveDays = leaveDays;
    }



    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
