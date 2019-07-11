package xyz.zaijushou.zhx.sys.entity;

/**
 * Created by looyer on 2019/3/1.
 */
public class ReduceFileList {

    private int id;
    private String fileName;
    private int reduceId;
    private String fileId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getReduceId() {
        return reduceId;
    }

    public void setReduceId(int reduceId) {
        this.reduceId = reduceId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
}
