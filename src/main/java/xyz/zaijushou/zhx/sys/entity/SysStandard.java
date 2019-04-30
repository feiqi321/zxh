package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * @author lsl
 * @Date:2019/4/29
 * @Time:20:57
 */
public class SysStandard extends CommonEntity {

    private BigDecimal standard1;
    private String standard1Msg;

    private BigDecimal standard2;
    private String standard2Msg;

    private BigDecimal reward1;
    private String reward1Msg;

    private BigDecimal reward2;
    private String reward2Msg;

    private BigDecimal reward3;
    private String reward3Msg;

    public BigDecimal getStandard1() {
        return standard1;
    }

    public void setStandard1(BigDecimal standard1) {
        this.standard1 = standard1;
    }

    public String getStandard1Msg() {
        return standard1Msg;
    }

    public void setStandard1Msg(String standard1Msg) {
        this.standard1Msg = standard1Msg;
    }

    public BigDecimal getStandard2() {
        return standard2;
    }

    public void setStandard2(BigDecimal standard2) {
        this.standard2 = standard2;
    }

    public String getStandard2Msg() {
        return standard2Msg;
    }

    public void setStandard2Msg(String standard2Msg) {
        this.standard2Msg = standard2Msg;
    }

    public BigDecimal getReward1() {
        return reward1;
    }

    public void setReward1(BigDecimal reward1) {
        this.reward1 = reward1;
    }

    public String getReward1Msg() {
        return reward1Msg;
    }

    public void setReward1Msg(String reward1Msg) {
        this.reward1Msg = reward1Msg;
    }

    public BigDecimal getReward2() {
        return reward2;
    }

    public void setReward2(BigDecimal reward2) {
        this.reward2 = reward2;
    }

    public String getReward2Msg() {
        return reward2Msg;
    }

    public void setReward2Msg(String reward2Msg) {
        this.reward2Msg = reward2Msg;
    }

    public BigDecimal getReward3() {
        return reward3;
    }

    public void setReward3(BigDecimal reward3) {
        this.reward3 = reward3;
    }

    public String getReward3Msg() {
        return reward3Msg;
    }

    public void setReward3Msg(String reward3Msg) {
        this.reward3Msg = reward3Msg;
    }
}
