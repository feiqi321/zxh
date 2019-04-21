package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelOpConstant {

    public enum LogMemorize implements ExcelEnum{
        COL10(10, "个案序列号", "seqNo", String.class),
        COL20(20, "姓名", "name", String.class),
        COL30(30, "催收员", "odvName", String.class),
        COL35(35, "催收员ID", "odv", String.class),
        COL40(40, "记录类型", "type", String.class),
        COL50(50, "记录时间", "opTime", String.class),
        COL60(60, "操作内容", "context",  BigDecimal.class),
        COL70(70, "操作人", "operName",  String.class),
        COL80(80, "操作人ID", "oper",  String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        LogMemorize(Integer sort, String col, String attr, Class... attrClazz) {
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
