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

import java.math.BigDecimal;
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
        temp.setMoney(temp.getMoney()==null?new BigDecimal(0):temp.getMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        temp.setBalance(temp.getBalance()==null?new BigDecimal(0):temp.getBalance().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        temp.setRate(temp.getRate()==null?new BigDecimal(0):temp.getRate().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        try{
            BigDecimal bMval = new BigDecimal(temp.getMVal()==null?"0":temp.getMVal());
        }catch (Exception e){
            list.add(WebResponse.error(WebResponseCode.IMPORT_ERROR.getCode(), "第" + (index + 2) + "行Mval格式不对，请修改后重新上传"));;
        }
        temp.setEnRepayAmt(temp.getEnRepayAmt()==null?new BigDecimal(0):temp.getEnRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        temp.setBankAmt(temp.getBankAmt()==null?new BigDecimal(0):temp.getBankAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        temp.setProRepayAmt(temp.getProRepayAmt()==null?new BigDecimal(0):temp.getProRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        temp.setCommissionMoney(temp.getCommissionMoney()==null?new BigDecimal(0):temp.getCommissionMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        temp.setLastRepayMoney(temp.getLastRepayMoney()==null?new BigDecimal(0):temp.getLastRepayMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        temp.setOutstandingAmount(temp.getOutstandingAmount()==null?new BigDecimal(0):temp.getOutstandingAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN));

        temp.setBatchNo(batch.getBatchNo());
        

        return list;
    }

}
