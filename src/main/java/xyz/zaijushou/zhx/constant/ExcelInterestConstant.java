package xyz.zaijushou.zhx.constant;

public class ExcelInterestConstant {

    public enum CaseInterest implements ExcelEnum{
        COL10(10, "*个案序列号", "seqNo", String.class),
        COL30(30, "*卡号", "cardNo", String.class),
        COL40(40, "*证件号", "identNo", String.class),
        COL50(50, "*委案日期", "caseDate", String.class),
        COL70(60, "*姓名", "name", String.class),
        COL60(70, "*币种", "currency",  String.class),
        COL80(80, "*最新欠款金额", "lastestDebt", String.class),
        COL90(90, "截止日期", "endDate", String.class),
        COL100(100, "本金", "principal", String.class),
        COL110(110, "利息", "interest", String.class),
        COL120(120, "违约金", "penalty", String.class),
        COL130(130, "滞纳金", "lateFee", String.class),
        COL140(140, "超限费", "overrunFee", String.class),
        COL150(150, "服务费", "serivceFee", String.class),
        COL160(160, "年费", "yearFee", String.class),
        COL170(170, "其他费用", "elseFee", String.class),
        COL180(180, "表外息", "sheetFee", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseInterest(Integer sort, String col, String attr, Class... attrClazz) {
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
    public enum CaseColor implements ExcelEnum{
        COL10(10, "*个案序列号", "seqNo", String.class),
        COL30(20, "*评语内容", "comment", String.class),
        COL40(40, "案件标色", "color", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseColor(Integer sort, String col, String attr, Class... attrClazz) {
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
