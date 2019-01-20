package xyz.zaijushou.zhx.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.util.Date;

/**
 * 公共实体类
 */
public class CommonEntity {

    /**
     * 主键 自增
     */
    private Integer id;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建人
     */
    private SysUserEntity createUser;

    /**
     * 更新人
     */
    private SysUserEntity updateUser;

    /**
     * 创建时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 删除标志， 0-正常， 1-已删除
     */
    private Integer deleteFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public SysUserEntity getCreateUser() {
        return createUser;
    }

    public void setCreateUser(SysUserEntity createUser) {
        this.createUser = createUser;
    }

    public SysUserEntity getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(SysUserEntity updateUser) {
        this.updateUser = updateUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleteFlag() {
        return deleteFlag;
    }

    public void setDeleteFlag(Integer deleteFlag) {
        this.deleteFlag = deleteFlag;
    }
}
