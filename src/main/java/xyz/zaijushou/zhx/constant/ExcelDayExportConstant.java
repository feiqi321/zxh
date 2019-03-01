package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelDayExportConstant {



    public enum CollecionList implements ExcelEnum{
        COL10(10, "催收员", "odv", String.class),
        COL20(20, "有效通量", "countConPhoneNum", String.class),
        COL30(30, "总通点量", "countPhoneNum", String.class),
        COL40(40, "个案量", "countCasePhoneNum",  String.class),
        COL50(50, "时间区域", "area", String.class),

        COL60(60, "时间区域", "area", String.class),
        COL70(70, "有效通量", "countConPhoneNum", String.class),
        COL80(80, "总通点量", "countPhoneNum", String.class),
        COL90(90, "个案量", "countCasePhoneNum",  String.class),

        COL100(100, "时间区域", "area", String.class),
        COL110(110, "有效通量", "countConPhoneNum", String.class),
        COL120(120, "总通点量", "countPhoneNum", String.class),
        COL130(130, "个案量", "countCasePhoneNum",  String.class),

        COL140(140, "催收员合计-有效通量", "sumConPhoneNum",  String.class),
        COL150(150, "催收员合计-总通点量", "sumPhoneNum",  String.class),
        COL160(160, "催收员合计-个案量", "sumCasePhoneNum", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CollecionList(Integer sort, String col, String attr, Class... attrClazz) {
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
