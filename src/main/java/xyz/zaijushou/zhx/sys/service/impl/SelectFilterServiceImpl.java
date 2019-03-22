package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SelectFilterMapper;
import xyz.zaijushou.zhx.sys.entity.SelectFilterEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SelectFilterService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;

/**
 * Created by looyer on 2019/3/21.
 */
@Service
public class SelectFilterServiceImpl implements SelectFilterService {

    @Resource
    private SelectFilterMapper selectFilterMapper;


    public void save(SelectFilterEntity bean){
        bean.setUserId(getUserInfo().getId());
        selectFilterMapper.saveFilter(bean);
    }

    public SelectFilterEntity selectByModule(SelectFilterEntity bean){
        bean.setUserId(getUserInfo().getId());
        return selectFilterMapper.selectByModule(bean);
    }
    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }
}
