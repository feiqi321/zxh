package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.common.entity.TreeEntity;

import java.util.List;

/**
 *案件退案作用表
 */
public class CaseTimeAreaEntity extends CommonEntity {

    /**
     * 退案的数据是否可以被看到,0-可以，1-不可以
     */
    private int seeFlag;

    private String seeFlagMsg;

    /**
     * 案件持续时间
     */
    private Integer timeArea;

    private String timeAreaMg;

    public String getTimeAreaMg() {
        return timeAreaMg;
    }

    public void setTimeAreaMg(String timeAreaMg) {
        this.timeAreaMg = timeAreaMg;
    }

    public String getSeeFlagMsg() {
        return seeFlagMsg;
    }

    public void setSeeFlagMsg(String seeFlagMsg) {
        this.seeFlagMsg = seeFlagMsg;
    }

    public int getSeeFlag() {
        return seeFlag;
    }

    public void setSeeFlag(int seeFlag) {
        this.seeFlag = seeFlag;
    }

    public Integer getTimeArea() {
        return timeArea;
    }

    public void setTimeArea(Integer timeArea) {
        this.timeArea = timeArea;
    }
}
