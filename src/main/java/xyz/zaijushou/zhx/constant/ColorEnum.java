package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/12.
 */
public enum  ColorEnum {

    BLACK("黑","#000000"),RED("红","#FF0000"),BLUE("蓝","#0000FF"),CHENG("橙","#FA8072"),ZI("紫","#A020F0"),ZONG("棕","#D2B48C");
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private ColorEnum(String key,String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static ColorEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ColorEnum temp:ColorEnum.values()){
            if(temp.getKey().equals(key)){
                return temp;
            }
        }
        return null;
    }
    public String getKey() {
        return key;
    }
    public String getValue() {
        return value;
    }

}
