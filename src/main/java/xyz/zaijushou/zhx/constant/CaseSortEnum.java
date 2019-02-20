package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum CaseSortEnum {
    COLUMN0("id","id"),
    COLUMN1("area","area"),
    COLUMN2("collectStatusMsg","collect_status"),
    COLUMN3("batchNo","batch_no"),
    COLUMN4("seqNo","seq_no"),
    COLUMN5("caseDate","case_date"),
    COLUMN6("expectTime","expect_time"),
    COLUMN7("name","name"),
    COLUMN8("cardNo","card_no"),
    COLUMN9("identNo","ident_no"),
    COLUMN10("money","money"),
    COLUMN11("balance","balance"),
    COLUMN12("collectDate","collect_date"),
    COLUMN13("newCase","new_case"),
    COLUMN14("odv","odv"),
    COLUMN15("collectTimes","collect_times"),
    COLUMN16("distributeTime","distribute_time"),
    COLUMN17("proRepayAmt","pro_repay_amt"),
    COLUMN18("bankAmt","bank_amt"),
    COLUMN19("enRepayAmt","en_repay_amt"),
    COLUMN20("accountAge","account_age"),
    COLUMN21("distributeHistory","distribute_history"),
    COLUMN22("summary","summary")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private CaseSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static CaseSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CaseSortEnum temp:CaseSortEnum.values()){
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
