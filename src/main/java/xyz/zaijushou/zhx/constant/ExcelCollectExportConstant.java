package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelCollectExportConstant {

    public enum CaseCollectExport implements ExcelEnum{
        COL10(10, "姓名", "name", String.class),
        COL20(20, "账号", "account", String.class),
        COL30(30, "卡号", "cardNo", String.class),
        COL40(40, "证件号", "identNo", String.class),
        COL50(50, "档案号", "archiveNo", String.class),
        COL60(60, "个案序列号", "seqno", String.class),
        COL70(70, "逾期账龄", "accountAge", String.class),
        COL80(80, "委案金额", "money", String.class),
        COL90(90, "欠款余额", "residualPrinciple", String.class),
        COL100(100, "对象姓名", "targetName", String.class),
        COL110(110, "关系", "relation", String.class),
        COL120(120, "电话/地址", "collectPhoneAddr", String.class),
        COL130(130, "联络类型", "contractType", String.class),
        COL140(140, "催收内容", "result", String.class),
        COL150(150, "催收人员", "odv", String.class),
        COL160(160, "谈判方式", "method", String.class),
        COL170(170, "催收状态", "collectStatusMsg", String.class),
        COL180(180, "承诺日期", "proRepayDate", String.class),
        COL190(190, "承诺金额", "proRepayAmt", String.class),
        COL200(200, "减免金额", "reduceAmt", String.class),
        COL210(210, "减免状态", "reduceStatusMsg", String.class),
        COL220(220, "催收小结", "collectInfo", String.class),
        COL230(230, "逾期天数", "overDays", String.class),
        COL240(240, "批次号", "batchNo", String.class),
        COL250(250, "委案日期", "caseDate", String.class),
        COL260(260, "预计退案日期", "expectTime", String.class),
        COL270(270, "最新欠款", "newMoney", String.class),
        COL280(280, "省份", "province", String.class),
        COL290(290, "城市", "city", String.class),
        COL300(300, "区县", "county", String.class),
        COL310(310, "币种", "currencyType", String.class),
        COL320(320, "申请单号", "applyOrderNo", String.class),
        COL330(330, "逾期金额", "overdueMoney", String.class),
        COL340(340, "自定义信息", "remark", String.class),
        COL350(350, "催收员", "odv", String.class),
        COL360(360, "催收状态", "collectStatusMsg", String.class),
        COL370(370, "下次跟进日期", "nextFollDate", String.class),
        COL380(380, "最低还款额", "minimumPayment", String.class),
        COL390(390, "信用额度", "creditLine", String.class),
        COL400(400, "催收模板", "module", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseCollectExport(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum CaseCollectExportConf{
        COL10("name", "姓名", "t.name", String.class),
        COL20("account", "账号", "t.account", String.class),
        COL30("cardNo", "卡号", "t.card_no as cardNo", String.class),
        COL40("identNo", "证件号", "t.ident_no as identNo", String.class),
        COL50("archiveNo", "档案号", "t.archive_no as archiveNo", String.class),
        COL60("seqNo", "个案序列号", "t.seq_no as seqNo", String.class),
        COL70("accountAge", "逾期账龄", "t.account_age as accountAge", String.class),
        COL80("money", "委案金额", "t.money", String.class),
        COL90("residualPrinciple", "欠款余额", "t.residual_principle as residualPrinciple", String.class),
        COL100("targetName", "对象姓名", "c.target_name as targetName", String.class),
        COL110("relation", "关系", "c.relation", String.class),
        COL120("collectPhoneAddr", "电话/地址", "c.tel_phone as collectPhoneAddr", String.class),
        COL130("contractType", "联络类型", "c.tel_type as contractType", String.class),
        COL140("result", "催收内容", "c.result", String.class),
        COL150("odv", "催收人员", "t.odv", String.class),
        COL160("method", "谈判方式", "c.method", String.class),
        COL170("collectStatus", "催收状态", "c.collect_status  as collectStatus", String.class),
        COL180("proRepayDate", "承诺日期", "t.pro_repay_date as proRepayDate", String.class),
        COL190("proRepayAmt", "承诺金额", "t.pro_repay_amt as proRepayAmt", String.class),
        COL200("reduceAmt", "减免金额", "c.reduce_amt as reduceAmt", String.class),
        COL210("reduceStatus", "减免状态", "c.reduce_status as reduceStatus", String.class),
        COL220("collectInfo", "催收小结", "c.collect_info as collectInfo", String.class),
        COL230("overDays", "逾期天数", "t.over_days as overDays", String.class),
        COL240("batchNo", "批次号", "t.batch_no as batchNo", String.class),
        COL250("caseDate", "委案日期", "t.case_date as caseDate", String.class),
        COL260("expectTime", "预计退案日期", "t.expect_time as expectTime", String.class),
        COL270("newMoney", "最新欠款", "c.new_money as newMoney", String.class),
        COL280("province", "省份", "t.province", String.class),
        COL290("city", "城市", "t.city", String.class),
        COL300("county", "区县", "t.county", String.class),
        COL310("currencyType", "币种", "t.currency_type as currencyType", String.class),
        COL320("applyOrderNo", "申请单号", "t.apply_order_no as applyOrderNo", String.class),
        COL330("overdueMoney", "逾期金额", "t.overdue_money as overdueMoney", String.class),
        COL340("remark", "自定义信息", "t.remark", String.class),
        COL350("odv2", "催收员", "t.odv", String.class),
        COL360("collectStatus2", "催收状态", "c.collect_status  as collectStatus", String.class),
        COL370("nextFollDate", "下次跟进日期", "c.next_foll_date as nextFollDate", String.class),
        COL380("minimumPayment", "最低还款额", "t.minimum_payment as minimumPayment", String.class),
        COL390("creditLine", "信用额度", "t.credit_line as creditLine", String.class),
        COL400("module", "催收模板", "c.module", String.class),
        ;

        private String pageCol;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseCollectExportConf(String pageCol, String col, String attr, Class... attrClazz) {
            this.pageCol = pageCol;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        //根据key获取枚举
        public static ExcelCollectExportConstant.CaseCollectExportConf getEnumByKey(String key){
            if(null == key){
                return null;
            }
            for(ExcelCollectExportConstant.CaseCollectExportConf temp: ExcelCollectExportConstant.CaseCollectExportConf.values()){
                if(temp.getPageCol().equals(key)){
                    return temp;
                }
            }
            return null;
        }


        public String getPageCol() {
            return pageCol;
        }

        public void setPageCol(String pageCol) {
            this.pageCol = pageCol;
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


    public enum CollectMemorize implements ExcelEnum{
        COL10(10, "个案序列号", "seqno", String.class),
        COL20(20, "姓名", "name", String.class),
        COL30(30, "催收措施", "measure", String.class),
        COL35(35, "催收时间", "collectTime", String.class),
        COL40(40, "对象姓名", "targetName", String.class),
        COL50(50, "与案人关系", "relation", String.class),
        COL60(60, "电话类型", "telType",  String.class),
        COL70(70, "电话号码", "mobile",  String.class),
        COL80(80, "催收记录", "collectInfo",  String.class),
        COL90(90, "催收结果", "result",  String.class),
        COL100(100, "谈判方式", "method",  String.class),
        COL110(110, "承诺还款日期", "repayTime",  String.class),
        COL120(120, "承诺还款金额", "repayAmt",  BigDecimal.class),
        COL130(120, "减免金额", "reduceAmt",  BigDecimal.class),
        COL140(120, "减免状态", "reduceStatusMsg",  String.class),
        COL150(120, "催收员", "odv",  String.class),
        COL160(120, "催收状态", "collectStatusMsg",  String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CollectMemorize(Integer sort, String col, String attr, Class... attrClazz) {
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
