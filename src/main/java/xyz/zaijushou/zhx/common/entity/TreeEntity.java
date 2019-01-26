package xyz.zaijushou.zhx.common.entity;

import java.util.List;

/**
 * 树形通用实体
 */
public class TreeEntity<T extends TreeEntity> extends CommonEntity {

    /**
     * 父节点
     */
    private T parent;

    /**
     * 排序
     */
    private String sort;

    /**
     * 子节点列表
     */
    private List<T> children;

    /**
     * 是否选中
     */
    private boolean select;

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

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }
}
