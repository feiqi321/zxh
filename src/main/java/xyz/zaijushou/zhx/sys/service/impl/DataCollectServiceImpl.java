package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/1/29.
 */
@Service
public class DataCollectServiceImpl implements DataCollectService {
    @Resource
    private DataCollectionMapper dataCollectionMapper;

    public void delete(DataCollectionEntity bean){
        dataCollectionMapper.deleteCollect(bean);
    }

    public List<DataCollectionEntity> pageDataCollect(DataCollectionEntity bean){
        List<DataCollectionEntity> list = dataCollectionMapper.pageDataCollect(bean);
        return list;
    }

}
