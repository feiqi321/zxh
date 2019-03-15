package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysPasswordEntity;

@Mapper
public interface SysPasswordMapper {
    SysPasswordEntity selectPassword();
}
