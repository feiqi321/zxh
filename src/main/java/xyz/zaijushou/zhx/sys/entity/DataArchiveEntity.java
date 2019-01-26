package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataArchiveEntity extends CommonEntity {

    private String name;

    private String identNo;

    private String mobile;

    private String address;

    private List<DataArchiveTelEntity> telList;

    private List<DataArchiveAddressEntity> addressList;

    private String startTime;

    private String endTime;

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
