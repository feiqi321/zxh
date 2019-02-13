package xyz.zaijushou.zhx.sys.entity;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
public class CollectionReturnEntity extends CommonEntity {
    List<DataCollectionEntity> list = new ArrayList<DataCollectionEntity>();
    private int countCase;//列表案量
    private BigDecimal sumMoney;//列表金额
    private int countCasePay;//列表还款案量
    private BigDecimal sumPayMoney;//列表还款数额
    private BigDecimal sumRepay;//列表CP值
    private BigDecimal sumBank;//列表PTP值
    private int pages; //总页数
    private int total;//总数量

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<DataCollectionEntity> getList() {
        return list;
    }

    public void setList(List<DataCollectionEntity> list) {
        this.list = list;
    }

    public int getCountCase() {
        return countCase;
    }

    public void setCountCase(int countCase) {
        this.countCase = countCase;
    }

    public BigDecimal getSumMoney() {
        return sumMoney;
    }

    public void setSumMoney(BigDecimal sumMoney) {
        this.sumMoney = sumMoney;
    }

    public int getCountCasePay() {
        return countCasePay;
    }

    public void setCountCasePay(int countCasePay) {
        this.countCasePay = countCasePay;
    }

    public BigDecimal getSumPayMoney() {
        return sumPayMoney;
    }

    public void setSumPayMoney(BigDecimal sumPayMoney) {
        this.sumPayMoney = sumPayMoney;
    }

    public BigDecimal getSumRepay() {
        return sumRepay;
    }

    public void setSumRepay(BigDecimal sumRepay) {
        this.sumRepay = sumRepay;
    }

    public BigDecimal getSumBank() {
        return sumBank;
    }

    public void setSumBank(BigDecimal sumBank) {
        this.sumBank = sumBank;
    }
}
