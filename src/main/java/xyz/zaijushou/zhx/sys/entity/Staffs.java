package xyz.zaijushou.zhx.sys.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author lsl
 */
public class Staffs {
    private String userName;

    /**
     * 提成金额
     */
    private BigDecimal commission;
    private String commissionMsg;
    /**
     * 已还款金额
     */
    private BigDecimal paidMoney;
    private String paidMoneyMsg;

    private Date actualTime;

    public Date getActualTime() {
        return actualTime;
    }

    public void setActualTime(Date actualTime) {
        this.actualTime = actualTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public BigDecimal getCommission() {
        return commission;
    }

    public void setCommission(BigDecimal commission) {
        this.commission = commission;
    }

    public String getCommissionMsg() {
        return commissionMsg;
    }

    public void setCommissionMsg(String commissionMsg) {
        this.commissionMsg = commissionMsg;
    }

    public BigDecimal getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(BigDecimal paidMoney) {
        this.paidMoney = paidMoney;
    }

    public String getPaidMoneyMsg() {
        return paidMoneyMsg;
    }

    public void setPaidMoneyMsg(String paidMoneyMsg) {
        this.paidMoneyMsg = paidMoneyMsg;
    }
}