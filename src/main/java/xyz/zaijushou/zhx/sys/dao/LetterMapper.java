package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.Letter;

import java.util.List;

public interface LetterMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Letter record);

    int insertSelective(Letter record);

    void confirmSynergy(Letter record);

    void cancelLetter(Letter record);

    void confirmLetter(Letter record);

    Letter selectByPrimaryKey(Integer id);

    List<Letter> pageDataLetter(Letter record);

    List<Letter> pageDataLetter2(Letter record);


    int updateByPrimaryKeySelective(Letter record);

    int updateByPrimaryKey(Letter record);
}