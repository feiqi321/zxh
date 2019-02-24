package xyz.zaijushou.zhx.sys.entity;

/**
 * Created by looyer on 2019/2/24.
 */
public class DataCaseDetail {
        //主要部分
    private String name;//名称
    private String client;//委托方

        //隐藏部分
    private String distributeTime;//分配时间
    private String distributeHistory;//分配历史

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getDistributeTime() {
        return distributeTime;
    }

    public void setDistributeTime(String distributeTime) {
        this.distributeTime = distributeTime;
    }

    public String getDistributeHistory() {
        return distributeHistory;
    }

    public void setDistributeHistory(String distributeHistory) {
        this.distributeHistory = distributeHistory;
    }
}
