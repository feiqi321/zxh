package xyz.zaijushou.zhx.sys.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import xyz.zaijushou.zhx.sys.entity.CallCenter;

/**
 * Created by looyer on 2019/4/19.
 */
@Mapper
public interface TelIpManageMapper {
    public List<CallCenter> queryCallCenters();

    public void updateCallCenter(CallCenter telIpManage);

	public void deleteCallCenters(List<Integer> callCenterIDs);

	public void addCallCenter(CallCenter callCenter);

	public CallCenter queryCallCenter(Integer callCenterID);
}
