package xyz.zaijushou.zhx.constant;

public enum  WebResponseCode {

    SUCCESS("100", "success"),
    TOKEN_ERROR("400", "token无效"),
    NO_AUTH("403", "权限不足"),
    COMMON_ERROR("500", "error");

    private String code;

    private String msg;

    WebResponseCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
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
}
