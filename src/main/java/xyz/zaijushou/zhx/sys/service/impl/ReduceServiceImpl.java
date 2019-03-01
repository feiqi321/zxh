package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.ReduceMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
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
        bean.setReduceFlag("1");//1为已删除
        List<DataCollectionEntity> list = reduceMapper.pageReduceApply(bean);
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }else {
            return PageInfo.of(list);
        }
    }

    public PageInfo<DataCollectionEntity> pageReduceApply(DataCollectionEntity bean){
        List<DataCollectionEntity> list = reduceMapper.pageReduceApply(bean);
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }else {
            return PageInfo.of(list);
        }
    }

    public List<DataCollectionEntity> listReduce(DataCollectionEntity bean){
        return  reduceMapper.pageReduceApply(bean);
    }
    public DataCollectionEntity findById(DataCollectionEntity bean){
        return reduceMapper.findById(bean);
    }
    public void updateStatus(DataCollectionEntity bean){
        reduceMapper.updateStatus(bean);
    }
    public void saveReduce(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getId()) || bean.getId() == 0){//保存
            bean.setReduceFlag("0");//0-减免结果有效
            bean.setApplyStatus("1");//1-已审核，
            reduceMapper.saveReduceApply(bean);
        }else{//更新
            DataCollectionEntity beanInfo = reduceMapper.findById(bean);
            if (StringUtils.isEmpty(beanInfo)){
                return ;
            }
            bean.setReduceFlag(beanInfo.getReduceFlag());
            bean.setUpdateTime(new Date());
            reduceMapper.updateReduceApply(bean);
        }
    }

    public void saveReduceInfo(List<DataCollectionEntity> list){
       for (DataCollectionEntity bean: list){
           reduceMapper.saveReduceApply(bean);
       }
    }
    public void updateReduce(DataCollectionEntity bean){
        return ;
    }


    public void saveReduceApply(DataCollectionEntity bean){
        if (StringUtils.isEmpty(bean.getId())){
            bean.setApplyStatus("0");
            reduceMapper.saveReduceApply(bean);
        }else {
            reduceMapper.updateReduceApply(bean);
        }
    }
    public DataCollectionEntity findApplyById(DataCollectionEntity bean){
        DataCollectionEntity info = reduceMapper.findById(bean);
        return info;
    }
    public void updateApplyStatus(DataCollectionEntity bean){
        reduceMapper.updateApplyStatus(bean);
    }


}
