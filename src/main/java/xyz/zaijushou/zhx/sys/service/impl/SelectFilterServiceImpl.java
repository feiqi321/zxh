package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SelectFilterMapper;
import xyz.zaijushou.zhx.sys.entity.SelectFilterEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SelectFilterService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/3/21.
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SelectFilterServiceImpl implements SelectFilterService {

    @Resource
    private SelectFilterMapper selectFilterMapper;


    public void save(SelectFilterEntity bean){
        bean.setUserId(getUserInfo().getId());
        SelectFilterEntity selectFilterEntity =  selectFilterMapper.selectByModule(bean);
        if (selectFilterEntity==null) {
            selectFilterMapper.saveFilter(bean);
        }else{
            selectFilterMapper.updateFilter(bean);
        }
    }

    public SelectFilterEntity selectByModule(SelectFilterEntity bean){
        bean.setUserId(getUserInfo().getId());
        return selectFilterMapper.selectByModule(bean);
    }

    @Override
    public void updateModule(SelectFilterEntity bean) {
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        bean.setUserId(userId);
        Integer count=selectFilterMapper.updateTable(bean);
        if (count==0){
            selectFilterMapper.saveFilter(bean);
        }
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }
}
