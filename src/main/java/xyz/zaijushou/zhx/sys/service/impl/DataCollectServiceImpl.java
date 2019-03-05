package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
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
    @Autowired
    private SysDictionaryService sysDictionaryService;

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
        bean.setOrderBy(CollectSortEnum.getEnumByKey(bean.getOrderBy()).getValue());
        List<DataCollectionEntity> list = dataCollectionMapper.pageDataCollect(bean);
        List<DataCollectionEntity> resultList = new ArrayList<DataCollectionEntity>();
        for (int i=0;i<list.size();i++){
            DataCollectionEntity temp = list.get(i);
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysDictionaryEntity dictionary3 = new SysDictionaryEntity();
            dictionary3.setId(temp.getMethod());
            SysDictionaryEntity sysDictionaryEntity3 = sysDictionaryService.getDataById(dictionary3);
            temp.setMethodMsg(sysDictionaryEntity3==null?"":sysDictionaryEntity3.getName());

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
        bean.setOrderBy(CollectSortEnum.getEnumByKey(bean.getOrderBy()).getValue());
        List<DataCollectExportEntity> list = dataCollectionMapper.totalDataCollect(bean);
        for (int i=0;i<list.size();i++){
            DataCollectExportEntity temp = list.get(i);
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

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
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

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
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdvName(user==null?"":user.getUserName());
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
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdvName(user==null?"":user.getUserName());
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
        bean.setOrderBy(CollectSortEnum.getEnumByKey(bean.getOrderBy()).getValue());
        List<DataCollectExportEntity> list = dataCollectionMapper.pageDataCollectExport(bean);
        for (int i=0;i<list.size();i++){
            DataCollectExportEntity temp = list.get(i);
            SysDictionaryEntity dictionary1 = new SysDictionaryEntity();
            dictionary1.setId(temp.getReduceStatus());
            SysDictionaryEntity sysDictionaryEntity = sysDictionaryService.getDataById(dictionary1);
            temp.setReduceStatusMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());

            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());

            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdvName(user==null?"":user.getUserName());
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
         webResponse.setData(list);
        return webResponse;
    }
    public WebResponse detailTelCollect(DataCollectionEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCollectionEntity> list = new ArrayList<DataCollectionEntity>();
        if(StringUtils.isEmpty(bean.getDetaiType()) || bean.getDetaiType().equals("1")){

        }else if (bean.getDetaiType().equals("2")){

        }
        return webResponse;
    }
}
