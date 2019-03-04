package xyz.zaijushou.zhx.constant;

public class ExcelBankReconciliationConstant {

        public enum BankReconciliationExport implements ExcelEnum{
            COL10(10, "ID", "id", Integer.class),
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


    }
