package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum ArchiveSortEnum {
    COLUMN0("id","id"),
    COLUMN1("identNo","ident_no"),
    COLUMN2("name","name"),
    COLUMN3("updateTime","update_time"),
    COLUMN4("remark","remark")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private ArchiveSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static ArchiveSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ArchiveSortEnum temp:ArchiveSortEnum.values()){
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
