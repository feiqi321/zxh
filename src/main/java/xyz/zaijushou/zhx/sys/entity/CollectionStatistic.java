package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Date;
import java.util.List;

/**
 * 角色实体类
 */
public class CollectionStatistic extends CommonEntity {
    @JSONField(format="HH:mm")//页面显示时间范围
    private Date dateStart ;
    @JSONField(format="HH:mm")
    private Date dateEnd;
    @JSONField(format="yyyy-MM-dd")
    private Date dateSearchStart;//时间搜索
    @JSONField(format="yyyy-MM-dd")
    private Date dateSearchEnd;
    private int comlun;//列数
    private String timeArea ; // 时间区域
    private int countConPhoneNum;//接通电话数
    private int countPhoneNum;//总通话数
    private int countCasePhoneNum;//通话涉及到的案件数

    public Date getDateStart() {
        return dateStart;
    }

    public void setDateStart(Date dateStart) {
        this.dateStart = dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public Date getDateSearchStart() {
        return dateSearchStart;
    }

    public void setDateSearchStart(Date dateSearchStart) {
        this.dateSearchStart = dateSearchStart;
    }

    public Date getDateSearchEnd() {
        return dateSearchEnd;
    }

    public void setDateSearchEnd(Date dateSearchEnd) {
        this.dateSearchEnd = dateSearchEnd;
    }

    public int getComlun() {
        return comlun;
    }

    public void setComlun(int comlun) {
        this.comlun = comlun;
    }

    public String getTimeArea() {
        return timeArea;
    }

    public void setTimeArea(String timeArea) {
        this.timeArea = timeArea;
    }

    public int getCountConPhoneNum() {
        return countConPhoneNum;
    }

    public void setCountConPhoneNum(int countConPhoneNum) {
        this.countConPhoneNum = countConPhoneNum;
    }

    public int getCountPhoneNum() {
        return countPhoneNum;
    }

    public void setCountPhoneNum(int countPhoneNum) {
        this.countPhoneNum = countPhoneNum;
    }

    public int getCountCasePhoneNum() {
        return countCasePhoneNum;
    }

    public void setCountCasePhoneNum(int countCasePhoneNum) {
        this.countCasePhoneNum = countCasePhoneNum;
    }
}
