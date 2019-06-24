package xyz.zaijushou.zhx.sys.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 协催实体
 */
public class DataCaseSynergisticEntity extends CommonEntity {

    private DataCaseEntity dataCase;    //所属案件

    private SysDictionaryEntity synergisticType;    //协催类型

    private String applyContent;    //申请内容

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date applyTime; //申请时间

    private SysUserEntity applyUser; //申请人

    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date synergisticTime;   //协催时间

    private SysNewUserEntity synergisticUser;   //协催人

    private String synergisticResult;   //协催结果

    private String applyStatus; //申请状态 0 待审批，1 同意协催，-1 撤销协催

    private String finishStatus;    //完成状态 0-未完成，1-已完成

    private String statusMsg;

    private Integer[] ids;  //id列表集合

    private Date applyTimeStart;    //申请时间 start

    private Date applyTimeEnd;  //申请时间 end

    private Integer countNum;   //统计数量，用于页面显示协催类型数量

    private Set<Integer> idsSet;

    private String orderBy;

    private String sort;

    private Map exportConf;

    private List exportKeyList;
    /**
     * 协催类型
     */
    private String[] names;

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

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

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public DataCaseEntity getDataCase() {
        return dataCase;
    }

    public void setDataCase(DataCaseEntity dataCase) {
        this.dataCase = dataCase;
    }

    public SysDictionaryEntity getSynergisticType() {
        return synergisticType;
    }

    public void setSynergisticType(SysDictionaryEntity synergisticType) {
        this.synergisticType = synergisticType;
    }

    public String getApplyContent() {
        return applyContent;
    }

    public void setApplyContent(String applyContent) {
        this.applyContent = applyContent;
    }

    public Date getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
    }

    public SysUserEntity getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(SysUserEntity applyUser) {
        this.applyUser = applyUser;
    }

    public Date getSynergisticTime() {
        return synergisticTime;
    }

    public void setSynergisticTime(Date synergisticTime) {
        this.synergisticTime = synergisticTime;
    }

    public SysNewUserEntity getSynergisticUser() {
        return synergisticUser;
    }

    public void setSynergisticUser(SysNewUserEntity synergisticUser) {
        this.synergisticUser = synergisticUser;
    }

    public String getSynergisticResult() {
        return synergisticResult;
    }

    public void setSynergisticResult(String synergisticResult) {
        this.synergisticResult = synergisticResult;
    }

    public String getApplyStatus() {
        return applyStatus;
    }

    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getFinishStatus() {
        return finishStatus;
    }

    public void setFinishStatus(String finishStatus) {
        this.finishStatus = finishStatus;
    }

    public Integer[] getIds() {
        return ids;
    }

    public void setIds(Integer[] ids) {
        this.ids = ids;
    }

    public Date getApplyTimeStart() {
        return applyTimeStart;
    }

    public void setApplyTimeStart(Date applyTimeStart) {
        this.applyTimeStart = applyTimeStart;
    }

    public Date getApplyTimeEnd() {
        return applyTimeEnd;
    }

    public void setApplyTimeEnd(Date applyTimeEnd) {
        this.applyTimeEnd = applyTimeEnd;
    }

    public Integer getCountNum() {
        return countNum;
    }

    public void setCountNum(Integer countNum) {
        this.countNum = countNum;
    }

    public Set<Integer> getIdsSet() {
        return idsSet;
    }

    public void setIdsSet(Set<Integer> idsSet) {
        this.idsSet = idsSet;
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
