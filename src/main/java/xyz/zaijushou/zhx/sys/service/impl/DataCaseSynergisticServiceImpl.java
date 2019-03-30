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
        if(synergistic.getDataCase()==null){
            DataCaseEntity dataCaseEntity = new DataCaseEntity();
            dataCaseEntity.setClientFlag(null);
            dataCaseEntity.setBatchNoFlag(null);
            dataCaseEntity.setNameFlag(null);
            dataCaseEntity.setIdentNoFlag(null);
            dataCaseEntity.setSeqNoFlag(null);
            synergistic.setDataCase(dataCaseEntity);
        }else {
            String[] clients = synergistic.getDataCase().getClients();
            if (clients == null || clients.length == 0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])) {
                synergistic.getDataCase().setClientFlag(null);
            } else {
                synergistic.getDataCase().setClientFlag("1");
            }
            String[] batchNos = synergistic.getDataCase().getBatchNos();
            if (batchNos == null || batchNos.length == 0 || org.apache.commons.lang3.StringUtils.isEmpty(batchNos[0])) {
                synergistic.getDataCase().setBatchNoFlag(null);
            } else {
                synergistic.getDataCase().setBatchNoFlag("1");
            }

            String[] names = synergistic.getDataCase().getNames();
            if (names == null || names.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(names[0])){
                synergistic.getDataCase().setNameFlag(null);
            }else {
                synergistic.getDataCase().setNameFlag("1");
            }

            String[] identNos = synergistic.getDataCase().getIdentNos();
            if (identNos == null || identNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(identNos[0])){
                synergistic.getDataCase().setIdentNoFlag(null);
            }else {
                synergistic.getDataCase().setIdentNoFlag("1");
            }
            String[] seqNos = synergistic.getDataCase().getSeqNos();
            if (seqNos == null || seqNos.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(seqNos[0])){
                synergistic.getDataCase().setSeqNoFlag(null);
            }else {
                synergistic.getDataCase().setSeqNoFlag("1");
            }

        }
        List<DataCaseSynergisticEntity> synergisticList = dataCaseSynergisticMapper.pageSynergisticList(synergistic);
        for(DataCaseSynergisticEntity entity : synergisticList) {
            if (entity.getSynergisticType()==null){
                SysDictionaryEntity sysDictionaryEntity = new SysDictionaryEntity();
                entity.setSynergisticType(sysDictionaryEntity);
            }else {
                SysDictionaryEntity sysDictionaryEntity = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC + entity.getSynergisticType().getId(), SysDictionaryEntity.class);
                entity.setSynergisticType(sysDictionaryEntity);
            }

            if (entity.getDataCase()==null){
                entity.getDataCase().setCollectStatusMsg("");
            }else{
                SysDictionaryEntity collectStatusEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getDataCase().getCollectStatus(),SysDictionaryEntity.class);
                entity.getDataCase().setCollectStatusMsg(collectStatusEntity==null?"":collectStatusEntity.getName());
            }



            if (entity.getApplyUser()==null){

            }else {
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + entity.getApplyUser().getId(), SysUserEntity.class);
                entity.setApplyUser(user);
            }
            if (entity.getSynergisticUser()==null){

            }else{
                SysUserEntity user2 = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ entity.getSynergisticUser().getId(), SysUserEntity.class);
                SysNewUserEntity sysNewUserEntity = new SysNewUserEntity();
                sysNewUserEntity.setId(user2.getId());
                sysNewUserEntity.setUserName(user2.getUserName());
                entity.setSynergisticUser(sysNewUserEntity);
            }

            if (StringUtils.isEmpty(entity.getApplyContent())){

            }else{
                SysDictionaryEntity contextEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getApplyContent(),SysDictionaryEntity.class);
                entity.setApplyContent(contextEntity==null?"":contextEntity.getName());
            }

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
        SysUserEntity curentuser = getUserInfo();
        bean.setApplyer(curentuser.getId());
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
