package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.CaseTimeAreaMapper;
import xyz.zaijushou.zhx.sys.entity.CaseTimeAreaEntity;
import xyz.zaijushou.zhx.sys.service.CaseTimeAreaService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class CaseTimeAreaServiceImpl implements CaseTimeAreaService {

    @Resource
    private CaseTimeAreaMapper caseTimeAreaMapper;

    @Override
    public void save(CaseTimeAreaEntity bean){
        caseTimeAreaMapper.saveOrg(bean);
    }

    @Override
    public void update(CaseTimeAreaEntity bean){
        caseTimeAreaMapper.updateOrg(bean);
    }

    @Override
    public WebResponse findAll(){
        WebResponse webResponse = WebResponse.buildResponse();
        List<CaseTimeAreaEntity> list= caseTimeAreaMapper.listAll();
        for (int i=0;i<list.size();i++){
            CaseTimeAreaEntity temp = list.get(i);
            if (temp.getSeeFlag()==0){
                temp.setSeeFlagMsg("可见");
            }else if (temp.getSeeFlag()==1){
                temp.setSeeFlagMsg("不可见");
            }
            temp.setTimeAreaMg(temp.getTimeArea()+"天");
            list.set(i,temp);
        }
        webResponse.setData(list);
        return webResponse;
    }

}
