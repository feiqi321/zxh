package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * Created by looyer on 2019/2/17.
 */
public class LegalDetail {

    private LegalEntity legalEntity;

    private List<LegalHandle> handleList ;

    private List<LegalFee> feeList;

    public List<LegalHandle> getHandleList() {
        return handleList;
    }

    public void setHandleList(List<LegalHandle> handleList) {
        this.handleList = handleList;
    }

    public List<LegalFee> getFeeList() {
        return feeList;
    }

    public void setFeeList(List<LegalFee> feeList) {
        this.feeList = feeList;
    }

    public LegalEntity getLegalEntity() {
        return legalEntity;
    }

    public void setLegalEntity(LegalEntity legalEntity) {
        this.legalEntity = legalEntity;
    }
}
