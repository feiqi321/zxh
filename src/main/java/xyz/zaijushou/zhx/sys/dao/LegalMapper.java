package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.LegalEntity;

import java.util.List;
@Mapper
public interface LegalMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LegalEntity record);

    int insertSelective(LegalEntity record);

    LegalEntity selectByPrimaryKey(Integer id);

    List<LegalEntity> pageDataLegal(LegalEntity record);

    List<LegalEntity> listLegal(LegalEntity record);

    int countDataLegal(LegalEntity record);

    int updateByPrimaryKeySelective(LegalEntity record);

    int updateByPrimaryKey(LegalEntity record);

    void checkLegal(LegalEntity record);
}