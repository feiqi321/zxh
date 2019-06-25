package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.TreeEntity;

import java.util.List;

/**
 * 组织机构实体类
 */
public class DepartmentEntity{

    /**
     *上级结构
     */
    private String upDept;

    /**
     * 下级结构
     */
    private String downDept;

    /**
     *  员工
     */
    private String userName;

    /**
     * 标识
     */
    private String flag;

    public String getUpDept() {
        return upDept;
    }

    public void setUpDept(String upDept) {
        this.upDept = upDept;
    }

    public String getDownDept() {
        return downDept;
    }

    public void setDownDept(String downDept) {
        this.downDept = downDept;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
