package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum SynergySortEnum {
    COLUMN0("id","t.id"),
    COLUMN1("seqno","t.seq_no"),
    COLUMN2("name","t.name"),
    COLUMN3("cardNo","t.card_no"),
    COLUMN4("moneyMsg","t.money"),
    COLUMN5("caseDate","t.case_date"),
    COLUMN6("collectStatusMsg","t.collect_status"),
    COLUMN7("collectDate","t.collect_date"),
    COLUMN8("proRepayAmtMsg","t.pro_repay_amt"),
    COLUMN9("enRepayAmtMsg","t.en_repay_amt"),
    COLUMN10("accountAge","t.account_age"),
    COLUMN11("odv","t.odv")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private SynergySortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static SynergySortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(SynergySortEnum temp:SynergySortEnum.values()){
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
