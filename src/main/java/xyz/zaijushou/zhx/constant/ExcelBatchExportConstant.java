package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelBatchExportConstant {



    public enum BatchMemorize implements ExcelEnum{
        COL10(10, "批次号", "batchNo", String.class),
        COL20(20, "批次状态", "batchStatus", String.class),
        COL30(30, "委托方", "client", String.class),
        COL40(40, "案件类型", "caseType",  String.class),
        COL50(50, "委托日期", "caseTime", String.class),
        COL60(60, "预计退案时间", "returnTime",  String.class),
        COL70(70, "实际退案时间", "realReturnTime",  String.class),
        COL80(80, "上传时间", "uploadTime", String.class),
        COL90(90, "户数", "userCount", String.class),
        COL100(100, "总金额", "totalAmt",  BigDecimal.class),
        COL120(110, "分配提示", "totalAmt",  BigDecimal.class),
        COL130(120, "备注", "remark",  String.class),
        COL140(130, "录入人员", "creatUser",  String.class),
        COL150(140, "录入时间", "createTime",  String.class),
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
