package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelDayExportConstant {



    public enum CollecionList implements ExcelEnum{
        COL10(10, "催收员", "odv", String.class),
        COL20(20, "时间区域", "timeArea1", String.class),
        COL30(30, "有效通量", "countConPhoneNum1", String.class),
        COL40(40, "总通点量", "countPhoneNum1", String.class),
        COL50(50, "个案量", "countCasePhoneNum1",  String.class),

        COL60(60, "时间区域", "timeArea2", String.class),
        COL70(70, "有效通量", "countConPhoneNum2", String.class),
        COL80(80, "总通点量", "countPhoneNum2", String.class),
        COL90(90, "个案量", "countCasePhoneNum2",  String.class),

        COL100(100, "时间区域", "timeArea3", String.class),
        COL110(110, "有效通量", "countConPhoneNum3", String.class),
        COL120(120, "总通点量", "countPhoneNum3", String.class),
        COL130(130, "个案量", "countCasePhoneNum3",  String.class),

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
