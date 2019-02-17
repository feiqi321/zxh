package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
@Service
public class DataCollectServiceImpl implements DataCollectService {
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Autowired
    private SysDictionaryService sysDictionaryService;

    public void delete(DataCollectionEntity bean){
        dataCollectionMapper.deleteCollect(bean);
    }

    public WebResponse pageDataCollect(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> list = dataCollectionMapper.pageDataCollect(bean);
        List<DataCollectionEntity> resultList = new ArrayList<DataCollectionEntity>();
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            resultList.add(temp);
        }
        webResponse.setData(PageInfo.of(resultList));

        return webResponse;
    }


    public WebResponse totalDataCollect(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> list = dataCollectionMapper.totalDataCollect(bean);
        List<DataCollectionEntity> resultList = new ArrayList<DataCollectionEntity>();
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            resultList.add(temp);
        }
        webResponse.setData(resultList);

        return webResponse;
    }

    public WebResponse selectDataCollect(List<DataCollectionEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> resultList = new ArrayList<DataCollectionEntity>();
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = dataCollectionMapper.selectDataCollect(list.get(i));
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            resultList.add(temp);
        }
        webResponse.setData(resultList);

        return webResponse;
    }

    public WebResponse selectDataCollectExportByBatch(List<DataCollectionEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> resultList = new ArrayList<DataCollectionEntity>();
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = dataCollectionMapper.selectDataCollect(list.get(i));
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            resultList.add(temp);
        }
        webResponse.setData(resultList);

        return webResponse;
    }

}
