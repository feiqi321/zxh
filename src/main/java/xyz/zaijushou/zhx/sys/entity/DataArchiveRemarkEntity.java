package xyz.zaijushou.zhx.sys.entity;

/**
 * Created by looyer on 2019/1/26.
 */
public class DataArchiveRemarkEntity {

    private int id;

    private int archiveId;

    private String remark;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
