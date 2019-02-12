package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.LegalMapper;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.LegalEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.LegalService;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by looyer on 2019/2/12.
 */
@Service
public class LegalServiceImpl implements LegalService {
    @Resource
    private LegalMapper legalMapper;

    public void saveLegal(LegalEntity bean){
        legalMapper.insertSelective(bean);
    }

    public void updateLegal(LegalEntity bean){
        legalMapper.updateByPrimaryKeySelective(bean);
    }

    public WebResponse pageDataLegal(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<LegalEntity> dataCaseEntities = legalMapper.pageDataLegal(bean);
        int count = legalMapper.countDataLegal(bean);

        int totalPageNum = 0 ;
        if (count%bean.getPageSize()>0){
            totalPageNum = count/bean.getPageSize()+1;
        }else{
            totalPageNum = count/bean.getPageSize();
        }
        webResponse.setData(dataCaseEntities);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setTotalNum(count);
        return webResponse;
    }

    public void deleteLegal(LegalEntity bean){
        legalMapper.deleteByPrimaryKey(bean.getId());
    }

}
