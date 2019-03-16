package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;

public class ExcelReduceExportConstant {



    public enum ReduceList implements ExcelEnum{
        COL10(10, "个人序列号", "seqno", String.class),
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


}
