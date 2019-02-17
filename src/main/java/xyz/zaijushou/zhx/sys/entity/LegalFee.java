package xyz.zaijushou.zhx.sys.entity;

import java.math.BigDecimal;

public class LegalFee {
    private Integer id;

    private Integer legalId;

    private BigDecimal fee;

    private String chargeDate;

    private String feeType;

    private String feeTarget;

    private String payMethod;

    private String payee;

    private String voucher;

    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLegalId() {
        return legalId;
    }

    public void setLegalId(Integer legalId) {
        this.legalId = legalId;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getChargeDate() {
        return chargeDate;
    }

    public void setChargeDate(String chargeDate) {
        this.chargeDate = chargeDate;
    }

    public String getFeeType() {
        return feeType;
    }

    public void setFeeType(String feeType) {
        this.feeType = feeType;
    }

    public String getFeeTarget() {
        return feeTarget;
    }

    public void setFeeTarget(String feeTarget) {
        this.feeTarget = feeTarget;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayee() {
        return payee;
    }

    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getVoucher() {
        return voucher;
    }

    public void setVoucher(String voucher) {
        this.voucher = voucher;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}