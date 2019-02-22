package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

/**
 * Created by looyer on 2019/1/26.
 */
public class DataArchiveTelEntity extends CommonEntity {


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



    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
