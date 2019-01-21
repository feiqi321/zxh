package xyz.zaijushou.zhx.sys.entity;

import xyz.zaijushou.zhx.common.entity.CommonEntity;

import java.util.List;

/**
 * 按钮权限实体类
 */
public class SysButtonEntity extends CommonEntity {

    /**
     * 按钮名称
     */
    private String buttonLabel;

    /**
     * 按钮权限标志
     */
    private String buttonAuthSymbol;

    /**
     * 所属菜单
     */
    private SysMenuEntity parentMenu;

    /**
     * 对应权限列表，完成该菜单请求所需要的后台权限
     */
    private List<SysAuthorityEntity> authorityList;

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public String getButtonAuthSymbol() {
        return buttonAuthSymbol;
    }

    public void setButtonAuthSymbol(String buttonAuthSymbol) {
        this.buttonAuthSymbol = buttonAuthSymbol;
    }

    public SysMenuEntity getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(SysMenuEntity parentMenu) {
        this.parentMenu = parentMenu;
    }

    public List<SysAuthorityEntity> getAuthorityList() {
        return authorityList;
    }

    public void setAuthorityList(List<SysAuthorityEntity> authorityList) {
        this.authorityList = authorityList;
    }
}
