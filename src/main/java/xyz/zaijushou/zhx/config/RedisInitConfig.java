package xyz.zaijushou.zhx.config;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataBatchMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.SysConfigMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysAuthorityEntity;
import xyz.zaijushou.zhx.sys.entity.SysButtonEntity;
import xyz.zaijushou.zhx.sys.entity.SysConfig;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysMenuEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysAuthorityService;
import xyz.zaijushou.zhx.sys.service.SysButtonService;
import xyz.zaijushou.zhx.sys.service.SysMenuService;
import xyz.zaijushou.zhx.sys.service.SysRoleService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.RedisUtils;

@Component
public class RedisInitConfig implements ApplicationRunner {
    private static Logger logger = LoggerFactory.getLogger(RedisInitConfig.class);

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

    @Resource
    private SysDictionaryMapper sysDictionaryMapper;

    @Resource
    private SysConfigMapper sysConfigMapper;

    private static final String RedisLoadingCases = "redis.loadingcases";

    @Override
    public void run(ApplicationArguments args) throws Exception {

        List<SysUserEntity> allUser = sysUserService.listAllUsers(new SysUserEntity());

        List<SysMenuEntity> allMenu = sysMenuService.listAllMenus(new SysMenuEntity());

        List<SysButtonEntity> allButton = sysButtonService.listAllButtons(new SysButtonEntity());

        List<SysAuthorityEntity> allAuthority = sysAuthorityService.listAllAuthorities(new SysAuthorityEntity());

        List<SysDictionaryEntity> allDic = sysDictionaryMapper.getDataList(new SysDictionaryEntity());

        List<DataBatchEntity> allBatch = dataBatchMapper.listAllDataBatch(new DataBatchEntity());

        initUserInfo(allUser);
        initMenuInfo(allMenu);
        initButtonInfo(allButton);
        initAuthorityInfo(allAuthority);
        initRoleInfo();
        initDic(allDic);
        initBatch(allBatch);
        initCase();
    }

    private void initDic(List<SysDictionaryEntity> allDic) {
        RedisUtils.refreshDicEntity(allDic, RedisKeyPrefix.SYS_DIC);
    }

    private void initBatch(List<DataBatchEntity> allBatch) {
        RedisUtils.refreshBatchEntity(allBatch, RedisKeyPrefix.DATA_BATCH);
    }

    private void initCase() {
        SysConfig redisConfig = sysConfigMapper.queryConfig(RedisLoadingCases);
        logger.debug("RedisLoadingCases : "+redisConfig.getCfgvalue());
        if(redisConfig.getCfgvalue().equals("0")){
            return;
        }

        RedisUtils.deleteKeysWihtPrefix(RedisKeyPrefix.DATA_CASE);
        Integer maxId = dataCaseMapper.findMaxId();
        if (maxId == null) {
            return;
        }
        int maxCaseNum = 50000;
        int cycleNum = (int) Math.ceil((double) maxId / maxCaseNum);
        for (int i = 0; i < cycleNum; i++) {
            DataCaseEntity DataCaseEntity = new DataCaseEntity();
            DataCaseEntity.setId(i * maxCaseNum);
            DataCaseEntity.setMaxId((i + 1) * maxCaseNum);
            List<DataCaseEntity> allCase = dataCaseMapper.listInitAllCaseInfo(DataCaseEntity);
            RedisUtils.refreshCaseEntity(allCase, RedisKeyPrefix.DATA_CASE);
        }

        sysConfigMapper.updateConfig(RedisLoadingCases, "0");
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
