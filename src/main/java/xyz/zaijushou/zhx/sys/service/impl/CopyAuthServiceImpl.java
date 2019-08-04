package xyz.zaijushou.zhx.sys.service.impl;

import org.apache.tools.ant.taskdefs.Copy;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.CopyAuthMapper;
import xyz.zaijushou.zhx.sys.entity.CopyAuth;
import xyz.zaijushou.zhx.sys.service.CopyAuthService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/8/3.
 */
@Service
public class CopyAuthServiceImpl implements CopyAuthService {

    @Resource
    private CopyAuthMapper copyAuthMapper;

    public List<CopyAuth> list(){
        List<CopyAuth> list = copyAuthMapper.list();
        for (int i=0;i<list.size();i++){
            CopyAuth copyAuth = list.get(i);
            if (copyAuth.getStatus()==1){
                copyAuth.setStatusMsg("可以复制");
            }else if(copyAuth.getStatus()==2){
                copyAuth.setStatusMsg("不可以复制");
            }
            list.set(i,copyAuth);
        }
        return list;
    }


    public void update(CopyAuth copyAuth){
        copyAuthMapper.update(copyAuth);
    }

}
