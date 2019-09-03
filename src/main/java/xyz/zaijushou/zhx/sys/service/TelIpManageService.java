package xyz.zaijushou.zhx.sys.service;

import java.util.List;

import xyz.zaijushou.zhx.sys.entity.CallCenter;

/**
 * Created by looyer on 2019/4/19.
 */
public interface TelIpManageService {

    public List<CallCenter> queryCallCenters();

    public void updateCallCenter(CallCenter callCenter);

	public void deleteCallCenters(List<Integer> callCenterIDs);

	public void addCallCenter(CallCenter callCenter);

	public CallCenter queryCallCenter(Integer callCenterID);
}
