package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.PointMapper;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.Notice;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.PointService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/6/27.
 */
@Service
public class PointServiceImpl implements PointService {

    @Resource
    private PointMapper pointMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    public WebResponse myCount(){
        Notice notice = new Notice();
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        notice.setReceiveUser(userId);
        return WebResponse.success(pointMapper.list(notice).size());
    }

    public void save(Notice notice){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        for (int i=0;i<notice.getReceiveUsers().length;i++){
            notice.setReceiveUser(notice.getReceiveUsers()[i]);
            notice.setSendUser(userId);
            pointMapper.save(notice);
        }

    }

    public void update(Notice notice){
        pointMapper.delete(notice);
    }

    public void delete(Notice notice){
        pointMapper.delete(notice);
    }

    public WebResponse pageList(Notice notice){
        WebResponse webResponse = WebResponse.buildResponse();
        List<Notice> list = pointMapper.pageList(notice);
        for (int i=0;i<list.size();i++){
            Notice temp = list.get(i);
            if (temp.getReceiveUser()!=null){
                SysNewUserEntity sysNewUserEntity = sysUserMapper.getDataById(temp.getReceiveUser());
                if(sysNewUserEntity!=null){
                    temp.setReceiveUserName(sysNewUserEntity.getUserName());
                }
            }

            if (temp.getSendUser()!=null){
                SysNewUserEntity sysNewUserEntity = sysUserMapper.getDataById(temp.getSendUser());
                if(sysNewUserEntity!=null){
                    temp.setSendUserName(sysNewUserEntity.getUserName());
                }
            }
            list.set(i,temp);
        }
        webResponse.setData(PageInfo.of(list));
        webResponse.setCode("100");
        return webResponse;
    }


}
