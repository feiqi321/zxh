package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelSynergisticConstant {

    public enum SynergisticExport implements ExcelEnum{
        COL10(10, "ID", "id", Integer.class),
        COL20(20, "批次号", "dataCase.batchNo", DataCaseEntity.class, String.class),
        COL30(30, "委托方", "dataCase.client", DataCaseEntity.class, String.class),
        COL40(40, "个案序列号", "dataCase.seqNo", DataCaseEntity.class, String.class),
        COL50(50, "姓名", "dataCase.name", DataCaseEntity.class, String.class),
        COL60(60, "协催类型", "synergisticType.name", SysDictionaryEntity.class, String.class),
        COL70(70, "申请内容", "applyContent", String.class),
        COL80(80, "申请时间", "applyTime", Date.class),
        COL90(90, "协催时间", "synergisticTime", Date.class),
        COL100(100, "协催人", "synergisticUser.userName", SysNewUserEntity.class, String.class),
        COL110(110, "协催结果", "synergisticResult", String.class),
        COL120(120, "证件号", "dataCase.identNo", DataCaseEntity.class, String.class),
        COL130(130, "卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL140(140, "卡类", "dataCase.cardType", DataCaseEntity.class, String.class),
        COL150(150, "账号", "dataCase.account", DataCaseEntity.class, String.class),
        COL160(160, "币种", "dataCase.currencyType", DataCaseEntity.class, String.class),
        COL170(170, "档案号", "dataCase.archiveNo", DataCaseEntity.class, String.class),
        COL180(180, "申请单号", "dataCase.applyOrderNo", DataCaseEntity.class, String.class),
        COL190(190, "委案金额", "dataCase.money", DataCaseEntity.class, BigDecimal.class),
        COL200(200, "还款金额", "dataCase.enRepayAmt", DataCaseEntity.class, BigDecimal.class),
        COL210(210, "本金", "dataCase.principle", DataCaseEntity.class, BigDecimal.class),
        COL220(220, "最后还款日", "dataCase.lastRepayDate", DataCaseEntity.class, Date.class),
        COL230(230, "委案日期", "dataCase.caseDate", DataCaseEntity.class, Date.class),
        COL240(240, "催收员", "dataCase.collectionUser.userName", DataCaseEntity.class, SysNewUserEntity.class, String.class),
        COL250(250, "信用额度", "dataCase.creditLine", DataCaseEntity.class, String.class),
        COL260(260, "家庭地址", "dataCase.homeAddress", DataCaseEntity.class, String.class),
        COL270(270, "家庭号码", "dataCase.homeTelNumber", DataCaseEntity.class, String.class),
        COL280(280, "单位名称", "dataCase.unitName", DataCaseEntity.class, String.class),
        COL290(290, "单位地址", "dataCase.unitAddress", DataCaseEntity.class, String.class),
        COL300(300, "手机", "dataCase.tel", DataCaseEntity.class, String.class),
        COL310(310, "单位号码", "dataCase.unitTelNumber", DataCaseEntity.class, String.class),
        COL320(320, "最新欠款", "dataCase.latestOverdueMoney", DataCaseEntity.class, BigDecimal.class),
//        COL330(330, "最新欠款导入时间", "dataCase.latestOverdueTime", DataCaseEntity.class, Date.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        SynergisticExport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum SynergisticExportConf{
        COL10("id", "ID", "s.id as id", Integer.class),
        COL20("batchNo", "批次号", "c.batch_no as 'dataCase.batchNo'", DataCaseEntity.class, String.class),
        COL30("client", "委托方", "c.client as 'dataCase.client'", DataCaseEntity.class, String.class),
        COL40("seqNo", "个案序列号", "c.seq_no as 'dataCase.seqNo'", DataCaseEntity.class, String.class),
        COL50("name", "姓名", "c.name as 'dataCase.name'", DataCaseEntity.class, String.class),
        COL60("synergisticType", "协催类型", "s.synergistic_type as 'synergisticType.id'", SysDictionaryEntity.class, String.class),
        COL70("applyContent", "申请内容", "s.apply_content as applyContent", String.class),
        COL80("applyTime", "申请时间", "s.apply_time as applyTime", Date.class),
        COL90("synergisticTime", "协催时间", "s.synergistic_time as synergisticTime", Date.class),
        COL100("synergisticUser", "协催人", "s.synergistic_user as 'synergisticUser.id'", SysNewUserEntity.class, String.class),
        COL110("synergisticResult", "协催结果", "s.synergistic_result as synergisticResult", String.class),
        COL120("identNo", "证件号", "c.ident_no as 'dataCase.identNo'", DataCaseEntity.class, String.class),
        COL130("cardNo", "卡号", "c.card_no as 'dataCase.cardNo'", DataCaseEntity.class, String.class),
        COL140("cardType", "卡类", "c.card_type as 'dataCase.cardType'", DataCaseEntity.class, String.class),
        COL150("account", "账号", "c.account as 'dataCase.account'", DataCaseEntity.class, String.class),
        COL160("currencyType", "币种", "c.currency_type as 'dataCase.currencyType'", DataCaseEntity.class, String.class),
        COL170("archiveNo", "档案号", "c.archive_no as 'dataCase.archiveNo',", DataCaseEntity.class, String.class),
        COL180("applyOrderNo", "申请单号", "c.apply_order_no as 'dataCase.applyOrderNo'", DataCaseEntity.class, String.class),
        COL190("money", "委案金额", "c.money as 'dataCase.money'", DataCaseEntity.class, BigDecimal.class),
        COL200("enRepayAmt", "还款金额", "c.en_repay_amt as 'dataCase.enRepayAmt'", DataCaseEntity.class, BigDecimal.class),
        COL210("principle", "本金", "c.principle as 'dataCase.principle'", DataCaseEntity.class, BigDecimal.class),
        COL220("lastRepayDate", "最后还款日", "c.last_repay_date as 'dataCase.lastRepayDate'", DataCaseEntity.class, Date.class),
        COL230("caseDate", "委案日期", "c.case_date as 'dataCase.caseDate'", DataCaseEntity.class, Date.class),
        COL240("collectionUser", "催收员", "c.collection_user as 'dataCase.collectionUser.id'", DataCaseEntity.class, SysNewUserEntity.class, String.class),
        COL250("creditLine", "信用额度", "c.credit_line as 'dataCase.creditLine'", DataCaseEntity.class, String.class),
        COL260("homeAddress", "家庭地址", "c.home_address as 'dataCase.homeAddress'", DataCaseEntity.class, String.class),
        COL270("homeTelNumber", "家庭号码", "c.home_tel_number as 'dataCase.homeTelNumber", DataCaseEntity.class, String.class),
        COL280("unitName", "单位名称", "c.unit_name as 'dataCase.unitName'", DataCaseEntity.class, String.class),
        COL290("unitAddress", "单位地址", "c.unit_address as 'dataCase.unitAddress'", DataCaseEntity.class, String.class),
        COL300("tel", "手机", "c.tel as 'dataCase.tel'", DataCaseEntity.class, String.class),
        COL310("unitTelNumber", "单位号码", "c.unit_tel_number as 'dataCase.unitTelNumber'", DataCaseEntity.class, String.class),
        COL320("latestOverdueMoney", "最新欠款", "c.latest_overdue_money as 'dataCase.latestOverdueMoney'", DataCaseEntity.class, BigDecimal.class),
//        COL330(330, "最新欠款导入时间", "dataCase.latestOverdueTime", DataCaseEntity.class, Date.class),
        ;

        private String pageCol;

        private String col;

        private String attr;

        private Class[] attrClazz;

        SynergisticExportConf(String pageCol, String col, String attr, Class... attrClazz) {
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

    public enum FinishedSynergisticImport implements ExcelEnum{
        COL10(10, "*协催ID", "id", Integer.class),
        COL20(20, "申请内容", "applyContent", String.class),
        COL30(30, "协催结果", "synergisticResult", String.class),
        COL40(40, "协催时间", "synergisticTime", Date.class),
        COL50(50, "协催人", "synergisticUser.userName", SysNewUserEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        FinishedSynergisticImport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum SynergisticRecordImport implements ExcelEnum{
        COL10(10, "*卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL20(20, "*证件号", "dataCase.identNo", DataCaseEntity.class, String.class),
        COL30(30, "*委案日期", "dataCase.caseDate", DataCaseEntity.class, String.class),
        COL40(40, "申请内容", "applyContent", String.class),
        COL50(50, "协催结果", "synergisticResult", String.class),
        COL60(60, "协催日期", "synergisticTime", Date.class),
        COL70(70, "协催人", "synergisticUser.userName", SysNewUserEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        SynergisticRecordImport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum SynergisticSortEnum {

        COLUMN1("id","s.id"),
        COLUMN2("synergisticType.name","s.synergistic_type"),
        COLUMN3("dataCase.collectStatus","c.collect_status"),
        COLUMN17("dataCase.collectStatusMsg","c.collect_status"),
        COLUMN4("dataCase.seqNo","c.seq_no"),
        COLUMN5("dataCase.identNo","c.ident_no"),
        COLUMN6("dataCase.name","c.name"),
        COLUMN7("dataCase.moneyMsg","c.money"),
        COLUMN15("dataCase.money","c.money"),
        COLUMN8("dataCase.repayMoneyMsg","c.en_repay_amt"),
        COLUMN16("dataCase.repayMoney","c.en_repay_amt"),
        COLUMN9("applyContent","s.apply_content"),
        COLUMN10("applyTime","s.apply_time"),
        COLUMN11("applyUser.userName","s.apply_user"),
        COLUMN12("synergisticTime","s.synergistic_time"),
        COLUMN13("synergisticUser.name","s.synergistic_user"),
        COLUMN14("synergisticResult","s.synergistic_result"),
        ;
        //防止字段值被修改，增加的字段也统一final表示常量
        private final String key;
        private final String value;

        private SynergisticSortEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        //根据key获取枚举
        public static SynergisticSortEnum getEnumByKey(String key) {
            if (null == key) {
                return null;
            }
            for (SynergisticSortEnum temp : SynergisticSortEnum.values()) {
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
