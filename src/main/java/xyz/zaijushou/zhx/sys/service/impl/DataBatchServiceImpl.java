package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataBatchMapper;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.BatchResponse;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataBatchService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataBatchServiceImpl implements DataBatchService {

    @Resource
    private DataBatchMapper dataBatchMapper;
    @Resource
    private SysDictionaryMapper dictionaryMapper;

    public void save(DataBatchEntity bean){
        JSONObject tokenData = JwtTokenUtil.tokenData();
        bean.setCreatUser(tokenData.getInteger("userId")==null?"":tokenData.getInteger("userId").toString());
        dataBatchMapper.saveBatch(bean);
    }

    public void update(DataBatchEntity bean){
        dataBatchMapper.updateBatch(bean);
    }

    public void returnCase(DataBatchEntity bean){
        dataBatchMapper.returnCase(bean);
    }

    public void deleteById(DataBatchEntity bean){
        dataBatchMapper.deleteById(bean.getId());
    }


    public WebResponse pageDataBatch(DataBatchEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataBatchEntity> dataCaseEntities = dataBatchMapper.pageDataBatch(bean);
        for (int i=0;i<dataCaseEntities.size();i++){
            DataBatchEntity dataBatchEntity = dataCaseEntities.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataBatchEntity.getCreatUser(), SysUserEntity.class);
            if (dataBatchEntity.getClient()==null || dataBatchEntity.getClient().equals("")){
                dataBatchEntity.setClient("");
            }else {
                List<SysDictionaryEntity> dictList = dictionaryMapper.getDataById(Integer.parseInt(dataBatchEntity.getClient()));
                if (dictList.size() > 0) {
                    SysDictionaryEntity sysDictionaryEntity = dictList.get(0);
                    dataBatchEntity.setClient(sysDictionaryEntity.getName());
                }
            }
            if (dataBatchEntity.getCaseType()==null || dataBatchEntity.getCaseType().equals("")){
                dataBatchEntity.setCaseType("");
            }else {
                List<SysDictionaryEntity> caseTypeList = dictionaryMapper.getDataById(Integer.parseInt(dataBatchEntity.getCaseType()));
                if (caseTypeList.size() > 0) {
                    SysDictionaryEntity sysDictionaryEntity = caseTypeList.get(0);
                    dataBatchEntity.setCaseType(sysDictionaryEntity.getName());
                }
            }
            if(dataBatchEntity.getBatchStatus()==1){
                dataBatchEntity.setStatusMsg("未退案");
            }else if (dataBatchEntity.getBatchStatus()==2){
                dataBatchEntity.setStatusMsg("已退案");
            }else{
                dataBatchEntity.setStatusMsg("未导入");
            }
            dataBatchEntity.setCreatUser(user==null?"":user.getUserName());

            dataCaseEntities.set(i,dataBatchEntity);
        }

        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setPageInfo(PageInfo.of(dataCaseEntities));
        batchResponse.setTotalAmt(new BigDecimal(0));
        batchResponse.setUserCount(0);
        webResponse.setData(batchResponse);

        return webResponse;
    }

    public DataBatchEntity getDataById(DataBatchEntity bean){
        return dataBatchMapper.selectBatchById(bean.getId());
    }

    public WebResponse selectBatchNo(DataBatchEntity bean){
        List<DataBatchEntity> list = dataBatchMapper.selectBatchNo(bean);
        return WebResponse.success(list);
    }


    public WebResponse totalDataBatch(DataBatchEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataBatchEntity> dataCaseEntities = dataBatchMapper.pageDataBatch(bean);
        for (int i=0;i<dataCaseEntities.size();i++){
            DataBatchEntity dataBatchEntity = dataCaseEntities.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataBatchEntity.getCreatUser(), SysUserEntity.class);
            if (dataBatchEntity.getClient()==null || dataBatchEntity.getClient().equals("")){
                dataBatchEntity.setClient("");
            }else {
                List<SysDictionaryEntity> dictList = dictionaryMapper.getDataById(Integer.parseInt(dataBatchEntity.getClient()));
                if (dictList.size() > 0) {
                    SysDictionaryEntity sysDictionaryEntity = dictList.get(0);
                    dataBatchEntity.setClient(sysDictionaryEntity.getName());
                }
            }
            if (dataBatchEntity.getCaseType()==null || dataBatchEntity.getCaseType().equals("")){
                dataBatchEntity.setCaseType("");
            }else {
                List<SysDictionaryEntity> caseTypeList = dictionaryMapper.getDataById(Integer.parseInt(dataBatchEntity.getCaseType()));
                if (caseTypeList.size() > 0) {
                    SysDictionaryEntity sysDictionaryEntity = caseTypeList.get(0);
                    dataBatchEntity.setCaseType(sysDictionaryEntity.getName());
                }
            }
            if(dataBatchEntity.getBatchStatus()==1){
                dataBatchEntity.setStatusMsg("未退案");
            }else if (dataBatchEntity.getBatchStatus()==2){
                dataBatchEntity.setStatusMsg("已退案");
            }else{
                dataBatchEntity.setStatusMsg("未导入");
            }
            dataBatchEntity.setCreatUser(user==null?"":user.getUserName());

            dataCaseEntities.set(i,dataBatchEntity);
        }

        BatchResponse batchResponse = new BatchResponse();
        batchResponse.setPageInfo(PageInfo.of(dataCaseEntities));
        batchResponse.setTotalAmt(new BigDecimal(0));
        batchResponse.setUserCount(0);
        webResponse.setData(batchResponse);

        return webResponse;
    }




    public WebResponse selectDataBatch(List<DataBatchEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataBatchEntity> resultList = new ArrayList<DataBatchEntity>();
        for (int i=0;i<list.size();i++){
            DataBatchEntity dataBatchEntity = dataBatchMapper.selectDataBatch(list.get(i));
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataBatchEntity.getCreatUser(), SysUserEntity.class);
            if (dataBatchEntity.getClient()==null || dataBatchEntity.getClient().equals("")){
                dataBatchEntity.setClient("");
            }else {
                List<SysDictionaryEntity> dictList = dictionaryMapper.getDataById(Integer.parseInt(dataBatchEntity.getClient()));
                if (dictList.size() > 0) {
                    SysDictionaryEntity sysDictionaryEntity = dictList.get(0);
                    dataBatchEntity.setClient(sysDictionaryEntity.getName());
                }
            }
            if (dataBatchEntity.getCaseType()==null || dataBatchEntity.getCaseType().equals("")){
                dataBatchEntity.setCaseType("");
            }else {
                List<SysDictionaryEntity> caseTypeList = dictionaryMapper.getDataById(Integer.parseInt(dataBatchEntity.getCaseType()));
                if (caseTypeList.size() > 0) {
                    SysDictionaryEntity sysDictionaryEntity = caseTypeList.get(0);
                    dataBatchEntity.setCaseType(sysDictionaryEntity.getName());
                }
            }
            if(dataBatchEntity.getBatchStatus()==1){
                dataBatchEntity.setStatusMsg("未退案");
            }else if (dataBatchEntity.getBatchStatus()==2){
                dataBatchEntity.setStatusMsg("已退案");
            }else{
                dataBatchEntity.setStatusMsg("未导入");
            }
            dataBatchEntity.setCreatUser(user==null?"":user.getUserName());

            resultList.add(dataBatchEntity);
        }

        webResponse.setData(resultList);

        return webResponse;
    }
}
