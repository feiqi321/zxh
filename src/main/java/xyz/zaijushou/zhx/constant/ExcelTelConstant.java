package xyz.zaijushou.zhx.constant;

public class ExcelTelConstant {

    public enum CaseTel implements ExcelEnum{
        COL10(10, "*个案序列号", "seqNo", String.class),
        COL20(20, "*档案号", "archiveNo", String.class),
        COL30(30, "*卡号", "cardNo", String.class),
        COL40(40, "*证件号", "identNo", String.class),
        COL50(50, "*委案日期", "caseDate", String.class),
        COL60(60, "*电话", "tel",  String.class),
        COL70(70, "*姓名", "name", String.class),
        COL80(80, "电话类型", "type", String.class),
        COL90(90, "关系", "relation", String.class),
        COL100(100, "备注", "remark", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseTel(Integer sort, String col, String attr, Class... attrClazz) {
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
