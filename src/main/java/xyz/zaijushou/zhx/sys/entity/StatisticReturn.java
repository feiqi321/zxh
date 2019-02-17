package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色实体类
 */
public class StatisticReturn extends CommonEntity {

    private String odv;
    private int sumConPhoneNum;//接通电话数
    private int sumPhoneNum;//总通话数
    private int sumCasePhoneNum;//通话涉及到的案件数
    private List<CollectionStatistic> list = new ArrayList<CollectionStatistic>();

    public int getSumConPhoneNum() {
        return sumConPhoneNum;
    }

    public void setSumConPhoneNum(int sumConPhoneNum) {
        this.sumConPhoneNum = sumConPhoneNum;
    }

    public int getSumPhoneNum() {
        return sumPhoneNum;
    }

    public void setSumPhoneNum(int sumPhoneNum) {
        this.sumPhoneNum = sumPhoneNum;
    }

    public int getSumCasePhoneNum() {
        return sumCasePhoneNum;
    }

    public void setSumCasePhoneNum(int sumCasePhoneNum) {
        this.sumCasePhoneNum = sumCasePhoneNum;
    }

    public String getOdv() {
        return odv;
    }

    public void setOdv(String odv) {
        this.odv = odv;
    }

    public List<CollectionStatistic> getList() {
        return list;
    }

    public void setList(List<CollectionStatistic> list) {
        this.list = list;
    }
}
