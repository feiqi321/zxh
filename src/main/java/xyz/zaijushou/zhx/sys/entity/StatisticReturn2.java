package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色实体类
 */
public class StatisticReturn2 extends CommonEntity {

    private String odv;
    /**接通电话数*/
    private int sumConPhoneNum;
    /**无效通话*/
    private int sumInvalidPhoneNum;
    /**总通话数*/
    private int sumPhoneNum;
    /**通话涉及到的案件数*/
    private int sumCasePhoneNum;

    public int getSumInvalidPhoneNum() {
        return sumInvalidPhoneNum;
    }

    public void setSumInvalidPhoneNum(int sumInvalidPhoneNum) {
        this.sumInvalidPhoneNum = sumInvalidPhoneNum;
    }

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
}
