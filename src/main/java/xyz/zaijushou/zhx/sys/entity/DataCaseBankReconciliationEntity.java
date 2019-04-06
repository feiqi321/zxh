package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 银行对账实体
 */
public class DataCaseBankReconciliationEntity extends CommonEntity {

    private DataCaseEntity dataCase;    //所属案件

    private BigDecimal cpMoney; //CP金额

    private String cpMoneyMsg;

    @JSONField(format="yyyy-MM-dd")
    private Date cpDate;    //CP日期

    private String repayUser;   //还款人

    private String repayType;   //还款方式

    private String repayDate;

    private String confirmMoneyMsg;

    private String confirmDate;

    private SysNewUserEntity submitUser;    //提交人

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date submitTime;    //提交时间

    private String status;  //对账状态

    private Date submitTimeStart;   //提交时间 start

    private Date submitTimeEnd; //提交时间 end

    private Integer[] ids;

    private String orderBy;

    private String sort;

    private Map exportConf;

    private List exportKeyList;

    public Map getExportConf() {
        return exportConf;
    }

    public void setExportConf(Map exportConf) {
        this.exportConf = exportConf;
    }

    public List getExportKeyList() {
        return exportKeyList;
    }

    public void setExportKeyList(List exportKeyList) {
        this.exportKeyList = exportKeyList;
    }

    public String getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(String repayDate) {
        this.repayDate = repayDate;
    }

    public String getConfirmMoneyMsg() {
        return confirmMoneyMsg;
    }

    public void setConfirmMoneyMsg(String confirmMoneyMsg) {
        this.confirmMoneyMsg = confirmMoneyMsg;
    }

    public String getConfirmDate() {
        return confirmDate;
    }

    public void setConfirmDate(String confirmDate) {
        this.confirmDate = confirmDate;
    }

    public String getCpMoneyMsg() {
        return cpMoneyMsg;
    }

    public void setCpMoneyMsg(String cpMoneyMsg) {
        this.cpMoneyMsg = cpMoneyMsg;
    }

    public DataCaseEntity getDataCase() {
        return dataCase;
    }

    public void setDataCase(DataCaseEntity dataCase) {
        this.dataCase = dataCase;
    }

    public BigDecimal getCpMoney() {
        return cpMoney;
    }

    public void setCpMoney(BigDecimal cpMoney) {
        this.cpMoney = cpMoney;
    }

    public Date getCpDate() {
        return cpDate;
    }

    public void setCpDate(Date cpDate) {
        this.cpDate = cpDate;
    }

    public String getRepayUser() {
        return repayUser;
    }

    public void setRepayUser(String repayUser) {
        this.repayUser = repayUser;
    }

    public String getRepayType() {
        return repayType;
    }

    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    public SysNewUserEntity getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(SysNewUserEntity submitUser) {
        this.submitUser = submitUser;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getSubmitTimeStart() {
        return submitTimeStart;
    }

    public void setSubmitTimeStart(Date submitTimeStart) {
        this.submitTimeStart = submitTimeStart;
    }

    public Date getSubmitTimeEnd() {
        return submitTimeEnd;
    }

    public void setSubmitTimeEnd(Date submitTimeEnd) {
        this.submitTimeEnd = submitTimeEnd;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
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
}
