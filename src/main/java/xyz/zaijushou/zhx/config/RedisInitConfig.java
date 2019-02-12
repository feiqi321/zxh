package xyz.zaijushou.zhx.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import xyz.zaijushou.zhx.sys.dao.DataBatchMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.*;

import javax.annotation.Resource;
import java.util.*;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.utils.RedisUtils;

@Component
public class RedisInitConfig implements ApplicationRunner {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysButtonService sysButtonService;

    @Resource
    private SysAuthorityService sysAuthorityService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private DataBatchMapper dataBatchMapper;

    @Resource
    private DataCaseMapper dataCaseMapper;


    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<SysUserEntity> allUser = sysUserService.listAllUsers(new SysUserEntity());

        List<SysMenuEntity> allMenu = sysMenuService.listAllMenus(new SysMenuEntity());

        List<SysButtonEntity> allButton = sysButtonService.listAllButtons(new SysButtonEntity());

        List<SysAuthorityEntity> allAuthority = sysAuthorityService.listAllAuthorities(new SysAuthorityEntity());

        List<DataBatchEntity> allBatch = dataBatchMapper.listAllDataBatch(new DataBatchEntity());

        List<DataCaseEntity> allCase = dataCaseMapper.listAllCaseInfo(new DataCaseEntity());

        initUserInfo(allUser);
        initMenuInfo(allMenu);
        initButtonInfo(allButton);
        initAuthorityInfo(allAuthority);
        initRoleInfo();
        initBatch(allBatch);
        initCase(allCase);
    }

    private void initBatch(List<DataBatchEntity> allBatch) {
        RedisUtils.refreshBatchEntity(allBatch, RedisKeyPrefix.DATA_BATCH);
    }

    private void initCase(List<DataCaseEntity> allCase) {
        RedisUtils.refreshCaseEntity(allCase, RedisKeyPrefix.DATA_CASE);
    }

    private void initUserInfo(List<SysUserEntity> allUser) {
        RedisUtils.refreshCommonEntityWithId(allUser, RedisKeyPrefix.USER_INFO);
    }

    private void initMenuInfo(List<SysMenuEntity> allMenu) {
        RedisUtils.refreshCommonEntityWithId(allMenu, RedisKeyPrefix.MENU_INFO);
    }

    private void initButtonInfo(List<SysButtonEntity> allButton) {
        RedisUtils.refreshCommonEntityWithId(allButton, RedisKeyPrefix.BUTTON_INFO);
    }

    private void initAuthorityInfo(List<SysAuthorityEntity> allAuthority) {
        RedisUtils.refreshCommonEntityWithId(allAuthority, RedisKeyPrefix.AUTHORITY_INFO);
    }

    private void initRoleInfo() {
        sysRoleService.refreshRoleRedis();
    }

}
