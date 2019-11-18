package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * WordsToSaveEntity
 */
public class WordsToSaveEntity {
    private Integer wordtype;
    private List<ForbiddenWord> words;

    public Integer getWordtype() {
        return this.wordtype;
    }

    public void setWordtype(Integer wordtype) {
        this.wordtype = wordtype;
    }

    public List<ForbiddenWord> getWords() {
        return this.words;
    }

    public void setWords(List<ForbiddenWord> words) {
        this.words = words;
    }
}