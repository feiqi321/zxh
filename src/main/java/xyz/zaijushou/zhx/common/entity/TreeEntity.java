package xyz.zaijushou.zhx.common.entity;

import java.util.List;

/**
 * 树形通用实体
 */
public class TreeEntity<T> extends CommonEntity {

    /**
     * 父节点
     */
    private T parent;

    /**
     * 子节点列表
     */
    private List<T> children;

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public List<T> getChildren() {
        return children;
    }

    public void setChildren(List<T> children) {
        this.children = children;
    }
}
