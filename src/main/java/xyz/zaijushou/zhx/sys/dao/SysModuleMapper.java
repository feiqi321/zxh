package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysModule;

import java.util.List;

/**
 * Created by looyer on 2019/2/15.
 */
@Mapper
public interface SysModuleMapper {


    public List<SysModule> findAll();

    public SysModule selectModuleById(SysModule sysModule);

    public void deleteById(SysModule sysModule);

    public void saveModule(SysModule sysModule);

    public void saveContext(SysModule sysModule);

}
