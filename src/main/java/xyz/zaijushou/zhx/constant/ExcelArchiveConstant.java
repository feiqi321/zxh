package xyz.zaijushou.zhx.constant;

public class ExcelArchiveConstant {

    public enum Archive implements ExcelEnum{
        COL10(10, "证件号", "identNo", String.class),
        COL20(20, "案人姓名", "name", String.class),
        COL30(30, "信息类型", "msgType",  String.class),
        COL40(40, "信息内容", "msgContext", String.class),
        COL50(50, "备注", "remark", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        Archive(Integer sort, String col, String attr, Class... attrClazz) {
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
