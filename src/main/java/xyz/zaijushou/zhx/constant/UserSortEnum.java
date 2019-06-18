package xyz.zaijushou.zhx.constant;

/**
 * Created by looyer on 2019/2/19.
 */
public enum UserSortEnum {

    COLUMN1("mobile","mobile"),
    COLUMN2("number","number"),
    COLUMN3("userName","username"),
    COLUMN4("sex","sex"),
    COLUMN5("officePhone","office_phone"),
    COLUMN6("joinTime","join_time"),
    COLUMN7("role","role_name"),
    COLUMN8("departId","department"),
    COLUMN9("department","department"),
    COLUMN10("position","position"),
    COLUMN11("leaveTime","leave_time"),
    COLUMN13("joinTime","join_time"),
    COLUMN14("loginName","login_name"),
    COLUMN15("id","su.id"),
    COLUMN16("actualTime","actual_time"),
    COLUMN17("roleList","role_name"),    //此处由于user表中没有对应的role字段，因此默认排序字段为username
    getColumn12("enableMsg","su.enable")
    ;
    //防止字段值被修改，增加的字段也统一final表示常量
    private final String key;
    private final String value;

    private UserSortEnum(String key, String value){
        this.key = key;
        this.value = value;
    }
    //根据key获取枚举
    public static UserSortEnum getEnumByKey(String key){
        if(null == key){
            return null;
        }
        for(UserSortEnum temp:UserSortEnum.values()){
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
