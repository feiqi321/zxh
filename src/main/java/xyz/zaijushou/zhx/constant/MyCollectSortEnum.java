package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum MyCollectSortEnum {
    COLUMN0("id","t.id"),
    COLUMN1("seqno","t.seq_no"),
    COLUMN2("name","t.name"),
    COLUMN4("collectTime","t.collect_date"),
    COLUMN5("targetName","t.name"),
    COLUMN9("collectInfo","t.summary"),
    COLUMN10("result","t.remark"),
    COLUMN12("repayTime","t.repay_date"),
    COLUMN13("repayAmtMsg","t.pro_repay_amt"),
    COLUMN17("collectStatusMsg","t.collect_status"),
    COLUMN18("countFollow","t.collect_times"),
    COLUMN19("remind","t.remind"),
    COLUMN20("nextFollDate","t.next_foll_date"),
    COLUMN21("collectStatus","t.collect_status"),
    COLUMN22("collectionType","t.collection_type"),
    COLUMN23("accountAge","t.account_age"),
    COLUMN24("caseDate","t.case_date"),
    COLUMN25("expectTime","t.expect_time"),
    COLUMN26("cardNo","t.card_no"),
    COLUMN27("identNo","t.ident_no"),
    COLUMN28("moneyMsg","t.money"),
    COLUMN29("balanceMsg","t.balance"),
    COLUMN30("moneyStartMsg","new_money"),
    COLUMN31("overDays","t.over_days"),
    COLUMN32("enRepayAmtMsg","t.en_repay_amt"),
    COLUMN33("repayAmtMsg","t.pro_repay_amt"),
    COLUMN34("bankAmtMsg","t.bank_amt"),
    COLUMN35("lastPhoneTime","t.collect_date"),
    COLUMN36("leaveDays","t.leave_Days"),
    COLUMN37("caseAllotTime","t.distribute_time"),
    COLUMN38("odv","t.odv"),
    COLUMN40("principle","t.principle"),
    COLUMN41("overdueDays","t.overdue_days"),
    COLUMN39("distributeStatusMsg","t.distribute_status");

    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private MyCollectSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static MyCollectSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(MyCollectSortEnum temp: MyCollectSortEnum.values()){
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
