package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SelectFilterMapper;
import xyz.zaijushou.zhx.sys.entity.SelectFilterEntity;
import xyz.zaijushou.zhx.sys.service.SelectFilterService;

import javax.annotation.Resource;

/**
 * Created by looyer on 2019/3/21.
 */
@Service
public class SelectFilterServiceImpl implements SelectFilterService {

    @Resource
    private SelectFilterMapper selectFilterMapper;


    public void save(SelectFilterEntity bean){
        selectFilterMapper.saveFilter(bean);
    }

    public SelectFilterEntity selectByModule(SelectFilterEntity bean){
        return selectFilterMapper.selectByModule(bean);
    }

}
