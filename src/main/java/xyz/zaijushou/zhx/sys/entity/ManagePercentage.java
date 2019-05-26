package xyz.zaijushou.zhx.sys.entity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/5/25.
 */
public class ManagePercentage {

    private Integer id;

    private Integer manage;

    private Integer odv;

    private String odvName;

    private BigDecimal repayAmt;

    private BigDecimal percentage;

    private String lineDate;

    public String getOdvName() {
        return odvName;
    }

    public void setOdvName(String odvName) {
        this.odvName = odvName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getManage() {
        return manage;
    }

    public void setManage(Integer manage) {
        this.manage = manage;
    }

    public Integer getOdv() {
        return odv;
    }

    public void setOdv(Integer odv) {
        this.odv = odv;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public String getLineDate() {
        return lineDate;
    }

    public void setLineDate(String lineDate) {
        this.lineDate = lineDate;
    }
}
