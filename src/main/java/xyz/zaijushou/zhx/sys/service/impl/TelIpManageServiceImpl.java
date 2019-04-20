package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.TelIpManageMapper;
import xyz.zaijushou.zhx.sys.entity.TelIpManage;
import xyz.zaijushou.zhx.sys.service.TelIpManageService;

import javax.annotation.Resource;

/**
 * Created by looyer on 2019/4/19.
 */
@Service
public class TelIpManageServiceImpl implements TelIpManageService {

    @Resource
    private TelIpManageMapper telIpManageMapper;

    @Override
    public TelIpManage findOne(){
        return telIpManageMapper.findOne();
    }

    @Override
    public void update(TelIpManage telIpManage){
        telIpManageMapper.update(telIpManage);
    }

}
