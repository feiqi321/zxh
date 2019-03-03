package xyz.zaijushou.zhx.sys.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * Created by looyer on 2019/2/24.
 */
public class DataCaseDetail {
    private  int id;
    private String batchNo; //批次编号 查询条件  列表
    private String client;//委托方 查询条件
    private BigDecimal money;//委案金额 查询条件    列表
    private BigDecimal balance;//委案余额    列表
    //委案日期 查询条件   列表
    private String caseDate;
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
    private int distributeStatus;//分配状态  查询条件
    private String license; //牌照号  查询条件
    private String  expectTime;// 预计退案日期 查询条件   列表
    private String collectHand;//催收手别（0-90天） 查询条件
    private String realReturnTime;//实际退案日期 查询条件
    private String archiveNo; //档案号 查询条件
    private String name; //姓名 查询条件   列表
    private String account;//账号 查询条件

    private String bank; //开户行 查询条件
    //最后一次跟进时间(催收时间) 查询条件
    private String collectDate;
    private int overDays;//逾期天数 查询条件
    private String identNo;//证件号 查询条件    列表
    private String seqNo;//个案序列号 查询条件  列表
    private String cardNo;//卡号 查询条件
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
    private int synerCkecker; //协催人
    private String synergyCkeckerName;
    private String synergyCheckTime;//协催时间
    private String synerCheckContext;//协催内容

    private String queryMethod; //精确查询1 模糊查询2

    private String unitName; //单位名称

    private String address;//地址
    private String tel; //本人手机

    private boolean batchBonds;//批次共债

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
    private String billDate;  //账单日
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

    private String contractNo;  //合同编号
    private String dealer;  //经销商
    private Date applyDate;   //申请日期
    private Date policyExpiryDate;  //保单到期日
    private BigDecimal cardPrice;   //车价
    private String cardModel;   //车型
    private String brand;   //品牌
    private String engineNo;    //发动机号

    private String orderBy;

    private String sort;

    //缺少字段
    private String inteviewStatus;    //外访状态

    private BigDecimal latestOverdueMoney;  //最新欠款

    private String lastCall;    //最后通电

    private BigDecimal repayMoney;  //已还款

    private Date distributeDate;    //分配时间

    private Date nextFollowDate;    //下次跟进日期

    private BigDecimal totalOverdueMoney;   //总欠款（委案金融+公司佣金）

    private BigDecimal overdueBalance;  //欠款余额

    private String mainCard;    //是否主卡

    private String warning; //警告

    private String selfInfo;    //自定义信息

    private String latestCollectMomorize;   //最新催记

    private List<DataCaseTelEntity> dataCaseTelEntityList;

    private List<DataCaseAddressEntity> dataCaseAddressEntityList;

    private List<DataCaseCommentEntity> dataCaseCommentEntityList;

    private List<DataCaseEntity> sameBatchCaseList;

    private String remark1;
    private String remark2;
    private String remark3;
    private String remark4;
    private String remark5;
    private String remark6;
    private String contactName1;
    private String contactIdentNo1;
    private String contactHomeTel1;
    private String contactUnitTel1;
    private String contactMobile1;
    private String contactUnit1;
    private String contactAddress1;
    private String contactName2;
    private String contactIdentNo2;
    private String contactHomeTel2;
    private String contactUnitTel2;
    private String contactMobile2;
    private String contactUnit2;
    private String contactAddress2;
    private String contactName3;
    private String contactIdentNo3;
    private String contactHomeTel3;
    private String contactUnitTel3;
    private String contactMobile3;
    private String contactUnit3;
    private String contactAddress3;
    private String contactName4;
    private String contactIdentNo4;
    private String contactHomeTel4;
    private String contactUnitTel4;
    private String contactMobile4;
    private String contactUnit4;
    private String contactAddress4;
    private String contactName5;
    private String contactIdentNo5;
    private String contactHomeTel5;
    private String contactUnitTel5;
    private String contactMobile5;
    private String contactUnit5;
    private String contactAddress5;
    private String contactName6;
    private String contactIdentNo6;
    private String contactHomeTel6;
    private String contactUnitTel6;
    private String contactMobile6;
    private String contactUnit6;
    private String contactAddress6;

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark4() {
        return remark4;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    public String getRemark5() {
        return remark5;
    }

    public void setRemark5(String remark5) {
        this.remark5 = remark5;
    }

    public String getRemark6() {
        return remark6;
    }

    public void setRemark6(String remark6) {
        this.remark6 = remark6;
    }

    public String getContactName1() {
        return contactName1;
    }

    public void setContactName1(String contactName1) {
        this.contactName1 = contactName1;
    }

    public String getContactIdentNo1() {
        return contactIdentNo1;
    }

    public void setContactIdentNo1(String contactIdentNo1) {
        this.contactIdentNo1 = contactIdentNo1;
    }

    public String getContactHomeTel1() {
        return contactHomeTel1;
    }

    public void setContactHomeTel1(String contactHomeTel1) {
        this.contactHomeTel1 = contactHomeTel1;
    }

    public String getContactUnitTel1() {
        return contactUnitTel1;
    }

    public void setContactUnitTel1(String contactUnitTel1) {
        this.contactUnitTel1 = contactUnitTel1;
    }

    public String getContactMobile1() {
        return contactMobile1;
    }

    public void setContactMobile1(String contactMobile1) {
        this.contactMobile1 = contactMobile1;
    }

    public String getContactUnit1() {
        return contactUnit1;
    }

    public void setContactUnit1(String contactUnit1) {
        this.contactUnit1 = contactUnit1;
    }

    public String getContactAddress1() {
        return contactAddress1;
    }

    public void setContactAddress1(String contactAddress1) {
        this.contactAddress1 = contactAddress1;
    }

    public String getContactName2() {
        return contactName2;
    }

    public void setContactName2(String contactName2) {
        this.contactName2 = contactName2;
    }

    public String getContactIdentNo2() {
        return contactIdentNo2;
    }

    public void setContactIdentNo2(String contactIdentNo2) {
        this.contactIdentNo2 = contactIdentNo2;
    }

    public String getContactHomeTel2() {
        return contactHomeTel2;
    }

    public void setContactHomeTel2(String contactHomeTel2) {
        this.contactHomeTel2 = contactHomeTel2;
    }

    public String getContactUnitTel2() {
        return contactUnitTel2;
    }

    public void setContactUnitTel2(String contactUnitTel2) {
        this.contactUnitTel2 = contactUnitTel2;
    }

    public String getContactMobile2() {
        return contactMobile2;
    }

    public void setContactMobile2(String contactMobile2) {
        this.contactMobile2 = contactMobile2;
    }

    public String getContactUnit2() {
        return contactUnit2;
    }

    public void setContactUnit2(String contactUnit2) {
        this.contactUnit2 = contactUnit2;
    }

    public String getContactAddress2() {
        return contactAddress2;
    }

    public void setContactAddress2(String contactAddress2) {
        this.contactAddress2 = contactAddress2;
    }

    public String getContactName3() {
        return contactName3;
    }

    public void setContactName3(String contactName3) {
        this.contactName3 = contactName3;
    }

    public String getContactIdentNo3() {
        return contactIdentNo3;
    }

    public void setContactIdentNo3(String contactIdentNo3) {
        this.contactIdentNo3 = contactIdentNo3;
    }

    public String getContactHomeTel3() {
        return contactHomeTel3;
    }

    public void setContactHomeTel3(String contactHomeTel3) {
        this.contactHomeTel3 = contactHomeTel3;
    }

    public String getContactUnitTel3() {
        return contactUnitTel3;
    }

    public void setContactUnitTel3(String contactUnitTel3) {
        this.contactUnitTel3 = contactUnitTel3;
    }

    public String getContactMobile3() {
        return contactMobile3;
    }

    public void setContactMobile3(String contactMobile3) {
        this.contactMobile3 = contactMobile3;
    }

    public String getContactUnit3() {
        return contactUnit3;
    }

    public void setContactUnit3(String contactUnit3) {
        this.contactUnit3 = contactUnit3;
    }

    public String getContactAddress3() {
        return contactAddress3;
    }

    public void setContactAddress3(String contactAddress3) {
        this.contactAddress3 = contactAddress3;
    }

    public String getContactName4() {
        return contactName4;
    }

    public void setContactName4(String contactName4) {
        this.contactName4 = contactName4;
    }

    public String getContactIdentNo4() {
        return contactIdentNo4;
    }

    public void setContactIdentNo4(String contactIdentNo4) {
        this.contactIdentNo4 = contactIdentNo4;
    }

    public String getContactHomeTel4() {
        return contactHomeTel4;
    }

    public void setContactHomeTel4(String contactHomeTel4) {
        this.contactHomeTel4 = contactHomeTel4;
    }

    public String getContactUnitTel4() {
        return contactUnitTel4;
    }

    public void setContactUnitTel4(String contactUnitTel4) {
        this.contactUnitTel4 = contactUnitTel4;
    }

    public String getContactMobile4() {
        return contactMobile4;
    }

    public void setContactMobile4(String contactMobile4) {
        this.contactMobile4 = contactMobile4;
    }

    public String getContactUnit4() {
        return contactUnit4;
    }

    public void setContactUnit4(String contactUnit4) {
        this.contactUnit4 = contactUnit4;
    }

    public String getContactAddress4() {
        return contactAddress4;
    }

    public void setContactAddress4(String contactAddress4) {
        this.contactAddress4 = contactAddress4;
    }

    public String getContactName5() {
        return contactName5;
    }

    public void setContactName5(String contactName5) {
        this.contactName5 = contactName5;
    }

    public String getContactIdentNo5() {
        return contactIdentNo5;
    }

    public void setContactIdentNo5(String contactIdentNo5) {
        this.contactIdentNo5 = contactIdentNo5;
    }

    public String getContactHomeTel5() {
        return contactHomeTel5;
    }

    public void setContactHomeTel5(String contactHomeTel5) {
        this.contactHomeTel5 = contactHomeTel5;
    }

    public String getContactUnitTel5() {
        return contactUnitTel5;
    }

    public void setContactUnitTel5(String contactUnitTel5) {
        this.contactUnitTel5 = contactUnitTel5;
    }

    public String getContactMobile5() {
        return contactMobile5;
    }

    public void setContactMobile5(String contactMobile5) {
        this.contactMobile5 = contactMobile5;
    }

    public String getContactUnit5() {
        return contactUnit5;
    }

    public void setContactUnit5(String contactUnit5) {
        this.contactUnit5 = contactUnit5;
    }

    public String getContactAddress5() {
        return contactAddress5;
    }

    public void setContactAddress5(String contactAddress5) {
        this.contactAddress5 = contactAddress5;
    }

    public String getContactName6() {
        return contactName6;
    }

    public void setContactName6(String contactName6) {
        this.contactName6 = contactName6;
    }

    public String getContactIdentNo6() {
        return contactIdentNo6;
    }

    public void setContactIdentNo6(String contactIdentNo6) {
        this.contactIdentNo6 = contactIdentNo6;
    }

    public String getContactHomeTel6() {
        return contactHomeTel6;
    }

    public void setContactHomeTel6(String contactHomeTel6) {
        this.contactHomeTel6 = contactHomeTel6;
    }

    public String getContactUnitTel6() {
        return contactUnitTel6;
    }

    public void setContactUnitTel6(String contactUnitTel6) {
        this.contactUnitTel6 = contactUnitTel6;
    }

    public String getContactMobile6() {
        return contactMobile6;
    }

    public void setContactMobile6(String contactMobile6) {
        this.contactMobile6 = contactMobile6;
    }

    public String getContactUnit6() {
        return contactUnit6;
    }

    public void setContactUnit6(String contactUnit6) {
        this.contactUnit6 = contactUnit6;
    }

    public String getContactAddress6() {
        return contactAddress6;
    }

    public void setContactAddress6(String contactAddress6) {
        this.contactAddress6 = contactAddress6;
    }

    public List<DataCaseEntity> getSameBatchCaseList() {
        return sameBatchCaseList;
    }

    public void setSameBatchCaseList(List<DataCaseEntity> sameBatchCaseList) {
        this.sameBatchCaseList = sameBatchCaseList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public String getCollectArea() {
        return collectArea;
    }

    public void setCollectArea(String collectArea) {
        this.collectArea = collectArea;
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccountAge() {
        return accountAge;
    }

    public void setAccountAge(String accountAge) {
        this.accountAge = accountAge;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public int getDistributeStatus() {
        return distributeStatus;
    }

    public void setDistributeStatus(int distributeStatus) {
        this.distributeStatus = distributeStatus;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getExpectTime() {
        return expectTime;
    }

    public void setExpectTime(String expectTime) {
        this.expectTime = expectTime;
    }

    public String getCollectHand() {
        return collectHand;
    }

    public void setCollectHand(String collectHand) {
        this.collectHand = collectHand;
    }

    public String getRealReturnTime() {
        return realReturnTime;
    }

    public void setRealReturnTime(String realReturnTime) {
        this.realReturnTime = realReturnTime;
    }

    public String getArchiveNo() {
        return archiveNo;
    }

    public void setArchiveNo(String archiveNo) {
        this.archiveNo = archiveNo;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(String collectDate) {
        this.collectDate = collectDate;
    }

    public int getOverDays() {
        return overDays;
    }

    public void setOverDays(int overDays) {
        this.overDays = overDays;
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

    public String getCardNoFlag() {
        return cardNoFlag;
    }

    public void setCardNoFlag(String cardNoFlag) {
        this.cardNoFlag = cardNoFlag;
    }

    public String getCollectInfo() {
        return collectInfo;
    }

    public void setCollectInfo(String collectInfo) {
        this.collectInfo = collectInfo;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getCollectStatusMsg() {
        return collectStatusMsg;
    }

    public void setCollectStatusMsg(String collectStatusMsg) {
        this.collectStatusMsg = collectStatusMsg;
    }

    public int getCollectTimes() {
        return collectTimes;
    }

    public void setCollectTimes(int collectTimes) {
        this.collectTimes = collectTimes;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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

    public BigDecimal getProRepayAmt() {
        return proRepayAmt;
    }

    public void setProRepayAmt(BigDecimal proRepayAmt) {
        this.proRepayAmt = proRepayAmt;
    }

    public String getRealRepayDate() {
        return realRepayDate;
    }

    public void setRealRepayDate(String realRepayDate) {
        this.realRepayDate = realRepayDate;
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

    public String getRepayStatus() {
        return repayStatus;
    }

    public void setRepayStatus(String repayStatus) {
        this.repayStatus = repayStatus;
    }

    public int getNewCase() {
        return newCase;
    }

    public void setNewCase(int newCase) {
        this.newCase = newCase;
    }

    public int getReduceStatus() {
        return reduceStatus;
    }

    public void setReduceStatus(int reduceStatus) {
        this.reduceStatus = reduceStatus;
    }

    public int getReportStatus() {
        return reportStatus;
    }

    public void setReportStatus(int reportStatus) {
        this.reportStatus = reportStatus;
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

    public String getSynergyType() {
        return synergyType;
    }

    public void setSynergyType(String synergyType) {
        this.synergyType = synergyType;
    }

    public int getSynerCkecker() {
        return synerCkecker;
    }

    public void setSynerCkecker(int synerCkecker) {
        this.synerCkecker = synerCkecker;
    }

    public String getSynergyCkeckerName() {
        return synergyCkeckerName;
    }

    public void setSynergyCkeckerName(String synergyCkeckerName) {
        this.synergyCkeckerName = synergyCkeckerName;
    }

    public String getSynergyCheckTime() {
        return synergyCheckTime;
    }

    public void setSynergyCheckTime(String synergyCheckTime) {
        this.synergyCheckTime = synergyCheckTime;
    }

    public String getSynerCheckContext() {
        return synerCheckContext;
    }

    public void setSynerCheckContext(String synerCheckContext) {
        this.synerCheckContext = synerCheckContext;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isBatchBonds() {
        return batchBonds;
    }

    public void setBatchBonds(boolean batchBonds) {
        this.batchBonds = batchBonds;
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

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
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

    public String getInteviewStatus() {
        return inteviewStatus;
    }

    public void setInteviewStatus(String inteviewStatus) {
        this.inteviewStatus = inteviewStatus;
    }

    public BigDecimal getLatestOverdueMoney() {
        return latestOverdueMoney;
    }

    public void setLatestOverdueMoney(BigDecimal latestOverdueMoney) {
        this.latestOverdueMoney = latestOverdueMoney;
    }

    public String getLastCall() {
        return lastCall;
    }

    public void setLastCall(String lastCall) {
        this.lastCall = lastCall;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public Date getDistributeDate() {
        return distributeDate;
    }

    public void setDistributeDate(Date distributeDate) {
        this.distributeDate = distributeDate;
    }

    public Date getNextFollowDate() {
        return nextFollowDate;
    }

    public void setNextFollowDate(Date nextFollowDate) {
        this.nextFollowDate = nextFollowDate;
    }

    public BigDecimal getTotalOverdueMoney() {
        return totalOverdueMoney;
    }

    public void setTotalOverdueMoney(BigDecimal totalOverdueMoney) {
        this.totalOverdueMoney = totalOverdueMoney;
    }

    public BigDecimal getOverdueBalance() {
        return overdueBalance;
    }

    public void setOverdueBalance(BigDecimal overdueBalance) {
        this.overdueBalance = overdueBalance;
    }

    public String getMainCard() {
        return mainCard;
    }

    public void setMainCard(String mainCard) {
        this.mainCard = mainCard;
    }

    public String getWarning() {
        return warning;
    }

    public void setWarning(String warning) {
        this.warning = warning;
    }

    public String getLatestCollectMomorize() {
        return latestCollectMomorize;
    }

    public void setLatestCollectMomorize(String latestCollectMomorize) {
        this.latestCollectMomorize = latestCollectMomorize;
    }

    public List<DataCaseCommentEntity> getDataCaseCommentEntityList() {
        return dataCaseCommentEntityList;
    }

    public void setDataCaseCommentEntityList(List<DataCaseCommentEntity> dataCaseCommentEntityList) {
        this.dataCaseCommentEntityList = dataCaseCommentEntityList;
    }

    public List<DataCaseAddressEntity> getDataCaseAddressEntityList() {
        return dataCaseAddressEntityList;
    }

    public void setDataCaseAddressEntityList(List<DataCaseAddressEntity> dataCaseAddressEntityList) {
        this.dataCaseAddressEntityList = dataCaseAddressEntityList;
    }

    public List<DataCaseTelEntity> getDataCaseTelEntityList() {
        return dataCaseTelEntityList;
    }

    public void setDataCaseTelEntityList(List<DataCaseTelEntity> dataCaseTelEntityList) {
        this.dataCaseTelEntityList = dataCaseTelEntityList;
    }

    public String getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(String selfInfo) {
        this.selfInfo = selfInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(String distributeTime) {
        this.distributeTime = distributeTime;
    }

    public String getDistributeHistory() {
        return distributeHistory;
    }

    public void setDistributeHistory(String distributeHistory) {
        this.distributeHistory = distributeHistory;
    }
}
