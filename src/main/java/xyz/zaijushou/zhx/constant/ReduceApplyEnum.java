package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum ReduceApplyEnum {
    COLUMN0("id","dcr.id"),
    COLUMN1("applyStatus","dcr.apply_status"),
    COLUMN2("seqno","dcr.seqno"),
    COLUMN3("targetName","dc.name"),
    COLUMN4("collectStatusMsg","dc.collect_status"),
    COLUMN5("moneyMsg","dc.money"),
    COLUMN6("completeTime","dcr.complete_time"),
    COLUMN7("completeUser","dcr.complete_user"),
    COLUMN8("approveRepayAmtMsg","dcr.approve_repay_amt"),
    COLUMN9("reduceValidTime","dcr.reduce_valid_time"),
    COLUMN10("reduceStatusMsg","dc.reduce_status"),
    COLUMN11("reduceResult","dcr.reduce_result"),
    COLUMN12("enRepayAmtMsg","dcr.en_repay_amt"),
    COLUMN13("reduceUpdateTime","dc.reduce_update_time"),
    COLUMN14("collectStatusMsg","dc.collect_status"),
    COLUMN15("reduceStatusMsg","dc.reduce_status"),
    COLUMN16("fileName","dcr.file_name"),
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
