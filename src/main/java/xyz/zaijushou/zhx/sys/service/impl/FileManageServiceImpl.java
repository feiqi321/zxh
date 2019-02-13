package xyz.zaijushou.zhx.sys.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ColorEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
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
    @Resource
    private DataCaseInterestMapper dataCaseInterestMapper;
    @Resource
    private DataCaseMapper dateCaseMapper;
    @Resource
    private DataArchiveMapper dataArchiveMapper;

    public WebResponse batchCaseTel(List<DataCaseTelEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        for (int i=0;i<list.size();i++){
            DataCaseTelEntity dataCaseTelEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseTelEntity.getSeqNo())){
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getCardNo()+"@"+dataCaseTelEntity.getCaseDate(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
                    dataCaseTelMapper.saveTel(dataCaseTelEntity);
                }
            }else{
                DataCaseEntity dataCaseEntity = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseTelEntity.getSeqNo(),DataCaseEntity.class);
                if (dataCaseEntity!=null){
                    dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
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
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getCardNo()+"@"+dataCaseAddressEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseAddressEntity.getSeqNo(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
                }
            }

        }

        webResponse.setCode("100");
        return webResponse;
    }

    public WebResponse batchCaseInterest(List<DataCaseInterestEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();
        for (int i=0;i<list.size();i++){
            DataCaseInterestEntity dataCaseInterestEntity = list.get(i);
            if (StringUtils.isEmpty(dataCaseInterestEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseInterestEntity.getCardNo()+"@"+dataCaseInterestEntity.getCaseDate(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseInterestMapper.saveInterest(dataCaseInterestEntity);
                }
            }else{
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseInterestEntity.getSeqNo(),DataCaseEntity.class);
                if (temp!=null){
                    dataCaseInterestMapper.saveInterest(dataCaseInterestEntity);
                }
            }

        }

        webResponse.setCode("100");
        return webResponse;
    }

    public WebResponse batchCaseComment(List<DataCaseEntity> list){
        WebResponse webResponse = WebResponse.buildResponse();

        for (int i=0;i<list.size();i++){
            DataCaseEntity dataCaseEntity = list.get(i);
            if (StringUtils.isNotEmpty(dataCaseEntity.getSeqNo())){
                DataCaseEntity temp = RedisUtils.entityGet(RedisKeyPrefix.DATA_CASE+dataCaseEntity.getSeqNo(),DataCaseEntity.class);
                if (temp!=null){
                    if (dataCaseEntity.getColor().equals("0")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("黑").getValue());
                    }else if (dataCaseEntity.getColor().equals("1")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("红").getValue());
                    }else if (dataCaseEntity.getColor().equals("2")){
                        dataCaseEntity.setColor(ColorEnum.getEnumByKey("蓝").getValue());
                    }
                    dateCaseMapper.updateComment(dataCaseEntity);
                }
            }else{

            }

        }


        webResponse.setCode("100");
        return webResponse;
    }

    public WebResponse batchArchive(List<DataArchiveEntity> list){

        WebResponse webResponse = WebResponse.buildResponse();

        for (int i=0;i<list.size();i++){
            DataArchiveEntity dataArchiveEntity = list.get(i);
            dataArchiveMapper.saveArchive(dataArchiveEntity);

        }


        webResponse.setCode("100");
        return webResponse;
    }


}
