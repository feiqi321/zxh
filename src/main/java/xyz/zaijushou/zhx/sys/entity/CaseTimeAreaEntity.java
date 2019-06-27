package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.TreeEntity;

import java.util.List;

/**
 *案件退案作用表
 */
public class CaseTimeAreaEntity {

    /**
     * 退案的数据是否可以被看到,0-可以，1-不可以
     */
    private int seeFlag;

    /**
     * 案件持续时间
     */
    private String timeArea;

    public int getSeeFlag() {
        return seeFlag;
    }

    public void setSeeFlag(int seeFlag) {
        this.seeFlag = seeFlag;
    }

    public String getTimeArea() {
        return timeArea;
    }

    public void setTimeArea(String timeArea) {
        this.timeArea = timeArea;
    }
}
