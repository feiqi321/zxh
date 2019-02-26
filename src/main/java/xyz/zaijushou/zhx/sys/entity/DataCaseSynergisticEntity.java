package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.Date;

/**
 * 协催实体
 */
public class DataCaseSynergisticEntity extends CommonEntity {

    private DataCaseEntity dataCase;    //所属案件

    private SysDictionaryEntity synergisticType;    //协催类型

    private String applyContent;    //申请内容

    private Date applyTime; //申请时间

    private SysNewUserEntity applyUser; //申请人

    private Date synergisticTime;   //协催时间

    private SysNewUserEntity synergisticUser;   //协催人

    private String synergisticResult;   //协催结果

    private String applyStatus; //申请状态

    private String finishStatus;    //完成状态

    private Integer ids[];  //id列表集合

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

    public SysNewUserEntity getApplyUser() {
        return applyUser;
    }

    public void setApplyUser(SysNewUserEntity applyUser) {
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
}
