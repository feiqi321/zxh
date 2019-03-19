package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataBatchEntity extends CommonEntity {

    private int[] ids;
    //批次编号
    private String batchNo;

    private String batchNoFlag;
    private String[] batchNos;
    //委托方
    private String client;

    private String clientMsg;

    private String[] clients;

    private String clientFlag;
    //催款区域
    private String area;

    private String areaMsg;
    //委案日期
    private String caseTime;
    //案件类型
    private String caseType;

    private String caseTypeMsg;
    //预计退案日期
    private String returnTime;
    //目标回款率
    private String targetRate;
    //备注

    private String uploadTime;
    //0 未导入 1未退案 2 已退案  4正常--包含未导入和未退案的
    private Integer batchStatus;

    private int[] batchStatusList;

    private String batchStatusFlag;

    private String statusMsg;
    //真实退案时间
    private String realReturnTime;

    private String creatUser;

    private int userCount;

    private BigDecimal totalAmt;

    private String totalAmtMsg;

    private String startTime;

    private String endTime;

    private String batchRemark;

    private String orderBy;

    private String sort;

    private String creatTime;

    public int[] getBatchStatusList() {
        return batchStatusList;
    }

    public void setBatchStatusList(int[] batchStatusList) {
        this.batchStatusList = batchStatusList;
    }

    public String getBatchStatusFlag() {
        return batchStatusFlag;
    }

    public void setBatchStatusFlag(String batchStatusFlag) {
        this.batchStatusFlag = batchStatusFlag;
    }

    public void setBatchStatus(Integer batchStatus) {
        this.batchStatus = batchStatus;
    }

    public String getTotalAmtMsg() {
        return totalAmtMsg;
    }

    public void setTotalAmtMsg(String totalAmtMsg) {
        this.totalAmtMsg = totalAmtMsg;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getAreaMsg() {
        return areaMsg;
    }

    public void setAreaMsg(String areaMsg) {
        this.areaMsg = areaMsg;
    }

    public String getClientMsg() {
        return clientMsg;
    }

    public void setClientMsg(String clientMsg) {
        this.clientMsg = clientMsg;
    }

    public String getCaseTypeMsg() {
        return caseTypeMsg;
    }

    public void setCaseTypeMsg(String caseTypeMsg) {
        this.caseTypeMsg = caseTypeMsg;
    }

    public int[] getIds() {
        return ids;
    }

    public void setIds(int[] ids) {
        this.ids = ids;
    }

    public String getBatchNoFlag() {
        return batchNoFlag;
    }

    public void setBatchNoFlag(String batchNoFlag) {
        this.batchNoFlag = batchNoFlag;
    }

    public String[] getBatchNos() {
        return batchNos;
    }

    public void setBatchNos(String[] batchNos) {
        this.batchNos = batchNos;
    }

    public String getClientFlag() {
        return clientFlag;
    }

    public void setClientFlag(String clientFlag) {
        this.clientFlag = clientFlag;
    }

    public String[] getClients() {
        return clients;
    }

    public void setClients(String[] clients) {
        this.clients = clients;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public Integer getBatchStatus() {
        return batchStatus;
    }

    public String getBatchRemark() {
        return batchRemark;
    }

    public void setBatchRemark(String batchRemark) {
        this.batchRemark = batchRemark;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(String creatUser) {
        this.creatUser = creatUser;
    }

    public int getUserCount() {
        return userCount;
    }

    public void setUserCount(int userCount) {
        this.userCount = userCount;
    }

    public BigDecimal getTotalAmt() {
        return totalAmt;
    }

    public void setTotalAmt(BigDecimal totalAmt) {
        this.totalAmt = totalAmt;
    }

    public String getRealReturnTime() {
        return realReturnTime;
    }

    public void setRealReturnTime(String realReturnTime) {
        this.realReturnTime = realReturnTime;
    }

    public String getUploadTime() {
        return uploadTime;
    }

    public void setUploadTime(String uploadTime) {
        this.uploadTime = uploadTime;
    }


    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCaseTime() {
        return caseTime;
    }

    public void setCaseTime(String caseTime) {
        this.caseTime = caseTime;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public String getReturnTime() {
        return returnTime;
    }

    public void setReturnTime(String returnTime) {
        this.returnTime = returnTime;
    }

    public String getTargetRate() {
        return targetRate;
    }

    public void setTargetRate(String targetRate) {
        this.targetRate = targetRate;
    }
}
