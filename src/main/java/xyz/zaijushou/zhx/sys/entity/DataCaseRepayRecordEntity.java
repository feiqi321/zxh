package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 还款记录实体
 */
public class DataCaseRepayRecordEntity extends CommonEntity {

    private DataCaseEntity dataCase;    //所属案件

    private DataCaseBankReconciliationEntity bankReconciliation;    //银行对账

    private SysNewUserEntity collectUser;   //回收催收员

    @JSONField(format="yyyy-MM-dd")
    private Date repayDate; //还款日期

    private BigDecimal repayMoney;  //还款金额

    private String repayMoneyMsg;

    private String repayUser;   //还款人

    private SysDictionaryEntity repayType;  //还款方式

    private SysNewUserEntity confirmUser;   //确认人

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date confirmTime;   //确认时间

    private String recordStatus;    //还款记录状态

    private Date confirmTimeStart;  //确认时间start

    private Date confirmTimeEnd;    //确认时间end

    private Date repayDateStart;    //还款日期 start

    private Date repayDateEnd;  //还款日期 end

    private BigDecimal latestOverdue;   //最新欠款

    private String settleFlag;  //结清标志


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

    public String getRepayMoneyMsg() {
        return repayMoneyMsg;
    }

    public void setRepayMoneyMsg(String repayMoneyMsg) {
        this.repayMoneyMsg = repayMoneyMsg;
    }

    public DataCaseEntity getDataCase() {
        return dataCase;
    }

    public void setDataCase(DataCaseEntity dataCase) {
        this.dataCase = dataCase;
    }

    public DataCaseBankReconciliationEntity getBankReconciliation() {
        return bankReconciliation;
    }

    public void setBankReconciliation(DataCaseBankReconciliationEntity bankReconciliation) {
        this.bankReconciliation = bankReconciliation;
    }

    public SysNewUserEntity getCollectUser() {
        return collectUser;
    }

    public void setCollectUser(SysNewUserEntity collectUser) {
        this.collectUser = collectUser;
    }

    public Date getRepayDate() {
        return repayDate;
    }

    public void setRepayDate(Date repayDate) {
        this.repayDate = repayDate;
    }

    public BigDecimal getRepayMoney() {
        return repayMoney;
    }

    public void setRepayMoney(BigDecimal repayMoney) {
        this.repayMoney = repayMoney;
    }

    public String getRepayUser() {
        return repayUser;
    }

    public void setRepayUser(String repayUser) {
        this.repayUser = repayUser;
    }

    public SysDictionaryEntity getRepayType() {
        return repayType;
    }

    public void setRepayType(SysDictionaryEntity repayType) {
        this.repayType = repayType;
    }

    public SysNewUserEntity getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(SysNewUserEntity confirmUser) {
        this.confirmUser = confirmUser;
    }

    public Date getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(Date confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getRecordStatus() {
        return recordStatus;
    }

    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    public Date getConfirmTimeStart() {
        return confirmTimeStart;
    }

    public void setConfirmTimeStart(Date confirmTimeStart) {
        this.confirmTimeStart = confirmTimeStart;
    }

    public Date getConfirmTimeEnd() {
        return confirmTimeEnd;
    }

    public void setConfirmTimeEnd(Date confirmTimeEnd) {
        this.confirmTimeEnd = confirmTimeEnd;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public Date getRepayDateStart() {
        return repayDateStart;
    }

    public void setRepayDateStart(Date repayDateStart) {
        this.repayDateStart = repayDateStart;
    }

    public Date getRepayDateEnd() {
        return repayDateEnd;
    }

    public void setRepayDateEnd(Date repayDateEnd) {
        this.repayDateEnd = repayDateEnd;
    }

    public BigDecimal getLatestOverdue() {
        return latestOverdue;
    }

    public void setLatestOverdue(BigDecimal latestOverdue) {
        this.latestOverdue = latestOverdue;
    }

    public String getSettleFlag() {
        return settleFlag;
    }

    public void setSettleFlag(String settleFlag) {
        this.settleFlag = settleFlag;
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
