package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysModuleConf;

@Mapper
public interface SysModuleConfMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysModuleConf record);

    int insertSelective(SysModuleConf record);

    SysModuleConf selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysModuleConf record);

    int updateByPrimaryKey(SysModuleConf record);
}