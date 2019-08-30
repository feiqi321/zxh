package xyz.zaijushou.zhx.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCaseInterestEntity extends CommonEntity {
    private int caseId;
    private String seqNo;
    private String cardNo;
    private String caseDate;
    private String name;
    private String currency;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal lastestDebt;
    private String endDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal principal;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal interest;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal penalty;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal lateFee;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal overrunFee;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal serivceFee;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal yearFee;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal elseFee;
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal sheetFee;

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public BigDecimal getLastestDebt() {
        return lastestDebt;
    }

    public void setLastestDebt(BigDecimal lastestDebt) {
        this.lastestDebt = lastestDebt;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getPrincipal() {
        return principal;
    }

    public void setPrincipal(BigDecimal principal) {
        this.principal = principal;
    }

    public BigDecimal getInterest() {
        return interest;
    }

    public void setInterest(BigDecimal interest) {
        this.interest = interest;
    }

    public BigDecimal getPenalty() {
        return penalty;
    }

    public void setPenalty(BigDecimal penalty) {
        this.penalty = penalty;
    }

    public BigDecimal getLateFee() {
        return lateFee;
    }

    public void setLateFee(BigDecimal lateFee) {
        this.lateFee = lateFee;
    }

    public BigDecimal getOverrunFee() {
        return overrunFee;
    }

    public void setOverrunFee(BigDecimal overrunFee) {
        this.overrunFee = overrunFee;
    }

    public BigDecimal getSerivceFee() {
        return serivceFee;
    }

    public void setSerivceFee(BigDecimal serivceFee) {
        this.serivceFee = serivceFee;
    }

    public BigDecimal getYearFee() {
        return yearFee;
    }

    public void setYearFee(BigDecimal yearFee) {
        this.yearFee = yearFee;
    }

    public BigDecimal getElseFee() {
        return elseFee;
    }

    public void setElseFee(BigDecimal elseFee) {
        this.elseFee = elseFee;
    }

    public BigDecimal getSheetFee() {
        return sheetFee;
    }

    public void setSheetFee(BigDecimal sheetFee) {
        this.sheetFee = sheetFee;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }


}
