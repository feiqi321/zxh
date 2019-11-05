package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;

import xyz.zaijushou.zhx.sys.entity.SysConfig;

/**
 * SysConfigMapper
 */
@Mapper
public interface SysConfigMapper {
    SysConfig queryConfig(String cfgtitle);

    void updateConfig(String cfgtitle,String cfgvalue);
}