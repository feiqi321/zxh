package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

/**
 * 操作日志实体类
 */
public class SysOperationLogEntity extends CommonEntity {

    /**
     * 请求路径
     */
    private String url;

    /**
     * 用户ip
     */
    private String userIp;

    /**
     * 用户浏览器标识
     */
    private String userBrowser;

    /**
     * 请求头部token
     */
    private String requestToken;

    /**
     * 用户id，从token中解析
     */
    private Integer userId;

    /**
     * 用户登录名
     */
    private String userLoginName;

    /**
     * 请求内容
     */
    private String requestBody;

    /**
     * 返回内容
     */
    private String responseBody;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserIp() {
        return userIp;
    }

    public void setUserIp(String userIp) {
        this.userIp = userIp;
    }

    public String getUserBrowser() {
        return userBrowser;
    }

    public void setUserBrowser(String userBrowser) {
        this.userBrowser = userBrowser;
    }

    public String getRequestToken() {
        return requestToken;
    }

    public void setRequestToken(String requestToken) {
        this.requestToken = requestToken;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserLoginName() {
        return userLoginName;
    }

    public void setUserLoginName(String userLoginName) {
        this.userLoginName = userLoginName;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}
