package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.ManagePercentage;
import xyz.zaijushou.zhx.sys.entity.OdvPercentage;

import java.util.List;

/**
 * Created by looyer on 2019/5/25.
 */
@Mapper
public interface ManageMapper {

    public void save(ManagePercentage bean);

    public void update(ManagePercentage bean);

    public List<ManagePercentage> list(ManagePercentage bean);


}
