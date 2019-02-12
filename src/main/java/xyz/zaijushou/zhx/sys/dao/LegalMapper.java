package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.Legal;

public interface LegalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Legal record);

    int insertSelective(Legal record);

    Legal selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Legal record);

    int updateByPrimaryKey(Legal record);
}