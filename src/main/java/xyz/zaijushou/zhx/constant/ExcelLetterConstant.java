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
        COL34(34, "最新欠款导入时间", "latestOverdueDate", String.class),
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


}
