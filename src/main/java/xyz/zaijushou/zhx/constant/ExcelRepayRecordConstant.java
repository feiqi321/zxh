package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelRepayRecordConstant {

    public enum RepayRecordImport implements ExcelEnum {

        COL10(10, "*个案序列号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL20(20, "*档案号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL30(30, "*卡号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL40(40, "*账号", "dataCase.seqNo", DataCaseEntity.class, Integer.class),
        COL50(50, "*还款金额", "repayMoney", BigDecimal.class),
        COL60(60, "*还款日期", "repayDate", Date.class),
        COL70(70, "最新欠款", "latestOverdue", BigDecimal.class),
        COL80(80, "备注", "remark", String.class),
        COL90(90, "是否结清", "settleFlag", String.class),
        COL100(100, "回收催收员ID", "collectUser.id", SysNewUserEntity.class, Integer.class),
        COL110(110, "还款人", "repayUser", String.class),
        COL120(120, "还款方式", "repayType", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        RepayRecordImport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum RepayRecordExport implements ExcelEnum {
        COL10(10, "ID", "id", Integer.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        RepayRecordExport(Integer sort, String col, String attr, Class... attrClazz) {
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
