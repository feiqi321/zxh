package xyz.zaijushou.zhx.sys.entity;

import java.util.List;

/**
 * Created by looyer on 2019/3/27.
 */
public class UserTree {

    private int id;

    private String name;

    private String type;

    private List<UserTree> children;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<UserTree> getChildren() {
        return children;
    }

    public void setChildren(List<UserTree> children) {
        this.children = children;
    }
}
