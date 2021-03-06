package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRemarkEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelBankReconciliationConstant {

    public enum BankReconciliationImport implements ExcelEnum {

        COL10(10, "*个案序列号", "dataCase.seqNo", DataCaseEntity.class, String.class),
        COL20(20, "*档案号", "dataCase.archiveNo", DataCaseEntity.class, String.class),
        COL30(30, "*卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL40(40, "*账号", "dataCase.account", DataCaseEntity.class, String.class),
        COL50(50, "*委案日期", "dataCase.caseDate", DataCaseEntity.class, String.class),
        COL60(60, "*CP金额", "cpMoney", BigDecimal.class),
        COL70(70, "*CP日期", "cpDate", Date.class),
        COL80(80, "备注", "remark", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        BankReconciliationImport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum BankReconciliationExport implements ExcelEnum {
        COL10(10, "还款ID", "id", Integer.class),
        COL20(20, "批次号", "dataCase.batchNo", DataCaseEntity.class, String.class),
        COL30(30, "卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL40(40, "证件号", "dataCase.identNo", DataCaseEntity.class, String.class),
        COL50(50, "姓名", "dataCase.name", DataCaseEntity.class, String.class),
        COL60(60, "个案序列号", "dataCase.seqNo", DataCaseEntity.class, String.class),
        COL70(70, "委托方", "dataCase.client", DataCaseEntity.class, String.class),
        COL80(80, "委案金额", "dataCase.money", DataCaseEntity.class, BigDecimal.class),
        COL90(90, "当前委案余额", "dataCase.balance", DataCaseEntity.class, BigDecimal.class),
        COL100(100, "账号", "dataCase.account", DataCaseEntity.class, String.class),
        COL110(110, "档案号", "dataCase.archiveNo", DataCaseEntity.class, String.class),
        COL120(120, "申请单号", "dataCase.applyOrderNo", DataCaseEntity.class, String.class),
        COL130(130, "CP金额", "cpMoney", BigDecimal.class),
        COL140(140, "CP日期", "cpDate", Date.class),
        COL150(150, "案件催收员", "dataCase.odv", DataCaseEntity.class, String.class),
        COL160(160, "回收催收员", ""),
        COL170(170, "回收部门", ""),
        COL180(180, "提交人", "submitUser.userName", SysNewUserEntity.class, String.class),
        COL190(190, "提交时间", "submitTime", Date.class),
        COL200(200, "还款金额", "dataCase.enRepayAmt", DataCaseEntity.class, BigDecimal.class),
        COL210(210, "还款日期", ""),
        COL220(220, "还款余额", "dataCase.balance", DataCaseEntity.class, BigDecimal.class),
        COL230(230, "提成金额", "dataCase.mMoney", DataCaseEntity.class, BigDecimal.class),
        COL240(240, "提成系数", "dataCase.mVal", DataCaseEntity.class, BigDecimal.class),
        COL250(250, "还款人", "repayUser", String.class),
        COL260(260, "还款方式", "repayType",  String.class),
        COL270(270, "确认人", ""),
        COL280(280, "确认时间", ""),
        COL290(290, "还款备注", "remark",  String.class),
        COL300(300, "逾期账龄", "dataCase.accountAge", DataCaseEntity.class, String.class),
        COL310(310, "逾期期数", "dataCase.overduePeriods", DataCaseEntity.class, String.class),
        COL320(320, "逾期日期", "dataCase.overdueDate", DataCaseEntity.class, Date.class),
        COL330(330, "逾期天数", "dataCase.overdueDays", DataCaseEntity.class, Double.class),
        COL340(340, "委案日期", "dataCase.caseDate", DataCaseEntity.class, String.class),
        COL350(350, "预计退案日", "dataCase.expectTime", DataCaseEntity.class, Date.class),
        COL360(360, "省份", "dataCase.province.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL370(370, "城市", "dataCase.city.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL380(380, "区县", "dataCase.county.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL390(390, "手机", "dataCase.tel", DataCaseEntity.class, String.class),
        COL400(400, "币种", "dataCase.currencyType", DataCaseEntity.class, String.class),
        COL410(410, "分配历史", "dataCase.distributeHistory", DataCaseEntity.class, String.class),
        COL420(420, "拖欠级别", "dataCase.defaultLevel", DataCaseEntity.class, String.class),
        COL430(430, "案件最新欠款", "dataCase.latestOverdueMoney", DataCaseEntity.class, BigDecimal.class),
        COL440(440, "外访状态", "dataCase.inteviewStatus", DataCaseEntity.class, String.class),
        COL450(450, "公司佣金", "dataCase.commissionMoney", DataCaseEntity.class, BigDecimal.class),
        COL460(460, "公司佣金比率", "dataCase.commissionRate", DataCaseEntity.class, String.class),
        COL470(470, "案件备注1", "dataCase.remark1", DataCaseEntity.class,  String.class),
        COL480(480, "案件备注2", "dataCase.remark2", DataCaseEntity.class, String.class),
        COL490(490, "案件备注3", "dataCase.remark3", DataCaseEntity.class, String.class),
        COL500(500, "案件备注4", "dataCase.remark4", DataCaseEntity.class, String.class),
        COL510(510, "案件备注5", "dataCase.remark5", DataCaseEntity.class, String.class),
        COL520(520, "案件备注6", "dataCase.remark6", DataCaseEntity.class, String.class),
        COL530(530, "催收区域", "dataCase.collectionArea.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        BankReconciliationExport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum BankReconciliationExportConf {
        COL10("id", "还款ID", "b.id as id", Integer.class),
        COL20("batchNo", "批次号", "c.batch_no as 'dataCase.batchNo'", DataCaseEntity.class, String.class),
        COL30("cardNo", "卡号", "c.card_no as 'dataCase.cardNo'", DataCaseEntity.class, String.class),
        COL40("identNo", "证件号", "c.ident_no as 'dataCase.identNo'", DataCaseEntity.class, String.class),
        COL50("name", "姓名", "c.name as 'dataCase.name'", DataCaseEntity.class, String.class),
        COL60("seqNo", "个案序列号", "c.seq_no as 'dataCase.seqNo'", DataCaseEntity.class, String.class),
        COL70("client", "委托方", "c.client as 'dataCase.client'", DataCaseEntity.class, String.class),
        COL80("money", "委案金额", "c.money as 'dataCase.money'", DataCaseEntity.class, BigDecimal.class),
        COL90("balance", "当前委案余额", "c.balance as 'dataCase.balance'", DataCaseEntity.class, BigDecimal.class),
        COL100("account", "账号", "c.account as 'dataCase.account'", DataCaseEntity.class, String.class),
        COL110("archiveNo", "档案号", "c.archive_no as 'dataCase.archiveNo'", DataCaseEntity.class, String.class),
        COL120("applyOrderNo", "申请单号", "c.apply_order_no as 'dataCase.applyOrderNo'", DataCaseEntity.class, String.class),
        COL130("cpMoney", "CP金额", "b.cp_money as cpMoney", DataCaseEntity.class, BigDecimal.class),
        COL140("cpDate", "CP日期", "b.cp_date as cpDate", Date.class),
        COL150("caseOdv", "案件催收员", "c.odv as 'dataCase.odv'", DataCaseEntity.class, String.class),
        COL160("backOdv", "回收催收员", ""),
        COL170("backDept", "回收部门", ""),
        COL180("submitUser", "提交人", "b.submit_user as 'submitUser.id'", SysNewUserEntity.class, String.class),
        COL190("submitTime", "提交时间", "b.submit_time as submitTime", Date.class),
        COL200("enRepayAmt", "还款金额", "c.en_repay_amt as 'dataCase.enRepayAmt'", DataCaseEntity.class, BigDecimal.class),
        COL210("repayDate", "还款日期", ""),
        COL220("repayBalance", "还款余额", "c.balance as 'dataCase.balance'", DataCaseEntity.class, BigDecimal.class),
        COL230("mMoney", "提成金额", "c.money as 'dataCase.mMoney'", DataCaseEntity.class, BigDecimal.class),
        COL240("mVal", "提成系数", "c.m_val as 'dataCase.mVal'", DataCaseEntity.class, BigDecimal.class),
        COL250("repayUser", "还款人", "b.repay_user as repayUser", String.class),
        COL260("repayType", "还款方式", "b.repay_type as repayType", SysDictionaryEntity.class, String.class),
        COL270("confirmUser", "确认人", ""),
        COL280("ConfirmTime", "确认时间", ""),
        COL290("repayRemark", "还款备注", "b.remark", SysDictionaryEntity.class, String.class),
        COL300("accountAge", "逾期账龄", "c.account_age as 'dataCase.accountAge'", DataCaseEntity.class, String.class),
        COL310("overduePeriods", "逾期期数", "c.overdue_periods as 'dataCase.overduePeriods'", DataCaseEntity.class, String.class),
        COL320("overdueDate", "逾期日期", "c.overdue_date as 'dataCase.overdueDate'", DataCaseEntity.class, Date.class),
        COL330("overdueDays", "逾期天数", "c.overdue_days as 'dataCase.overdueDays'", DataCaseEntity.class, Double.class),
        COL340("caseDate", "委案日期", "c.case_date as 'dataCase.caseDate'", DataCaseEntity.class, String.class),
        COL350("expectTime", "预计退案日", "c.expect_time as 'dataCase.expectTime'", DataCaseEntity.class, Date.class),
        COL360("province", "省份", "c.province as 'dataCase.province.name'", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL370("city", "城市", "c.city as 'dataCase.city.name'", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL380("county", "区县", "c.city as 'dataCase.county.name'", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL390("tel", "手机", "c.tel as 'dataCase.tel'", DataCaseEntity.class, String.class),
        COL400("currencyType", "币种", "c.currency_type as 'dataCase.currencyType'", DataCaseEntity.class, String.class),
        COL410("distributeHistory", "分配历史", "c.distribute_history as 'dataCase.distributeHistory'", DataCaseEntity.class, String.class),
        COL420("defaultLevel", "拖欠级别", "c.default_level as 'dataCase.defaultLevel'", DataCaseEntity.class, String.class),
        COL430("latestOverdueMoney", "案件最新欠款", "c.money  as 'dataCase.latestOverdueMoney'", DataCaseEntity.class, BigDecimal.class),
        COL440("inteviewStatus", "外访状态", "'' as 'dataCase.inteviewStatus'", DataCaseEntity.class, String.class),
        COL450("commissionMoney", "公司佣金", "c.commission_money as 'dataCase.commissionMoney'", DataCaseEntity.class, BigDecimal.class),
        COL460("commissionRate", "公司佣金比率", "c.commission_rate as 'dataCase.commissionRate'", DataCaseEntity.class, String.class),
        COL470("caseRemarks0", "案件备注1", "c.remark1 as 'dataCase.remark1'", DataCaseEntity.class, String.class),
        COL480("caseRemarks1", "案件备注2", "c.remark2 as 'dataCase.remark2'", DataCaseEntity.class, String.class),
        COL490("caseRemarks2", "案件备注3", "c.remark3 as 'dataCase.remark3'", DataCaseEntity.class, String.class),
        COL500("caseRemarks3", "案件备注4", "c.remark4 as 'dataCase.remark4'", DataCaseEntity.class, String.class),
        COL510("caseRemarks4", "案件备注5", "c.remark5 as 'dataCase.remark5'", DataCaseEntity.class, String.class),
        COL520("caseRemarks5", "案件备注6", "c.remark6 as 'dataCase.remark6'", DataCaseEntity.class, String.class),
        COL530("collectionArea", "催收区域", "c.collect_area as 'dataCase.collectionArea.id'", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        ;

        private String pageCol;

        private String col;

        private String attr;

        private Class[] attrClazz;

        BankReconciliationExportConf(String pageCol, String col, String attr, Class... attrClazz) {
            this.pageCol = pageCol;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        //根据key获取枚举
        public static ExcelBankReconciliationConstant.BankReconciliationExportConf getEnumByKey(String key){
            if(null == key){
                return null;
            }
            for(ExcelBankReconciliationConstant.BankReconciliationExportConf temp: ExcelBankReconciliationConstant.BankReconciliationExportConf.values()){
                if(temp.getPageCol().equals(key)){
                    return temp;
                }
            }
            return null;
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

    public enum BankReconciliationSortEnum {

        COLUMN1("id","b.id"),
        COLUMN2("dataCase.batchNo","c.batch_no"),
        COLUMN3("dataCase.cardNo","c.card_no"),
        COLUMN4("dataCase.identNo","c.ident_no"),
        COLUMN5("dataCase.name","c.name"),
        COLUMN6("dataCase.seqNo","c.seq_no"),
        COLUMN7("dataCase.client","c.client"),
        COLUMN8("dataCase.moneyMsg","c.money"),
        COLUMN17("dataCase.money","c.money"),
        COLUMN9("dataCase.repayMoneyMsg","c.en_repay_amt"),
        COLUMN10("cpMoneyMsg","b.cp_money"),
        COLUMN18("cpMoney","b.cp_money"),
        COLUMN19("dataCase.enRepayAmt","c.en_repay_amt"),
        COLUMN20("dataCase.enRepayAmtMsg","c.en_repay_amt"),
        COLUMN11("cpDate","b.cp_date"),
        COLUMN12("repayUser","b.repay_user"),
        COLUMN13("repayType","b.repay_type"),
        COLUMN14("submitTime","b.submit_time"),
        COLUMN15("remark","b.remark"),
        COLUMN16("submitUser.name",""),

        ;
        //防止字段值被修改，增加的字段也统一final表示常量
        private final String key;
        private final String value;

        private BankReconciliationSortEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        //根据key获取枚举
        public static BankReconciliationSortEnum getEnumByKey(String key) {
            if (null == key) {
                return null;
            }
            for (BankReconciliationSortEnum temp : BankReconciliationSortEnum.values()) {
                if (temp.getKey().equals(key)) {
                    return temp;
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }


}
