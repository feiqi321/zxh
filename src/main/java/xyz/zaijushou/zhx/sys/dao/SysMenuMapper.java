package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysMenuEntity;

import java.util.List;

@Mapper
public interface SysMenuMapper {
    List<SysMenuEntity> listAllMenus(SysMenuEntity sysMenuEntity);
}
