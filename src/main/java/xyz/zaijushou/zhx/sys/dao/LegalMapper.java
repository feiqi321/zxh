package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.LegalEntity;

import java.util.List;

public interface LegalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LegalEntity record);

    int insertSelective(LegalEntity record);

    LegalEntity selectByPrimaryKey(Integer id);

    List<LegalEntity> pageDataLegal(LegalEntity record);

    int countDataLegal(LegalEntity record);

    int updateByPrimaryKeySelective(LegalEntity record);

    int updateByPrimaryKey(LegalEntity record);
}