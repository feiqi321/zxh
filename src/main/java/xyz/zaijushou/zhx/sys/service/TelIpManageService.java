package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.TelIpManage;

/**
 * Created by looyer on 2019/4/19.
 */
public interface TelIpManageService {

    public TelIpManage findOne();


    public void update(TelIpManage telIpManage);

}
