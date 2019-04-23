package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysPercent;

import java.util.List;

/**
 * Created by looyer on 2019/3/29.
 */
public interface SysPercentService {

    public void updatePercent(List<SysPercent> list);

    public List<SysPercent> listPencent();

    public SysPercent findRemark();

    public void updateRemark(SysPercent bean);

}
