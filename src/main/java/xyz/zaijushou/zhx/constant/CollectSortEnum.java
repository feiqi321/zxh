package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum CollectSortEnum {
    COLUMN0("id","id"),
    COLUMN1("seqno","seqno"),
    COLUMN2("name","name"),
    COLUMN3("measure","measure"),
    COLUMN4("collectTime","collect_time"),
    COLUMN5("targetName","target_name"),
    COLUMN6("relation","relation"),
    COLUMN7("telType","tel_type"),
    COLUMN8("mobile","mobile"),
    COLUMN9("collectInfo","collect_info"),
    COLUMN10("result","result"),
    COLUMN11("method","method"),
    COLUMN12("repayTime","repay_time"),
    COLUMN13("repayAmt","repay_amt"),
    COLUMN14("reduceAmt","reduce_amt"),
    COLUMN15("reduceStatusMsg","reduce_status"),
    COLUMN16("odv","odv"),
    COLUMN17("collectStatusMsg","collect_status"),
    COLUMN18("countFollow","count_follow"),
    COLUMN19("remind","remind"),
    COLUMN20("nextFollDate","next_foll_date"),
    COLUMN21("collectStatus","collect_status"),
    COLUMN22("collectionType","collection_type"),
    COLUMN23("accountAge","account_age"),
    COLUMN24("caseDate","case_date"),
    COLUMN25("expectTime","expect_time"),
    COLUMN26("cardNo","card_no"),
    COLUMN27("identNo","ident_no"),
    COLUMN28("money","money"),
    COLUMN29("balance","balance"),
    COLUMN30("moneyStart","new_money"),
    COLUMN31("overDays","over_days"),
    COLUMN32("enRepayAmt","en_repay_amt"),
    COLUMN33("repayAmt","repay_amt"),
    COLUMN34("bankAmt","bank_amt"),
    COLUMN35("lastPhoneTime","last_phone_time"),
    COLUMN36("leaveDays","leave_days")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private CollectSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static CollectSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CollectSortEnum temp:CollectSortEnum.values()){
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