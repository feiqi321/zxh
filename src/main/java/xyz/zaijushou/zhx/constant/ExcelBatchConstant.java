package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelBatchConstant {



    public enum BatchMemorize implements ExcelEnum{
        COL10(10, "催收区域", "area", String.class),
        COL20(20, "批次号", "batchNo", String.class),
        COL30(30, "委托方", "client", String.class),
        COL35(35, "批次状态", "batchStatus", String.class),
        COL40(40, "委托日期", "caseTime", String.class),
        COL50(50, "户数", "userCount", String.class),
        COL60(60, "总金额", "totalAmt",  BigDecimal.class),
        COL70(70, "案件类型", "caseType",  String.class),
        COL80(80, "预计退案时间", "returnTime",  String.class),
        COL90(90, "实际退案时间", "realReturnTime",  String.class),
        COL100(100, "录入时间", "createTime",  String.class),
        COL110(110, "批次备注", "batchRemark",  String.class),
        COL120(120, "录入人员", "creatUser",  String.class),
        COL130(120, "备注", "remark",  String.class),
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


}
