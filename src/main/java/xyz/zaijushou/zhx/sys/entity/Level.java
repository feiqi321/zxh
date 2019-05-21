package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * @author lsl
 * @version [1.0.0, 2019/5/9,20:56]
 */
public class Level {

    private List<Staffs> level0;

    private List<Staffs> level1;

    private List<Staffs> level2;

    private List<Staffs> level3;

    public List<Staffs> getLevel0() {
        return level0;
    }

    public void setLevel0(List<Staffs> level0) {
        this.level0 = level0;
    }

    public List<Staffs> getLevel1() {
        return level1;
    }

    public void setLevel1(List<Staffs> level1) {
        this.level1 = level1;
    }

    public List<Staffs> getLevel2() {
        return level2;
    }

    public void setLevel2(List<Staffs> level2) {
        this.level2 = level2;
    }

    public List<Staffs> getLevel3() {
        return level3;
    }

    public void setLevel3(List<Staffs> level3) {
        this.level3 = level3;
    }
}
