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


    public enum RepayRecordSortEnum {

        COLUMN1("id","r.id"),
        COLUMN2("dataCase.batchNo","c.batch_no"),
        COLUMN3("dataCase.cardNo","c.card_no"),
        COLUMN4("dataCase.identNo","c.ident_no"),
        COLUMN5("dataCase.name","c.name"),
        COLUMN6("dataCase.seqNo","c.seq_no"),
        COLUMN7("dataCase.client","c.client"),
        COLUMN8("dataCase.overdueBillTime","c.overdue_bill_time"),
        COLUMN9("dataCase.money","c.money"),
        COLUMN10("repayMoney","r.repay_money"),
        COLUMN11("dataCase.overdueBalance",""),
        COLUMN12("repayDate","r.repay_date"),
        COLUMN13("repayUser","r.repay_user"),
        COLUMN14("repayType","r.repay_type"),
        COLUMN15("confirmUser.id",""),
        COLUMN16("confirmTime","r.confirm_time"),
        COLUMN17("remark","r.remark"),
        COLUMN18("dataCase.mVal","c.m_val"),
        ;
        //防止字段值被修改，增加的字段也统一final表示常量
        private final String key;
        private final String value;

        private RepayRecordSortEnum(String key, String value) {
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
