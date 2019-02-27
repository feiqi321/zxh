package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * Created by looyer on 2019/2/24.
 */
public class DataCaseDetail {

    private  int id;
        //主要部分
    private String name;//名称
    private String client;//委托方

        //隐藏部分
    private String distributeTime;//分配时间
    private String distributeHistory;//分配历史

    private String selfInfo;

    private List<DataCaseTelEntity> dataCaseTelEntityList;

    private List<DataCaseAddressEntity> dataCaseAddressEntityList;

    private List<DataCaseCommentEntity> dataCaseCommentEntityList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<DataCaseCommentEntity> getDataCaseCommentEntityList() {
        return dataCaseCommentEntityList;
    }

    public void setDataCaseCommentEntityList(List<DataCaseCommentEntity> dataCaseCommentEntityList) {
        this.dataCaseCommentEntityList = dataCaseCommentEntityList;
    }

    public List<DataCaseAddressEntity> getDataCaseAddressEntityList() {
        return dataCaseAddressEntityList;
    }

    public void setDataCaseAddressEntityList(List<DataCaseAddressEntity> dataCaseAddressEntityList) {
        this.dataCaseAddressEntityList = dataCaseAddressEntityList;
    }

    public List<DataCaseTelEntity> getDataCaseTelEntityList() {
        return dataCaseTelEntityList;
    }

    public void setDataCaseTelEntityList(List<DataCaseTelEntity> dataCaseTelEntityList) {
        this.dataCaseTelEntityList = dataCaseTelEntityList;
    }

    public String getSelfInfo() {
        return selfInfo;
    }

    public void setSelfInfo(String selfInfo) {
        this.selfInfo = selfInfo;
    }

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
