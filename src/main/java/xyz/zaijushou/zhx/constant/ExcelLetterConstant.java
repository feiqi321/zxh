package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelLetterConstant {

    public enum Letter implements ExcelEnum{
        COL10(10, "*卡号", "cardNo", String.class),
        COL20(20, "*证件号", "identNo", String.class),
        COL30(30, "*委案日期", "caseDate", String.class),
        COL40(40, "*信函地址", "address", String.class),
        COL50(50, "姓名", "name", String.class),
        COL60(60, "关系", "relation",  String.class),
        COL70(70, "地址类型", "addressType", String.class),
        COL80(80, "申请内容", "applyContext", String.class),
        COL90(90, "协催结果", "synergyResult", String.class),
        COL100(100, "协催日期", "synergyDate", String.class),
        COL110(110, "协催人", "synergyer", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        Letter(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum LetterExport implements ExcelEnum{
        COL1(1, "ID", "id", String.class),
        COL2(2, "批次号", "batchNo", String.class),
        COL3(3, "委托方", "client", String.class),
        COL4(4, "个案序列号", "seqNo", String.class),
        COL5(5, "姓名", "name", String.class),
        COL6(6, "信函地址", "address", String.class),
        COL7(7, "地址类型", "addressType", String.class),
        COL8(8, "申请内容", "applyContext", String.class),
        COL9(9, "申请时间", "applyDate", String.class),
        COL10(10, "协催时间", "synergyDate", String.class),
        COL11(11, "协催人", "synergyer", String.class),
        COL12(12, "协催结果", "synergyResult", String.class),
        COL13(13, "证件号", "identNo", String.class),
        COL14(14, "卡号", "cardNo", String.class),
        COL15(15, "卡类", "cardType", String.class),
        COL16(16, "账号", "account", String.class),
        COL17(17, "币种", "currencyType", String.class),
        COL18(18, "档案号", "archiveNo", String.class),
        COL19(19, "申请单号", "applyNo", String.class),
        COL20(20, "委案金额", "money", String.class),
        COL21(21, "还款金额", "enRepayAmt", String.class),
        COL22(22, "本金", "principle", String.class),
        COL23(23, "最后还款日", "lastRepayDate", String.class),
        COL24(24, "委案日期", "caseDate", String.class),
        COL25(25, "催收员", "odv", String.class),
        COL26(26, "信用额度", "creditLine", String.class),
        COL27(27, "家庭地址", "homeAddress", String.class),
        COL28(28, "家庭号码", "homeTelNumber", String.class),
        COL29(29, "单位名称", "unitName", String.class),
        COL30(30, "单位地址", "unitAddress", String.class),
        COL31(31, "手机", "tel", String.class),
        COL32(32, "单位号码", "unitTelNumber", String.class),
        COL33(33, "最新欠款", "latestOverdueMoney", String.class),
        COL34(34, "最新欠款导入时间", "latestOverdueDate", String.class)
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        LetterExport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum LetterExportConf{
        COL1("id", "ID", "t.id", String.class),
        COL2("batchNo", "批次号", "d.batch_no as batchNo", String.class),
        COL3("client", "委托方", "d.client", String.class),
        COL4("seqNo", "个案序列号", "d.seq_no as seqNo", String.class),
        COL5("name", "姓名", "d.name", String.class),
        COL6("address", "信函地址", "t.address", String.class),
        COL7("addressType", "地址类型", "t.address_type as addressType", String.class),
        COL8("applyContext", "申请内容", "t.apply_context as applyContext", String.class),
        COL9("applyDate", "申请时间", "t.apply_date as applyDate", String.class),
        COL10("synergyDate", "协催时间", "t.synergy_date as synergyDate", String.class),
        COL11("synergyer", "协催人", "t.synergyer", String.class),
        COL12("synergyResult", "协催结果", "t.synergy_result as synergyResult", String.class),
        COL13("identNo", "证件号", "d.ident_no as identNo", String.class),
        COL14("cardNo", "卡号", "d.card_no as cardNo", String.class),
        COL15("cardType", "卡类", "d.card_type as cardType", String.class),
        COL16("account", "账号", "d.account", String.class),
        COL17("currencyType", "币种", "d.currency_type as currencyType", String.class),
        COL18("archiveNo", "档案号", "d.archive_no as archiveNo", String.class),
        COL19("applyNo", "申请单号", "d.apply_order_no as applyNo", String.class),
        COL20("money", "委案金额", "d.money", String.class),
        COL21("enRepayAmt", "还款金额", "d.en_repay_amt as  enRepayAmt", String.class),
        COL22("principle", "本金", "d.principle", String.class),
        COL23("lastRepayDate", "最后还款日", "d.last_repay_date as lastRepayDate", String.class),
        COL24("caseDate", "委案日期", "d.case_date as caseDate", String.class),
        COL25("odv", "催收员", "d.odv", String.class),
        COL26("creditLine", "信用额度", "d.credit_line as creditLine", String.class),
        COL27("homeAddress", "家庭地址", "d.home_address as homeAddress", String.class),
        COL28("homeTelNumber", "家庭号码", "d.home_tel_number as homeTelNumber", String.class),
        COL29("unitName", "单位名称", "d.unit_name as unitName", String.class),
        COL30("unitAddress", "单位地址", "d.unit_address as unitAddress", String.class),
        COL31("tel", "手机", "d.tel", String.class),
        COL32("unitTelNumber", "单位号码", "d.unit_tel_number as unitTelNumber", String.class),
        COL33("latestOverdueMoney", "最新欠款", "d.latest_overdue_money as latestOverdueMoney", String.class),
        COL34("latestOverdueDate", "最新欠款导入时间", "'' as latestOverdueDate", String.class),
        ;

        private String pageCol;

        private String col;

        private String attr;

        private Class[] attrClazz;

        LetterExportConf(String pageCol, String col, String attr, Class... attrClazz) {
            this.pageCol = pageCol;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        //根据key获取枚举
        public static ExcelLetterConstant.LetterExportConf getEnumByKey(String key){
            if(null == key){
                return null;
            }
            for(ExcelLetterConstant.LetterExportConf temp: ExcelLetterConstant.LetterExportConf.values()){
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


}
