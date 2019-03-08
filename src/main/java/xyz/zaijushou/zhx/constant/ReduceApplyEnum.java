package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum ReduceApplyEnum {

    COLUMN1("applyStatus","apply_status"),
    COLUMN2("seqno","seqno"),
    COLUMN3("targetName","name"),
    COLUMN4("collectStatus","collect_status"),
    COLUMN5("accountAge","overdue_bill_time"),
    COLUMN6("completeTime","complete_time"),
    COLUMN7("completeUser","complete_user"),
    COLUMN8("approveRepayAmt","approve_repay_amt"),
    COLUMN9("reduceValidTime","reduce_valid_time"),
    COLUMN10("reduceStatus","reduce_status"),
    COLUMN11("reduceResult","reduce_result"),
    COLUMN12("enRepayAmt","en_repay_amt"),
    COLUMN13("reduceUpdateTime","reduce_update_time"),
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private ReduceApplyEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static ReduceApplyEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(ReduceApplyEnum temp:ReduceApplyEnum.values()){
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
