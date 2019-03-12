package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CaseSortEnum;
import xyz.zaijushou.zhx.constant.CollectSortEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectExportEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectService;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
import xyz.zaijushou.zhx.utils.RedisUtils;

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

    public void delete(DataCollectionEntity bean){
        dataCollectionMapper.deleteCollect(bean);
    }

    public WebResponse selectAllByCaseId(DataCollectionEntity bean){
        List<DataCollectionEntity> list = dataCollectionMapper.selectAllByCaseId(bean);

        return WebResponse.success(list);
    }

    public WebResponse pageDataCollect(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = bean.getClients();
        if (clients == null || clients.length==0 || StringUtils.isEmpty(clients[0])){
            bean.setClientFlag(null);
        }else{
            bean.setClientFlag("1");
        }
        String[] odvs = bean.getOdvs();
        if (odvs == null || odvs.length==0 || StringUtils.isEmpty(odvs[0])){
            bean.setOdvFlag(null);
        }else{
            bean.setOdvFlag("1");
        }
        String[] batchs = bean.getBatchNos();
        if (batchs == null || batchs.length==0 || StringUtils.isEmpty(batchs[0])){
            bean.setBatchFlag(null);
        }else{
            bean.setBatchFlag("1");
        }
        if(StringUtils.isEmpty(bean.getOrderBy())){
            bean.setSort("desc");
            bean.setOrderBy("id");
        }else {
            bean.setOrderBy(CollectSortEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        List<DataCollectionEntity> list = dataCollectionMapper.pageDataCollect(bean);
        List<DataCollectionEntity> resultList = new ArrayList<DataCollectionEntity>();
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getReduceStatus(),SysDictionaryEntity.class);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getMethod(),SysDictionaryEntity.class);
            temp.setMethodMsg(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

            SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getResult(),SysDictionaryEntity.class);
            temp.setResult(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());

            SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getTelType(),SysDictionaryEntity.class);
            temp.setTelType(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());

            temp.setRepayAmtMsg(temp.getRepayAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getRepayAmt()+""));
            temp.setReduceAmtMsg(temp.getReduceAmt()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(temp.getReduceAmt()+""));
            resultList.add(temp);
        }
        webResponse.setData(PageInfo.of(resultList));

        return webResponse;
    }


    public WebResponse totalDataCollect(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = bean.getClients();
        if (clients == null || clients.length==0 || StringUtils.isEmpty(clients[0])){
            bean.setClientFlag(null);
        }else{
            bean.setClientFlag("1");
        }
        String[] odvs = bean.getOdvs();
        if (odvs == null || odvs.length==0 || StringUtils.isEmpty(odvs[0])){
            bean.setOdvFlag(null);
        }else{
            bean.setOdvFlag("1");
        }
        String[] batchs = bean.getBatchNos();
        if (batchs == null || batchs.length==0 || StringUtils.isEmpty(batchs[0])){
            bean.setBatchFlag(null);
        }else{
            bean.setBatchFlag("1");
        }
        if (StringUtils.isEmpty(bean.getOrderBy())) {
            bean.setOrderBy("id");
            bean.setSort("desc");
        }else{
            bean.setOrderBy(CollectSortEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        List<DataCollectExportEntity> list = dataCollectionMapper.totalDataCollect(bean);
        for (int i=0;i<list.size();i++){
            DataCollectExportEntity temp = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getReduceStatus(),SysDictionaryEntity.class);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getModule(),SysDictionaryEntity.class);
            temp.setModule(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

            SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getResult(),SysDictionaryEntity.class);
            temp.setResult(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());

            SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getMethod(),SysDictionaryEntity.class);
            temp.setMethod(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            list.set(i,temp);
        }
        webResponse.setData(list);

        return webResponse;
    }

    public WebResponse selectDataCollect(int[] ids){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectExportEntity> list = dataCollectionMapper.selectDataCollect(ids);
        for (int i=0;i<list.size();i++){
            DataCollectExportEntity temp = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getReduceStatus(),SysDictionaryEntity.class);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getModule(),SysDictionaryEntity.class);
            temp.setModule(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

            SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getResult(),SysDictionaryEntity.class);
            temp.setResult(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());

            SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getMethod(),SysDictionaryEntity.class);
            temp.setMethod(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            list.set(i,temp);
        }
        webResponse.setData(list);

        return webResponse;
    }

    public WebResponse selectDataCollectExportByBatch(String[] batchs){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectExportEntity> resultList = dataCollectionMapper.selectDataCollectByBatch(batchs);
        for (int i=0;i<resultList.size();i++){
            DataCollectExportEntity temp = resultList.get(i);

            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getReduceStatus(),SysDictionaryEntity.class);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getModule(),SysDictionaryEntity.class);
            temp.setModule(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

            SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getResult(),SysDictionaryEntity.class);
            temp.setResult(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());

            SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getMethod(),SysDictionaryEntity.class);
            temp.setMethod(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());

            SysDictionaryEntity sysDictionaryEntity6 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            temp.setAccountAge(sysDictionaryEntity6==null?"":sysDictionaryEntity6.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            resultList.set(i,temp);
        }
        webResponse.setData(resultList);

        return webResponse;
    }

    public WebResponse selectDataCollectExportByCase(String[] caseIds){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectExportEntity> resultList = dataCollectionMapper.selectDataCollectExportByCase(caseIds);
        for (int i=0;i<resultList.size();i++){
            DataCollectExportEntity temp = resultList.get(i);

            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getReduceStatus(),SysDictionaryEntity.class);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getModule(),SysDictionaryEntity.class);
            temp.setModule(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

            SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getResult(),SysDictionaryEntity.class);
            temp.setResult(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());

            SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getMethod(),SysDictionaryEntity.class);
            temp.setMethod(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());

            SysDictionaryEntity sysDictionaryEntity6 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getAccountAge(),SysDictionaryEntity.class);
            temp.setAccountAge(sysDictionaryEntity6==null?"":sysDictionaryEntity6.getName());

            SysDictionaryEntity sysDictionaryEntity7 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getContractType(),SysDictionaryEntity.class);
            temp.setContractType(sysDictionaryEntity7==null?"":sysDictionaryEntity7.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            resultList.set(i,temp);
        }
        webResponse.setData(resultList);

        return webResponse;
    }

    public WebResponse pageDataCollectExport(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = bean.getClients();
        if (clients == null || clients.length==0 || StringUtils.isEmpty(clients[0])){
            bean.setClientFlag(null);
        }else{
            bean.setClientFlag("1");
        }
        String[] odvs = bean.getOdvs();
        if (odvs == null || odvs.length==0 || StringUtils.isEmpty(odvs[0])){
            bean.setOdvFlag(null);
        }else{
            bean.setOdvFlag("1");
        }
        String[] batchs = bean.getBatchNos();
        if (batchs == null || batchs.length==0 || StringUtils.isEmpty(batchs[0])){
            bean.setBatchFlag(null);
        }else{
            bean.setBatchFlag("1");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(bean.getOrderBy())){
            bean.setOrderBy("id");
            bean.setSort("desc");
        }else {
            bean.setOrderBy(CollectSortEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        List<DataCollectExportEntity> list = dataCollectionMapper.pageDataCollectExport(bean);
        for (int i=0;i<list.size();i++){
            DataCollectExportEntity temp = list.get(i);

            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getReduceStatus(),SysDictionaryEntity.class);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity sysDictionaryEntity2 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity sysDictionaryEntity3 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getModule(),SysDictionaryEntity.class);
            temp.setModule(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

            SysDictionaryEntity sysDictionaryEntity4 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getResult(),SysDictionaryEntity.class);
            temp.setResult(sysDictionaryEntity4==null?"":sysDictionaryEntity4.getName());

            SysDictionaryEntity sysDictionaryEntity5 =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getMethod(),SysDictionaryEntity.class);
            temp.setMethod(sysDictionaryEntity5==null?"":sysDictionaryEntity5.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            list.set(i,temp);
        }
        webResponse.setData(PageInfo.of(list));

        return webResponse;
    }


    public WebResponse detailCollect(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> list = new ArrayList<DataCollectionEntity>();
        if (StringUtils.isEmpty(bean.getDetaiType()) || bean.getDetaiType().equals("1")){//本案催記
            list = dataCollectionMapper.detailCollect1(bean);
        }else if (bean.getDetaiType().equals("2")){ //同批次公债催记
            list = dataCollectionMapper.detailCollect2(bean);
        }else if (bean.getDetaiType().equals("3")){//公债催记
            list = dataCollectionMapper.detailCollect3(bean);
        }else if (bean.getDetaiType().equals("4")){//同卡催记
            list = dataCollectionMapper.detailCollect4(bean);
        }
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());

            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getTelType(),SysDictionaryEntity.class);
            temp.setTelType(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity relationEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getRelation(),SysDictionaryEntity.class);
            temp.setRelation(relationEntity==null?temp.getRelation():relationEntity.getName());

            SysDictionaryEntity collectStatusEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getCollectStatus(),SysDictionaryEntity.class);
            temp.setCollectStatusMsg(collectStatusEntity==null?"":collectStatusEntity.getName());

            SysDictionaryEntity reduceStatusEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getReduceStatus(),SysDictionaryEntity.class);
            temp.setReduceStatusMsg(reduceStatusEntity==null?"":reduceStatusEntity.getName());

            SysDictionaryEntity methodEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getMethod(),SysDictionaryEntity.class);
            temp.setMethodMsg(methodEntity==null?"":methodEntity.getName());

            SysDictionaryEntity telTypeEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getTelType(),SysDictionaryEntity.class);
            temp.setTelType(telTypeEntity==null?"":telTypeEntity.getName());

            SysDictionaryEntity resultEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getResult(),SysDictionaryEntity.class);
            temp.setResult(resultEntity==null?temp.getResult():resultEntity.getName());

            temp.setRepayAmtMsg(temp.getRepayAmt()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(temp.getRepayAmt()+""));
            temp.setReduceAmtMsg(temp.getReduceAmt()==null?"￥0.00": "￥"+ FmtMicrometer.fmtMicrometer(temp.getReduceAmt()+""));

            list.set(i,temp);
        }
         webResponse.setData(list);
        return webResponse;
    }
    public WebResponse detailTelCurentCollect(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> list = new ArrayList<DataCollectionEntity>();
        if(StringUtils.isEmpty(bean.getDetaiType()) || bean.getDetaiType().equals("1")){
            list = dataCollectionMapper.detailTelCurentCollect1(bean);
        }else if (bean.getDetaiType().equals("2")){
            list = dataCollectionMapper.detailTelCurentCollect2(bean);
        }
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user==null?"":user.getUserName());
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+temp.getTelType(),SysDictionaryEntity.class);
            temp.setTelType(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            list.set(i,temp);
        }
        webResponse.setCode("100");
        webResponse.setData(list);
        return webResponse;
    }
}
