package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.TelIpManage;

/**
 * Created by looyer on 2019/4/19.
 */
@Mapper
public interface TelIpManageMapper {

    public TelIpManage findOne();


    public void update(TelIpManage telIpManage);
}
