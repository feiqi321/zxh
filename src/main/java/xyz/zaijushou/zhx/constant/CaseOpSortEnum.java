package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum CaseOpSortEnum {
    COLUMN13("id","t1.id"),
    COLUMN0("client","t2.client"),
    COLUMN1("seqNo","t2.seq_no"),
    COLUMN2("contractNo","t2.contract_no"),
    COLUMN3("name","t2.name"),
    COLUMN4("identNo","t2.ident_no"),
    COLUMN5("statusMsg","t2.status"),
    COLUMN6("collectStatusMsg","t2.collect_status"),
    COLUMN7("createTime","t1.create_time"),
    COLUMN8("creatorName","t1.creator"),
    COLUMN9("lastOdv","t1.last_odv"),
    COLUMN10("odv","t2.odv"),
    COLUMN11("dept","t2.dept"),
    COLUMN12("context","t1.context"),
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private CaseOpSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static CaseOpSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(CaseOpSortEnum temp: CaseOpSortEnum.values()){
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
