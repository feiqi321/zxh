package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseContactsEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRemarkEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelCaseConstant {

    public enum StandardCase implements ExcelEnum {
        COL10(10, "个案序列号", "seqNo", String.class),
        COL20(20, "委案日期", "caseDate", String.class),
        COL30(30, "委案金额", "money", BigDecimal.class),
        COL40(40, "委案期限", "caseDeadline", String.class),
        COL50(50, "催收手别", "collectHand", String.class),
        COL60(60, "人民币", "rmb", BigDecimal.class),
        COL70(70, "港币", "hkd", BigDecimal.class),
        COL80(80, "外币", "foreignCurrency", BigDecimal.class),
        COL90(90, "性别", "gender", String.class),
        COL100(100, "姓名", "name", String.class),
        COL110(110, "卡号", "cardNo", String.class),
        COL120(120, "证件号", "identNo", String.class),
        COL130(130, "证件类型", "identType", String.class),
        COL140(140, "催收员ID", "collectionUser.id", SysNewUserEntity.class, Integer.class),
        COL150(150, "催收区域ID", "collectionArea.id", SysDictionaryEntity.class, Integer.class),
        COL160(160, "催收小结", "collectInfo", String.class),
        COL170(170, "M值系数", "mVal", String.class),
        COL180(180, "公司佣金比率", "commissionRate", Double.class),
        COL190(190, "公司佣金金额", "commissionMoney", BigDecimal.class),
        COL200(200, "家庭号码", "homeTelNumber", String.class),
        COL210(210, "单位号码", "unitTelNumber", String.class),
        COL220(220, "本人手机", "tel", String.class),
        COL230(230, "单位名称", "unitName", String.class),
        COL240(240, "案人职位", "caseUserPosition", String.class),
        COL250(250, "案人部门", "caseUserDepart", String.class),
        COL260(260, "单位地址", "unitAddress", String.class),
        COL270(270, "单位邮编", "unitZipCode", String.class),
        COL280(280, "家庭地址", "homeAddress", String.class),
        COL290(290, "家庭邮编", "homeZipCode", String.class),
        COL300(300, "对账单地址", "statementAddress", String.class),
        COL310(310, "对账单邮编", "statementZipCode", String.class),
        COL320(320, "户籍地址", "censusRegisterAddress", String.class),
        COL330(330, "户籍地邮编", "censusRegisterZipCode", String.class),
        COL340(340, "QQ", "qq", String.class),
        COL350(350, "邮箱", "email", String.class),
        COL360(360, "生日", "birthday", String.class),
        COL370(370, "年龄", "age", Integer.class),
        COL380(380, "省份", "province.name", SysDictionaryEntity.class, String.class),
        COL390(390, "城市", "city.name", SysDictionaryEntity.class, String.class),
        COL400(400, "区县", "county.name", SysDictionaryEntity.class, String.class),
        COL410(410, "预计退案日", "expectTime", String.class),
        COL420(420, "备注1", "caseRemarks[0].remark", DataCaseRemarkEntity.class, String.class),
        COL430(430, "备注2", "caseRemarks[1].remark", DataCaseRemarkEntity.class, String.class),
        COL440(440, "备注3", "caseRemarks[2].remark", DataCaseRemarkEntity.class, String.class),
        COL450(450, "备注4", "caseRemarks[3].remark", DataCaseRemarkEntity.class, String.class),
        COL460(460, "备注5", "caseRemarks[4].remark", DataCaseRemarkEntity.class, String.class),
        COL470(470, "备注6", "caseRemarks[5].remark", DataCaseRemarkEntity.class, String.class),
        COL480(480, "商品", "goods", String.class),
        COL490(490, "商户", "commercialTenant", String.class),
        COL500(500, "档案号", "archiveNo", String.class),
        COL510(510, "申请单号", "applyOrderNo", String.class),
        COL520(520, "社保电脑号", "socialSecurityComputerNo", String.class),
        COL530(530, "社保卡号", "socialSecurityCardNo", String.class),
        COL540(540, "开户行", "bank", String.class),
        COL550(550, "账号", "account", String.class),
        COL560(560, "账户名称", "accountName", String.class),
        COL570(570, "卡类", "cardType", String.class),
        COL580(580, "本金", "principle", BigDecimal.class),
        COL590(590, "贷款利率", "loanRate", BigDecimal.class),
        COL600(600, "剩余本金", "residualPrinciple", BigDecimal.class),
        COL610(610, "每月还款", "monthlyRepayments", BigDecimal.class),
        COL620(620, "最低还款额", "minimumPayment", BigDecimal.class),
        COL630(630, "信用额度", "creditLine", BigDecimal.class),
        COL640(640, "固定额度", "fixedQuota", BigDecimal.class),
        COL650(650, "拖欠级别", "defaultLevel", String.class),
        COL660(660, "最后还款金额", "lastRepayMoney", BigDecimal.class),
        COL670(670, "最后还款日", "lastRepayDate", Date.class),
        COL680(680, "最后消费日", "lastConsumeDate", Date.class),
        COL690(690, "最后提现日", "lastWithdrawDate", Date.class),
        COL700(700, "停卡日", "stopCardDate", Date.class),
        COL710(710, "开卡日", "activeCardDate", Date.class),
        COL720(720, "账单日", "billDate", String.class),
        COL730(730, "账单周期", "billCycle", String.class),
        COL740(740, "未出账金额", "outstandingAmount", BigDecimal.class),
        COL750(750, "是否主副卡", "mainDeputyCard", String.class),
        COL760(760, "副卡卡人", "deputyCardUserName", String.class),
        COL770(770, "贷款日期", "loanDate", Date.class),
        COL780(780, "贷款截止日", "loanDeadline", Date.class),
        COL790(790, "逾期日", "overdueDate", Date.class),
        COL800(800, "逾期期数", "overduePeriods", Double.class),
        COL810(810, "逾期天数", "overdueDays", Double.class),
        COL820(820, "曾逾期次数", "overdueTimes", Integer.class),
        COL830(830, "委案期数", "entrustPeriods", Double.class),
        COL840(840, "还款期限", "repayDeadline", Date.class),
        COL850(850, "已还期数", "repaidPeriods", Double.class),
        COL860(860, "信贷分类", "loanType", String.class),
        COL870(870, "催收分类", "collectionType", String.class),
        COL880(880, "逾期金额", "overdueMoney", BigDecimal.class),
        COL890(890, "逾期本金", "overduePrinciple", BigDecimal.class),
        COL900(900, "逾期利息", "overdueInterest", BigDecimal.class),
        COL910(910, "逾期管理费", "overdueManagementCost", BigDecimal.class),
        COL920(920, "逾期罚息", "overdueDefaultInterest", BigDecimal.class),
        COL930(930, "违约金", "penalty", BigDecimal.class),
        COL940(940, "滞纳金", "lateFee", BigDecimal.class),
        COL950(950, "超限费", "overrunFee", BigDecimal.class),
        COL960(960, "保证金", "bail", BigDecimal.class),
        COL970(970, "币种", "currencyType", String.class),
        COL980(980, "原催收记录", "lastCollectionRecord", String.class),
        COL990(990, "逾期账龄", "accountAge", String.class),
        COL1000(1000, "联系人1姓名", "contacts[0].name", DataCaseContactsEntity.class, String.class),
        COL1010(1010, "联系人1证件号", "contacts[0].identNo", DataCaseContactsEntity.class, String.class),
        COL1020(1020, "联系人1关系", "contacts[0].relation", DataCaseContactsEntity.class, String.class),
        COL1030(1030, "联系人1家庭电话", "contacts[0].homeTel", DataCaseContactsEntity.class, String.class),
        COL1040(1040, "联系人1单位电话", "contacts[0].unitTel", DataCaseContactsEntity.class, String.class),
        COL1050(1050, "联系人1手机", "contacts[0].mobile", DataCaseContactsEntity.class, String.class),
        COL1060(1060, "联系人1地址", "contacts[0].address", DataCaseContactsEntity.class, String.class),
        COL1070(1070, "联系人1单位", "contacts[0].unit", DataCaseContactsEntity.class, String.class),
        COL1080(1080, "联系人2姓名", "contacts[1].name", DataCaseContactsEntity.class, String.class),
        COL1090(1090, "联系人2证件号", "contacts[1].identNo", DataCaseContactsEntity.class, String.class),
        COL1100(1100, "联系人2关系", "contacts[1].relation", DataCaseContactsEntity.class, String.class),
        COL1110(1110, "联系人2家庭电话", "contacts[1].homeTel", DataCaseContactsEntity.class, String.class),
        COL1120(1120, "联系人2单位电话", "contacts[1].unitTel", DataCaseContactsEntity.class, String.class),
        COL1130(1130, "联系人2手机", "contacts[1].mobile", DataCaseContactsEntity.class, String.class),
        COL1140(1140, "联系人2地址", "contacts[1].address", DataCaseContactsEntity.class, String.class),
        COL1150(1150, "联系人2单位", "contacts[1].unit", DataCaseContactsEntity.class, String.class),
        COL1160(1160, "联系人3姓名", "contacts[2].name", DataCaseContactsEntity.class, String.class),
        COL1170(1170, "联系人3证件号", "contacts[2].identNo", DataCaseContactsEntity.class, String.class),
        COL1180(1180, "联系人3关系", "contacts[2].relation", DataCaseContactsEntity.class, String.class),
        COL1190(1190, "联系人3家庭电话", "contacts[2].homeTel", DataCaseContactsEntity.class, String.class),
        COL1200(1200, "联系人3单位电话", "contacts[2].unitTel", DataCaseContactsEntity.class, String.class),
        COL1210(1210, "联系人3手机", "contacts[2].mobile", DataCaseContactsEntity.class, String.class),
        COL1220(1220, "联系人3地址", "contacts[2].address", DataCaseContactsEntity.class, String.class),
        COL1230(1230, "联系人3单位", "contacts[2].unit", DataCaseContactsEntity.class, String.class),
        COL1240(1240, "联系人4姓名", "contacts[3].name", DataCaseContactsEntity.class, String.class),
        COL1250(1250, "联系人4证件号", "contacts[3].identNo", DataCaseContactsEntity.class, String.class),
        COL1260(1260, "联系人4关系", "contacts[3].relation", DataCaseContactsEntity.class, String.class),
        COL1270(1270, "联系人4家庭电话", "contacts[3].homeTel", DataCaseContactsEntity.class, String.class),
        COL1280(1280, "联系人4单位电话", "contacts[3].unitTel", DataCaseContactsEntity.class, String.class),
        COL1290(1290, "联系人4手机", "contacts[3].mobile", DataCaseContactsEntity.class, String.class),
        COL1300(1300, "联系人4地址", "contacts[3].address", DataCaseContactsEntity.class, String.class),
        COL1310(1310, "联系人4单位", "contacts[3].unit", DataCaseContactsEntity.class, String.class),
        COL1320(1320, "联系人5姓名", "contacts[4].name", DataCaseContactsEntity.class, String.class),
        COL1330(1330, "联系人5证件号", "contacts[4].identNo", DataCaseContactsEntity.class, String.class),
        COL1340(1340, "联系人5关系", "contacts[4].relation", DataCaseContactsEntity.class, String.class),
        COL1350(1350, "联系人5家庭电话", "contacts[4].homeTel", DataCaseContactsEntity.class, String.class),
        COL1360(1360, "联系人5单位电话", "contacts[4].unitTel", DataCaseContactsEntity.class, String.class),
        COL1370(1370, "联系人5手机", "contacts[4].mobile", DataCaseContactsEntity.class, String.class),
        COL1380(1380, "联系人5地址", "contacts[4].address", DataCaseContactsEntity.class, String.class),
        COL1390(1390, "联系人5单位", "contacts[4].unit", DataCaseContactsEntity.class, String.class),
        COL1400(1400, "联系人6姓名", "contacts[5].name", DataCaseContactsEntity.class, String.class),
        COL1410(1410, "联系人6证件号", "contacts[5].identNo", DataCaseContactsEntity.class, String.class),
        COL1420(1420, "联系人6关系", "contacts[5].relation", DataCaseContactsEntity.class, String.class),
        COL1430(1430, "联系人6家庭电话", "contacts[5].homeTel", DataCaseContactsEntity.class, String.class),
        COL1440(1440, "联系人6单位电话", "contacts[5].unitTel", DataCaseContactsEntity.class, String.class),
        COL1450(1450, "联系人6手机", "contacts[5].mobile", DataCaseContactsEntity.class, String.class),
        COL1460(1460, "联系人6地址", "contacts[5].address", DataCaseContactsEntity.class, String.class),
        COL1470(1470, "联系人6单位", "contacts[5].unit", DataCaseContactsEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        StandardCase(Integer sort, String col, String attr, Class... attrClazz) {
            this.sort = sort;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public String getCol() {
            return col;
        }

        public void setCol(String col) {
            this.col = col;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public Class[] getAttrClazz() {
            return attrClazz;
        }

        public void setAttrClazz(Class... attrClazz) {
            this.attrClazz = attrClazz;
        }
    }

    public enum CardLoanCase implements ExcelEnum {
        COL10(10, "个案序列号", "seqNo", String.class),
        COL20(20, "委案日期", "caseDate", String.class),
        COL30(30, "委案期限", "caseDeadline", String.class),
        COL40(40, "姓名", "name", String.class),
        COL50(50, "合同编号", "contractNo", String.class),
        COL60(60, "性别", "gender", String.class),
        COL70(70, "生日", "birthday", String.class),
        COL80(80, "证件号", "identNo", String.class),
        COL90(90, "委案金额", "money", BigDecimal.class),
        COL100(100, "人民币", "rmb", BigDecimal.class),
        COL110(110, "港币", "hkd", BigDecimal.class),
        COL120(120, "外币", "foreignCurrency", BigDecimal.class),
        COL130(130, "逾期天数", "overDays", int.class),
        COL140(140, "逾期金额", "overdueMoney", BigDecimal.class),
        COL150(150, "每月还款", "monthlyRepayments", BigDecimal.class),
        COL160(160, "委案期数", "entrustPeriods", Double.class),
        COL170(170, "逾期期数", "overduePeriods", Double.class),
        COL180(180, "曾经逾期次数", "overdueTimes", Integer.class),
        COL190(190, "已还期数", "repaidPeriods", Double.class),
        COL200(200, "剩余本金", "residualPrinciple", BigDecimal.class),
        COL210(210, "省份", "province.name", SysDictionaryEntity.class, String.class),
        COL220(220, "城市", "city.name", SysDictionaryEntity.class, String.class),
        COL230(230, "区县", "county.name", SysDictionaryEntity.class, String.class),
        COL240(240, "经销商", "dealer", String.class),
        COL250(250, "本人手机", "tel", String.class),
        COL260(260, "家庭号码", "homeTelNumber", String.class),
        COL270(270, "单位名称", "unitName", String.class),
        COL280(280, "单位号码", "unitTelNumber", String.class),
        COL290(290, "家庭邮编", "homeZipCode", String.class),
        COL300(300, "单位邮编", "unitZipCode", String.class),
        COL310(310, "申请日期", "applyDate", Date.class),
        COL320(320, "贷款日期", "loanDate", Date.class),
        COL330(330, "贷款截止日", "loanDeadline", Date.class),
        COL340(340, "还款日", "repayDate", String.class),
        COL350(350, "档案号", "archiveNo", String.class),
        COL360(360, "申请单号", "applyOrderNo", String.class),
        COL370(370, "社保电脑号", "socialSecurityComputerNo", String.class),
        COL380(380, "社保卡号", "socialSecurityCardNo", String.class),
        COL390(390, "保单到期日", "policyExpiryDate", Date.class),
        COL400(400, "本金", "principle", BigDecimal.class),
        COL410(410, "信贷分类", "loanType", String.class),
        COL420(420, "贷款利率", "loanRate", BigDecimal.class),
        COL430(430, "车价", "cardPrice", BigDecimal.class),
        COL440(440, "车型", "cardModel", String.class),
        COL450(450, "案人职位", "caseUserPosition", String.class),
        COL460(460, "案人部门", "caseUserDepart", String.class),
        COL470(470, "银行", "bank", String.class),
        COL480(480, "账号", "account", String.class),
        COL490(490, "牌照号", "license", String.class),
        COL500(500, "品牌", "brand", String.class),
        COL510(510, "车架号", "vin", String.class),
        COL520(520, "发动机号", "engineNo", String.class),
        COL530(530, "催收员ID", "collectionUser.id", SysNewUserEntity.class, Integer.class),
        COL540(540, "催收区域ID", "collectionArea.id", SysDictionaryEntity.class, Integer.class),
        COL550(550, "催收小结", "collectInfo", String.class),
        COL560(560, "M值系数", "mVal", String.class),
        COL570(570, "公司佣金比率", "commissionRate", Double.class),
        COL580(580, "公司佣金金额", "commissionMoney", BigDecimal.class),
        COL590(590, "电邮", "email", String.class),
        COL600(600, "预计退案日", "expectTime", String.class),
        COL610(610, "币种", "currencyType", String.class),
        COL620(620, "原催收记录", "lastCollectionRecord", String.class),
        COL630(630, "逾期账龄", "overdueBillTime", String.class),
        COL640(640, "逾期日", "overdueDate", Date.class),
        COL650(650, "还款期限", "repayDeadline", Date.class),
        COL660(660, "催收分类", "collectionType", String.class),
        COL670(670, "逾期利息", "overdueInterest", BigDecimal.class),
        COL680(680, "滞纳金", "lateFee", BigDecimal.class),
        COL690(690, "单位地址", "unitAddress", String.class),
        COL700(700, "家庭地址", "homeAddress", String.class),
        COL710(710, "户籍地址", "censusRegisterAddress", String.class),
        COL720(720, "户籍地邮编", "censusRegisterZipCode", String.class),
        COL730(730, "对账单地址", "statementAddress", String.class),
        COL740(740, "对账单邮编", "statementZipCode", String.class),
        COL750(750, "备注1", "caseRemarks[0].remark", DataCaseRemarkEntity.class, String.class),
        COL760(760, "备注2", "caseRemarks[1].remark", DataCaseRemarkEntity.class, String.class),
        COL770(770, "备注3", "caseRemarks[2].remark", DataCaseRemarkEntity.class, String.class),
        COL780(780, "备注4", "caseRemarks[3].remark", DataCaseRemarkEntity.class, String.class),
        COL790(790, "备注5", "caseRemarks[4].remark", DataCaseRemarkEntity.class, String.class),
        COL800(800, "备注6", "caseRemarks[5].remark", DataCaseRemarkEntity.class, String.class),
        COL810(810, "配偶姓名", "contacts[0].name", DataCaseContactsEntity.class, String.class),
        COL820(820, "配偶证件号", "contacts[0].identNo", DataCaseContactsEntity.class, String.class),
        COL830(830, "配偶家庭电话", "contacts[0].homeTel", DataCaseContactsEntity.class, String.class),
        COL840(840, "配偶单位电话", "contacts[0].unitTel", DataCaseContactsEntity.class, String.class),
        COL850(850, "配偶手机", "contacts[0].mobile", DataCaseContactsEntity.class, String.class),
        COL860(860, "配偶地址", "contacts[0].address", DataCaseContactsEntity.class, String.class),
        COL870(870, "配偶单位", "contacts[0].unit", DataCaseContactsEntity.class, String.class),
        COL880(880, "担保人姓名", "contacts[1].name", DataCaseContactsEntity.class, String.class),
        COL890(890, "担保人证件号", "contacts[1].identNo", DataCaseContactsEntity.class, String.class),
        COL900(900, "担保人关系", "contacts[1].relation", DataCaseContactsEntity.class, String.class),
        COL910(910, "担保人家庭电话", "contacts[1].homeTel", DataCaseContactsEntity.class, String.class),
        COL920(920, "担保人单位电话", "contacts[1].unitTel", DataCaseContactsEntity.class, String.class),
        COL930(930, "担保人手机", "contacts[1].mobile", DataCaseContactsEntity.class, String.class),
        COL940(940, "担保人地址", "contacts[1].address", DataCaseContactsEntity.class, String.class),
        COL950(950, "担保人单位", "contacts[1].unit", DataCaseContactsEntity.class, String.class),
        COL960(960, "联系人2姓名", "contacts[2].name", DataCaseContactsEntity.class, String.class),
        COL970(970, "联系人2证件号", "contacts[2].identNo", DataCaseContactsEntity.class, String.class),
        COL980(980, " 联系人2关系", "contacts[2].relation", DataCaseContactsEntity.class, String.class),
        COL990(990, "联系人2家庭电话", "contacts[2].homeTel", DataCaseContactsEntity.class, String.class),
        COL1000(1000, "联系人2单位电话", "contacts[2].unitTel", DataCaseContactsEntity.class, String.class),
        COL1010(1010, "联系人2手机", "contacts[2].mobile", DataCaseContactsEntity.class, String.class),
        COL1020(1020, "联系人2地址", "contacts[2].address", DataCaseContactsEntity.class, String.class),
        COL1030(1030, "联系人2单位", "contacts[2].unit", DataCaseContactsEntity.class, String.class),
        COL1040(1040, "联系人3姓名", "contacts[3].name", DataCaseContactsEntity.class, String.class),
        COL1050(1050, "联系人3证件号", "contacts[3].identNo", DataCaseContactsEntity.class, String.class),
        COL1060(1060, " 联系人3关系", "contacts[3].relation", DataCaseContactsEntity.class, String.class),
        COL1070(1070, "联系人3家庭电话", "contacts[3].homeTel", DataCaseContactsEntity.class, String.class),
        COL1080(1080, "联系人3单位电话", "contacts[3].unitTel", DataCaseContactsEntity.class, String.class),
        COL1090(1090, "联系人3手机", "contacts[3].mobile", DataCaseContactsEntity.class, String.class),
        COL1100(1100, "联系人3地址", "contacts[3].address", DataCaseContactsEntity.class, String.class),
        COL1110(1110, "联系人3单位", "contacts[3].unit", DataCaseContactsEntity.class, String.class),
        COL1120(1120, "联系人4姓名", "contacts[4].name", DataCaseContactsEntity.class, String.class),
        COL1130(1130, "联系人4证件号", "contacts[4].identNo", DataCaseContactsEntity.class, String.class),
        COL1140(1140, " 联系人4关系", "contacts[4].relation", DataCaseContactsEntity.class, String.class),
        COL1150(1150, "联系人4家庭电话", "contacts[4].homeTel", DataCaseContactsEntity.class, String.class),
        COL1160(1160, "联系人4单位电话", "contacts[4].unitTel", DataCaseContactsEntity.class, String.class),
        COL1170(1170, "联系人4手机", "contacts[4].mobile", DataCaseContactsEntity.class, String.class),
        COL1180(1180, "联系人4地址", "contacts[4].address", DataCaseContactsEntity.class, String.class),
        COL1190(1190, "联系人4单位", "contacts[4].unit", DataCaseContactsEntity.class, String.class),
        COL1200(1200, "联系人5姓名", "contacts[5].name", DataCaseContactsEntity.class, String.class),
        COL1210(1210, "联系人5证件号", "contacts[5].identNo", DataCaseContactsEntity.class, String.class),
        COL1220(1220, " 联系人5关系", "contacts[5].relation", DataCaseContactsEntity.class, String.class),
        COL1230(1230, "联系人5家庭电话", "contacts[5].homeTel", DataCaseContactsEntity.class, String.class),
        COL1240(1240, "联系人5单位电话", "contacts[5].unitTel", DataCaseContactsEntity.class, String.class),
        COL1250(1250, "联系人5手机", "contacts[5].mobile", DataCaseContactsEntity.class, String.class),
        COL1260(1260, "联系人5地址", "contacts[5].address", DataCaseContactsEntity.class, String.class),
        COL1270(1270, "联系人5单位", "contacts[5].unit", DataCaseContactsEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CardLoanCase(Integer sort, String col, String attr, Class... attrClazz) {
            this.sort = sort;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public String getCol() {
            return col;
        }

        public void setCol(String col) {
            this.col = col;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public Class[] getAttrClazz() {
            return attrClazz;
        }

        public void setAttrClazz(Class... attrClazz) {
            this.attrClazz = attrClazz;
        }
    }

    public enum CaseExportCase implements ExcelEnum {
        COL10(10, "ID", "id", Integer.class),
        COL20(20, "个案序列号", "seqNo", String.class),
        COL30(30, "姓名", "name", String.class),
        COL40(40, "委托方", "client", String.class),
        COL50(50, "批次号", "batchNo", String.class),
        COL60(60, "案件状态", "status", int.class),
        COL70(70, "证件号", "identNo", String.class),
        COL80(80, "证件类型", "identType", String.class),
        COL90(90, "性别", "gender", String.class),
        COL100(100, "催收状态", "collectStatusMsg", String.class),
        COL110(110, "外访状态", "inteviewStatus", String.class),
        COL120(120, "开户行", "bank", String.class),
        COL130(130, "卡号", "cardNo", String.class),
        COL140(140, "账号", "account", String.class),
        COL150(150, "账户名称", "accountName", String.class),
        COL160(160, "卡类", "cardType", String.class),
        COL170(170, "档案号", "archiveNo", String.class),
        COL180(180, "委案日期", "caseDate", String.class),
        COL190(190, "委案金额", "money", BigDecimal.class),
        COL200(200, "PTP金额", "proRepayAmt", BigDecimal.class),
        COL210(210, "CP金额", "bankAmt", BigDecimal.class),
        COL220(220, "最新欠款（导入利息后更新）", "latestOverdueMoney", BigDecimal.class),
        COL230(230, "人民币", "rmb", BigDecimal.class),
        COL240(240, "港币", "hkd", BigDecimal.class),
        COL250(250, "外币", "foreignCurrency", BigDecimal.class),
        COL260(260, "催收员", "odv", String.class),
        COL270(270, "催收员ID", "collectionUser.id", SysNewUserEntity.class, Integer.class),
        COL280(280, "催收员部门", "dept", String.class),
        COL290(290, "催收区域", "collectArea", String.class),
        COL300(300, "催收小结", "collectInfo", String.class),
        COL310(310, "最后通电", "lastCall", String.class),
        COL320(320, "已还款", "repayMoney", BigDecimal.class),
        COL330(330, "分配历史", "distributeHistory", String.class),
        COL340(340, "分配时间", "distributeDate", Date.class),
        COL350(350, "下次跟进日期", "nextFollowDate", Date.class),
        COL360(360, "跟进次数", "collectTimes", int.class),
        COL370(370, "M值系数", "mVal", String.class),
        COL380(380, "逾期账龄", "accountAge", String.class),
        COL390(390, "邮箱", "email", String.class),
        COL400(400, "QQ", "qq", String.class),
        COL410(410, "手机", "tel", String.class),
        COL420(420, "家庭号码", "homeTelNumber", String.class),
        COL430(430, "单位号码", "unitTelNumber", String.class),
        COL440(440, "单位名称", "unitName", String.class),
        COL450(450, "单位地址", "unitAddress", String.class),
        COL460(460, "单位邮编", "unitZipCode", String.class),
        COL470(470, "家庭地址", "homeAddress", String.class),
        COL480(480, "家庭邮编", "homeZipCode", String.class),
        COL490(490, "对账单地址", "statementAddress", String.class),
        COL500(500, "对账单邮编", "statementZipCode", String.class),
        COL510(510, "户籍地址", "censusRegisterAddress", String.class),
        COL520(520, "户籍地邮编", "censusRegisterZipCode", String.class),
        COL540(540, "部门", "dept", String.class),
        COL550(550, "省份", "province.name", SysDictionaryEntity.class, String.class),
        COL560(560, "城市", "city.name", SysDictionaryEntity.class, String.class),
        COL570(570, "区县", "county.name", SysDictionaryEntity.class, String.class),
        COL580(580, "生日", "birthday", String.class),
        COL590(590, "年龄", "age", Integer.class),
        COL600(600, "未出账金额", "outstandingAmount", BigDecimal.class),
        COL610(610, "币种", "currencyType", String.class),
        COL620(620, "原催收记录", "lastCollectionRecord", String.class),
        COL630(630, "本金", "principle", BigDecimal.class),
        COL640(640, "最低还款额", "minimumPayment", BigDecimal.class),
        COL650(650, "信用额度", "creditLine", BigDecimal.class),
        COL660(660, "拖欠级别", "defaultLevel", String.class),
        COL670(670, "信贷分类", "loanType", String.class),
        COL680(680, "催收分类", "collectionType", String.class),
        COL690(690, "逾期利息", "overdueInterest", BigDecimal.class),
        COL700(700, "滞纳金", "lateFee", BigDecimal.class),
        COL710(710, "最后还款日", "lastRepayDate", Date.class),
        COL720(720, "最后消费日", "lastConsumeDate", Date.class),
        COL730(730, "最后提现日", "lastWithdrawDate", Date.class),
        COL740(740, "停卡日", "stopCardDate", Date.class),
        COL750(750, "开卡日", "activeCardDate", Date.class),
        COL760(760, "还款期限", "repayDeadline", Date.class),
        COL770(770, "联系人1姓名", "contacts[0].name", DataCaseContactsEntity.class, String.class),
        COL780(780, "联系人1证件号", "contacts[0].identNo", DataCaseContactsEntity.class, String.class),
        COL790(790, "联系人1关系", "contacts[0].relation", DataCaseContactsEntity.class, String.class),
        COL800(800, "联系人1单位", "contacts[0].unit", DataCaseContactsEntity.class, String.class),
        COL810(810, "联系人1家庭电话", "contacts[0].homeTel", DataCaseContactsEntity.class, String.class),
        COL820(820, "联系人1单位电话", "contacts[0].unitTel", DataCaseContactsEntity.class, String.class),
        COL830(830, "联系人1手机", "contacts[0].mobile", DataCaseContactsEntity.class, String.class),
        COL840(840, "联系人1地址", "contacts[0].address", DataCaseContactsEntity.class, String.class),
        COL850(850, "联系人2姓名", "contacts[1].name", DataCaseContactsEntity.class, String.class),
        COL860(860, "联系人2证件号", "contacts[1].identNo", DataCaseContactsEntity.class, String.class),
        COL870(870, "联系人2关系", "contacts[1].relation", DataCaseContactsEntity.class, String.class),
        COL880(880, "联系人2单位", "contacts[1].unit", DataCaseContactsEntity.class, String.class),
        COL890(890, "联系人2家庭电话", "contacts[1].homeTel", DataCaseContactsEntity.class, String.class),
        COL900(900, "联系人2单位电话", "contacts[1].unitTel", DataCaseContactsEntity.class, String.class),
        COL910(910, "联系人2手机", "contacts[1].mobile", DataCaseContactsEntity.class, String.class),
        COL920(920, "联系人2地址", "contacts[1].address", DataCaseContactsEntity.class, String.class),
        COL930(930, "联系人3姓名", "contacts[2].name", DataCaseContactsEntity.class, String.class),
        COL940(940, "联系人3证件号", "contacts[2].identNo", DataCaseContactsEntity.class, String.class),
        COL950(950, "联系人3关系", "contacts[2].relation", DataCaseContactsEntity.class, String.class),
        COL960(960, "联系人3单位", "contacts[2].unit", DataCaseContactsEntity.class, String.class),
        COL970(970, "联系人3家庭电话", "contacts[2].homeTel", DataCaseContactsEntity.class, String.class),
        COL980(980, "联系人3单位电话", "contacts[2].unitTel", DataCaseContactsEntity.class, String.class),
        COL990(990, "联系人3手机", "contacts[2].mobile", DataCaseContactsEntity.class, String.class),
        COL1000(1000, "联系人3地址", "contacts[2].address", DataCaseContactsEntity.class, String.class),
        COL1010(1010, "联系人4姓名", "contacts[3].name", DataCaseContactsEntity.class, String.class),
        COL1020(1020, "联系人4证件号", "contacts[3].identNo", DataCaseContactsEntity.class, String.class),
        COL1030(1030, "联系人4关系", "contacts[3].relation", DataCaseContactsEntity.class, String.class),
        COL1040(1040, "联系人4单位", "contacts[3].unit", DataCaseContactsEntity.class, String.class),
        COL1050(1050, "联系人4家庭电话", "contacts[3].homeTel", DataCaseContactsEntity.class, String.class),
        COL1060(1060, "联系人4单位电话", "contacts[3].unitTel", DataCaseContactsEntity.class, String.class),
        COL1070(1070, "联系人4手机", "contacts[3].mobile", DataCaseContactsEntity.class, String.class),
        COL1080(1080, "联系人4地址", "contacts[3].address", DataCaseContactsEntity.class, String.class),
        COL1090(1090, "联系人5姓名", "contacts[4].name", DataCaseContactsEntity.class, String.class),
        COL1100(1100, "联系人5证件", "contacts[4].identNo", DataCaseContactsEntity.class, String.class),
        COL1110(1110, "联系人5关系", "contacts[4].relation", DataCaseContactsEntity.class, String.class),
        COL1120(1120, "联系人5单位", "contacts[4].unit", DataCaseContactsEntity.class, String.class),
        COL1130(1130, "联系人5家庭电话", "contacts[4].homeTel", DataCaseContactsEntity.class, String.class),
        COL1140(1140, "联系人5单位电话", "contacts[4].unitTel", DataCaseContactsEntity.class, String.class),
        COL1150(1150, "联系人5手机", "contacts[4].mobile", DataCaseContactsEntity.class, String.class),
        COL1160(1160, "联系人5地址", "contacts[4].address", DataCaseContactsEntity.class, String.class),
        COL1170(1170, "联系人6姓名", "contacts[5].name", DataCaseContactsEntity.class, String.class),
        COL1180(1180, "联系人6证件", "contacts[5].identNo", DataCaseContactsEntity.class, String.class),
        COL1190(1190, "联系人6关系", "contacts[5].relation", DataCaseContactsEntity.class, String.class),
        COL1200(1200, "联系人6单位", "contacts[5].unit", DataCaseContactsEntity.class, String.class),
        COL1210(1210, "联系人6家庭电话", "contacts[5].homeTel", DataCaseContactsEntity.class, String.class),
        COL1220(1220, "联系人6单位电话", "contacts[5].unitTel", DataCaseContactsEntity.class, String.class),
        COL1230(1230, "联系人6手机", "contacts[5].mobile", DataCaseContactsEntity.class, String.class),
        COL1240(1240, "联系人6地址", "contacts[5].address", DataCaseContactsEntity.class, String.class),
        COL1250(1250, "备注1", "caseRemarks[0].remark", DataCaseRemarkEntity.class, String.class),
        COL1260(1260, "备注2", "caseRemarks[1].remark", DataCaseRemarkEntity.class, String.class),
        COL1270(1270, "备注3", "caseRemarks[2].remark", DataCaseRemarkEntity.class, String.class),
        COL1280(1280, "备注4", "caseRemarks[3].remark", DataCaseRemarkEntity.class, String.class),
        COL1290(1290, "备注5", "caseRemarks[4].remark", DataCaseRemarkEntity.class, String.class),
        COL1300(1300, "备注6", "caseRemarks[5].remark", DataCaseRemarkEntity.class, String.class),
        COL1310(1310, "商品", "goods", String.class),
        COL1320(1320, "商户", "commercialTenant", String.class),
        COL1330(1330, "总欠款(委案金融+公司佣金)", "totalOverdueMoney", BigDecimal.class),
        COL1340(1340, "欠款余额", "overdueBalance", BigDecimal.class),
        COL1350(1350, "申请单号", "applyOrderNo", String.class),
        COL1360(1360, "逾期日期", "overdueDate", Date.class),
        COL1370(1370, "催收手别", "collectHand", String.class),
        COL1380(1380, "逾期天数", "overDays", int.class),
        COL1390(1390, "委托期限", "caseDeadline", String.class),
        COL1400(1400, "委案期数", "entrustPeriods", Double.class),
        COL1410(1410, "已还期数", "repaidPeriods", Double.class),
        COL1420(1420, "账单日", "billDate", Date.class),
        COL1430(1430, "固定额度", "fixedQuota", BigDecimal.class),
        COL1440(1440, "账单周期", "billCycle", String.class),
        COL1450(1450, "最后还款额", "lastRepayMoney", BigDecimal.class),
        COL1460(1460, "预计退案日期", "expectTime", String.class),
        COL1470(1470, "是否主卡", "mainCard", String.class),
        COL1480(1480, "副卡卡人", "deputyCardUserName", String.class),
        COL1490(1490, "贷款日期", "loanDate", Date.class),
        COL1500(1500, "剩余本金", "residualPrinciple", BigDecimal.class),
        COL1510(1510, "逾期期数", "overduePeriods", Double.class),
        COL1520(1520, "曾逾期次数", "overdueTimes", Integer.class),
        COL1530(1530, "贷款利率", "loanRate", BigDecimal.class),
        COL1540(1540, "每月还款", "monthlyRepayments", BigDecimal.class),
        COL1550(1550, "逾期金额", "overdueMoney", BigDecimal.class),
        COL1560(1560, "逾期本金", "overduePrinciple", BigDecimal.class),
        COL1570(1570, "逾期罚息", "overdueDefaultInterest", BigDecimal.class),
        COL1580(1580, "逾期管理费", "overdueManagementCost", BigDecimal.class),
        COL1590(1590, "违约金", "penalty", BigDecimal.class),
        COL1600(1600, "超限费", "overrunFee", BigDecimal.class),
        COL1610(1610, "贷款截止日", "loanDeadline", Date.class),
        COL1620(1620, "保证金", "bail", BigDecimal.class),
        COL1630(1630, "社保电脑号", "socialSecurityComputerNo", String.class),
        COL1640(1640, "社保卡号", "socialSecurityCardNo", String.class),
        COL1650(1650, "实际退案日", "realReturnTime", String.class),
        COL1660(1660, "车型", "cardModel", String.class),
        COL1670(1670, "牌照号", "license", String.class),
        COL1680(1680, "车架号", "vin", String.class),
        COL1690(1690, "警告", "warning", String.class),
        COL1700(1700, "自定义信息", "selfInfo", String.class),
        COL1710(1710, "最新催记", "newCase", String.class),
        COL1720(1720, "公司佣金比率", "commissionRate", Double.class),
        ;


        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseExportCase(Integer sort, String col, String attr, Class... attrClazz) {
            this.sort = sort;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        public Integer getSort() {
            return sort;
        }

        public void setSort(Integer sort) {
            this.sort = sort;
        }

        public String getCol() {
            return col;
        }

        public void setCol(String col) {
            this.col = col;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public Class[] getAttrClazz() {
            return attrClazz;
        }

        public void setAttrClazz(Class... attrClazz) {
            this.attrClazz = attrClazz;
        }
    }


    public enum CaseExportCaseConf {
        COL10("id", "ID", "da.id as id", Integer.class),
        COL20("seqNo", "个案序列号", "da.seq_no as seqNo", String.class),
        COL30("name", "姓名", "da.name as name", String.class),
        COL40("client", "委托方", "da.client as client", String.class),
        COL50("batchNo", "批次号", "da.batch_no as batchNo", String.class),
        COL60("status", "案件状态", "da.status as status,", int.class),
        COL70("identNo", "证件号", "da.ident_no as identNo", String.class),
        COL80("identType", "证件类型", "da.ident_type as identType", String.class),
        COL90("gender", "性别", "da.gender as gender", String.class),
        COL100("collectStatus", "催收状态", "da.collect_status as collectStatus", String.class),
        COL110("inteviewStatus", "外访状态", "'' as inteviewStatus", String.class),
        COL120("bank", "开户行", "da.bank as bank", String.class),
        COL130("cardNo", "卡号", "da.card_no as cardNo", String.class),
        COL140("account", "账号", "da.account as account", String.class),
        COL150("accountName", "账户名称", "da.account_name as accountName", String.class),
        COL160("cardType", "卡类", "da.card_type as cardType", String.class),
        COL170("archiveNo", "档案号", "da.archive_no as archiveNo", String.class),
        COL180("caseDate", "委案日期", "da.case_date as caseDate", String.class),
        COL190("money", "委案金额", "da.money as money", BigDecimal.class),
        COL200("proRepayAmt", "PTP金额", "da.pro_repay_amt as proRepayAmt", BigDecimal.class),
        COL210("bankAmt", "CP金额", "da.bank_amt as bankAmt", BigDecimal.class),
        COL220("latestOverdueMoney", "最新欠款（导入利息后更新）", "da.latest_overdue_money as latestOverdueMoney", BigDecimal.class),
        COL230("rmb", "人民币", "da.rmb as rmb", BigDecimal.class),
        COL240("hkd", "港币", "da.hkd as hkd", BigDecimal.class),
        COL250("foreignCurrency", "外币", "da.foreign_currency as foreignCurrency", BigDecimal.class),
        COL260("odv", "催收员", "da.odv as odv", String.class),
        COL270("collectionUser", "催收员ID", "da.odv as 'collectionUser.id'", SysNewUserEntity.class, Integer.class),
        COL280("dept", "催收员部门", "da.dept as dept", String.class),
        COL290("collectArea", "催收区域", "da.collect_area as collectArea", String.class),
        COL300("collectInfo", "催收小结", "da.collect_result as collectInfo", String.class),
        COL310("lastCall", "最后通电", "", String.class),
        COL320("repayMoney", "已还款", "da.en_repay_amt as repayMoney", BigDecimal.class),
        COL330("distributeHistory", "分配历史", "da.distribute_history as distributeHistory", String.class),
        COL340("distributeDate", "分配时间", "da.distribute_time as distributeDate", Date.class),
        COL350("nextFollowDate", "下次跟进日期", "'' as nextFollowDate", Date.class),
        COL360("collectTimes", "跟进次数", "da.collect_times as collectTimes", int.class),
        COL370("mVal", "M值系数", "da.m_val as mVal", String.class),
        COL380("accountAge", "逾期账龄", "da.account_age as accountAge", String.class),
        COL390("email", "邮箱", "da.email as email", String.class),
        COL400("qq", "QQ", "da.qq as qq", String.class),
        COL410("tel", "手机", "da.tel as tel", String.class),
        COL420("homeTelNumber", "家庭号码", "da.home_tel_number as homeTelNumber", String.class),
        COL430("unitTelNumber", "单位号码", "da.unit_tel_number as unitTelNumber", String.class),
        COL440("unitName", "单位名称", "da.unit_name as unitName", String.class),
        COL450("unitAddress", "单位地址", "da.unit_address as unitAddress", String.class),
        COL460("unitZipCode", "单位邮编", "da.unit_zip_code as unitZipCode", String.class),
        COL470("homeAddress", "家庭地址", "da.home_address as homeAddress", String.class),
        COL480("homeZipCode", "家庭邮编", "da.home_zip_code as homeZipCode", String.class),
        COL490("statementAddress", "对账单地址", "da.statement_address as statementAddress", String.class),
        COL500("statementZipCode", "对账单邮编", "da.statement_zip_code as statementZipCode", String.class),
        COL510("censusRegisterAddress", "户籍地址", "da.census_register_address as censusRegisterAddress", String.class),
        COL520("censusRegisterZipCode", "户籍地邮编", "da.census_register_zip_code as censusRegisterZipCode", String.class),
        COL540("dept", "部门", "da.dept as dept", String.class),
        COL550("province", "省份", "da.province as 'province.id'", SysDictionaryEntity.class, String.class),
        COL560("city", "城市", "da.city as 'city.id'", SysDictionaryEntity.class, String.class),
        COL570("county", "区县", "da.county as 'county.id'", SysDictionaryEntity.class, String.class),
        COL580("birthday", "生日", "da.birthday as birthday", String.class),
        COL590("age", "年龄", "da.age as age", Integer.class),
        COL600("outstandingAmount", "未出账金额", "da.outstanding_amount as outstandingAmount", BigDecimal.class),
        COL610("currencyType", "币种", "da.currency_type as currencyType", String.class),
        COL620("lastCollectionRecord", "原催收记录", "da.last_collection_record as lastCollectionRecord", String.class),
        COL630("principle", "本金", "da.principle as principle", BigDecimal.class),
        COL640("minimumPayment", "最低还款额", "da.minimum_payment as minimumPayment", BigDecimal.class),
        COL650("creditLine", "信用额度", "da.credit_line as creditLine", BigDecimal.class),
        COL660("defaultLevel", "拖欠级别", "da.default_level as defaultLevel", String.class),
        COL670("loanType", "信贷分类", "da.loan_type as loanType", String.class),
        COL680("collectionType", "催收分类", "da.collection_type as collectionType", String.class),
        COL690("overdueInterest", "逾期利息", "da.overdue_interest as overdueInterest", BigDecimal.class),
        COL700("lateFee", "滞纳金", "da.late_fee as lateFee", BigDecimal.class),
        COL710("lastRepayDate", "最后还款日", "da.last_repay_date as lastRepayDate", Date.class),
        COL720("lastConsumeDate", "最后消费日", "da.last_consume_date as lastConsumeDate", Date.class),
        COL730("lastWithdrawDate", "最后提现日", "da.last_withdraw_date as lastWithdrawDate", Date.class),
        COL740("stopCardDate", "停卡日", "da.stop_card_date as stopCardDate", Date.class),
        COL750("activeCardDate", "开卡日", "da.active_card_date as activeCardDate", Date.class),
        COL760("repayDeadline", "还款期限", "da.repay_deadline as repayDeadline", Date.class),
        COL770("contactsName0", "联系人1姓名", "contacts[0].name", DataCaseContactsEntity.class, String.class),
        COL780("contactsIdentNo0", "联系人1证件号", "contacts[0].identNo", DataCaseContactsEntity.class, String.class),
        COL790("contactsRelation0", "联系人1关系", "contacts[0].relation", DataCaseContactsEntity.class, String.class),
        COL800("contactsUnit0", "联系人1单位", "contacts[0].unit", DataCaseContactsEntity.class, String.class),
        COL810("contactsHomeTel0", "联系人1家庭电话", "contacts[0].homeTel", DataCaseContactsEntity.class, String.class),
        COL820("contactsUnitTel0", "联系人1单位电话", "contacts[0].unitTel", DataCaseContactsEntity.class, String.class),
        COL830("contactsMobile0", "联系人1手机", "contacts[0].mobile", DataCaseContactsEntity.class, String.class),
        COL840("contactsAddress0", "联系人1地址", "contacts[0].address", DataCaseContactsEntity.class, String.class),
        COL850("contactsName1", "联系人2姓名", "contacts[1].name", DataCaseContactsEntity.class, String.class),
        COL860("contactsIdentNo1", "联系人2证件号", "contacts[1].identNo", DataCaseContactsEntity.class, String.class),
        COL870("contactsRelation1", "联系人2关系", "contacts[1].relation", DataCaseContactsEntity.class, String.class),
        COL880("contactsUnit1", "联系人2单位", "contacts[1].unit", DataCaseContactsEntity.class, String.class),
        COL890("contactsHomeTel1", "联系人2家庭电话", "contacts[1].homeTel", DataCaseContactsEntity.class, String.class),
        COL900("contactsUnitTel1", "联系人2单位电话", "contacts[1].unitTel", DataCaseContactsEntity.class, String.class),
        COL910("contactsMobile1", "联系人2手机", "contacts[1].mobile", DataCaseContactsEntity.class, String.class),
        COL920("contactsAddress1", "联系人2地址", "contacts[1].address", DataCaseContactsEntity.class, String.class),
        COL930("contactsName2", "联系人3姓名", "contacts[2].name", DataCaseContactsEntity.class, String.class),
        COL940("contactsIdentNo2", "联系人3证件号", "contacts[2].identNo", DataCaseContactsEntity.class, String.class),
        COL950("contactsRelation2", "联系人3关系", "contacts[2].relation", DataCaseContactsEntity.class, String.class),
        COL960("contactsUnit2", "联系人3单位", "contacts[2].unit", DataCaseContactsEntity.class, String.class),
        COL970("contactsHomeTel2", "联系人3家庭电话", "contacts[2].homeTel", DataCaseContactsEntity.class, String.class),
        COL980("contactsUnitTel2", "联系人3单位电话", "contacts[2].unitTel", DataCaseContactsEntity.class, String.class),
        COL990("contactsMobile2", "联系人3手机", "contacts[2].mobile", DataCaseContactsEntity.class, String.class),
        COL1000("contactsAddress2", "联系人3地址", "contacts[2].address", DataCaseContactsEntity.class, String.class),
        COL1010("contactsName3", "联系人4姓名", "contacts[3].name", DataCaseContactsEntity.class, String.class),
        COL1020("contactsIdentNo3", "联系人4证件号", "contacts[3].identNo", DataCaseContactsEntity.class, String.class),
        COL1030("contactsRelation3", "联系人4关系", "contacts[3].relation", DataCaseContactsEntity.class, String.class),
        COL1040("contactsUnit3", "联系人4单位", "contacts[3].unit", DataCaseContactsEntity.class, String.class),
        COL1050("contactsHomeTel3", "联系人4家庭电话", "contacts[3].homeTel", DataCaseContactsEntity.class, String.class),
        COL1060("contactsUnitTel3", "联系人4单位电话", "contacts[3].unitTel", DataCaseContactsEntity.class, String.class),
        COL1070("contactsMobile3", "联系人4手机", "contacts[3].mobile", DataCaseContactsEntity.class, String.class),
        COL1080("contactsAddress3", "联系人4地址", "contacts[3].address", DataCaseContactsEntity.class, String.class),
        COL1090("contactsName4", "联系人5姓名", "contacts[4].name", DataCaseContactsEntity.class, String.class),
        COL1100("contactsIdentNo4", "联系人5证件", "contacts[4].identNo", DataCaseContactsEntity.class, String.class),
        COL1110("contactsRelation4", "联系人5关系", "contacts[4].relation", DataCaseContactsEntity.class, String.class),
        COL1120("contactsUnit4", "联系人5单位", "contacts[4].unit", DataCaseContactsEntity.class, String.class),
        COL1130("contactsHomeTel4", "联系人5家庭电话", "contacts[4].homeTel", DataCaseContactsEntity.class, String.class),
        COL1140("contactsUnitTel4", "联系人5单位电话", "contacts[4].unitTel", DataCaseContactsEntity.class, String.class),
        COL1150("contactsMobile4", "联系人5手机", "contacts[4].mobile", DataCaseContactsEntity.class, String.class),
        COL1160("contactsAddress4", "联系人5地址", "contacts[4].address", DataCaseContactsEntity.class, String.class),
        COL1170("contactsName5", "联系人6姓名", "contacts[5].name", DataCaseContactsEntity.class, String.class),
        COL1180("contactsIdentNo5", "联系人6证件", "contacts[5].identNo", DataCaseContactsEntity.class, String.class),
        COL1190("contactsRelation5", "联系人6关系", "contacts[5].relation", DataCaseContactsEntity.class, String.class),
        COL1200("contactsUnit5", "联系人6单位", "contacts[5].unit", DataCaseContactsEntity.class, String.class),
        COL1210("contactsHomeTel5", "联系人6家庭电话", "contacts[5].homeTel", DataCaseContactsEntity.class, String.class),
        COL1220("contactsUnitTel5", "联系人6单位电话", "contacts[5].unitTel", DataCaseContactsEntity.class, String.class),
        COL1230("contactsMobile5", "联系人6手机", "contacts[5].mobile", DataCaseContactsEntity.class, String.class),
        COL1240("contactsAddress5", "联系人6地址", "contacts[5].address", DataCaseContactsEntity.class, String.class),
        COL1250("remark0", "备注1", "caseRemarks[0].remark", DataCaseRemarkEntity.class, String.class),
        COL1260("remark1", "备注2", "caseRemarks[1].remark", DataCaseRemarkEntity.class, String.class),
        COL1270("remark2", "备注3", "caseRemarks[2].remark", DataCaseRemarkEntity.class, String.class),
        COL1280("remark3", "备注4", "caseRemarks[3].remark", DataCaseRemarkEntity.class, String.class),
        COL1290("remark4", "备注5", "caseRemarks[4].remark", DataCaseRemarkEntity.class, String.class),
        COL1300("remark5", "备注6", "caseRemarks[5].remark", DataCaseRemarkEntity.class, String.class),
        COL1310("goods", "商品", "da.goods as goods", String.class),
        COL1320("commercialTenant", "商户", "da.commercial_tenant as commercialTenant", String.class),
        COL1330("totalOverdueMoney", "总欠款(委案金额+公司佣金)", "'' as totalOverdueMoney", BigDecimal.class),
        COL1340("overdueBalance", "欠款余额", "'' as overdueBalance", BigDecimal.class),
        COL1350("applyOrderNo", "申请单号", "da.apply_order_no as applyOrderNo", String.class),
        COL1360("overdueDate", "逾期日期", "da.overdue_date as overdueDate", Date.class),
        COL1370("collectHand", "催收手别", "da.collect_hand as collectHand", String.class),
        COL1380("overDays", "逾期天数", "da.over_days as overDays", int.class),
        COL1390("caseDeadline", "委托期限", "da.case_deadline as caseDeadline", String.class),
        COL1400("entrustPeriods", "委案期数", "da.entrust_periods as entrustPeriods", Double.class),
        COL1410("repaidPeriods", "已还期数", "da.repaid_periods as repaidPeriods", Double.class),
        COL1420("billDate", "账单日", "da.bill_date as billDate", Date.class),
        COL1430("fixedQuota", "固定额度", "da.fixed_quota as fixedQuota", BigDecimal.class),
        COL1440("billCycle", "账单周期", "da.bill_cycle as billCycle", String.class),
        COL1450("lastRepayMoney", "最后还款额", "da.last_repay_money as lastRepayMoney", BigDecimal.class),
        COL1460("expectTime", "预计退案日期", "da.expect_time as expectTime,", String.class),
        COL1470("mainCard", "是否主卡", "da.main_deputy_card as mainCard", String.class),
        COL1480("deputyCardUserName", "副卡卡人", "da.deputy_card_user_name as deputyCardUserName", String.class),
        COL1490("loanDate", "贷款日期", "da.loan_date as loanDate", Date.class),
        COL1500("residualPrinciple", "剩余本金", "da.residual_principle as residualPrinciple", BigDecimal.class),
        COL1510("overduePeriods", "逾期期数", "da.overdue_periods as overduePeriods", Double.class),
        COL1520("overdueTimes", "曾逾期次数", "da.overdue_times as overdueTimes,", Integer.class),
        COL1530("loanRate", "贷款利率", "da.loan_rate as loanRate", BigDecimal.class),
        COL1540("monthlyRepayments", "每月还款", "da.monthly_repayments as monthlyRepayments", BigDecimal.class),
        COL1550("overdueMoney", "逾期金额", "da.overdue_money as overdueMoney", BigDecimal.class),
        COL1560("overduePrinciple", "逾期本金", "da.overdue_principle as overduePrinciple", BigDecimal.class),
        COL1570("overdueDefaultInterest", "逾期罚息", "da.overdue_default_interest as overdueDefaultInterest", BigDecimal.class),
        COL1580("overdueManagementCost", "逾期管理费", "da.overdue_management_cost as overdueManagementCost", BigDecimal.class),
        COL1590("penalty", "违约金", "da.penalty as penalty", BigDecimal.class),
        COL1600("overrunFee", "超限费", "da.overrun_fee as overrunFee", BigDecimal.class),
        COL1610("loanDeadline", "贷款截止日", "da.loan_deadline as loanDeadline", Date.class),
        COL1620("bail", "保证金", "da.bail as bail,", BigDecimal.class),
        COL1630("socialSecurityComputerNo", "社保电脑号", "da.social_security_computer_no as socialSecurityComputerNo", String.class),
        COL1640("socialSecurityCardNo", "社保卡号", "da.social_security_card_no as socialSecurityCardNo", String.class),
        COL1650("realReturnTime", "实际退案日", "da.real_return_time as realReturnTime", String.class),
        COL1660("cardModel", "车型", "da.card_model as cardModel", String.class),
        COL1670("license", "牌照号", "da.license as license", String.class),
        COL1680("vin", "车架号", "da.vin as vin", String.class),
        COL1690("warning", "警告", "'' as warning", String.class),
        COL1700("selfInfo", "自定义信息", "da.remark as selfInfo", String.class),
        COL1710("newCase", "最新催记", "da.new_case as newCase", String.class),
        COL1720("commissionRate", "公司佣金比率", "da.commission_rate as commissionRate", Double.class),
        ;


        private String pageCol;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseExportCaseConf(String pageCol, String col, String attr, Class... attrClazz) {
            this.pageCol = pageCol;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        public String getPageCol() {
            return pageCol;
        }

        public void setPageCol(String pageCol) {
            this.pageCol = pageCol;
        }

        public String getCol() {
            return col;
        }

        public void setCol(String col) {
            this.col = col;
        }

        public String getAttr() {
            return attr;
        }

        public void setAttr(String attr) {
            this.attr = attr;
        }

        public Class[] getAttrClazz() {
            return attrClazz;
        }

        public void setAttrClazz(Class... attrClazz) {
            this.attrClazz = attrClazz;
        }
    }

}
