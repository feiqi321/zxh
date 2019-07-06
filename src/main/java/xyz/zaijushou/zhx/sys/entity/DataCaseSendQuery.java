package xyz.zaijushou.zhx.sys.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by looyer on 2019/6/25.
 */
public class DataCaseSendQuery implements Serializable
{
    private Integer totalCount;

    private String totalAmt;

    private String enAmt;

    private String unAmt;

    private Integer unCount;

    private Integer enCount;

    public Integer getUnCount() {
        return unCount;
    }

    public void setUnCount(Integer unCount) {
        this.unCount = unCount;
    }

    public Integer getEnCount() {
        return enCount;
    }

    public void setEnCount(Integer enCount) {
        this.enCount = enCount;
    }

    public Integer getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }

    public String getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(String totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getEnAmt() {
        return enAmt;
    }

    public void setEnAmt(String enAmt) {
        this.enAmt = enAmt;
    }

    public String getUnAmt() {
        return unAmt;
    }

    public void setUnAmt(String unAmt) {
        this.unAmt = unAmt;
    }
}
