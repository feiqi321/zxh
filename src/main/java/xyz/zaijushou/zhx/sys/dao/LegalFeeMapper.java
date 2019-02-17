package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.LegalFee;

import java.util.List;

public interface LegalFeeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LegalFee record);

    int insertSelective(LegalFee record);

    LegalFee selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LegalFee record);

    int updateByPrimaryKey(LegalFee record);

    List<LegalFee> findAllLegalFee(LegalFee record);
}