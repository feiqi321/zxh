package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * AllForbiddenWords
 */
public class AllForbiddenWords {
    private List<ForbiddenWord> dataCollectionType;
    private List<ForbiddenWord> remarkType;

    public List<ForbiddenWord> getDataCollectionType() {
        return this.dataCollectionType;
    }

    public void setDataCollectionType(List<ForbiddenWord> dataCollectionType) {
        this.dataCollectionType = dataCollectionType;
    }

    public List<ForbiddenWord> getRemarkType() {
        return this.remarkType;
    }

    public void setRemarkType(List<ForbiddenWord> remarkType) {
        this.remarkType = remarkType;
    }
}