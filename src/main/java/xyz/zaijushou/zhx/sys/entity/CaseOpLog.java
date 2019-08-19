package xyz.zaijushou.zhx.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Date;

/**
 * Created by looyer on 2019/8/15.
 */
public class CaseOpLog extends CommonEntity {

    private String context;

    private String lastOdv;

    private Integer caseId;

    private Integer creator;

    private String creatorName;

    private String client;

    private String seqNo;

    private String contractNo;

    private String name;

    private String identNo;

    private Integer status; //案件状态
    private String statusMsg;//案件状态中文说明

    private int collectStatus; //催收状态   列表
    private String collectStatusMsg;//催收状态中文说明

    private String opType;

    private String odv;

    private String dept;

    private String[] clients;
    private String[] seqNos;
    private String[] identNos;
    private String[] names;
    private Integer[] statuss;
    private Integer[] collectStatuss;
    private Integer[] depts;
    private String[] odvs;
    private String createTimeStart;
    private String createTimeEnd;
    private Integer[] creators;
    private String caseDateStart;
    private String caseDateEnd;
    private String expectStartTime;
    private String expectEndTime;

    private String sort;
    private String orderBy;

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getExpectStartTime() {
        return expectStartTime;
    }

    public void setExpectStartTime(String expectStartTime) {
        this.expectStartTime = expectStartTime;
    }

    public String getExpectEndTime() {
        return expectEndTime;
    }

    public void setExpectEndTime(String expectEndTime) {
        this.expectEndTime = expectEndTime;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String[] getClients() {
        return clients;
    }

    public void setClients(String[] clients) {
        this.clients = clients;
    }

    public String[] getSeqNos() {
        return seqNos;
    }

    public void setSeqNos(String[] seqNos) {
        this.seqNos = seqNos;
    }

    public String[] getIdentNos() {
        return identNos;
    }

    public void setIdentNos(String[] identNos) {
        this.identNos = identNos;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public Integer[] getStatuss() {
        return statuss;
    }

    public void setStatuss(Integer[] statuss) {
        this.statuss = statuss;
    }

    public Integer[] getCollectStatuss() {
        return collectStatuss;
    }

    public void setCollectStatuss(Integer[] collectStatuss) {
        this.collectStatuss = collectStatuss;
    }

    public Integer[] getDepts() {
        return depts;
    }

    public void setDepts(Integer[] depts) {
        this.depts = depts;
    }

    public String[] getOdvs() {
        return odvs;
    }

    public void setOdvs(String[] odvs) {
        this.odvs = odvs;
    }

    public String getCreateTimeStart() {
        return createTimeStart;
    }

    public void setCreateTimeStart(String createTimeStart) {
        this.createTimeStart = createTimeStart;
    }

    public String getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setCreateTimeEnd(String createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Integer[] getCreators() {
        return creators;
    }

    public void setCreators(Integer[] creators) {
        this.creators = creators;
    }

    public String getCaseDateStart() {
        return caseDateStart;
    }

    public void setCaseDateStart(String caseDateStart) {
        this.caseDateStart = caseDateStart;
    }

    public String getCaseDateEnd() {
        return caseDateEnd;
    }

    public void setCaseDateEnd(String caseDateEnd) {
        this.caseDateEnd = caseDateEnd;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getLastOdv() {
        return lastOdv;
    }

    public void setLastOdv(String lastOdv) {
        this.lastOdv = lastOdv;
    }

    public Integer getCreator() {
        return creator;
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSeqNo() {
        return seqNo;
    }

    public void setSeqNo(String seqNo) {
        this.seqNo = seqNo;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public int getCollectStatus() {
        return collectStatus;
    }

    public void setCollectStatus(int collectStatus) {
        this.collectStatus = collectStatus;
    }

    public String getCollectStatusMsg() {
        return collectStatusMsg;
    }

    public void setCollectStatusMsg(String collectStatusMsg) {
        this.collectStatusMsg = collectStatusMsg;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getOdv() {
        return odv;
    }

    public void setOdv(String odv) {
        this.odv = odv;
    }

    public String getDept() {
        return dept;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }
}
