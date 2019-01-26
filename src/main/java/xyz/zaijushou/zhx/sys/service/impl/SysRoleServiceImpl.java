package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysRoleMapper;
import xyz.zaijushou.zhx.sys.entity.SysRoleEntity;
import xyz.zaijushou.zhx.sys.entity.SysToRoleButton;
import xyz.zaijushou.zhx.sys.entity.SysToRoleMenu;
import xyz.zaijushou.zhx.sys.service.SysRoleService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysRoleServiceImpl implements SysRoleService {

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Override
    public List<SysRoleEntity> listAllRoles(SysRoleEntity sysRoleEntity) {
        return sysRoleMapper.listAllRoles(sysRoleEntity);
    }

    @Override
    public List<SysToRoleMenu> listAllRoleMenus(SysToRoleMenu sysToRoleMenu) {
        return sysRoleMapper.listAllRoleMenus(sysToRoleMenu);
    }

    @Override
    public List<SysToRoleButton> listAllRoleButtons(SysToRoleButton sysToRoleButton) {
        return sysRoleMapper.listAllRoleButtons(sysToRoleButton);
    }

    @Override
    public void updateRole(SysRoleEntity roleEntity) {
        sysRoleMapper.updateRole(roleEntity);
    }

    @Override
    public void saveRole(SysRoleEntity roleEntity) {
        sysRoleMapper.saveRole(roleEntity);
    }

    @Override
    public void deleteRole(SysRoleEntity roleEntity) {
        sysRoleMapper.deleteRole(roleEntity);
    }

    @Override
    public SysRoleEntity selectByRoleName(SysRoleEntity roleEntity) {
        return sysRoleMapper.selectByRoleName(roleEntity);
    }
}
