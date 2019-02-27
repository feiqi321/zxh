package xyz.zaijushou.zhx.constant;

/**
 * 还款类型枚举
 */
public enum RepayTypeEnum {

    DKK("10", "代扣卡"),
    DGHK("20", "对公还款")
    ;

    private String code;

    private String typeName;

    RepayTypeEnum(String code, String typeName) {
        this.code = code;
        this.typeName = typeName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }}
