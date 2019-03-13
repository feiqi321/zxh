package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.ExcelSynergisticConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCaseSynergisticMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseSynergisticService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataCaseSynergisticServiceImpl implements DataCaseSynergisticService {

    @Resource
    private DataCaseSynergisticMapper dataCaseSynergisticMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysDictionaryMapper sysDictionaryMapper;

    @Resource
    private SysUserService sysUserService;//用户业务控制层

    @Override
    public PageInfo<DataCaseSynergisticEntity> pageSynergisticList(DataCaseSynergisticEntity synergistic) {
        if(synergistic != null && synergistic.getApplyUser() != null && StringUtils.isNotEmpty(synergistic.getApplyUser().getUserName())) {
            List<SysUserEntity> users = sysUserMapper.listUsers(synergistic.getApplyUser());
            if(!CollectionUtils.isEmpty(users)) {
                List<Integer> idsList = new ArrayList<>();
                for(SysUserEntity user : users) {
                    idsList.add(user.getId());
                }
                synergistic.getApplyUser().setIdsList(idsList);
            }
        }
        if(!StringUtils.isEmpty(synergistic.getOrderBy())){
            synergistic.setOrderBy(ExcelSynergisticConstant.SynergisticSortEnum.getEnumByKey(synergistic.getOrderBy()).getValue());
        }
        List<DataCaseSynergisticEntity> synergisticList = dataCaseSynergisticMapper.pageSynergisticList(synergistic);
        for(DataCaseSynergisticEntity entity : synergisticList) {
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getSynergisticType().getId(),SysDictionaryEntity.class);
            entity.setSynergisticType(sysDictionaryEntity);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ entity.getApplyUser().getId(), SysUserEntity.class);
            entity.setApplyUser(user);
            SysUserEntity user2 = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ entity.getSynergisticUser().getId(), SysUserEntity.class);
            SysNewUserEntity sysNewUserEntity = new SysNewUserEntity();
            sysNewUserEntity.setId(user2.getId());
            sysNewUserEntity.setUserName(user2.getUserName());
            entity.setSynergisticUser(sysNewUserEntity);
            if ("0".equals(entity.getApplyStatus()) && "0".equals(entity.getFinishStatus())){
                entity.setStatusMsg("待审核");
            }else if ("1".equals(entity.getApplyStatus()) && "0".equals(entity.getFinishStatus())){
                entity.setStatusMsg("待办");
            }else if ("1".equals(entity.getApplyStatus()) && "1".equals(entity.getFinishStatus())){
                entity.setStatusMsg("已完成");
            }else if ("-1".equals(entity.getApplyStatus())){
                entity.setStatusMsg("已撤销");
            }
            entity.getDataCase().setMoneyMsg(entity.getDataCase().getMoney()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getMoney()+""));
            entity.getDataCase().setRepayMoneyMsg(entity.getDataCase().getRepayMoneyMsg()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(entity.getDataCase().getRepayMoneyMsg()+""));
        }
        return PageInfo.of(synergisticList);
    }

    @Override
    public void approve(DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticMapper.updateApplyStatus(synergistic);
    }
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }
    public void saveApply(DataCaseSynergyDetailEntity bean){
        SysUserEntity curentuser = getUserInfo();
        bean.setApplyer(curentuser.getId());
        dataCaseSynergisticMapper.saveApply(bean);
    }

    public void saveResult(DataCaseSynergyDetailEntity bean){
        dataCaseSynergisticMapper.saveResult(bean);
    }

    @Override
    public void finish(DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticMapper.updateFinishStatus(synergistic);
    }

    @Override
    public List<DataCaseSynergisticEntity> listSynergistic(DataCaseSynergisticEntity synergistic) {
        return dataCaseSynergisticMapper.listSynergistic(synergistic);
    }

    @Override
    public List<SysDictionaryEntity> listAllTypes() {
        SysDictionaryEntity dict = new SysDictionaryEntity();
        dict.setName("催收类型");
        return sysDictionaryMapper.listDataByName(dict);
    }

    @Override
    public List<DataCaseSynergisticEntity> countNum(DataCaseSynergisticEntity synergistic) {
        return dataCaseSynergisticMapper.countNum(synergistic);
    }

    @Override
    public List<DataCaseSynergisticEntity> listByIdsSet(DataCaseSynergisticEntity queryEntity) {
        return dataCaseSynergisticMapper.listByIdsSet(queryEntity);
    }

    @Override
    public void updateInfo(DataCaseSynergisticEntity entity) {
        dataCaseSynergisticMapper.updateInfo(entity);
    }

    @Override
    public void updateInfoByCaseId(DataCaseSynergisticEntity entity) {
        dataCaseSynergisticMapper.updateInfoByCaseId(entity);
    }
}
