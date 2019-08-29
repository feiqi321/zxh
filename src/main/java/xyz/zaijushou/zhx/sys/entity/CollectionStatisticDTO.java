package xyz.zaijushou.zhx.sys.entity;

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
    /**
     * 催收区域
     */
    private String area;

    public CollectionStatisticDTO() {
    }

    public CollectionStatisticDTO(Integer countConPhoneNum, Integer countPhoneNum, Integer countCasePhoneNum) {
        this.countConPhoneNum = countConPhoneNum;
        this.countPhoneNum = countPhoneNum;
        this.countCasePhoneNum = countCasePhoneNum;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
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
