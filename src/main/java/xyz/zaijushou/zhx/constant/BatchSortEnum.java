package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum BatchSortEnum {

    COLUMN0("id","id"),
    COLUMN1("batchNo","batch_no"),
    COLUMN2("client","client"),
    COLUMN3("statusMsg","batch_status"),
    COLUMN4("caseTime","bail_date"),
    COLUMN5("userCount","user_count"),
    COLUMN6("totalAmtMsg","total_amt"),
    COLUMN7("uploadTime","upload_time"),
    COLUMN8("area","area"),
    COLUMN9("caseType","case_type"),
    COLUMN10("returnTime","expect_time"),
    COLUMN11("realReturnTime","real_time"),
    COLUMN12("createTime","create_time"),
    COLUMN13("remark","remark"),
    COLUMN14("creatUser","create_user"),
    COLUMN15("batchRemark","batch_remark")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private BatchSortEnum(String key,String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static BatchSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(BatchSortEnum temp:BatchSortEnum.values()){
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
