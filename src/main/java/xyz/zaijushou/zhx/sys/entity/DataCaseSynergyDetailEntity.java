package xyz.zaijushou.zhx.sys.entity;

/**
 * Created by looyer on 2019/3/10.
 */
public class DataCaseSynergyDetailEntity {

    private int id;

    private int caseId;

    private int opType;

    private int synergisticType;

    private String synergisticResult;

    private int synergisticUser;

    private String applyContent;

    private int applyer;

    private String synergisticApplyStatus;

    private String synergisticFinishStatus;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getSynergisticUser() {
        return synergisticUser;
    }

    public void setSynergisticUser(int synergisticUser) {
        this.synergisticUser = synergisticUser;
    }

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSynergisticType() {
        return synergisticType;
    }

    public void setSynergisticType(int synergisticType) {
        this.synergisticType = synergisticType;
    }

    public String getSynergisticResult() {
        return synergisticResult;
    }

    public void setSynergisticResult(String synergisticResult) {
        this.synergisticResult = synergisticResult;
    }

    public String getApplyContent() {
        return applyContent;
    }

    public void setApplyContent(String applyContent) {
        this.applyContent = applyContent;
    }

    public int getApplyer() {
        return applyer;
    }

    public void setApplyer(int applyer) {
        this.applyer = applyer;
    }

    public String getSynergisticApplyStatus() {
        return synergisticApplyStatus;
    }

    public void setSynergisticApplyStatus(String synergisticApplyStatus) {
        this.synergisticApplyStatus = synergisticApplyStatus;
    }

    public String getSynergisticFinishStatus() {
        return synergisticFinishStatus;
    }

    public void setSynergisticFinishStatus(String synergisticFinishStatus) {
        this.synergisticFinishStatus = synergisticFinishStatus;
    }
}
