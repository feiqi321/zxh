package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;

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
        int count  =  dataCollectionMapper.countDataCollect(bean);
        List<DataCollectionEntity> resultList = new ArrayList<DataCollectionEntity>();
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2.getName());

            resultList.add(temp);
        }
        webResponse.setData(list);
        int totalPageNum = 0 ;
        if (count%bean.getPageSize()>0){
            totalPageNum = count/bean.getPageSize()+1;
        }else{
            totalPageNum = count/bean.getPageSize();
        }

        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setTotalNum(count);
        return webResponse;
    }

}
