package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelReduceExportConstant {



    public enum ReduceList implements ExcelEnum{
        COL10(10, "个案序列号", "seqno", String.class),
        COL20(20, "案人姓名", "targetName", String.class),
        COL30(30, "催收状态", "collectStatusMsg", String.class),
        COL40(40, "委案金额", "accountAge",  String.class),
        COL50(50, "完成时间", "completeTime", String.class),
        COL60(60, "完成人", "completeUser",  String.class),
        COL70(70, "批复还款金额", "approveRepayAmtMsg",  BigDecimal.class),
        COL80(80, "有效日期", "reduceValidTime", String.class),
        COL90(90, "减免状态", "reduceStatusMsg", String.class),
        COL100(100, "减免结果", "reduceResult",  String.class),
        COL110(110, "实际还款金额", "enRepayAmtMsg",  BigDecimal.class),
        COL120(120, "减免状态更新时间", "reduceUpdateTime",  String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        ReduceList(Integer sort, String col, String attr, Class... attrClazz) {
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


    public enum ReduceConfList{
        COL10("seqno", "个案序列号", "dcr.seqno", String.class),
        COL20("name", "案人姓名", "dc.name as targetName", String.class),
        COL30("collect_status", "催收状态", "dc.collect_status as collectStatus", String.class),
        COL40("money", "委案金额", "dc.money",  String.class),
        COL50("complete_time", "完成时间", "dcr.complete_time as completeTime", String.class),
        COL60("complete_user", "完成人", "dcr.complete_user as completeUser",  String.class),
        COL70("approve_repay_amt", "批复还款金额", "dcr.approve_repay_amt as approveRepayAmt",  BigDecimal.class),
        COL80("reduce_valid_time", "有效日期", "dcr.reduce_valid_time as reduceValidTime", String.class),
        COL90("reduce_status", "减免状态", "dc.reduce_status as reduceStatus", String.class),
        COL100("reduce_result", "减免结果", "dcr.reduce_result as reduceResult",  String.class),
        COL110("en_repay_amt", "实际还款金额", "dc.en_repay_amt as enRepayAmt",  BigDecimal.class),
        COL120("reduce_update_time", "减免状态更新时间", "dc.reduce_update_time as reduceUpdateTime",  String.class),
        ;

        private String pageCol;

        private String col;

        private String attr;

        private Class[] attrClazz;

        ReduceConfList(String pageCol, String col, String attr, Class... attrClazz) {
            this.pageCol = pageCol;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
        }

        //根据key获取枚举
        public static ReduceConfList getEnumByKey(String key){
            if(null == key){
                return null;
            }
            for(ReduceConfList temp:ReduceConfList.values()){
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


}
