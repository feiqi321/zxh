package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/3/7.
 */
public enum LegalSortEnum {

    COLUMN0("id","id"),
    COLUMN1("legalStatusMsg","legal_status"),
    COLUMN2("progressMsg","progress"),
    COLUMN3("legalTypeMsg","legal_type"),
    COLUMN4("cstName","cst_name"),
    COLUMN5("legalDate","legal_date"),
    COLUMN6("clientele","clientele"),
    COLUMN7("accused","accused"),
    COLUMN9("tital","tital"),
    COLUMN10("costMsg","cost"),
    COLUMN11("ownerName","owner"),
    COLUMN12("agent","agent"),
    COLUMN13("court","court"),
    COLUMN14("legalNo","legal_no"),
    COLUMN15("remark","remark")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private LegalSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static LegalSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(LegalSortEnum temp: LegalSortEnum.values()){
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
