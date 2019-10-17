package xyz.zaijushou.zhx.sys.entity;

/**
 * @author lsl
 * @version [1.0.0, 2019/10/16,21:14]
 */
public class FollowUpData {
    private Integer id;

    private Integer status;

    private String statusMsg;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }
}
