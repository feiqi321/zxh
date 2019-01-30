package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCollectionServiceImpl implements DataCollectionService {
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;

    @Override
    public void save(DataCollectionEntity dataCollectionEntity){

    }
    @Override
    public void update(DataCollectionEntity dataCollectionEntity){
    }
    @Override
    public void delete(DataCollectionEntity dataCollectionEntity){

    }
    @Override
    public List<DataCollectionEntity> pageDataCollectionList(DataCollectionEntity dataCollectionEntity){
       List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollection(dataCollectionEntity);
       for (int i=0;i<list.size();i++){
           DataCollectionEntity temp = list.get(i);
           list.set(i,temp);
       }
       return list;
    }

    @Override
    public List<DataCollectionEntity> pageMyCase(DataCollectionEntity dataCollectionEntity){
        List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollect(dataCollectionEntity);
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            list.set(i,temp);
        }
        return list;
    }
}
