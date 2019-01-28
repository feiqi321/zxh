package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCaseRepayEntity extends CommonEntity {
    private int caseId;
    private String repayTime;
    private BigDecimal repayAmt;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(String repayTime) {
        this.repayTime = repayTime;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }
}
