package xyz.zaijushou.zhx.sys.entity;

public class LegalHandle {
    private Integer id;

    private Integer legalId;
    //办案进度
    private String progress;
    //办理时间
    private String handleDate;
    //保全费缴纳时间
    private String preservationDate;
    //保全资产清单
    private String preservationList;
    //备注
    private String remark;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getLegalId() {
        return legalId;
    }

    public void setLegalId(Integer legalId) {
        this.legalId = legalId;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getHandleDate() {
        return handleDate;
    }

    public void setHandleDate(String handleDate) {
        this.handleDate = handleDate;
    }

    public String getPreservationDate() {
        return preservationDate;
    }

    public void setPreservationDate(String preservationDate) {
        this.preservationDate = preservationDate;
    }

    public String getPreservationList() {
        return preservationList;
    }

    public void setPreservationList(String preservationList) {
        this.preservationList = preservationList;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}