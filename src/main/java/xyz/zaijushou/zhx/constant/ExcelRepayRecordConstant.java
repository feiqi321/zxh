package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRemarkEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelRepayRecordConstant {

    public enum RepayRecordImport implements ExcelEnum {

        COL10(10, "*个案序列号", "dataCase.seqNo", DataCaseEntity.class, String.class),
        COL20(20, "*档案号", "dataCase.archiveNo", DataCaseEntity.class, String.class),
        COL30(30, "*卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL40(40, "*账号", "dataCase.account", DataCaseEntity.class, String.class),
        COL50(50, "*还款金额", "repayMoney", BigDecimal.class),
        COL60(60, "*还款日期", "repayDate", Date.class),
        COL70(70, "最新欠款", "latestOverdue", BigDecimal.class),
        COL80(80, "备注", "remark", String.class),
        COL90(90, "是否结清", "settleFlag", String.class),
        COL100(100, "回收催收员ID", "collectUser.id", SysNewUserEntity.class, Integer.class),
        COL110(110, "还款人", "repayUser", String.class),
        COL120(120, "还款方式", "repayType.name", SysDictionaryEntity.class, String.class),
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
        COL130(130, "CP金额", ""),
        COL140(140, "CP日期", ""),
        COL150(150, "案件催收员", "dataCase.collectionUser.userName", DataCaseEntity.class, SysNewUserEntity.class, String.class),
        COL160(160, "回收催收员", "collectUser.userName", SysNewUserEntity.class, String.class),
        COL170(170, "回收部门", ""),
        COL180(180, "提交人", ""),
        COL190(190, "提交时间", ""),
        COL200(200, "还款金额", "dataCase.enRepayAmt", DataCaseEntity.class, BigDecimal.class),
        COL210(210, "还款日期", ""),
        COL220(220, "还款余额", "dataCase.balance", DataCaseEntity.class, BigDecimal.class),
        COL230(230, "M值金额", ""),
        COL240(240, "M值系数", "dataCase.mVal", DataCaseEntity.class, String.class),
        COL250(250, "还款人", ""),
        COL260(260, "还款方式", ""),
        COL270(270, "确认人", "confirmUser.userName", SysNewUserEntity.class, String.class),
        COL280(280, "确认时间", "confirmTime", Date.class),
        COL290(290, "还款备注", "remark", String.class),
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
        COL470(470, "案件备注1", "dataCase.caseRemarks[0].remark", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL480(480, "案件备注2", "dataCase.caseRemarks[1].remark", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL490(490, "案件备注3", "dataCase.caseRemarks[2].remark", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL500(500, "案件备注4", "dataCase.caseRemarks[3].remark", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL510(510, "案件备注5", "dataCase.caseRemarks[4].remark", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL520(520, "案件备注6", "dataCase.caseRemarks[5].remark", DataCaseEntity.class, DataCaseRemarkEntity.class, String.class),
        COL530(530, "催收区域", "dataCase.collectionArea.name", DataCaseEntity.class, SysDictionaryEntity.class, String.class),
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
        COLUMN8("dataCase.overdueBillTime","c.account_age"),
        COLUMN9("dataCase.moneyMsg","c.money"),
        COLUMN25("dataCase.enRepayAmt","c.en_repay_amt"),
        COLUMN10("repayMoneyMsg","r.repay_money"),
        COLUMN11("dataCase.overdueBalanceMsg",""),
        COLUMN12("repayDate","r.repay_date"),
        COLUMN13("repayUser","r.repay_user"),
        COLUMN14("repayType","r.repay_type"),
        COLUMN15("confirmUser.id",""),
        COLUMN16("confirmTime","r.confirm_time"),
        COLUMN17("remark","r.remark"),
        COLUMN18("dataCase.mVal","c.m_val"),
        COLUMN19("bankReconciliation.cpMoneyMsg","r.repay_money"),
        COLUMN20("bankReconciliation.cpDate","r.repay_date"),
        COLUMN21("collectUser.userName","r.collect_user"),
        COLUMN22("repayType.name","r.repay_money"),
        COLUMN23("confirmUser.userName","r.confirm_user"),
        COLUMN24("dataCase.commissionMoney","c.commission_money"),

        ;
        //防止字段值被修改，增加的字段也统一final表示常量
        private final String key;
        private final String value;

        private RepayRecordSortEnum(String key, String value) {
            this.key = key;
            this.value = value;
        }

        //根据key获取枚举
        public static RepayRecordSortEnum getEnumByKey(String key) {
            if (null == key) {
                return null;
            }
            for (RepayRecordSortEnum temp : RepayRecordSortEnum.values()) {
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
