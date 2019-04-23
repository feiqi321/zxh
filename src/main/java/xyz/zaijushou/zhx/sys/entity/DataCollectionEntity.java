package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by looyer on 2019/1/29.
 */
public class DataCollectionEntity extends CommonEntity {
    private int[] ids;//催收数组

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
    /**
     * 部门查询标识
     */
    private int deptFlag;
    /**
     * 部门列表
     */
    private Integer[] depts;
    //催收员
    private String odv;

    private String[] odvs;

    private String odvFlag;
    //催收措施
    private String measure;
    //委托方
    private String client;

    private String[] clients;

    private String clientFlag;
    //催收时间
    private String collectTime;
    //催收时间  开始时间
    private String collectStartTime;
    //催收时间  结束
    private String collectEndTime;
    //查询类型：1-部门，0-个人(0-导出全部，1-导出当前页)
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
    private int method;

    private String methodMsg;

    //承诺还款金额
    private BigDecimal repayAmt;
    private String repayAmtMsg;
    //待銀行对账金额
    private BigDecimal bankAmt;
    private String bankAmtMsg;
    //银行对账时间
    private String bankTime;
    //减免金额
    private BigDecimal reduceAmt;

    private String reduceAmtMsg;
    //催收状态
    private int collectStatus;

    private String collectStatusMsg;
//    //减免状态
   private int reduceStatus;

   private String reduceStatusMsg;
    //案件状态
    private Integer caseStatus;

    private String batchNo;

    private String[] batchNos;

    private String batchFlag;

    private String identNo;

    private String cardNo;

    private String accountAge;//逾期账龄

    private int overDays;//逾期天数
    private String newCase;//逾期天数

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
    private String moneyMsg;
    private BigDecimal moneyStart;//委案金额
    private BigDecimal moneyEnd;//委案金额
    private BigDecimal balance;//委案余额
    private String balanceMsg;
    private BigDecimal enRepayAmt;//已还金额
    private String enRepayAmtMsg;

    private BigDecimal repaidAmtM;//已还款金额的提成金额（M）
    private BigDecimal repaidBankAmtM;//银行查账金额的提成金额（M）
    private BigDecimal mVal;//M值系数

    private String archiveNo;//档案号

    private String payName;//还款人
    private String payMethod; // 还款方式
    private String confimName;//确认人
    private String confimTime;//确认时间

    private String account;//账号
    private String contractDate;//联系时间
    private String resultId;//催收结果id

    private String orderBy;

    private String sort;

    private BigDecimal newMoney;
    private String newMoneyMsg;

    //完成人
    private String completeUser;
    //完成时间
    private String completeTime;
    private String completeTimeStart;
    private String completeTimeEnd;

    //减免提交时间
    private String reduceReferTime;
    private String reduceReferTimeStart;
    private String reduceReferTimeEnd;

    //减免有效时间
    private String reduceValidTime;
    private String reduceValidTimeStart;
    private String reduceValidTimeEnd;

    //减免结果
    private String reduceResult;

    //减免更新状态时间
    private String reduceUpdateTime;

    // 减免标识（0-减免结果有效，1-减免无效（删除）,2-已撤销）
    private String reduceFlag;

    //批复还款金额
    private BigDecimal approveRepayAmt;

    private String approveRepayAmtMsg;

    //案件分配时间
    private String caseAllotTime;
    private String caseAllotTimeStart;
    private String caseAllotTimeEnd;

    private String detaiType;//1 本案催记  2 同批次公债催记 3共债催记 4 同卡催记

    private String module;

    private String payer;
    private String contactWay;
    private String sex;
    private String age;
    private String visitFlag;
    private String joinFlag;
    private String connectFlag;
    private String reduceReason;
    private String reduceData;

    private String applyStatus;

    private String reduceType;

    private String applyTime;
    private String applyUser;
    private String auditUser;
    private String auditTime;

    private Map exportConf;

    private List exportKeyList;

    private Integer distributeStatus;

    private String distributeStatusMsg;

    private String distributeTime;//分配時間   列表

    public int getDeptFlag() {
        return deptFlag;
    }

    public void setDeptFlag(int deptFlag) {
        this.deptFlag = deptFlag;
    }

    public Integer[] getDepts() {
        return depts;
    }

    public void setDepts(Integer[] depts) {
        this.depts = depts;
    }

    public String getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(String distributeTime) {
        this.distributeTime = distributeTime;
    }

    public String getDistributeStatusMsg() {
        return distributeStatusMsg;
    }

    public void setDistributeStatusMsg(String distributeStatusMsg) {
        this.distributeStatusMsg = distributeStatusMsg;
    }

    public Integer getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(Integer distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public List getExportKeyList() {
        return exportKeyList;
    }

    public void setExportKeyList(List exportKeyList) {
        this.exportKeyList = exportKeyList;
    }

    public Map getExportConf() {
        return exportConf;
    }

    public void setExportConf(Map exportConf) {
        this.exportConf = exportConf;
    }

    public String getApproveRepayAmtMsg() {
        return approveRepayAmtMsg;
    }

    public void setApproveRepayAmtMsg(String approveRepayAmtMsg) {
        this.approveRepayAmtMsg = approveRepayAmtMsg;
    }

    public String getMethodMsg() {
        return methodMsg;
    }

    public void setMethodMsg(String methodMsg) {
        this.methodMsg = methodMsg;
    }

    public String getBankAmtMsg() {
        return bankAmtMsg;
    }

    public void setBankAmtMsg(String bankAmtMsg) {
        this.bankAmtMsg = bankAmtMsg;
    }

    public String getMoneyMsg() {
        return moneyMsg;
    }

    public void setMoneyMsg(String moneyMsg) {
        this.moneyMsg = moneyMsg;
    }

    public String getBalanceMsg() {
        return balanceMsg;
    }

    public void setBalanceMsg(String balanceMsg) {
        this.balanceMsg = balanceMsg;
    }

    public String getEnRepayAmtMsg() {
        return enRepayAmtMsg;
    }

    public void setEnRepayAmtMsg(String enRepayAmtMsg) {
        this.enRepayAmtMsg = enRepayAmtMsg;
    }

    public String getNewMoneyMsg() {
        return newMoneyMsg;
    }

    public void setNewMoneyMsg(String newMoneyMsg) {
        this.newMoneyMsg = newMoneyMsg;
    }

    public String getRepayAmtMsg() {
        return repayAmtMsg;
    }

    public void setRepayAmtMsg(String repayAmtMsg) {
        this.repayAmtMsg = repayAmtMsg;
    }

    public String getReduceAmtMsg() {
        return reduceAmtMsg;
    }

    public void setReduceAmtMsg(String reduceAmtMsg) {
        this.reduceAmtMsg = reduceAmtMsg;
    }

    private String fileName;//附件
    private String fileUuid;//附件

    private String[] fileNames;//附件

    public String getFileUuid() {
        return fileUuid;
    }

    public void setFileUuid(String fileUuid) {
        this.fileUuid = fileUuid;
    }

    public String[] getFileNames() {
        return fileNames;
    }

    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(String applyTime) {
        this.applyTime = applyTime;
    }

    public String getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(String applyUser) {
        this.applyUser = applyUser;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }

    public String getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(String auditTime) {
        this.auditTime = auditTime;
    }


    public String getReduceType() {
        return reduceType;
    }

    public void setReduceType(String reduceType) {
        this.reduceType = reduceType;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
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

    public String getReduceReason() {
        return reduceReason;
    }

    public void setReduceReason(String reduceReason) {
        this.reduceReason = reduceReason;
    }

    public String getReduceData() {
        return reduceData;
    }

    public void setReduceData(String reduceData) {
        this.reduceData = reduceData;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDetaiType() {
        return detaiType;
    }

    public void setDetaiType(String detaiType) {
        this.detaiType = detaiType;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public String getCaseAllotTime() {
        return caseAllotTime;
    }

    public void setCaseAllotTime(String caseAllotTime) {
        this.caseAllotTime = caseAllotTime;
    }

    public String getCaseAllotTimeStart() {
        return caseAllotTimeStart;
    }

    public void setCaseAllotTimeStart(String caseAllotTimeStart) {
        this.caseAllotTimeStart = caseAllotTimeStart;
    }

    public String getCaseAllotTimeEnd() {
        return caseAllotTimeEnd;
    }

    public void setCaseAllotTimeEnd(String caseAllotTimeEnd) {
        this.caseAllotTimeEnd = caseAllotTimeEnd;
    }

    public String getCompleteUser() {
        return completeUser;
    }

    public void setCompleteUser(String completeUser) {
        this.completeUser = completeUser;
    }

    public String getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(String completeTime) {
        this.completeTime = completeTime;
    }

    public String getCompleteTimeStart() {
        return completeTimeStart;
    }

    public void setCompleteTimeStart(String completeTimeStart) {
        this.completeTimeStart = completeTimeStart;
    }

    public String getCompleteTimeEnd() {
        return completeTimeEnd;
    }

    public void setCompleteTimeEnd(String completeTimeEnd) {
        this.completeTimeEnd = completeTimeEnd;
    }

    public String getReduceReferTime() {
        return reduceReferTime;
    }

    public void setReduceReferTime(String reduceReferTime) {
        this.reduceReferTime = reduceReferTime;
    }

    public String getReduceReferTimeStart() {
        return reduceReferTimeStart;
    }

    public void setReduceReferTimeStart(String reduceReferTimeStart) {
        this.reduceReferTimeStart = reduceReferTimeStart;
    }

    public String getReduceReferTimeEnd() {
        return reduceReferTimeEnd;
    }

    public void setReduceReferTimeEnd(String reduceReferTimeEnd) {
        this.reduceReferTimeEnd = reduceReferTimeEnd;
    }

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

    public String getReduceResult() {
        return reduceResult;
    }

    public void setReduceResult(String reduceResult) {
        this.reduceResult = reduceResult;
    }

    public String getReduceUpdateTime() {
        return reduceUpdateTime;
    }

    public void setReduceUpdateTime(String reduceUpdateTime) {
        this.reduceUpdateTime = reduceUpdateTime;
    }

    public String getReduceFlag() {
        return reduceFlag;
    }

    public void setReduceFlag(String reduceFlag) {
        this.reduceFlag = reduceFlag;
    }

    public BigDecimal getApproveRepayAmt() {
        return approveRepayAmt;
    }

    public void setApproveRepayAmt(BigDecimal approveRepayAmt) {
        this.approveRepayAmt = approveRepayAmt;
    }

    public BigDecimal getNewMoney() {
        return newMoney;
    }

    public void setNewMoney(BigDecimal newMoney) {
        this.newMoney = newMoney;
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

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }


    List<DataCollectionTelEntity> collTelList = new ArrayList<DataCollectionTelEntity>();

    public BigDecimal getRepaidAmtM() {
        return repaidAmtM;
    }

    public void setRepaidAmtM(BigDecimal repaidAmtM) {
        this.repaidAmtM = repaidAmtM;
    }

    public BigDecimal getRepaidBankAmtM() {
        return repaidBankAmtM;
    }

    public void setRepaidBankAmtM(BigDecimal repaidBankAmtM) {
        this.repaidBankAmtM = repaidBankAmtM;
    }

    public BigDecimal getmVal() {
        return mVal;
    }

    public void setmVal(BigDecimal mVal) {
        this.mVal = mVal;
    }

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

    public String getNewCase() {
        return newCase;
    }

    public void setNewCase(String newCase) {
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

    public Integer getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Integer caseStatus) {
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

    public int getMethod() {
        return method;
    }

    public void setMethod(int method) {
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
