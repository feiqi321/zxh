package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.Letter;
import xyz.zaijushou.zhx.sys.entity.LetterExportEntity;

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

    List<Letter> pageDataLetterInfo(Letter record);

    List<Letter> pageDataLetter2(Letter record);

    List<Letter> findByCaseId(Letter letter);


    int updateByPrimaryKeySelective(Letter record);

    int updateByPrimaryKey(Letter record);

    List<LetterExportEntity> pageExportList(Letter letter);

    List<LetterExportEntity> totalExportList(Letter letter);

    List<LetterExportEntity> pageInfoExportList(Letter letter);

    List<LetterExportEntity> totalInfoExportList(Letter letter);
}