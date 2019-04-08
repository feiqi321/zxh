package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelBatchExportConstant {



    public enum BatchMemorize implements ExcelEnum{
        COL10(10, "批次号", "batchNo", String.class),
        COL20(20, "批次状态", "statusMsg", String.class),
        COL30(30, "委托方", "client", String.class),
        COL40(40, "案件类型", "caseType",  String.class),
        COL50(50, "委托日期", "caseTime", String.class),
        COL60(60, "预计退案时间", "returnTime",  String.class),
        COL70(70, "实际退案时间", "realReturnTime",  String.class),
        COL80(80, "上传时间", "uploadTime", String.class),
        COL90(90, "户数", "userCount", String.class),
        COL100(100, "总金额", "totalAmt",  BigDecimal.class),
        COL110(110, "录入时间", "creatTime",  String.class),
        COL120(120, "录入人员", "creatUser",  String.class),
        COL130(130, "备注", "remark",  String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        BatchMemorize(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum BatchMemorizeConf{
        COL10("batchNo", "批次号", "batch_no as batchNo", String.class),
        COL20("batchStatus", "批次状态", "batch_status as batchStatus", String.class),
        COL30("client", "委托方", "client", String.class),
        COL40("caseType", "案件类型", "case_type as caseType",  String.class),
        COL50("caseTime", "委托日期", "bail_date as caseTime", String.class),
        COL60("returnTime", "预计退案时间", "expect_time as returnTime",  String.class),
        COL70("realReturnTime", "实际退案时间", "real_time as realReturnTime",  String.class),
        COL80("uploadTime", "上传时间", "upload_time as uploadTime", String.class),
        COL90("userCount", "户数", "user_count as userCount", String.class),
        COL100("totalAmt", "总金额", "total_amt as totalAmt",  BigDecimal.class),
        COL110("createTime", "录入时间", "DATE_FORMAT(create_time,'%Y-%m-%d %H:%i:%S') as createTime",  String.class),
        COL120("creatUser", "录入人员", "create_user as creatUser",  String.class),
        COL130("remark", "备注", "remark",  String.class),
        ;

        private String pageCol;

        private String col;

        private String attr;

        private Class[] attrClazz;

        //根据key获取枚举
        public static ExcelBatchExportConstant.BatchMemorizeConf getEnumByKey(String key){
            if(null == key){
                return null;
            }
            for(ExcelBatchExportConstant.BatchMemorizeConf temp: ExcelBatchExportConstant.BatchMemorizeConf.values()){
                if(temp.getPageCol().equals(key)){
                    return temp;
                }
            }
            return null;
        }

        BatchMemorizeConf(String pageCol, String col, String attr, Class... attrClazz) {
            this.pageCol = pageCol;
            this.col = col;
            this.attr = attr;
            this.attrClazz = attrClazz;
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
