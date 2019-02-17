package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.LegalHandle;

import java.util.List;

public interface LegalHandleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LegalHandle record);

    int insertSelective(LegalHandle record);

    LegalHandle selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LegalHandle record);

    int updateByPrimaryKey(LegalHandle record);

    List<LegalHandle> findALlLegalHandle(LegalHandle record);
}