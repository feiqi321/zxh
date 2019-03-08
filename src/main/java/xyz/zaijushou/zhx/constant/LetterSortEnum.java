package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/3/7.
 */
public enum LetterSortEnum {

    COLUMN0("id","id"),
    COLUMN1("seqno","seqno"),
    COLUMN2("name","name"),
    COLUMN3("collectStatusMsg","collectStatus"),
    COLUMN4("caseAmtMsg","money"),
    COLUMN5("repayAmtMsg","repayAmt"),
    COLUMN6("address","address"),
    COLUMN7("times","times"),
    COLUMN9("applyContext","applyContext"),
    COLUMN10("module","module"),
    COLUMN11("relationer","relationer"),
    COLUMN12("applyDate","applyDate"),
    COLUMN13("applyer","applyer"),
    COLUMN14("synergyDate","synergyDate"),
    COLUMN15("synergyer","synergyer"),
    COLUMN16("synergyResult","synergyResult")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private LetterSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static LetterSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(LetterSortEnum temp:LetterSortEnum.values()){
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
