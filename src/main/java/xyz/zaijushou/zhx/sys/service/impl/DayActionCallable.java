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
import xyz.zaijushou.zhx.utils.StringUtils;

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

        bean.setOdvName(odv);
        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ odv, SysUserEntity.class);
        CollectionStatistic col = new CollectionStatistic();
        col.setOdvName(user.getUserName());
        col.setOdv(odv);
        List<CollectionStatistic> colData = dataCollectionTelMapper.countCollectionDayAction(bean);
        List<SysDictionaryEntity> collectionIds = sysDictionaryMapper.getCollectionDataByDicId(CaseBaseConstant.COLLECTION_TYPE);
        //List<String> descs = collectionIds.stream().map(each -> each.getDescription()).collect(Collectors.toList());
        Map map = new HashMap();
        for (int i=0;i<collectionIds.size();i++){
            SysDictionaryEntity dict = collectionIds.get(i);
            map.put(dict.getId(),dict);
        }
        ConnectionListToInfo(map ,colData, col);
        list.add(col);

        return list;
    }

    public  void ConnectionListToInfo(Map map , List<CollectionStatistic> colData,CollectionStatistic col){
        //col.setCollectionDics(descs);
        //List<Integer> numbers = Lists.newArrayList();
        int countSearchNo=0;
        int countDX1=0;
        int countDX2=0;
        int countDX3=0;
        int countDX4=0;
        int countRepay=0;
        int countConSelf=0;
        int countConVillage=0;
        int countConThird=0;
        int countConFamily=0;
        int countDeadNumber=0;
        int countSearchInvalid=0;
        int countNoAnswer=0;
        int countInvalidCall=0;
        //for(SysDictionaryEntity desc : collectionIds) {
        for (int i=0;i<colData.size();i++){
            CollectionStatistic collectionStatistic = colData.get(i);
            SysDictionaryEntity desc = StringUtils.isEmpty(collectionStatistic.getCollectionResult())?new SysDictionaryEntity():(SysDictionaryEntity)map.get(Integer.parseInt(collectionStatistic.getCollectionResult()));
            //Optional<CollectionStatistic> current = colData.stream().filter(each -> desc.getId().equals(each.getCollectionResult())).findFirst();
            //if (current.isPresent()) {
                if ("114查询无效".equals(desc.getName())){
                    countSearchNo = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("DX1".equals(desc.getName())){
                    countDX1 = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("DX2".equals(desc.getName())){
                    countDX2 = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("DX3".equals(desc.getName())){
                    countDX3 = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("DX4".equals(desc.getName())){
                    countDX4 = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("承诺还款".equals(desc.getName())){
                    countRepay = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("可联本人".equals(desc.getName())){
                    countConSelf = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("可联村委".equals(desc.getName())){
                    countConVillage = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("可联第三人".equals(desc.getName())){
                    countConThird = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("可联家人".equals(desc.getName())){
                    countConFamily = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("空号错号".equals(desc.getName())){
                    countDeadNumber = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("网搜无效".equals(desc.getName())){
                    countSearchInvalid = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("无人接听".equals(desc.getName())){
                    countNoAnswer = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }else if ("无效电话".equals(desc.getName())){
                    countInvalidCall = collectionStatistic.getCountResult();
                    col.setCountResult(col.getCountResult() + collectionStatistic.getCountResult());
                }
            //}

        }
        //col.setCollectionDicResults(numbers);
        col.setCountSearchNo(countSearchNo);
        col.setCountDX1(countDX1);
        col.setCountDX2(countDX2);
        col.setCountDX3(countDX3);
        col.setCountDX4(countDX4);
        col.setCountRepay(countRepay);
        col.setCountConSelf(countConSelf);
        col.setCountConVillage(countConVillage);
        col.setCountConThird(countConThird);
        col.setCountConFamily(countConFamily);
        col.setCountDeadNumber(countDeadNumber);
        col.setCountSearchInvalid(countSearchInvalid);
        col.setCountNoAnswer(countNoAnswer);
        col.setCountInvalidCall(countInvalidCall);
    }

}
