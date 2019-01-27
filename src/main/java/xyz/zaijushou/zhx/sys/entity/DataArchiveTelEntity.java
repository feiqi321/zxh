package xyz.zaijushou.zhx.sys.entity;

/**
 * Created by looyer on 2019/1/26.
 */
public class DataArchiveTelEntity {

    private int id;

    private int archiveId;

    private String tel;

    private String teltype;

    public String getTeltype() {
        return teltype;
    }

    public void setTeltype(String teltype) {
        this.teltype = teltype;
    }

    public int getArchiveId() {
        return archiveId;
    }

    public void setArchiveId(int archiveId) {
        this.archiveId = archiveId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
