package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysModuleMapper;
import xyz.zaijushou.zhx.sys.entity.SysModule;
import xyz.zaijushou.zhx.sys.service.SysModuleService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/2/15.
 */
@Service
public class SysModuleServiceImpl implements SysModuleService {

    @Resource
    private SysModuleMapper sysModuleMapper;


    public List<SysModule> findAll(){
        return sysModuleMapper.findAll();
    }

    public SysModule selectModuleById(SysModule sysModule){
        return sysModuleMapper.selectModuleById(sysModule);
    }

    public void deleteById(SysModule sysModule){
        sysModuleMapper.deleteById(sysModule);
    }

    public void saveModule(SysModule sysModule){
        if (sysModule.getId()==null ||  sysModule.getId()==0) {
            sysModuleMapper.saveModule(sysModule);
        }else{
            sysModuleMapper.saveContext(sysModule);
        }
    }

    public void saveContext(SysModule sysModule){
        sysModuleMapper.saveContext(sysModule);
    }

}
