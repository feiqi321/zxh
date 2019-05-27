package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/5/25.
 */
public class OdvPercentage extends CommonEntity {

    private Integer odv;

    private String line;

    private String lineDate;

    private BigDecimal repayAmt;

    private BigDecimal percentage;

    public Integer getOdv() {
        return odv;
    }

    public void setOdv(Integer odv) {
        this.odv = odv;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

    public String getLineDate() {
        return lineDate;
    }

    public void setLineDate(String lineDate) {
        this.lineDate = lineDate;
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
}
