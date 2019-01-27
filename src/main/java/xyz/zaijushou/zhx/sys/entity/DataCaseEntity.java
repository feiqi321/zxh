package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCaseEntity extends CommonEntity {
    private int newColumn;
    private String batchNo;
    private BigDecimal money;
    private BigDecimal balance;
    private String caseTime;
    private String caseType;
    private String returnTime;
    private BigDecimal rate;
    private String remark;
    private String realReturnTime;
    private int status;

    List<DataCaseAddressEntity> addressList = new ArrayList<DataCaseAddressEntity>();
    List<DataCaseTelEntity> telList = new ArrayList<DataCaseTelEntity>();
    List<DataCaseCommentEntity> commentList = new ArrayList<DataCaseCommentEntity>();
    List<DataCaseInterestEntity> interestList = new ArrayList<DataCaseInterestEntity>();

    public int getNewColumn() {
        return newColumn;
    }

    public void setNewColumn(int newColumn) {
        this.newColumn = newColumn;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRealReturnTime() {
        return realReturnTime;
    }

    public void setRealReturnTime(String realReturnTime) {
        this.realReturnTime = realReturnTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DataCaseAddressEntity> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<DataCaseAddressEntity> addressList) {
        this.addressList = addressList;
    }

    public List<DataCaseTelEntity> getTelList() {
        return telList;
    }

    public void setTelList(List<DataCaseTelEntity> telList) {
        this.telList = telList;
    }

    public List<DataCaseCommentEntity> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<DataCaseCommentEntity> commentList) {
        this.commentList = commentList;
    }

    public List<DataCaseInterestEntity> getInterestList() {
        return interestList;
    }

    public void setInterestList(List<DataCaseInterestEntity> interestList) {
        this.interestList = interestList;
    }
}
