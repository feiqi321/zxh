package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum CaseSortEnum {
    COLUMN0("id","da.id"),
    COLUMN1("collectArea","da.collect_area"),
    COLUMN2("collectStatusMsg","da.collect_status"),
    COLUMN3("batchNo","da.batch_no"),
    COLUMN4("seqNo","da.seq_no"),
    COLUMN5("caseDate","da.case_date"),
    COLUMN6("expectTime","da.expect_time"),
    COLUMN7("name","da.name"),
    COLUMN8("cardNo","da.card_no"),
    COLUMN9("identNo","da.ident_no"),
    COLUMN10("moneyMsg","da.money"),
    COLUMN11("balanceMsg","da.balance"),
    COLUMN12("collectDate","da.collect_date"),
    COLUMN13("newCase","da.new_case"),
    COLUMN14("odv","da.odv"),
    COLUMN15("collectTimes","da.collect_times"),
    COLUMN16("distributeTime","da.distribute_time"),
    COLUMN17("proRepayAmtMsg","da.pro_repay_amt"),
    COLUMN18("bankAmtMsg","da.bank_amt"),
    COLUMN19("enRepayAmtMsg","da.en_repay_amt"),
    COLUMN20("accountAge","da.account_age"),
    COLUMN21("distributeHistory","da.distribute_history"),
    COLUMN22("summary","da.summary")
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
