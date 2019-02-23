package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCaseEntity extends CommonEntity {
    private String idStr;
    private String idFlag;
    private String[] ids;
    private String batchNo; //批次编号 查询条件  列表
    private String batchNoFlag;
    private String[] batchNos;
    private String client;//委托方 查询条件
    private String[] clients;
    private String clientFlag;
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
    private String[] odvs;
    private String odvFlag;
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
    private String  expectTime;// 预计退案日期 查询条件   列表
    private String expectStartTime;
    private String expectEndTime;
    private String collectHand;//催收手别（0-90天） 查询条件
    private String realReturnTime;//实际退案日期 查询条件
    private String realReturnStartTime;
    private String realReturnEndTime;
    private String archiveNo; //档案号 查询条件
    private String archiveNoFlag;
    private String[] archiveNos;
    private String name; //姓名 查询条件   列表
    private String nameFlag;
    private String[] names;
    private String account;//账号 查询条件
    private String accountFlag;
    private String[] accounts;

    private String bank; //开户行 查询条件
    //最后一次跟进时间(催收时间) 查询条件
    private String collectDate;
    private String collectStartDate;
    private String collectEndDate;
    private int overDays;//逾期天数 查询条件
    private String identNo;//证件号 查询条件    列表
    private String[] identNos;
    private String identNoFlag;
    private String seqNo;//个案序列号 查询条件  列表
    private String seqNoFlag;
    private String[] seqNos;
    private String cardNo;//卡号 查询条件
    private String[] cardNos;
    private String cardNoFlag;
    private String collectInfo;//催收小结（催收表） 催收记录 查询条件
    private int collectStatus; //催收状态   列表
    private String collectStatusMsg;//催收状态中文说明s
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
    private BigDecimal proRepayAmt;//承诺还款金额-PTP   列表

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
    private String synergyType;

    private String queryMethod; //精确查询1 模糊查询2

    private String unitName; //单位名称

    private String address;//地址
    private String tel; //本人手机

    private boolean batchBonds;//批次共债

    List<DataCaseAddressEntity> addressList = new ArrayList<DataCaseAddressEntity>();
    List<DataCaseTelEntity> telList = new ArrayList<DataCaseTelEntity>();
    List<DataCaseCommentEntity> commentList = new ArrayList<DataCaseCommentEntity>();
    List<DataCaseInterestEntity> interestList = new ArrayList<DataCaseInterestEntity>();
    List<DataCaseRepayEntity> repayList = new ArrayList<DataCaseRepayEntity>();

    private String caseDeadline;    //委案期限

    private BigDecimal rmb; //人民币
    private BigDecimal hkd; //港币
    private BigDecimal foreignCurrency; //外币
    private String gender;  //性别
    private String identType;   //证件类型
    private SysNewUserEntity collectionUser;    //催收员
    private SysDictionaryEntity collectionArea; //催收区域
    private Double commissionRate;  //公司佣金比率
    private BigDecimal commissionMoney; //公司佣金金额
    private String homeTelNumber;   //家庭号码
    private String unitTelNumber;    //单位号码
//    private String mobile;  //本人手机
//    private String companyName; //单位名称
    private String caseUserPosition;    //案人职位
    private String caseUserDepart;  //案人部门
    private String unitAddress;  //单位地址
    private String unitZipCode;  //单位邮编
    private String homeAddress; //家庭地址
    private String homeZipCode; //家庭邮编
    private String statementAddress;    //对账单地址
    private String statementZipCode;    //对账单邮编
    private String censusRegisterAddress;   //户籍地址
    private String censusRegisterZipCode;   //户籍地邮编
    private String qq;  //QQ
    private String email;   //邮箱
    private String birthday;    //生日
    private Integer age;    //年龄
    private SysDictionaryEntity province;   //省份
    private SysDictionaryEntity city;   //城市
    private SysDictionaryEntity county; //区县
    private Date expectRetireCaseDate;  //预计退案日期
    private List<DataCaseRemarkEntity> caseRemarks;   //备注
    private String goods;   //商品
    private String commercialTenant;    //商户
    private String applyOrderNo;    //申请单号
    private String socialSecurityComputerNo;    //社保电脑号
    private String socialSecurityCardNo;    //社保卡号
//    private String depositBank; //开户行
    private String accountNo;   //账号
    private String accountName; //账户名称
    private String cardType;    //卡类
    private BigDecimal principle;   //本金
    private BigDecimal loanRate;    //贷款利率
    private BigDecimal residualPrinciple;    //剩余本金
    private BigDecimal monthlyRepayments;   //每月还款
    private BigDecimal minimumPayment;  //最低还款额
    private BigDecimal creditLine;  //信用额度
    private BigDecimal fixedQuota;  //固定额度
    private String defaultLevel;    //拖欠级别
    private BigDecimal lastRepayMoney;  //最后还款金额
    private Date lastRepayDate; //最后还款日
    private Date lastConsumeDate;   //最后消费日
    private Date lastWithdrawDate;  //最后提现日
    private Date stopCardDate;  //停卡日
    private Date activeCardDate;    //开卡日
    private Date billDate;  //账单日
    private String billCycle;   //账单周期
    private BigDecimal outstandingAmount;  //未出账金额
    private String mainDeputyCard;  //是否主副卡
    private String deputyCardUserName;  //副卡卡人
    private Date loanDate;    //贷款日期
    private Date loanDeadline;  //贷款截止日
    private Date overdueDate;   //逾期日
    private Double overduePeriods;  //逾期期数
    private Double overdueDays; //逾期天数
    private Integer overdueTimes;   //曾逾期次数
    private Double entrustPeriods;  //委案期数
    private Date repayDeadline; //还款期限
    private Double repaidPeriods;   //已还期数
    private String loanType;    //信贷分类
    private String collectionType;  //催收分类
    private BigDecimal overdueMoney;    //逾期金额
    private BigDecimal overduePrinciple;    //逾期本金
    private BigDecimal overdueInterest; //逾期利息
    private BigDecimal overdueManagementCost;   //逾期管理费
    private BigDecimal overdueDefaultInterest;  //逾期罚息
    private BigDecimal penalty; //违约金
    private BigDecimal lateFee; //滞纳金
    private BigDecimal overrunFee;  //超限费
    private BigDecimal bail;    //保证金
    private String currencyType;    //币种
    private String lastCollectionRecord;    //原催收记录
    private String overdueBillTime; //逾期账龄

    private List<DataCaseContactsEntity> contacts;

    private String contractNo;  //合同编号
    private String dealer;  //经销商
    private Date applyDate;   //申请日期
    private Date policyExpiryDate;  //保单到期日
    private BigDecimal cardPrice;   //车价
    private String cardModel;   //车型
    private String brand;   //品牌
    private String engineNo;    //发动机号

    //查询条件
    private Set<String> seqNoSet;

    private String orderBy;

    private String sort;

    public String getSynergyType() {
        return synergyType;
    }

    public void setSynergyType(String synergyType) {
        this.synergyType = synergyType;
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

    public String getBatchNoFlag() {
        return batchNoFlag;
    }

    public void setBatchNoFlag(String batchNoFlag) {
        this.batchNoFlag = batchNoFlag;
    }

    public String[] getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(String[] batchNos) {
        this.batchNos = batchNos;
    }

    public String getIdStr() {
        return idStr;
    }

    public void setIdStr(String idStr) {
        this.idStr = idStr;
    }

    public String getIdFlag() {
        return idFlag;
    }

    public void setIdFlag(String idFlag) {
        this.idFlag = idFlag;
    }

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    public String getArchiveNoFlag() {
        return archiveNoFlag;
    }

    public void setArchiveNoFlag(String archiveNoFlag) {
        this.archiveNoFlag = archiveNoFlag;
    }

    public String[] getArchiveNos() {
        return archiveNos;
    }

    public void setArchiveNos(String[] archiveNos) {
        this.archiveNos = archiveNos;
    }

    public String getNameFlag() {
        return nameFlag;
    }

    public void setNameFlag(String nameFlag) {
        this.nameFlag = nameFlag;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public String getAccountFlag() {
        return accountFlag;
    }

    public void setAccountFlag(String accountFlag) {
        this.accountFlag = accountFlag;
    }

    public String[] getAccounts() {
        return accounts;
    }

    public void setAccounts(String[] accounts) {
        this.accounts = accounts;
    }

    public String[] getIdentNos() {
        return identNos;
    }

    public void setIdentNos(String[] identNos) {
        this.identNos = identNos;
    }

    public String getIdentNoFlag() {
        return identNoFlag;
    }

    public void setIdentNoFlag(String identNoFlag) {
        this.identNoFlag = identNoFlag;
    }

    public String getSeqNoFlag() {
        return seqNoFlag;
    }

    public void setSeqNoFlag(String seqNoFlag) {
        this.seqNoFlag = seqNoFlag;
    }

    public String[] getSeqNos() {
        return seqNos;
    }

    public void setSeqNos(String[] seqNos) {
        this.seqNos = seqNos;
    }

    public String[] getCardNos() {
        return cardNos;
    }

    public void setCardNos(String[] cardNos) {
        this.cardNos = cardNos;
    }

    public String getCardNoFlag() {
        return cardNoFlag;
    }

    public void setCardNoFlag(String cardNoFlag) {
        this.cardNoFlag = cardNoFlag;
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

    public String getCollectStatusMsg() {
        return collectStatusMsg;
    }

    public void setCollectStatusMsg(String collectStatusMsg) {
        this.collectStatusMsg = collectStatusMsg;
    }

    public void setBatchBonds(boolean batchBonds) {
        this.batchBonds = batchBonds;
    }

    public boolean isBatchBonds() {
        return batchBonds;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQueryMethod() {
        return queryMethod;
    }

    public void setQueryMethod(String queryMethod) {
        this.queryMethod = queryMethod;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
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

    public BigDecimal getProRepayAmt() {
        return proRepayAmt;
    }

    public void setProRepayAmt(BigDecimal proRepayAmt) {
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

    public String getMVal() {
        return mVal;
    }

    public void setMVal(String mVal) {
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

    public String getCaseDeadline() {
        return caseDeadline;
    }

    public void setCaseDeadline(String caseDeadline) {
        this.caseDeadline = caseDeadline;
    }

    public BigDecimal getRmb() {
        return rmb;
    }

    public void setRmb(BigDecimal rmb) {
        this.rmb = rmb;
    }

    public BigDecimal getHkd() {
        return hkd;
    }

    public void setHkd(BigDecimal hkd) {
        this.hkd = hkd;
    }

    public BigDecimal getForeignCurrency() {
        return foreignCurrency;
    }

    public void setForeignCurrency(BigDecimal foreignCurrency) {
        this.foreignCurrency = foreignCurrency;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentType() {
        return identType;
    }

    public void setIdentType(String identType) {
        this.identType = identType;
    }

    public SysNewUserEntity getCollectionUser() {
        return collectionUser;
    }

    public void setCollectionUser(SysNewUserEntity collectionUser) {
        this.collectionUser = collectionUser;
    }

    public SysDictionaryEntity getCollectionArea() {
        return collectionArea;
    }

    public void setCollectionArea(SysDictionaryEntity collectionArea) {
        this.collectionArea = collectionArea;
    }

    public Double getCommissionRate() {
        return commissionRate;
    }

    public void setCommissionRate(Double commissionRate) {
        this.commissionRate = commissionRate;
    }

    public BigDecimal getCommissionMoney() {
        return commissionMoney;
    }

    public void setCommissionMoney(BigDecimal commissionMoney) {
        this.commissionMoney = commissionMoney;
    }

    public String getHomeTelNumber() {
        return homeTelNumber;
    }

    public void setHomeTelNumber(String homeTelNumber) {
        this.homeTelNumber = homeTelNumber;
    }

    public String getUnitTelNumber() {
        return unitTelNumber;
    }

    public void setUnitTelNumber(String unitTelNumber) {
        this.unitTelNumber = unitTelNumber;
    }

    public String getCaseUserPosition() {
        return caseUserPosition;
    }

    public void setCaseUserPosition(String caseUserPosition) {
        this.caseUserPosition = caseUserPosition;
    }

    public String getCaseUserDepart() {
        return caseUserDepart;
    }

    public void setCaseUserDepart(String caseUserDepart) {
        this.caseUserDepart = caseUserDepart;
    }

    public String getUnitAddress() {
        return unitAddress;
    }

    public void setUnitAddress(String unitAddress) {
        this.unitAddress = unitAddress;
    }

    public String getUnitZipCode() {
        return unitZipCode;
    }

    public void setUnitZipCode(String unitZipCode) {
        this.unitZipCode = unitZipCode;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getHomeZipCode() {
        return homeZipCode;
    }

    public void setHomeZipCode(String homeZipCode) {
        this.homeZipCode = homeZipCode;
    }

    public String getStatementAddress() {
        return statementAddress;
    }

    public void setStatementAddress(String statementAddress) {
        this.statementAddress = statementAddress;
    }

    public String getStatementZipCode() {
        return statementZipCode;
    }

    public void setStatementZipCode(String statementZipCode) {
        this.statementZipCode = statementZipCode;
    }

    public String getCensusRegisterAddress() {
        return censusRegisterAddress;
    }

    public void setCensusRegisterAddress(String censusRegisterAddress) {
        this.censusRegisterAddress = censusRegisterAddress;
    }

    public String getCensusRegisterZipCode() {
        return censusRegisterZipCode;
    }

    public void setCensusRegisterZipCode(String censusRegisterZipCode) {
        this.censusRegisterZipCode = censusRegisterZipCode;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public SysDictionaryEntity getProvince() {
        return province;
    }

    public void setProvince(SysDictionaryEntity province) {
        this.province = province;
    }

    public SysDictionaryEntity getCity() {
        return city;
    }

    public void setCity(SysDictionaryEntity city) {
        this.city = city;
    }

    public SysDictionaryEntity getCounty() {
        return county;
    }

    public void setCounty(SysDictionaryEntity county) {
        this.county = county;
    }

    public Date getExpectRetireCaseDate() {
        return expectRetireCaseDate;
    }

    public void setExpectRetireCaseDate(Date expectRetireCaseDate) {
        this.expectRetireCaseDate = expectRetireCaseDate;
    }

    public List<DataCaseRemarkEntity> getCaseRemarks() {
        return caseRemarks;
    }

    public void setCaseRemarks(List<DataCaseRemarkEntity> caseRemarks) {
        this.caseRemarks = caseRemarks;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public String getCommercialTenant() {
        return commercialTenant;
    }

    public void setCommercialTenant(String commercialTenant) {
        this.commercialTenant = commercialTenant;
    }

    public String getApplyOrderNo() {
        return applyOrderNo;
    }

    public void setApplyOrderNo(String applyOrderNo) {
        this.applyOrderNo = applyOrderNo;
    }

    public String getSocialSecurityComputerNo() {
        return socialSecurityComputerNo;
    }

    public void setSocialSecurityComputerNo(String socialSecurityComputerNo) {
        this.socialSecurityComputerNo = socialSecurityComputerNo;
    }

    public String getSocialSecurityCardNo() {
        return socialSecurityCardNo;
    }

    public void setSocialSecurityCardNo(String socialSecurityCardNo) {
        this.socialSecurityCardNo = socialSecurityCardNo;
    }

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public BigDecimal getPrinciple() {
        return principle;
    }

    public void setPrinciple(BigDecimal principle) {
        this.principle = principle;
    }

    public BigDecimal getLoanRate() {
        return loanRate;
    }

    public void setLoanRate(BigDecimal loanRate) {
        this.loanRate = loanRate;
    }

    public BigDecimal getResidualPrinciple() {
        return residualPrinciple;
    }

    public void setResidualPrinciple(BigDecimal residualPrinciple) {
        this.residualPrinciple = residualPrinciple;
    }

    public BigDecimal getMonthlyRepayments() {
        return monthlyRepayments;
    }

    public void setMonthlyRepayments(BigDecimal monthlyRepayments) {
        this.monthlyRepayments = monthlyRepayments;
    }

    public BigDecimal getMinimumPayment() {
        return minimumPayment;
    }

    public void setMinimumPayment(BigDecimal minimumPayment) {
        this.minimumPayment = minimumPayment;
    }

    public BigDecimal getCreditLine() {
        return creditLine;
    }

    public void setCreditLine(BigDecimal creditLine) {
        this.creditLine = creditLine;
    }

    public BigDecimal getFixedQuota() {
        return fixedQuota;
    }

    public void setFixedQuota(BigDecimal fixedQuota) {
        this.fixedQuota = fixedQuota;
    }

    public String getDefaultLevel() {
        return defaultLevel;
    }

    public void setDefaultLevel(String defaultLevel) {
        this.defaultLevel = defaultLevel;
    }

    public BigDecimal getLastRepayMoney() {
        return lastRepayMoney;
    }

    public void setLastRepayMoney(BigDecimal lastRepayMoney) {
        this.lastRepayMoney = lastRepayMoney;
    }

    public Date getLastRepayDate() {
        return lastRepayDate;
    }

    public void setLastRepayDate(Date lastRepayDate) {
        this.lastRepayDate = lastRepayDate;
    }

    public Date getLastConsumeDate() {
        return lastConsumeDate;
    }

    public void setLastConsumeDate(Date lastConsumeDate) {
        this.lastConsumeDate = lastConsumeDate;
    }

    public Date getLastWithdrawDate() {
        return lastWithdrawDate;
    }

    public void setLastWithdrawDate(Date lastWithdrawDate) {
        this.lastWithdrawDate = lastWithdrawDate;
    }

    public Date getStopCardDate() {
        return stopCardDate;
    }

    public void setStopCardDate(Date stopCardDate) {
        this.stopCardDate = stopCardDate;
    }

    public Date getActiveCardDate() {
        return activeCardDate;
    }

    public void setActiveCardDate(Date activeCardDate) {
        this.activeCardDate = activeCardDate;
    }

    public Date getBillDate() {
        return billDate;
    }

    public void setBillDate(Date billDate) {
        this.billDate = billDate;
    }

    public String getBillCycle() {
        return billCycle;
    }

    public void setBillCycle(String billCycle) {
        this.billCycle = billCycle;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }

    public void setOutstandingAmount(BigDecimal outstandingAmount) {
        this.outstandingAmount = outstandingAmount;
    }

    public String getMainDeputyCard() {
        return mainDeputyCard;
    }

    public void setMainDeputyCard(String mainDeputyCard) {
        this.mainDeputyCard = mainDeputyCard;
    }

    public String getDeputyCardUserName() {
        return deputyCardUserName;
    }

    public void setDeputyCardUserName(String deputyCardUserName) {
        this.deputyCardUserName = deputyCardUserName;
    }

    public Date getLoanDate() {
        return loanDate;
    }

    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    public Date getLoanDeadline() {
        return loanDeadline;
    }

    public void setLoanDeadline(Date loanDeadline) {
        this.loanDeadline = loanDeadline;
    }

    public Date getOverdueDate() {
        return overdueDate;
    }

    public void setOverdueDate(Date overdueDate) {
        this.overdueDate = overdueDate;
    }

    public Double getOverduePeriods() {
        return overduePeriods;
    }

    public void setOverduePeriods(Double overduePeriods) {
        this.overduePeriods = overduePeriods;
    }

    public Double getOverdueDays() {
        return overdueDays;
    }

    public void setOverdueDays(Double overdueDays) {
        this.overdueDays = overdueDays;
    }

    public Integer getOverdueTimes() {
        return overdueTimes;
    }

    public void setOverdueTimes(Integer overdueTimes) {
        this.overdueTimes = overdueTimes;
    }

    public Double getEntrustPeriods() {
        return entrustPeriods;
    }

    public void setEntrustPeriods(Double entrustPeriods) {
        this.entrustPeriods = entrustPeriods;
    }

    public Date getRepayDeadline() {
        return repayDeadline;
    }

    public void setRepayDeadline(Date repayDeadline) {
        this.repayDeadline = repayDeadline;
    }

    public Double getRepaidPeriods() {
        return repaidPeriods;
    }

    public void setRepaidPeriods(Double repaidPeriods) {
        this.repaidPeriods = repaidPeriods;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }

    public BigDecimal getOverdueMoney() {
        return overdueMoney;
    }

    public void setOverdueMoney(BigDecimal overdueMoney) {
        this.overdueMoney = overdueMoney;
    }

    public BigDecimal getOverduePrinciple() {
        return overduePrinciple;
    }

    public void setOverduePrinciple(BigDecimal overduePrinciple) {
        this.overduePrinciple = overduePrinciple;
    }

    public BigDecimal getOverdueInterest() {
        return overdueInterest;
    }

    public void setOverdueInterest(BigDecimal overdueInterest) {
        this.overdueInterest = overdueInterest;
    }

    public BigDecimal getOverdueManagementCost() {
        return overdueManagementCost;
    }

    public void setOverdueManagementCost(BigDecimal overdueManagementCost) {
        this.overdueManagementCost = overdueManagementCost;
    }

    public BigDecimal getOverdueDefaultInterest() {
        return overdueDefaultInterest;
    }

    public void setOverdueDefaultInterest(BigDecimal overdueDefaultInterest) {
        this.overdueDefaultInterest = overdueDefaultInterest;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getOverrunFee() {
        return overrunFee;
    }

    public void setOverrunFee(BigDecimal overrunFee) {
        this.overrunFee = overrunFee;
    }

    public BigDecimal getBail() {
        return bail;
    }

    public void setBail(BigDecimal bail) {
        this.bail = bail;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getLastCollectionRecord() {
        return lastCollectionRecord;
    }

    public void setLastCollectionRecord(String lastCollectionRecord) {
        this.lastCollectionRecord = lastCollectionRecord;
    }

    public String getOverdueBillTime() {
        return overdueBillTime;
    }

    public void setOverdueBillTime(String overdueBillTime) {
        this.overdueBillTime = overdueBillTime;
    }

    public List<DataCaseContactsEntity> getContacts() {
        return contacts;
    }

    public void setContacts(List<DataCaseContactsEntity> contacts) {
        this.contacts = contacts;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public Date getApplyDate() {
        return applyDate;
    }

    public void setApplyDate(Date applyDate) {
        this.applyDate = applyDate;
    }

    public Date getPolicyExpiryDate() {
        return policyExpiryDate;
    }

    public void setPolicyExpiryDate(Date policyExpiryDate) {
        this.policyExpiryDate = policyExpiryDate;
    }

    public BigDecimal getCardPrice() {
        return cardPrice;
    }

    public void setCardPrice(BigDecimal cardPrice) {
        this.cardPrice = cardPrice;
    }

    public String getCardModel() {
        return cardModel;
    }

    public void setCardModel(String cardModel) {
        this.cardModel = cardModel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getEngineNo() {
        return engineNo;
    }

    public void setEngineNo(String engineNo) {
        this.engineNo = engineNo;
    }

    public Set<String> getSeqNoSet() {
        return seqNoSet;
    }

    public void setSeqNoSet(Set<String> seqNoSet) {
        this.seqNoSet = seqNoSet;
    }
}
