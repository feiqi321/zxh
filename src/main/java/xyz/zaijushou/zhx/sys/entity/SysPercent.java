package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/3/29.
 */
public class SysPercent extends CommonEntity {

    private Integer client;

    private Integer enable;

    private BigDecimal odvLow;

    private BigDecimal odvBasic;

    private BigDecimal odvReward;

    private String odvRemark;

    private BigDecimal manageReward;

    private String manageRemark;

    public Integer getClient() {
        return client;
    }

    public void setClient(Integer client) {
        this.client = client;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
    }

    public BigDecimal getOdvLow() {
        return odvLow;
    }

    public void setOdvLow(BigDecimal odvLow) {
        this.odvLow = odvLow;
    }

    public BigDecimal getOdvBasic() {
        return odvBasic;
    }

    public void setOdvBasic(BigDecimal odvBasic) {
        this.odvBasic = odvBasic;
    }

    public BigDecimal getOdvReward() {
        return odvReward;
    }

    public void setOdvReward(BigDecimal odvReward) {
        this.odvReward = odvReward;
    }

    public String getOdvRemark() {
        return odvRemark;
    }

    public void setOdvRemark(String odvRemark) {
        this.odvRemark = odvRemark;
    }

    public BigDecimal getManageReward() {
        return manageReward;
    }

    public void setManageReward(BigDecimal manageReward) {
        this.manageReward = manageReward;
    }

    public String getManageRemark() {
        return manageRemark;
    }

    public void setManageRemark(String manageRemark) {
        this.manageRemark = manageRemark;
    }
}
