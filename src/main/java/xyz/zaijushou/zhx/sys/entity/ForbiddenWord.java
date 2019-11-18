package xyz.zaijushou.zhx.sys.entity;

/**
 * ForbiddenWord
 */
public class ForbiddenWord {
    private Integer wordid;
    private String word;
    private Integer wordtype;
    private Integer enabled;

    public Integer getWordid() {
        return this.wordid;
    }

    public void setWordid(Integer wordid) {
        this.wordid = wordid;
    }

    public String getWord() {
        return this.word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getWordtype() {
        return this.wordtype;
    }

    public void setWordtype(Integer wordtype) {
        this.wordtype = wordtype;
    }

    public Integer getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Integer enabled) {
        this.enabled = enabled;
    }
}