package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCollectionEntity extends CommonEntity {
    private String caseId;
    private String area;
    private String dept;
    private String odv;
    private String measure;
    private String collectTime;
    private String targetName;
    private String relation;
    private String telType;
    private String mobile;
    private String result;
    private String collectInfo;
    private String method;//谈判方式(催收分类)
    private int repayTime;
    private BigDecimal repayAmt;
    private BigDecimal reduceAmt;
    private int collectStatus;
    private int reduceStatus;
    List<DataCollectionTelEntity> collTelList = new ArrayList<DataCollectionTelEntity>();

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public String getOdv() {
        return odv;
    }

    public void setOdv(String odv) {
        this.odv = odv;
    }

    public String getMeasure() {
        return measure;
    }

    public void setMeasure(String measure) {
        this.measure = measure;
    }

    public String getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(String collectTime) {
        this.collectTime = collectTime;
    }

    public String getTargetName() {
        return targetName;
    }

    public void setTargetName(String targetName) {
        this.targetName = targetName;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getTelType() {
        return telType;
    }

    public void setTelType(String telType) {
        this.telType = telType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getCollectInfo() {
        return collectInfo;
    }

    public void setCollectInfo(String collectInfo) {
        this.collectInfo = collectInfo;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public int getRepayTime() {
        return repayTime;
    }

    public void setRepayTime(int repayTime) {
        this.repayTime = repayTime;
    }

    public BigDecimal getRepayAmt() {
        return repayAmt;
    }

    public void setRepayAmt(BigDecimal repayAmt) {
        this.repayAmt = repayAmt;
    }

    public BigDecimal getReduceAmt() {
        return reduceAmt;
    }

    public void setReduceAmt(BigDecimal reduceAmt) {
        this.reduceAmt = reduceAmt;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public int getReduceStatus() {
        return reduceStatus;
    }

    public void setReduceStatus(int reduceStatus) {
        this.reduceStatus = reduceStatus;
    }

    public List<DataCollectionTelEntity> getCollTelList() {
        return collTelList;
    }

    public void setCollTelList(List<DataCollectionTelEntity> collTelList) {
        this.collTelList = collTelList;
    }
}
