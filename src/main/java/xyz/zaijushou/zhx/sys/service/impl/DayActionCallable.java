package xyz.zaijushou.zhx.sys.service.impl;

import com.google.common.collect.Lists;
import xyz.zaijushou.zhx.constant.CaseBaseConstant;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.CollectionStatistic;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.RedisUtils;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * Created by looyer on 2019/4/15.
 */
public class DayActionCallable implements Callable<List<CollectionStatistic>> {

    DataCollectionTelMapper dataCollectionTelMapper;
    SysDictionaryMapper sysDictionaryMapper;
    List<CollectionStatistic> list;
    String odv;
    CollectionStatistic bean ;

    DayActionCallable(List<CollectionStatistic> list, String odv, DataCollectionTelMapper dataCollectionTelMapper, SysDictionaryMapper sysDictionaryMapper, CollectionStatistic bean){
        this.list = list;
        this.odv = odv;
        this.bean = bean;
        this.dataCollectionTelMapper = dataCollectionTelMapper;
        this.sysDictionaryMapper = sysDictionaryMapper;
    }

    public List<CollectionStatistic> call() throws Exception{

        bean.setOdv(odv);
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odv, SysUserEntity.class);
        CollectionStatistic col = new CollectionStatistic();
        col.setOdv(user.getUserName());
        List<CollectionStatistic> colData = dataCollectionTelMapper.countCollectionDayAction(bean);
        List<SysDictionaryEntity> collectionIds = sysDictionaryMapper.getCollectionDataByDicId(CaseBaseConstant.COLLECTION_TYPE);
        List<String> descs = collectionIds.stream().map(each -> each.getDescription()).collect(Collectors.toList());
        ConnectionListToInfo(descs,colData, col);
        list.add(col);

        return list;
    }

    public  void ConnectionListToInfo(List<String> descs, List<CollectionStatistic> colData,CollectionStatistic col){
        col.setCollectionDics(descs);
        List<Integer> numbers = Lists.newArrayList();
        for(String desc : descs) {
            Optional<CollectionStatistic> current = colData.stream().filter(each -> desc.equals(each.getCollectionResult())).findFirst();
            if (!current.isPresent()) {
                numbers.add(0);
            } else {
                numbers.add(current.get().getCountResult());
                col.setCountResult(col.getCountResult() + current.get().getCountResult());
            }
        }
        col.setCollectionDicResults(numbers);

    }

}
