package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.FollowUpSetMapper;
import xyz.zaijushou.zhx.sys.entity.FollowUpData;
import xyz.zaijushou.zhx.sys.service.FollowUpSetService;

import javax.annotation.Resource;

/**
 * @author lsl
 * @version [1.0.0, 2019/10/16,21:18]
 */
@Service
public class FollowUpSetServiceImpl implements FollowUpSetService {
    @Resource
    private FollowUpSetMapper followUpSetMapper;

    @Override
    public FollowUpData find(){
       FollowUpData followUpData = followUpSetMapper.find();
            if (followUpData.getStatus()==1){
                followUpData.setStatusMsg("显示");
            }else if(followUpData.getStatus()==2){
                followUpData.setStatusMsg("不显示");
            }
        return followUpData;
    }

    @Override
    public void update(FollowUpData followUpData){
        followUpSetMapper.update(followUpData);
    }
}
