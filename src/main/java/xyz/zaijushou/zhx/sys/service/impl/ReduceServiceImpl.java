package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.ReduceMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataReduceEntity;
import xyz.zaijushou.zhx.sys.service.ReduceService;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/2/25.
 */
@Service
public class ReduceServiceImpl implements ReduceService {
    @Resource
    private ReduceMapper reduceMapper;

    public PageInfo<DataCollectionEntity> pageReduce(DataCollectionEntity bean){
        List<DataCollectionEntity> list = reduceMapper.pageReduce(bean);
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }else {
            return PageInfo.of(list);
        }
    }

    public List<DataCollectionEntity> listReduce(DataCollectionEntity bean){
        return  reduceMapper.pageReduce(bean);
    }
    public DataCollectionEntity findById(DataCollectionEntity bean){
        return reduceMapper.findById(bean);
    }
    public void updateStatus(DataCollectionEntity bean){
        reduceMapper.updateStatus(bean);
    }
    public void saveReduce(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getId()) || bean.getId() == 0){//保存
            reduceMapper.saveReduce(bean);
        }else{//更新
            DataCollectionEntity beanInfo = reduceMapper.findById(bean);
            if (StringUtils.isEmpty(beanInfo)){
                return ;
            }
            bean.setReduceFlag(beanInfo.getReduceFlag());
            bean.setUpdateTime(new Date());
            reduceMapper.updateReduce(bean);
        }
    }

    public void saveReduceInfo(List<DataCollectionEntity> list){
       for (DataCollectionEntity bean: list){
           reduceMapper.saveReduce(bean);
       }
    }
    public void updateReduce(DataCollectionEntity bean){
        return ;
    }


    public void saveReduceApply(DataReduceEntity bean){
        if (StringUtils.isEmpty(bean.getId())){
            reduceMapper.saveReduceApply(bean);
        }else {
            reduceMapper.updateReduceApply(bean);
        }
    }
    public DataReduceEntity findApplyById(DataReduceEntity bean){
        DataReduceEntity info = reduceMapper.findApplyById(bean);
        return info;
    }
    public void updateApplyStatus(DataReduceEntity bean){
        reduceMapper.updateApplyStatus(bean);
    }
}
