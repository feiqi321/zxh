package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.Letter;

import java.util.List;

public interface LetterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Letter record);

    int insertSelective(Letter record);

    Letter selectByPrimaryKey(Integer id);

    List<Letter> pageDataLetter(Letter record);

    int updateByPrimaryKeySelective(Letter record);

    int updateByPrimaryKey(Letter record);
}