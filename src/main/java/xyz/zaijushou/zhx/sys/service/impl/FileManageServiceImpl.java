package xyz.zaijushou.zhx.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataCaseAddressMapper;
import xyz.zaijushou.zhx.sys.dao.DataCaseTelMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseTelEntity;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/2/12.
 */
@Service
public class FileManageServiceImpl implements FileManageService {
    @Resource
    private DataCaseTelMapper dataCaseTelMapper;
    @Resource
    private DataCaseAddressMapper dataCaseAddressMapper;

    public WebResponse batchCaseTel(List<DataCaseTelEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        for (int i=0;i<list.size();i++){
            DataCaseTelEntity dataCaseTelEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseTelEntity.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getCardNo()+"@"+dataCaseTelEntity.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelMapper.saveTel(dataCaseTelEntity);
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelMapper.saveTel(dataCaseTelEntity);
                }
            }

        }

        webResponse.setCode("100");
        return webResponse;
    }


    public WebResponse batchCaseAddress(List<DataCaseAddressEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        for (int i=0;i<list.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseAddressEntity.getSeqNo())){
                DataCaseAddressEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getCardNo()+"@"+dataCaseAddressEntity.getCaseDate(),DataCaseAddressEntity.class);
                if (temp!=null){
                    dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
                }
            }else{
                DataCaseAddressEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getSeqNo(),DataCaseAddressEntity.class);
                if (temp!=null){
                    dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
                }
            }

        }

        webResponse.setCode("100");
        return webResponse;
    }
}
