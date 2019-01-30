package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.DataBatchMapper;
import xyz.zaijushou.zhx.sys.entity.DataBatchEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataBatchService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataBatchServiceImpl implements DataBatchService {

    @Resource
    private DataBatchMapper dataBatchMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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


    public List<DataBatchEntity> pageDataBatch(DataBatchEntity bean){
        List<DataBatchEntity> dataCaseEntities = dataBatchMapper.pageDataBatch(bean);
        for (int i=0;i<dataCaseEntities.size();i++){
            DataBatchEntity dataBatchEntity = dataCaseEntities.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ dataBatchEntity.getCreatUser(), SysUserEntity.class);
            dataBatchEntity.setCreatUser(user==null?"":user.getUserName());
            dataBatchEntity.setTotalAmt(new BigDecimal(100));
            dataCaseEntities.set(i,dataBatchEntity);
        }
        return dataCaseEntities;
    }

    public DataBatchEntity getDataById(DataBatchEntity bean){
        return dataBatchMapper.selectBatchById(bean.getId());
    }

}
