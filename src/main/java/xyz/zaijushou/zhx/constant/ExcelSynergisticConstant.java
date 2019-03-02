package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelSynergisticConstant {

    public enum SynergisticExport implements ExcelEnum{
        COL10(10, "ID", "id", Integer.class),
        COL20(20, "批次号", "dataCase.batchNo", DataCaseEntity.class, String.class),
        COL30(30, "委托方", "dataCase.client", DataCaseEntity.class, String.class),
        COL40(40, "个案序列号", "dataCase.seqNo", DataCaseEntity.class, String.class),
        COL50(50, "姓名", "dataCase.name", DataCaseEntity.class, String.class),
        COL60(60, "协催类型", "synergisticType.name", SysDictionaryEntity.class, String.class),
        COL70(70, "申请内容", "applyContent", String.class),
        COL80(80, "申请时间", "applyTime", Date.class),
        COL90(90, "协催时间", "synergisticTime", Date.class),
        COL100(100, "协催人", "synergisticUser.userName", SysUserEntity.class, String.class),
        COL110(110, "协催结果", "synergisticResult", String.class),
        COL120(120, "证件号", "dataCase.identNo", DataCaseEntity.class, String.class),
        COL130(130, "卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL140(140, "卡类", "dataCase.cardType", DataCaseEntity.class, String.class),
        COL150(150, "账号", "dataCase.account", DataCaseEntity.class, String.class),
        COL160(160, "币种", "dataCase.currencyType", DataCaseEntity.class, String.class),
        COL170(170, "档案号", "dataCase.archiveNo", DataCaseEntity.class, String.class),
        COL180(180, "申请单号", "dataCase.applyOrderNo", DataCaseEntity.class, String.class),
        COL190(190, "委案金额", "dataCase.money", DataCaseEntity.class, BigDecimal.class),
        COL200(200, "还款金额", "dataCase.repayMoney", DataCaseEntity.class, BigDecimal.class),
        COL210(210, "本金", "dataCase.principle", DataCaseEntity.class, BigDecimal.class),
        COL220(220, "最后还款日", "dataCase.lastRepayDate", DataCaseEntity.class, Date.class),
        COL230(230, "委案日期", "dataCase.caseDate", DataCaseEntity.class, Date.class),
        COL240(240, "催收员", "dataCase.collectionUser.userName", DataCaseEntity.class, SysNewUserEntity.class, String.class),
        COL250(250, "信用额度", "dataCase.creditLine", DataCaseEntity.class, String.class),
        COL260(260, "家庭地址", "dataCase.homeAddress", DataCaseEntity.class, String.class),
        COL270(270, "家庭号码", "dataCase.homeTelNumber", DataCaseEntity.class, String.class),
        COL280(280, "单位名称", "dataCase.unitName", DataCaseEntity.class, String.class),
        COL290(290, "单位地址", "dataCase.unitAddress", DataCaseEntity.class, String.class),
        COL300(300, "手机", "dataCase.tel", DataCaseEntity.class, String.class),
        COL310(310, "单位号码", "dataCase.unitTelNumber", DataCaseEntity.class, String.class),
        COL320(320, "最新欠款", "dataCase.latestOverdueMoney", DataCaseEntity.class, BigDecimal.class),
        COL330(330, "最新欠款导入时间", "dataCase.latestOverdueTime", DataCaseEntity.class, Date.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        SynergisticExport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum FinishedSynergisticImport implements ExcelEnum{
        COL10(10, "ID", "id", Integer.class),
        COL20(20, "申请内容", "applyContent", String.class),
        COL30(30, "协催结果", "synergisticResult", String.class),
        COL40(40, "协催时间", "synergisticTime", Date.class),
        COL50(50, "协催人", "synergisticUser.userName", SysUserEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        FinishedSynergisticImport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum SynergisticRecordImport implements ExcelEnum{
        COL10(10, "卡号", "dataCase.cardNo", DataCaseEntity.class, String.class),
        COL20(20, "证件号", "dataCase.identNo", DataCaseEntity.class, String.class),
        COL30(30, "委案日期", "dataCase.caseDate", DataCaseEntity.class, Date.class),
        COL40(40, "申请内容", "applyContent", String.class),
        COL50(50, "协催结果", "synergisticResult", String.class),
        COL60(60, "协催时间", "synergisticTime", Date.class),
        COL70(70, "协催人", "synergisticUser.userName", SysUserEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        SynergisticRecordImport(Integer sort, String col, String attr, Class... attrClazz) {
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