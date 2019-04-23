package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysPercent;

import java.util.List;

/**
 * Created by looyer on 2019/3/29.
 */
@Mapper
public interface SysPercentMapper {

    public void updatePercent(SysPercent sysPercent);

    public List<SysPercent> listPencent();

    public SysPercent findByClient(SysPercent bean);

    public SysPercent findRemark();

    public void updateRemark(SysPercent bean);

}
