package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelBankReconciliationConstant {

    public enum BankReconciliationImport implements ExcelEnum {

        COL10(10, "*个案序列号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL20(20, "*档案号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL30(30, "*卡号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL40(40, "*账号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL50(50, "*委案日期", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL60(60, "*CP金额", "cpMoney", BigDecimal.class),
        COL70(70, "*CP日期", "cpDate", Date.class),
        COL80(80, "备注", "remark", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        BankReconciliationImport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum BankReconciliationExport implements ExcelEnum {
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
