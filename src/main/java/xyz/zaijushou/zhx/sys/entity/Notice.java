package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

/**
 * Created by looyer on 2019/6/27.
 */
public class Notice extends CommonEntity {

    private Integer receiveUser;

    private Integer[] receiveUsers;

    private String receiveUserName;

    private String context;

    private Integer sendUser;

    private String sendUserName;

    public Integer[] getReceiveUsers() {
        return receiveUsers;
    }

    public void setReceiveUsers(Integer[] receiveUsers) {
        this.receiveUsers = receiveUsers;
    }

    public Integer getSendUser() {
        return sendUser;
    }

    public void setSendUser(Integer sendUser) {
        this.sendUser = sendUser;
    }


    public Integer getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(Integer receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getReceiveUserName() {
        return receiveUserName;
    }

    public void setReceiveUserName(String receiveUserName) {
        this.receiveUserName = receiveUserName;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getSendUserName() {
        return sendUserName;
    }

    public void setSendUserName(String sendUserName) {
        this.sendUserName = sendUserName;
    }
}
