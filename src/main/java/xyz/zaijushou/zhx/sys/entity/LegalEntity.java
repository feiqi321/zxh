package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;

public class LegalEntity extends CommonEntity {
    //姓名
    private String cstName;
    //案件状态
    private Integer legalStatus;
    //案件状态说明
    private String legalStatusMsg;
    //证件号码
    private String identNo;
    //办案进度
    private String progress;
    //案件類型
    private Integer legalType;
    //标的
    private String tital;
    //委托人
    private String clientele;
    //被告人
    private String accused;
    //所属人
    private String owner;
    //代理律师
    private String agent;
    //律师联系方式
    private String agentTel;
    //立案日期
    private String filingDate;
    //委案日期
    private String legalDate;

    private BigDecimal cost;
    //法官联系方式
    private String judgeTel;
    //办案法官
    private String judge;
    //受案法院
    private String court;
    //诉讼费缴纳日期
    private String costDate;
    //保全费缴纳日期
    private String preservationDate;
    //保全资产清单
    private String preservationList;
    //执行终结日期
    private String exeEndDate;
    //申请执行日期
    private String exeDate;
    //案号
    private String legalNo;
    //申请执行案号
    private String exeNo;
    //首次开庭日期
    private String firstDate;
    //判决日期
    private String judgeDate;
    //送达情况
    private String arriveInfo;
    //判决书
    private String judgment;
    //备注
    private String remark;

    private Date createTime;

    private String checker;

    private String checkDate;

    public String getLegalStatusMsg() {
        return legalStatusMsg;
    }

    public void setLegalStatusMsg(String legalStatusMsg) {
        this.legalStatusMsg = legalStatusMsg;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getCheckDate() {
        return checkDate;
    }

    public void setCheckDate(String checkDate) {
        this.checkDate = checkDate;
    }

    public String getCstName() {
        return cstName;
    }

    public void setCstName(String cstName) {
        this.cstName = cstName;
    }

    public Integer getLegalStatus() {
        return legalStatus;
    }

    public void setLegalStatus(Integer legalStatus) {
        this.legalStatus = legalStatus;
    }

    public String getIdentNo() {
        return identNo;
    }

    public void setIdentNo(String identNo) {
        this.identNo = identNo;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public Integer getLegalType() {
        return legalType;
    }

    public void setLegalType(Integer legalType) {
        this.legalType = legalType;
    }

    public String getTital() {
        return tital;
    }

    public void setTital(String tital) {
        this.tital = tital;
    }

    public String getClientele() {
        return clientele;
    }

    public void setClientele(String clientele) {
        this.clientele = clientele;
    }

    public String getAccused() {
        return accused;
    }

    public void setAccused(String accused) {
        this.accused = accused;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getAgentTel() {
        return agentTel;
    }

    public void setAgentTel(String agentTel) {
        this.agentTel = agentTel;
    }

    public String getFilingDate() {
        return filingDate;
    }

    public void setFilingDate(String filingDate) {
        this.filingDate = filingDate;
    }

    public String getLegalDate() {
        return legalDate;
    }

    public void setLegalDate(String legalDate) {
        this.legalDate = legalDate;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getJudgeTel() {
        return judgeTel;
    }

    public void setJudgeTel(String judgeTel) {
        this.judgeTel = judgeTel;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    public String getCourt() {
        return court;
    }

    public void setCourt(String court) {
        this.court = court;
    }

    public String getCostDate() {
        return costDate;
    }

    public void setCostDate(String costDate) {
        this.costDate = costDate;
    }

    public String getPreservationDate() {
        return preservationDate;
    }

    public void setPreservationDate(String preservationDate) {
        this.preservationDate = preservationDate;
    }

    public String getPreservationList() {
        return preservationList;
    }

    public void setPreservationList(String preservationList) {
        this.preservationList = preservationList;
    }

    public String getExeEndDate() {
        return exeEndDate;
    }

    public void setExeEndDate(String exeEndDate) {
        this.exeEndDate = exeEndDate;
    }

    public String getExeDate() {
        return exeDate;
    }

    public void setExeDate(String exeDate) {
        this.exeDate = exeDate;
    }

    public String getLegalNo() {
        return legalNo;
    }

    public void setLegalNo(String legalNo) {
        this.legalNo = legalNo;
    }

    public String getExeNo() {
        return exeNo;
    }

    public void setExeNo(String exeNo) {
        this.exeNo = exeNo;
    }

    public String getFirstDate() {
        return firstDate;
    }

    public void setFirstDate(String firstDate) {
        this.firstDate = firstDate;
    }

    public String getJudgeDate() {
        return judgeDate;
    }

    public void setJudgeDate(String judgeDate) {
        this.judgeDate = judgeDate;
    }

    public String getArriveInfo() {
        return arriveInfo;
    }

    public void setArriveInfo(String arriveInfo) {
        this.arriveInfo = arriveInfo;
    }

    public String getJudgment() {
        return judgment;
    }

    public void setJudgment(String judgment) {
        this.judgment = judgment;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}