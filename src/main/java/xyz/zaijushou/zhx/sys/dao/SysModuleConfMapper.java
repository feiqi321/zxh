package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysModuleConf;

import java.util.List;

@Mapper
public interface SysModuleConfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysModuleConf record);

    int insertSelective(SysModuleConf record);

    SysModuleConf selectByPrimaryKey(Integer id);

    List<SysModuleConf> findAllConf(SysModuleConf record);

    int updateByPrimaryKeySelective(SysModuleConf record);

    int updateByPrimaryKey(SysModuleConf record);
}