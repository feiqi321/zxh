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

    public enum BankReconciliationSortEnum {

        COLUMN1("id","b.id"),
        COLUMN2("dataCase.batchNo","c.batch_no"),
        COLUMN3("dataCase.cardNo","c.card_no"),
        COLUMN4("dataCase.identNo","c.ident_no"),
        COLUMN5("dataCase.name","c.name"),
        COLUMN6("dataCase.seqNo","c.seq_no"),
        COLUMN7("dataCase.client","c.client"),
        COLUMN8("dataCase.money","c.money"),
        COLUMN9("dataCase.repayMoney",""),
        COLUMN10("cpMoney","b.cp_money"),
        COLUMN11("cpDate","b.cp_date"),
        COLUMN12("repayUser","b.repay_user"),
        COLUMN13("repayType","b.repay_type"),
        COLUMN14("submitTime","b.submit_time"),
        COLUMN15("remark","b.remark"),
        ;
        //防止字段值被修改，增加的字段也统一final表示常量
        private final String key;
        private final String value;

        private BankReconciliationSortEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        //根据key获取枚举
        public static CollectSortEnum getEnumByKey(String key) {
            if (null == key) {
                return null;
            }
            for (CollectSortEnum temp : CollectSortEnum.values()) {
                if (temp.getKey().equals(key)) {
                    return temp;
                }
            }
            return null;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

    }


}
