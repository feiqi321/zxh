package xyz.zaijushou.zhx.constant;

public enum  SynergisticTypeEnum {

    TSYJ("10", "投诉预警"),
    DZCX("20", "地址查询"),
    FYXC("30", "法院协催"),
    SBCX("40", "社保查询"),
    GAXC("50", "公安协催"),
    ZYCX("60", "争议咨询"),
    YDCX("70", "移动查询"),
    ZGXC("80", "主管协催"),
    KFZX("90", "客服咨询"),
    DXCX("100", "电信查询"),
    DXSQ("110", "短信申请"),
    TJCX("120", "退件查询"),
    ;

    private String key;

    private String typeName;

    SynergisticTypeEnum(String key, String typeName) {
        this.key = key;
        this.typeName = typeName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
