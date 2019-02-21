package xyz.zaijushou.zhx.sys.entity;

import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by looyer on 2019/2/21.
 */
public class CaseResponse {

    private int totalCaseNum;

    private BigDecimal totalAmt;

    private int repayNum;

    private BigDecimal repayTotalAmt;

    private BigDecimal totalCp;

    private BigDecimal totalPtp;

    private PageInfo pageInfo;

    public int getTotalCaseNum() {
        return totalCaseNum;
    }

    public void setTotalCaseNum(int totalCaseNum) {
        this.totalCaseNum = totalCaseNum;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public int getRepayNum() {
        return repayNum;
    }

    public void setRepayNum(int repayNum) {
        this.repayNum = repayNum;
    }

    public BigDecimal getRepayTotalAmt() {
        return repayTotalAmt;
    }

    public void setRepayTotalAmt(BigDecimal repayTotalAmt) {
        this.repayTotalAmt = repayTotalAmt;
    }

    public BigDecimal getTotalCp() {
        return totalCp;
    }

    public void setTotalCp(BigDecimal totalCp) {
        this.totalCp = totalCp;
    }

    public BigDecimal getTotalPtp() {
        return totalPtp;
    }

    public void setTotalPtp(BigDecimal totalPtp) {
        this.totalPtp = totalPtp;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
