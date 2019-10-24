package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * @author lsl
 * @version [1.0.0, 2019/8/28,17:10]
 */
public class CollectionStatisticDTO {
    /**
     * 接通电话数
     */

    private Integer countConPhoneNum;

    /**
     * 总通话数
     */
    private Integer countPhoneNum;
    /**
     * 通话涉及到的案件数
     */
    private Integer countCasePhoneNum;

    private Integer countInvalidPhoneNum;

    public Integer getCountInvalidPhoneNum() {
        return countInvalidPhoneNum;
    }

    public void setCountInvalidPhoneNum(Integer countInvalidPhoneNum) {
        this.countInvalidPhoneNum = countInvalidPhoneNum;
    }

    private List<CollectionStatistic> conList;

    public List<CollectionStatistic> getConList() {
        return conList;
    }

    public void setConList(List<CollectionStatistic> conList) {
        this.conList = conList;
    }

    public CollectionStatisticDTO() {
    }

    public CollectionStatisticDTO(Integer countConPhoneNum, Integer countPhoneNum, Integer countCasePhoneNum, Integer countInvalidPhoneNum) {
        this.countConPhoneNum = countConPhoneNum;
        this.countPhoneNum = countPhoneNum;
        this.countCasePhoneNum = countCasePhoneNum;
        this.countInvalidPhoneNum = countInvalidPhoneNum;
    }

    public Integer getCountConPhoneNum() {
        return countConPhoneNum;
    }

    public void setCountConPhoneNum(Integer countConPhoneNum) {
        this.countConPhoneNum = countConPhoneNum;
    }

    public Integer getCountPhoneNum() {
        return countPhoneNum;
    }

    public void setCountPhoneNum(Integer countPhoneNum) {
        this.countPhoneNum = countPhoneNum;
    }

    public Integer getCountCasePhoneNum() {
        return countCasePhoneNum;
    }

    public void setCountCasePhoneNum(Integer countCasePhoneNum) {
        this.countCasePhoneNum = countCasePhoneNum;
    }
}
