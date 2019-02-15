package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.Synergy;

public interface SynergyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Synergy record);

    int insertSelective(Synergy record);

    Synergy selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Synergy record);

    int updateByPrimaryKey(Synergy record);
}