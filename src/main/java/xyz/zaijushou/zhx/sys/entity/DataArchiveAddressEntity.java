package xyz.zaijushou.zhx.sys.entity;

/**
 * Created by looyer on 2019/1/26.
 */
public class DataArchiveAddressEntity {

    private int id;

    private int archiveId;

    private String address;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
