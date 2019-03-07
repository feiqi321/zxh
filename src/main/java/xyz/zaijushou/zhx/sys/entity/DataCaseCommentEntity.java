package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;

/**
 * Created by looyer on 2019/1/25.
 */
public class DataCaseCommentEntity extends CommonEntity {
    private int caseId;
    private String comment;
    private String commentColor;
    private int creatUser;
    private String creatUserName;

    public String getCommentColor() {
        return commentColor;
    }

    public void setCommentColor(String commentColor) {
        this.commentColor = commentColor;
    }

    public String getCreatUserName() {
        return creatUserName;
    }

    public void setCreatUserName(String creatUserName) {
        this.creatUserName = creatUserName;
    }

    public int getCreatUser() {
        return creatUser;
    }

    public void setCreatUser(int creatUser) {
        this.creatUser = creatUser;
    }

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
