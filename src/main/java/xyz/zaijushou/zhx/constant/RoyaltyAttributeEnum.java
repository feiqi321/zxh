package xyz.zaijushou.zhx.constant;

/**
 * 提成 条线 属性数组
 */
public enum RoyaltyAttributeEnum {
    /**
     * 属性数组
     */
    SHAGNQICAR(2, "上汽长账龄上汽拖车"),
    SHAGNQICAROHTER(2, "上汽长账龄其他"),
    SHANGQISHORT(3, "上汽短期"),
    ANJI(3, "安吉蓝海")
    ;
    private int code;
    private String text;

    RoyaltyAttributeEnum(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public static int getText(String text) {
        for (RoyaltyAttributeEnum typeEnum : RoyaltyAttributeEnum.values()) {
            if ((typeEnum.getText()) == (text)) {
                return typeEnum.getCode();
            }
        }
        return 0;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }

}
