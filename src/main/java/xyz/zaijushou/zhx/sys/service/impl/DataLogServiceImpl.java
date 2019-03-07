package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataOpLogMapper;
import xyz.zaijushou.zhx.sys.entity.DataOpLog;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataLogService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
@Service
public class DataLogServiceImpl implements DataLogService {

    @Resource
    private DataOpLogMapper dataOpLogMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutStatusById(user);
    }

    public void saveDataLog(String caseId,String type,String context){
        DataOpLog log = new DataOpLog();
        log.setCaseId(caseId);
        log.setContext(context);
        log.setType(type);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        log.setOpTime(sdf.format(new Date()));
        SysUserEntity sysUserEntity = this.getUserInfo();
        log.setOper(sysUserEntity.getId());
        dataOpLogMapper.saveDataLog(log);
    }

    public void saveDataLog(DataOpLog log){
        dataOpLogMapper.saveDataLog(log);
    }


    public void updateDataLog(DataOpLog log){
        dataOpLogMapper.updateDataLog(log);
    }

    public void delDataLog(DataOpLog log){
        dataOpLogMapper.delDataLog(log);
    }

    public List<DataOpLog> listDataOpLog(DataOpLog log){
        List<DataOpLog>  list = dataOpLogMapper.listDataOpLog(log);
        for (int i=0;i<list.size();i++){
            DataOpLog temp = list.get(i);
            SysUserEntity tempuser = new SysUserEntity();
            tempuser.setId(Integer.valueOf(temp.getOper()));
            SysUserEntity user = sysUserService.findUserInfoWithoutStatusById(tempuser);
            temp.setOperName(user == null ? "" : user.getUserName());
            list.set(i,temp);
        }
        return list;
    }

}
