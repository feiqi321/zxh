package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.DataCollectExportEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class CollectExportCallable implements Callable<List<DataCollectExportEntity>> {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    DataCollectExportEntity collection;
    List<DataCollectExportEntity> list;
    int index;

    CollectExportCallable(List<DataCollectExportEntity> list, DataCollectExportEntity dataCollectionEntity, int index){
        this.collection = dataCollectionEntity;
        this.list = list;
        this.index = index;
    }

    public List<DataCollectExportEntity> call() throws Exception{

        SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getReduceStatus(),SysDictionaryEntity.class);
        collection.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

        SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getCollectStatus(),SysDictionaryEntity.class);
        collection.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

        SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getModule(),SysDictionaryEntity.class);
        collection.setModule(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

        SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getResult(),SysDictionaryEntity.class);
        collection.setResult(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());

        SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+collection.getMethod(),SysDictionaryEntity.class);
        collection.setMethod(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());

        SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ collection.getOdv(), SysUserEntity.class);
        collection.setOdv(user==null?"":user.getUserName());


        list.set(index,collection);
        return list;
    }

}
