package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class CaseImportCallable implements Callable<List<WebResponse>> {

    DataCaseEntity temp;
    DataBatchEntity batch;
    List<WebResponse> list;
    int index;

    public CaseImportCallable(List<WebResponse> list, DataCaseEntity dataCaseEntity,DataBatchEntity batch, int index){
        this.temp = dataCaseEntity;
        this.batch = batch;
        this.list = list;
        this.index = index;
    }

    public List<WebResponse> call() throws Exception{

        DataBatchEntity batchEntity =  RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH+batch.getBatchNo(),DataBatchEntity.class);
        temp.setClient(batchEntity ==null ?"":batchEntity.getClient());
        if(temp.getCollectionArea() != null && temp.getCollectionArea().getId() != null) {
            SysDictionaryEntity collectAreaEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH + temp.getCollectionArea().getId(), SysDictionaryEntity.class);
            temp.setCollectArea(collectAreaEntity == null ? "" : temp.getCollectionArea().getId() + "");
        }
        if(temp.getCollectionUser()!=null && temp.getCollectionUser().getId()!=null) {
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getCollectionUser().getId(), SysUserEntity.class);
            if (user==null){
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行催收员id不正确，请核实后再上传"));;
            }

        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(temp.getSeqNo()) && (org.apache.commons.lang3.StringUtils.isEmpty(temp.getCardNo()) &&  org.apache.commons.lang3.StringUtils.isEmpty(temp.getCaseDate()))) {
            list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行未填写个案序列号或者卡号和委案日期，请填写后上传，并检查excel的个案序列号是否均填写了"));;
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getAccountAge())){
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            if (sysDictionaryEntity==null){
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行逾期账龄值"+temp.getCollectionType()+"不在枚举配置中，并检查excel的逾期账龄是否均填写正确"));;
            }else{
                temp.setAccountAge(sysDictionaryEntity.getId()+"");
            }
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getCollectionType())){
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectionType(),SysDictionaryEntity.class);
            if (sysDictionaryEntity==null){
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行催收分类值"+temp.getCollectionType()+"不在枚举配置中，并检查excel的催收分类是否均填写正确"));;
            }else{
                temp.setCollectionType(sysDictionaryEntity.getId()+"");
            }
        }

        if (org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getSeqNo())){
            DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getSeqNo(),DataCaseEntity.class);
            if (dataCaseEntity!=null) {
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行案件已存在，请修改后重新上传"));;
            }
        }else{
            DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+temp.getCardNo()+"@"+temp.getCaseDate(),DataCaseEntity.class);
            if (dataCaseEntity!=null){
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行案件已存在，请修改后重新上传"));;
            }
        }

        if(temp.getCollectionUser()!=null && temp.getCollectionUser().getId()!=null) {
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO + temp.getCollectionUser().getId(), SysUserEntity.class);
            temp.setDept(user == null ? "" : user.getDepartment());
        }
        if (temp.getProvince()!=null && org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getProvince().getName())){
            SysDictionaryEntity provicneDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getProvince().getName().replace("省","").replace("市",""),SysDictionaryEntity.class);
            if (provicneDic==null){
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行省"+temp.getProvince().getName()+"不在枚举配置中，并检查excel的省是否均填写正确"));;
            }else{
                temp.getProvince().setId(provicneDic.getId());
            }
        }
        if (temp.getCity()!=null && org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getCity().getName())){
            SysDictionaryEntity cityDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCity().getName().replace("市",""),SysDictionaryEntity.class);
            if (cityDic==null){
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行市"+temp.getCity().getName()+"不在枚举配置中，并检查excel的市是否均填写正确"));;
            }else{
                temp.getCity().setId(cityDic.getId());
            }
        }
        if (temp.getCounty()!=null && org.apache.commons.lang3.StringUtils.isNotEmpty(temp.getCounty().getName())){
            SysDictionaryEntity countyDic =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCounty().getName(),SysDictionaryEntity.class);
            if (countyDic==null){
                list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行县"+temp.getCounty().getName()+"不在枚举配置中，并检查excel的县是否均填写正确"));;
            }else{
                temp.getCounty().setId(countyDic.getId());
            }
        }

        temp.setBatchNo(batch.getBatchNo());
        

        return list;
    }

}
