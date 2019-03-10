package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelDayActionExportConstant {

    public enum CollecionList implements ExcelEnum{
        COL10(10, "催收员", "odv", String.class),
        COL20(20, "DX1", "countDX1", String.class),
        COL30(30, "DX2", "countDX2", String.class),
        COL40(40, "DX3", "countDX3",  String.class),
        COL50(50, "DX4", "countDX4", String.class),
        COL60(60, "承诺还款", "countRepay",  String.class),
        COL70(70, "可联本人", "countConSelf",  String.class),
        COL80(80, "可联村委", "countConVillage", String.class),
        COL90(90, "可联第三人", "countConThird", String.class),
        COL100(100, "可联家人", "countConFamily",  String.class),
        COL110(110, "空号错号", "countDeadNumber",  String.class),
        COL120(120, "网搜无效", "countSearchInvalid",  String.class),
        COL130(130, "无人接听", "countNoAnswer",  String.class),
        COL140(140, "无效电话", "countInvalidCall",  String.class),
        COL150(150, "114查询无效", "countSearchNo",  String.class),
        COL160(160, "催收员催收结果统计", "countResult",  String.class),
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
