package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.List;

/**
 * 菜单实体类
 */
public class SysMenuEntity extends CommonEntity {

    /**
     * 菜单名称
     */
    private String menuLabel;

    /**
     * 父菜单
     */
    private SysMenuEntity parent;

    /**
     * 菜单url
     */
    private String menuUrl;

    /**
     * 菜单层级
     */
    private String menuLevel;

    /**
     * 是否叶子节点
     */
    private Integer leafNode;

    /**
     * 菜单权限标志
     */
    private String menuAuthSymbol;

    /**
     * 菜单图标
     */
    private String menuIcon;

    /**
     * 排序
     */
    private String sort;

    /**
     * 子菜单列表
     */
    private List<SysMenuEntity> children;

    /**
     * 按钮列表
     */
    private List<SysButtonEntity> buttonList;

    public String getMenuLabel() {
        return menuLabel;
    }

    public void setMenuLabel(String menuLabel) {
        this.menuLabel = menuLabel;
    }

    public SysMenuEntity getParent() {
        return parent;
    }

    public void setParent(SysMenuEntity parent) {
        this.parent = parent;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuLevel() {
        return menuLevel;
    }

    public void setMenuLevel(String menuLevel) {
        this.menuLevel = menuLevel;
    }

    public Integer getLeafNode() {
        return leafNode;
    }

    public void setLeafNode(Integer leafNode) {
        this.leafNode = leafNode;
    }

    public String getMenuAuthSymbol() {
        return menuAuthSymbol;
    }

    public void setMenuAuthSymbol(String menuAuthSymbol) {
        this.menuAuthSymbol = menuAuthSymbol;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public List<SysMenuEntity> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuEntity> children) {
        this.children = children;
    }

    public List<SysButtonEntity> getButtonList() {
        return buttonList;
    }

    public void setButtonList(List<SysButtonEntity> buttonList) {
        this.buttonList = buttonList;
    }
}
