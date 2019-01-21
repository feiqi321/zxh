package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysButtonEntity;
import xyz.zaijushou.zhx.sys.entity.SysToButtonAuthority;

import java.util.List;

@Mapper
public interface SysButtonMapper {
    List<SysButtonEntity> listAllButtons(SysButtonEntity sysButtonEntity);

    List<SysToButtonAuthority> listAllButtonAuthorities(SysToButtonAuthority sysToButtonAuthority);
}
