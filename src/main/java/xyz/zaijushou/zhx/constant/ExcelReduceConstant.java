package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelReduceConstant {

    public enum ReduceInfo implements ExcelEnum{
        COL10(10, "*个案序列号", "seqno", String.class),
        COL20(20, "批复还款金额", "approveRepayAmt", BigDecimal.class),
        COL30(30, "*有效期", "reduceValidTime", String.class),
        COL40(40, "减免结果", "reduceResult", String.class),

        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        ReduceInfo(Integer sort, String col, String attr, Class... attrClazz) {
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
