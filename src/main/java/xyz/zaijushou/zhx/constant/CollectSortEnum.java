package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum CollectSortEnum {
    COLUMN0("id","c.id"),
    COLUMN1("seqno","t.seq_no"),
    COLUMN2("name","t.name"),
    COLUMN3("measure","c.measure"),
    COLUMN4("collectTime","c.collect_time"),
    COLUMN5("targetName","c.target_name"),
    COLUMN6("relation","c.relation"),
    COLUMN7("telType","c.tel_type"),
    COLUMN8("mobile","c.mobile"),
    COLUMN9("collectInfo","c.collect_info"),
    COLUMN10("result","c.result"),
    COLUMN11("methodMsg","c.method"),
    COLUMN12("repayTime","c.repay_time"),
    COLUMN13("repayAmtMsg","c.repay_amt"),
    COLUMN14("reduceAmtMsg","c.reduce_amt"),
    COLUMN15("reduceStatusMsg","c.reduce_status"),
    COLUMN16("odv","odv"),
    COLUMN17("collectStatusMsg","c.collect_status"),
    COLUMN18("countFollow","count_follow"),
    COLUMN19("remind","remind"),
    COLUMN20("nextFollDate","next_foll_date"),
    COLUMN21("collectStatus","c.collect_status"),
    COLUMN22("collectionType","c.collection_type"),
    COLUMN23("accountAge","t.account_age"),
    COLUMN24("caseDate","t.case_date"),
    COLUMN25("expectTime","t.expect_time"),
    COLUMN26("cardNo","t.card_no"),
    COLUMN27("identNo","t.ident_no"),
    COLUMN28("moneyMsg","money"),
    COLUMN29("balanceMsg","balance"),
    COLUMN30("moneyStartMsg","new_money"),
    COLUMN31("overDays","t.over_days"),
    COLUMN32("enRepayAmtMsg","t.en_repay_amt"),
    COLUMN33("repayAmtMsg","repay_amt"),
    COLUMN34("bankAmtMsg","t.bank_amt"),
    COLUMN35("lastPhoneTime","last_phone_time"),
    COLUMN36("leaveDays","leave_days"),
    COLUMN37("caseAllotTime","case_allot_time"),
    COLUMN38("money","t.money"),
    COLUMN39("balance","t.balance"),
    COLUMN40("moneyStart","new_money"),
    COLUMN41("enRepayAmt","t.en_repay_amt"),
    COLUMN42("repayAmt","c.repay_amt"),
    COLUMN43("bankAmt","t.bank_amt")
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
