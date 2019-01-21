package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysAuthorityEntity;

import java.util.List;

@Mapper
public interface SysAuthorityMapper {
    List<SysAuthorityEntity> listAllAuthorities(SysAuthorityEntity sysAuthorityEntity);
}
