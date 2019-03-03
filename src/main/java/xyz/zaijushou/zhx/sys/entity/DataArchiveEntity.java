package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataArchiveEntity extends CommonEntity {
    //姓名
    private String name;

    private String identNo;

    private String cardNo;

    private String addressType;

    private String mobile;

    private String address;

    private String remark;

    private String caseDate;

    private List<DataArchiveTelEntity> telList;

    private List<DataArchiveAddressEntity> addressList;

    private List<DataArchiveRemarkEntity> remarkList;

    private String startTime;

    private String endTime;
    //信息类型
    private String msgType;
    //信息内容
    private String msgContext;

    private String orderBy;

    private String sort;

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCaseDate() {
        return caseDate;
    }

    public void setCaseDate(String caseDate) {
        this.caseDate = caseDate;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public List<DataArchiveRemarkEntity> getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(List<DataArchiveRemarkEntity> remarkList) {
        this.remarkList = remarkList;
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

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getMsgContext() {
        return msgContext;
    }

    public void setMsgContext(String msgContext) {
        this.msgContext = msgContext;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }

    public List<DataArchiveTelEntity> getTelList() {
        return telList;
    }

    public void setTelList(List<DataArchiveTelEntity> telList) {
        this.telList = telList;
    }

    public List<DataArchiveAddressEntity> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<DataArchiveAddressEntity> addressList) {
        this.addressList = addressList;
    }
}
