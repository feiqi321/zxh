package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRemarkEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;

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
        COL10(10, "还款ID", "id", Integer.class),
        COL20(20, "批次号", "dataCase.batchNo", DataCaseEntity.class, String.class),
        COL30(30, "卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL40(40, "证件号", "dataCase.identNo", DataCaseEntity.class, String.class),
        COL50(50, "姓名", "dataCase.name", DataCaseEntity.class, String.class),
        COL60(60, "个案序列号", "dataCase.seqNo", DataCaseEntity.class, String.class),
        COL70(70, "委托方", "dataCase.client", DataCaseEntity.class, String.class),
        COL80(80, "委案金额", "dataCase.money", DataCaseEntity.class, BigDecimal.class),
        COL90(90, "当前委案余额", "dataCase.balance", DataCaseEntity.class, BigDecimal.class),
        COL100(100, "账号", "dataCase.account", DataCaseEntity.class, String.class),
        COL110(110, "档案号", "dataCase.archiveNo", DataCaseEntity.class, String.class),
        COL120(120, "申请单号", "dataCase.applyOrderNo", DataCaseEntity.class, String.class),
        COL130(130, "CP金额", "cpMoney", BigDecimal.class),
        COL140(140, "CP日期", "cpDate", Date.class),
        COL150(150, "案件催收员", ""),
        COL160(160, "回收催收员", ""),
        COL170(170, "回收部门", ""),
        COL180(180, "提交人", "submitUser.userName", SysNewUserEntity.class, String.class),
        COL190(190, "提交时间", "submitTime", Date.class),
        COL200(200, "还款金额", "dataCase.repayMoney", DataCaseEntity.class, BigDecimal.class),
        COL210(210, "还款日期", ""),
        COL220(220, "还款余额", ""),
        COL230(230, "M值金额", ""),
        COL240(240, "M值系数", "dataCase.mVal", DataCaseEntity.class, BigDecimal.class),
        COL250(250, "还款人", "repayUser", String.class),
        COL260(260, "还款方式", "repayType.name", SysDictionaryEntity.class, String.class),
        COL270(270, "确认人", ""),
        COL280(280, "确认时间", ""),
        COL290(290, "还款备注", ""),
        COL300(300, "逾期账龄", "dataCase.accountAge", DataCaseEntity.class, String.class),
        COL310(310, "逾期期数", "dataCase.overduePeriods", DataCaseEntity.class, Double.class),
        COL320(320, "逾期日期", "dataCase.overdueDate", DataCaseEntity.class, Date.class),
        COL330(330, "逾期天数", "dataCase.overdueDays", DataCaseEntity.class, Double.class),
        COL340(340, "委案日期", "dataCase.caseDate", DataCaseEntity.class, String.class),
        COL350(350, "预计退案日", "dataCase.expectTime", DataCaseEntity.class, Date.class),
        COL360(360, "省份", "dataCase.province.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL370(370, "城市", "dataCase.city.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL380(380, "区县", "dataCase.county.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
        COL390(390, "手机", "dataCase.tel", DataCaseEntity.class, String.class),
        COL400(400, "币种", "dataCase.currencyType", DataCaseEntity.class, String.class),
        COL410(410, "分配历史", "dataCase.distributeHistory", DataCaseEntity.class, String.class),
        COL420(420, "拖欠级别", "dataCase.defaultLevel", DataCaseEntity.class, String.class),
        COL430(430, "案件最新欠款", "dataCase.latestOverdueMoney", DataCaseEntity.class, BigDecimal.class),
        COL440(440, "外访状态", "dataCase.inteviewStatus", DataCaseEntity.class, String.class),
        COL450(450, "公司佣金", "dataCase.commissionMoney", DataCaseEntity.class, BigDecimal.class),
        COL460(460, "公司佣金比率", "dataCase.commissionRate", DataCaseEntity.class, Date.class),
        COL470(470, "案件备注1", "dataCase.caseRemarks[0].name", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL480(480, "案件备注2", "dataCase.caseRemarks[1].name", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL490(490, "案件备注3", "dataCase.caseRemarks[2].name", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL500(500, "案件备注4", "dataCase.caseRemarks[3].name", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL510(510, "案件备注5", "dataCase.caseRemarks[4].name", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL520(520, "案件备注6", "dataCase.caseRemarks[5].name", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL530(530, "催收区域", "dataCase.collectionArea.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
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
        COLUMN8("dataCase.moneyMsg","c.money"),
        COLUMN9("dataCase.repayMoneyMsg",""),
        COLUMN10("cpMoneyMsg","b.cp_money"),
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
        public static BankReconciliationSortEnum getEnumByKey(String key) {
            if (null == key) {
                return null;
            }
            for (BankReconciliationSortEnum temp : BankReconciliationSortEnum.values()) {
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
