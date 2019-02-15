package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysModule;

import java.util.List;

/**
 * Created by looyer on 2019/2/15.
 */
public interface SysModuleService {


    public List<SysModule> findAll();

    public SysModule selectModuleById(SysModule sysModule);

    public void deleteById(SysModule sysModule);

    public void saveModule(SysModule sysModule);

    public void saveContext(SysModule sysModule);

}
