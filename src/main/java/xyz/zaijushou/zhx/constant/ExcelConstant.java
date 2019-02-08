package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseTelEntity;

public class ExcelConstant {

    public enum CaseTel {
        COL10(10, "*个案序列号", "seqNo", String.class),
        COL20(20, "*档案号", "archiveNo", String.class),
        COL30(30, "*卡号", "cardNo", String.class),
        COL40(40, "*证件号", "identNo", String.class),
        COL50(50, "*委案日期", "caseDate", String.class),
        COL60(60, "*电话", "telList[0].tel", DataCaseTelEntity.class, String.class),
        COL70(70, "*姓名", "telList[0].name", DataCaseTelEntity.class, String.class),
        COL80(80, "电话类型", "telList[0].type", DataCaseTelEntity.class, String.class),
        COL90(90, "关系", "telList[0].relation", DataCaseTelEntity.class, String.class),
        COL100(100, "备注", "telList[0].remark", DataCaseTelEntity.class, String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        CaseTel(Integer sort, String col, String attr, Class... attrClazz) {
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

    public static String[] CASE_TEL_EXCEL_COL = {
            "*个案序列号",
            "*档案号",
            "*卡号",
            "*证件号",
            "*委案日期",
            "*电话",
            "*姓名",
            "电话类型",
            "关系",
            "备注"
    };

    public static String[] CASE_TEL_EXCEL_ATTR = {
            "seqNo",
            "archiveNo",
            "cardNo",
            "identNo",
            "caseDate",
            "telList[0].tel",
            "telList[0].name",
            "telList[0].type",
            "telList[0].relation",
            "telList[0].remark"
    };

}
