package xyz.zaijushou.zhx.common.web;

import java.math.BigDecimal;

public class WebResponse {

    private String code = "100";

    private String msg = "success";

    private int totalPageNum;

    private int totalNum;

    private BigDecimal totalAmt;

    private int userCount;

    private Object data;

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getTotalPageNum() {
        return totalPageNum;
    }

    public void setTotalPageNum(int totalPageNum) {
        this.totalPageNum = totalPageNum;
    }

    private WebResponse() {

    }

    public static WebResponse buildResponse() {
        return new WebResponse();
    }

    public static WebResponse success() {
        return new WebResponse();
    }

    public static WebResponse success(Object data) {
        return success().data(data);
    }

    public static WebResponse error(String code, String msg) {
        return buildResponse().code(code).msg(msg);
    }

    public WebResponse code(String code) {
        this.code = code;
        return this;
    }

    public WebResponse msg(String msg) {
        this.msg = msg;
        return this;
    }

    public WebResponse data(Object data) {
        this.data = data;
        return this;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
