package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.ExcelSynergisticConstant;
import xyz.zaijushou.zhx.sys.dao.DataCaseSynergisticMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergisticEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseSynergisticService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DataCaseSynergisticServiceImpl implements DataCaseSynergisticService {

    @Resource
    private DataCaseSynergisticMapper dataCaseSynergisticMapper;

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysDictionaryMapper sysDictionaryMapper;

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
        SysDictionaryEntity dict = new SysDictionaryEntity();
        dict.setName("协催类型");
        List<SysDictionaryEntity> typeList =  sysDictionaryMapper.listDataByName(dict);
        Map<Integer, SysDictionaryEntity> typeMap = CollectionsUtils.listToMap(typeList);
        for(DataCaseSynergisticEntity entity : synergisticList) {
            if(entity != null && entity.getSynergisticType() != null && entity.getSynergisticType().getId() != null && typeMap.containsKey(entity.getSynergisticType().getId())) {
                entity.setSynergisticType(typeMap.get(entity.getSynergisticType().getId()));
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
        }
        return PageInfo.of(synergisticList);
    }

    @Override
    public void approve(DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticMapper.updateApplyStatus(synergistic);
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
