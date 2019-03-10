package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.ReduceApplyEnum;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.ReduceMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.service.ReduceService;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/2/25.
 */
@Service
public class ReduceServiceImpl implements ReduceService {
    @Resource
    private ReduceMapper reduceMapper;
    @Resource
    private DataCaseMapper caseMapper;

    public PageInfo<DataCollectionEntity> pageReduce(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getOrderBy())){
            bean.setOrderBy("dcr.id");
        }else {
            bean.setOrderBy(ReduceApplyEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(bean.getSort())){
            bean.setSort(" desc");
        }
//        bean.setReduceFlag("1");//1为已删除
        List<DataCollectionEntity> list = reduceMapper.pageReduceApply(bean);
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity collectDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getCollectStatus(), SysDictionaryEntity.class);
            temp.setCollectStatusMsg(collectDic==null?"":collectDic.getName());
            SysDictionaryEntity reduceDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getReduceStatus(), SysDictionaryEntity.class);
            temp.setReduceStatusMsg(reduceDic==null?"":reduceDic.getName());
            list.set(i,temp);
        }
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
        switch (bean.getReduceFlag()){
            case "0"://启动
                bean.setReduceFlag("0");
            case "1"://删除
                bean.setDeleteFlag(1);
            case "2"://撤销
                bean.setApplyStatus("2");
            case "3"://审核通过
                bean.setApplyStatus("1");
                bean.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            case "4"://已完成
                bean.setApplyStatus("2");
                bean.setCompleteUser("系统管理员");
                bean.setCompleteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            case "5"://停用
                bean.setReduceFlag("5");
        }
        reduceMapper.updateStatus(bean);
    }
    public void saveReduce(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getId()) || bean.getId() == 0){//保存
            bean.setReduceFlag("0");//0-减免结果有效
            bean.setApplyStatus("1");//1-已审核，
            bean.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//审核时间
            reduceMapper.saveReduceApply(bean);
        }else{//更新
            DataCollectionEntity beanInfo = reduceMapper.findById(bean);
            if (StringUtils.isEmpty(beanInfo)){
                return ;
            }
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
        if(StringUtils.notEmpty(bean.getCaseId())){
            DataCaseEntity caseBean = new DataCaseEntity();
            caseBean.setId(Integer.valueOf(bean.getCaseId()));
            DataCaseEntity entity = caseMapper.findById(caseBean);
            bean.setSeqno(entity.getSeqNo());
        }
        if (StringUtils.isEmpty(bean.getId())){
            bean.setApplyStatus("0");
            bean.setReduceFlag("0");
            bean.setApplyUser("系统管理员");//申请人
            bean.setApplyTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));//申请时间
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
