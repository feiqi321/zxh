package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/3/29.
 */
public class SysPercent extends CommonEntity {

    private String client;

    private String enable;

    private BigDecimal odvLow;

    private String odvLowMsg;

    private BigDecimal odvBasic;

    private String odvBasicMsg;

    private BigDecimal odvReward;

    private String odvRewardMsg;

    private BigDecimal odvHighBasic;

    private String odvHighBasicMsg;

    private BigDecimal odvHighReward;

    private String odvHighRewardMsg;

    private BigDecimal manageReward;

    private String manageRewardMsg;

    private BigDecimal manageBasic;

    private String remark;

    public String getOdvBasicMsg() {
        return odvBasicMsg;
    }

    public void setOdvBasicMsg(String odvBasicMsg) {
        this.odvBasicMsg = odvBasicMsg;
    }

    public String getOdvRewardMsg() {
        return odvRewardMsg;
    }

    public void setOdvRewardMsg(String odvRewardMsg) {
        this.odvRewardMsg = odvRewardMsg;
    }

    public String getOdvHighBasicMsg() {
        return odvHighBasicMsg;
    }

    public void setOdvHighBasicMsg(String odvHighBasicMsg) {
        this.odvHighBasicMsg = odvHighBasicMsg;
    }

    public String getOdvHighRewardMsg() {
        return odvHighRewardMsg;
    }

    public void setOdvHighRewardMsg(String odvHighRewardMsg) {
        this.odvHighRewardMsg = odvHighRewardMsg;
    }

    public String getManageRewardMsg() {
        return manageRewardMsg;
    }

    public void setManageRewardMsg(String manageRewardMsg) {
        this.manageRewardMsg = manageRewardMsg;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public void setEnable(String enable) {
        this.enable = enable;
    }

    public BigDecimal getOdvHighBasic() {
        return odvHighBasic;
    }

    public void setOdvHighBasic(BigDecimal odvHighBasic) {
        this.odvHighBasic = odvHighBasic;
    }

    public BigDecimal getOdvHighReward() {
        return odvHighReward;
    }

    public void setOdvHighReward(BigDecimal odvHighReward) {
        this.odvHighReward = odvHighReward;
    }

    public BigDecimal getManageBasic() {
        return manageBasic;
    }

    public void setManageBasic(BigDecimal manageBasic) {
        this.manageBasic = manageBasic;
    }

    public String getOdvLowMsg() {
        return odvLowMsg;
    }

    public void setOdvLowMsg(String odvLowMsg) {
        this.odvLowMsg = odvLowMsg;
    }

    public String getClient() {
        return client;
    }

    public String getEnable() {
        return enable;
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

    public BigDecimal getManageReward() {
        return manageReward;
    }

    public void setManageReward(BigDecimal manageReward) {
        this.manageReward = manageReward;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }
}
