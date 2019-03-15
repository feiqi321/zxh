package xyz.zaijushou.zhx.constant;

import xyz.zaijushou.zhx.sys.entity.DataCaseContactsEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseRemarkEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;

import java.math.BigDecimal;
import java.util.Date;

public class ExcelUserConstant {

    public enum UserInfo implements ExcelEnum {
        COL10(10, "*用户名", "userName", String.class),
        COL20(20, "*账号", "number", String.class),
        COL30(30, "*部门", "department", String.class),
        COL40(40, "*角色", "role", String.class),
        COL50(50, "性别", "sex", String.class),
        COL60(60, "手机号", "mobile", String.class),
        COL70(70, "座机号", "officePhone", String.class),
        COL80(80, "入职日期", "joinTime", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        UserInfo(Integer sort, String col, String attr, Class... attrClazz) {
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

    public enum UserInfoExport implements ExcelEnum{
        COL1(1, "员工ID", "id", Integer.class),
        COL10(10, "员工姓名", "userName", String.class),
        COL20(20, "账号", "loginName", String.class),
        COL30(30, "性别", "sex", String.class),
        COL40(40, "座机号", "officePhone", String.class),
        COL50(50, "手机号", "mobile", String.class),
        COL60(60, "入职日期", "joinTime", Date.class),
        COL70(70, "角色", "role", String.class),
        COL80(80, "部门", "department", String.class),
        ;

        private Integer sort;

        private String col;

        private String attr;

        private Class[] attrClazz;

        UserInfoExport(Integer sort, String col, String attr, Class... attrClazz) {
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
