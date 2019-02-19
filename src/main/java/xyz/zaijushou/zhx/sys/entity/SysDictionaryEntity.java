package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.common.entity.TreeEntity;

/**
 * 权限实体类
 */
public class SysDictionaryEntity extends TreeEntity<SysDictionaryEntity> {

    /**
     * 名称
     */
    private String name;

    private String currentName;

    /**
     * 描述
     */
    private String description;

    /**
     * 0停用 1启用
     */
    private Integer status;

    /**
     * 1-地区，0-其他
     */
    private Integer type;

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}
