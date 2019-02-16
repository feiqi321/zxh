package xyz.zaijushou.zhx.sys.entity;

import com.github.pagehelper.PageInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by looyer on 2019/2/16.
 */
public class BatchResponse {

    private int userCount;

    private BigDecimal totalAmt;

    private PageInfo pageInfo;

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }
}
