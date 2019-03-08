package xyz.zaijushou.zhx.sys.entity;

import java.io.Serializable;

/**
 * Created by looyer on 2019/3/1.
 */
public class DataOpLog implements Serializable{

    private int id;
    private String type; //电话催收  电话管理  案件管理
    private String context;
    private String opTime;
    private int oper;
    private String operName;
    private String caseId;

    public String getOperName() {
        return operName;
    }

    public void setOperName(String operName) {
        this.operName = operName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getOpTime() {
        return opTime;
    }

    public void setOpTime(String opTime) {
        this.opTime = opTime;
    }

    public int getOper() {
        return oper;
    }

    public void setOper(int oper) {
        this.oper = oper;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }
}
