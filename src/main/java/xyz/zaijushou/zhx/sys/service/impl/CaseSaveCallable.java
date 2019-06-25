package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.dao.DataCaseMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by looyer on 2019/4/15.
 */
public class CaseSaveCallable implements Callable<List<DataCaseTelEntity>> {

    DataCaseEntity entity;
    DataBatchEntity batch;
    StringRedisTemplate stringRedisTemplate;
    List<DataCaseTelEntity> list;
    DataCaseMapper dataCaseMapper;

    public CaseSaveCallable(List<DataCaseTelEntity> list, DataCaseEntity dataCaseEntity, DataBatchEntity batch, DataCaseMapper dataCaseMapper, StringRedisTemplate stringRedisTemplate){
        this.entity = dataCaseEntity;
        this.batch = batch;
        this.stringRedisTemplate = stringRedisTemplate;
        this.list = list;
        this.dataCaseMapper = dataCaseMapper;
    }

    public List<DataCaseTelEntity> call() throws Exception{

        /*DataBatchEntity batchEntity =  RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH+batch.getBatchNo(),DataBatchEntity.class);
        entity.setClient(batchEntity ==null ?"":batchEntity.getClient());
        if(entity.getCollectionArea() != null && entity.getCollectionArea().getId() != null) {
            SysDictionaryEntity collectAreaEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_BATCH + entity.getCollectionArea().getId(), SysDictionaryEntity.class);
            entity.setCollectArea(collectAreaEntity == null ? "" : entity.getCollectionArea().getId() + "");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getAccountAge())){
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getAccountAge(),SysDictionaryEntity.class);
            entity.setAccountAge(sysDictionaryEntity.getId()+"");
        }
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(entity.getCollectionType())){
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+entity.getCollectionType(),SysDictionaryEntity.class);
            entity.setCollectionType(sysDictionaryEntity.getId()+"");
        }

        entity.setMoney(entity.getMoney()==null?new BigDecimal(0):entity.getMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        entity.setBalance(entity.getBalance()==null?new BigDecimal(0):entity.getBalance().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        entity.setRate(entity.getRate()==null?new BigDecimal(0):entity.getRate().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        try{
            BigDecimal bMval = new BigDecimal(entity.getMVal()==null?"0":entity.getMVal());
        }catch (Exception e){
        }
        entity.setEnRepayAmt(entity.getEnRepayAmt()==null?new BigDecimal(0):entity.getEnRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        entity.setBankAmt(entity.getBankAmt()==null?new BigDecimal(0):entity.getBankAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        entity.setProRepayAmt(entity.getProRepayAmt()==null?new BigDecimal(0):entity.getProRepayAmt().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        entity.setCommissionMoney(entity.getCommissionMoney()==null?new BigDecimal(0):entity.getCommissionMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        entity.setLastRepayMoney(entity.getLastRepayMoney()==null?new BigDecimal(0):entity.getLastRepayMoney().setScale(2, BigDecimal.ROUND_HALF_DOWN));
        entity.setOutstandingAmount(entity.getOutstandingAmount()==null?new BigDecimal(0):entity.getOutstandingAmount().setScale(2, BigDecimal.ROUND_HALF_DOWN));
*/


        BigDecimal tmp = batch.getTotalAmt();
        batch.setTotalAmt(tmp.add(entity.getMoney()));
        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + entity.getSeqNo(), JSONObject.toJSONString(entity));
        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_CASE + entity.getCardNo()+"@"+entity.getCaseDate(), JSONObject.toJSONString(entity));


        if (StringUtils.notEmpty(entity.getContactName1()) || StringUtils.notEmpty(entity.getContactMobile1())) {
            DataCaseTelEntity dataCaseTelEntity1 = new DataCaseTelEntity();
            dataCaseTelEntity1.setCaseId(entity.getId());
            dataCaseTelEntity1.setName(entity.getContactName1());
            dataCaseTelEntity1.setIdentNo(entity.getContactIdentNo1());
            dataCaseTelEntity1.setRelation(entity.getContactRelation1());
            dataCaseTelEntity1.setTel(entity.getContactMobile1());
            dataCaseTelEntity1.setTelStatusMsg("未知");
            list.add(dataCaseTelEntity1);
        }
        if (StringUtils.notEmpty(entity.getContactName2()) || StringUtils.notEmpty(entity.getContactMobile2())) {
            DataCaseTelEntity dataCaseTelEntity2 = new DataCaseTelEntity();
            dataCaseTelEntity2.setCaseId(entity.getId());
            dataCaseTelEntity2.setName(entity.getContactName2());
            dataCaseTelEntity2.setIdentNo(entity.getContactIdentNo2());
            dataCaseTelEntity2.setRelation(entity.getContactRelation2());
            dataCaseTelEntity2.setTel(entity.getContactMobile2());
            dataCaseTelEntity2.setTelStatusMsg("未知");
            list.add(dataCaseTelEntity2);
        }
        if (StringUtils.notEmpty(entity.getContactName3()) || StringUtils.notEmpty(entity.getContactMobile3())) {
            DataCaseTelEntity dataCaseTelEntity3 = new DataCaseTelEntity();
            dataCaseTelEntity3.setCaseId(entity.getId());
            dataCaseTelEntity3.setName(entity.getContactName3());
            dataCaseTelEntity3.setIdentNo(entity.getContactIdentNo3());
            dataCaseTelEntity3.setRelation(entity.getContactRelation3());
            dataCaseTelEntity3.setTel(entity.getContactMobile3());
            dataCaseTelEntity3.setTelStatusMsg("未知");
            list.add(dataCaseTelEntity3);
        }
        if (StringUtils.notEmpty(entity.getContactName4()) || StringUtils.notEmpty(entity.getContactMobile4())) {
            DataCaseTelEntity dataCaseTelEntity4 = new DataCaseTelEntity();
            dataCaseTelEntity4.setCaseId(entity.getId());
            dataCaseTelEntity4.setName(entity.getContactName4());
            dataCaseTelEntity4.setIdentNo(entity.getContactIdentNo4());
            dataCaseTelEntity4.setRelation(entity.getContactRelation4());
            dataCaseTelEntity4.setTel(entity.getContactMobile4());
            dataCaseTelEntity4.setTelStatusMsg("未知");
            list.add(dataCaseTelEntity4);
        }
        if (StringUtils.notEmpty(entity.getContactName5()) || StringUtils.notEmpty(entity.getContactMobile5())) {
            DataCaseTelEntity dataCaseTelEntity5 = new DataCaseTelEntity();
            dataCaseTelEntity5.setCaseId(entity.getId());
            dataCaseTelEntity5.setName(entity.getContactName5());
            dataCaseTelEntity5.setIdentNo(entity.getContactIdentNo5());
            dataCaseTelEntity5.setRelation(entity.getContactRelation5());
            dataCaseTelEntity5.setTel(entity.getContactMobile5());
            dataCaseTelEntity5.setTelStatusMsg("未知");
            list.add(dataCaseTelEntity5);
        }
        if (StringUtils.notEmpty(entity.getContactName6()) || StringUtils.notEmpty(entity.getContactMobile6())) {
            DataCaseTelEntity dataCaseTelEntity6 = new DataCaseTelEntity();
            dataCaseTelEntity6.setCaseId(entity.getId());
            dataCaseTelEntity6.setName(entity.getContactName6());
            dataCaseTelEntity6.setIdentNo(entity.getContactIdentNo6());
            dataCaseTelEntity6.setRelation(entity.getContactRelation6());
            dataCaseTelEntity6.setTel(entity.getContactMobile6());
            dataCaseTelEntity6.setTelStatusMsg("未知");
            list.add(dataCaseTelEntity6);
        }
        

        return list;
    }

}
