package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.TelIpManageMapper;
import xyz.zaijushou.zhx.sys.entity.CallCenter;
import xyz.zaijushou.zhx.sys.service.TelIpManageService;

import java.util.List;

import javax.annotation.Resource;

/**
 * Created by looyer on 2019/4/19.
 */
@Service
public class TelIpManageServiceImpl implements TelIpManageService {

    @Resource
    private TelIpManageMapper telIpManageMapper;

    @Override
    public List<CallCenter> queryCallCenters(){
        return telIpManageMapper.queryCallCenters();
    }

    @Override
    public void updateCallCenter(CallCenter callCenter){
        telIpManageMapper.updateCallCenter(callCenter);
    }

    @Override
    public void deleteCallCenters(List<Integer> callCenterIDs) {
        telIpManageMapper.deleteCallCenters(callCenterIDs);
    }

    @Override
    public void addCallCenter(CallCenter callCenter) {
        telIpManageMapper.addCallCenter(callCenter);
    }

    @Override
    public CallCenter queryCallCenter(Integer callCenterID) {
        return telIpManageMapper.queryCallCenter(callCenterID);
    }

}
