package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.ReduceApplyEnum;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.dao.ReduceFileListMapper;
import xyz.zaijushou.zhx.sys.dao.ReduceMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.ReduceService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
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
    @Resource
    private ReduceFileListMapper reduceFileListMapper;

    @Override
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

            temp.setMoneyMsg(temp.getMoney()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getMoney().stripTrailingZeros()+""));
            temp.setApproveRepayAmtMsg(temp.getApproveRepayAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getApproveRepayAmt().stripTrailingZeros()+""));
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()==null?"￥0": "￥"+ FmtMicrometer.fmtMicrometer(temp.getEnRepayAmt().stripTrailingZeros()+""));

            list.set(i,temp);
        }
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }else {
            return PageInfo.of(list);
        }
    }

    @Override
    public PageInfo<DataCollectionEntity> pageReduceExport(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getOrderBy())){
            bean.setOrderBy("dcr.id");
        }else {
            bean.setOrderBy(ReduceApplyEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(bean.getSort())){
            bean.setSort(" desc");
        }
//        bean.setReduceFlag("1");//1为已删除
        List<DataCollectionEntity> list = reduceMapper.pageReduceApplyExport(bean);
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity collectDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getCollectStatus(), SysDictionaryEntity.class);
            temp.setCollectStatusMsg(collectDic==null?"":collectDic.getName());
            SysDictionaryEntity reduceDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getReduceStatus(), SysDictionaryEntity.class);
            temp.setReduceStatusMsg(reduceDic==null?"":reduceDic.getName());

            temp.setMoneyMsg(temp.getMoney()+"");
            temp.setApproveRepayAmtMsg(temp.getApproveRepayAmt()+"");
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()+"");

            list.set(i,temp);
        }
        if (StringUtils.isEmpty(list)){
            return new PageInfo<>();
        }else {
            return PageInfo.of(list);
        }
    }

    @Override
    public PageInfo<DataCollectionEntity> pageReduceApply(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getOrderBy())){
            bean.setOrderBy("dcr.id");
        }else {
            bean.setOrderBy(ReduceApplyEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(bean.getSort())){
            bean.setSort(" desc");
        }
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

    @Override
    public List<DataCollectionEntity> listReduce(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getOrderBy())){
            bean.setOrderBy("dcr.id");
        }else {
            bean.setOrderBy(ReduceApplyEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(bean.getSort())){
            bean.setSort(" desc");
        }
        List<DataCollectionEntity> list = reduceMapper.pageReduceApply(bean);
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity collectDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getCollectStatus(), SysDictionaryEntity.class);
            temp.setCollectStatusMsg(collectDic==null?"":collectDic.getName());
            SysDictionaryEntity reduceDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getReduceStatus(), SysDictionaryEntity.class);
            temp.setReduceStatusMsg(reduceDic==null?"":reduceDic.getName());

            temp.setMoneyMsg(temp.getMoney()+"");
            temp.setApproveRepayAmtMsg(temp.getApproveRepayAmt()+"");
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()+"");

            list.set(i,temp);
        }
        return  list;
    }

    @Override
    public List<DataCollectionEntity> totalExport(DataCollectionEntity bean){
        if(StringUtils.isEmpty(bean.getOrderBy())){
            bean.setOrderBy("dcr.id");
        }else {
            bean.setOrderBy(ReduceApplyEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(bean.getSort())){
            bean.setSort(" desc");
        }
        List<DataCollectionEntity> list = reduceMapper.totalExport(bean);
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity collectDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getCollectStatus(), SysDictionaryEntity.class);
            temp.setCollectStatusMsg(collectDic==null?"":collectDic.getName());
            SysDictionaryEntity reduceDic = RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+ temp.getReduceStatus(), SysDictionaryEntity.class);
            temp.setReduceStatusMsg(reduceDic==null?"":reduceDic.getName());

            temp.setMoneyMsg(temp.getMoney()+"");
            temp.setApproveRepayAmtMsg(temp.getApproveRepayAmt()+"");
            temp.setEnRepayAmtMsg(temp.getEnRepayAmt()+"");

            list.set(i,temp);
        }
        return  list;
    }
    @Override
    public DataCollectionEntity findById(DataCollectionEntity bean){
        return reduceMapper.findById(bean);
    }
    @Override
    public void updateStatus(DataCollectionEntity bean){
        switch (bean.getReduceFlag()){
            case "0"://启动
                bean.setReduceFlag("0");
                break;
            case "1"://删除
                bean.setDeleteFlag(1);
                break;
            case "2"://撤销
                bean.setApplyStatus("2");
                break;
            case "3"://审核通过
                bean.setApplyStatus("1");
                bean.setAuditTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                break;
            case "4"://已完成
                bean.setApplyStatus("2");
                SysUserEntity user = this.getUserInfo();
                if (user == null){
                    bean.setCompleteUser("系统管理员");
                }else {
                    bean.setCompleteUser(user.getUserName());
                }
                bean.setCompleteTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                break;
            case "5"://停用
                bean.setReduceFlag("5");
                break;
                default:break;
        }
        reduceMapper.updateStatus(bean);
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }

    @Override
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

    @Override
    public void saveReduceInfo(List<DataCollectionEntity> list){
       for (DataCollectionEntity bean: list){
           bean.setApplyStatus("2");
           reduceMapper.saveReduceApply(bean);
       }
    }
    @Override
    public void updateReduce(DataCollectionEntity bean){
        return ;
    }

    @Override
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
            if (StringUtils.notEmpty(bean.getFileName())) {
                ReduceFileList fileList = new ReduceFileList();
                fileList.setFileId(bean.getFileUuid());
                fileList.setFileName(bean.getFileName());
                fileList.setReduceId(bean.getId());
                reduceFileListMapper.saveFile(fileList);
            }
        }

    }
    @Override
    public DataCollectionEntity findApplyById(DataCollectionEntity bean){
        DataCollectionEntity info = reduceMapper.findById(bean);
        return info;
    }
    @Override
    public void updateApplyStatus(DataCollectionEntity bean){
        reduceMapper.updateApplyStatus(bean);
    }

    @Override
    public void delete(ReduceFileList bean){
        reduceFileListMapper.delete(bean);
    }

    @Override
    public List<ReduceFileList> listFile(ReduceFileList bean){
        return reduceFileListMapper.listFile(bean);
    }


}
