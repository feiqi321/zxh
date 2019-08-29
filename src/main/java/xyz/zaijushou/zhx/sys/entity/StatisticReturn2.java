package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色实体类
 */
public class StatisticReturn2 extends CommonEntity {

    private String odv;
    private String dOdv;
    private int sumConPhoneNum;//接通电话数
    private int sumPhoneNum;//总通话数
    private int sumCasePhoneNum;//通话涉及到的案件数

    private List<CollectionStatisticDTO> list = new ArrayList<>();

    public List<CollectionStatisticDTO> getList() {
        return list;
    }

    public void setList(List<CollectionStatisticDTO> list) {
        this.list = list;
    }

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

    public String getdOdv() {
        return dOdv;
    }

    public void setdOdv(String dOdv) {
        this.dOdv = dOdv;
    }
}
