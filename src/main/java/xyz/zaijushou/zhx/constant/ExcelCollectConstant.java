package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelCollectConstant {

    public enum CaseCollect implements ExcelEnum{
        COL10(10, "*个案序列号", "seqno", String.class),
        COL20(20, "*档案号", "archiveNo", String.class),
        COL30(30, "*卡号", "cardNo", String.class),
        COL40(40, "*账号", "account", String.class),
        COL50(50, "*委案日期", "caseDate", String.class),
        COL60(60, "*联络时间", "contractDate",  String.class),
        COL70(70, "催收结果ID", "resultId",  String.class),
        COL80(80, "联络对象姓名", "targetName",  String.class),
        COL90(90, "关系", "relation",  String.class),
        COL100(100, "*通话记录", "collectInfo",  String.class),
        COL110(110, "联络电话", "mobile",  String.class),
        COL120(120, "电话类型", "telType",  String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseCollect(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum CollectMemorize implements ExcelEnum{
        COL10(10, "个案序列号", "seqno", String.class),
        COL20(20, "姓名", "name", String.class),
        COL30(30, "催收措施", "measure", String.class),
        COL35(35, "催收时间", "collectTime", String.class),
        COL40(40, "对象姓名", "targetName", String.class),
        COL50(50, "与案人关系", "relation", String.class),
        COL60(60, "电话类型", "telType",  String.class),
        COL70(70, "电话号码", "mobile",  String.class),
        COL80(80, "催收记录", "collectInfo",  String.class),
        COL90(90, "催收结果", "result",  String.class),
        COL100(100, "谈判方式", "method",  String.class),
        COL110(110, "承诺还款日期", "repayTime",  String.class),
        COL120(120, "承诺还款金额", "repayAmt",  BigDecimal.class),
        COL130(120, "减免金额", "reduceAmt",  BigDecimal.class),
        COL140(120, "减免状态", "reduceStatusMsg",  String.class),
        COL150(120, "催收员", "odv",  String.class),
        COL160(120, "催收状态", "collectStatusMsg",  String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CollectMemorize(Integer sort, String col, String attr, Class... attrClazz) {
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
