package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.Letter;

public interface LetterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Letter record);

    int insertSelective(Letter record);

    Letter selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Letter record);

    int updateByPrimaryKey(Letter record);
}