package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysPercentMapper;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysPercent;
import xyz.zaijushou.zhx.sys.service.SysPercentService;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by looyer on 2019/3/29.
 */
@Service
public class SysPercentServiceImpl implements SysPercentService {

    @Resource
    private SysPercentMapper sysPercentMapper;

    public void updatePercent(List<SysPercent> list){

        for (int i=0;i<list.size();i++){
            SysPercent sysPercent = list.get(i);
            if (sysPercent.getOdvLowMsg()!=null && sysPercent.getOdvLowMsg().equals("-")){
                sysPercent.setOdvLow(new BigDecimal(0));
                sysPercent.setOdvReward(sysPercent.getOdvBasic());
            }else{
                sysPercent.setOdvLow(sysPercent.getOdvLowMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvLowMsg()));
            }
            list.set(i,sysPercent);
        }

        sysPercentMapper.updatePercent(list);
    }

    public List<SysPercent> listPencent(){
        List<SysPercent> list = sysPercentMapper.listPencent();

        for (int i=0;i<list.size();i++){
            SysPercent sysPercent = list.get(i);
            SysDictionaryEntity sysDictionaryEntity =  RedisUtils.entityGet(RedisKeyPrefix.SYS_DIC+sysPercent.getClient(),SysDictionaryEntity.class);
            sysPercent.setClientMsg(sysDictionaryEntity==null?"":sysDictionaryEntity.getName());
            if (sysPercent.getEnable()!=null && sysPercent.getEnable()==1){
                sysPercent.setEnableMsg("是");
            }else if (sysPercent.getEnable()!=null && sysPercent.getEnable()==2) {
                sysPercent.setEnableMsg("否");
            }else{
                sysPercent.setEnable(2);
                sysPercent.setEnableMsg("否");
            }
            if (StringUtils.isEmpty(sysPercent.getOdvLow())){
                sysPercent.setOdvLowMsg("");
            }else{
                sysPercent.setOdvLowMsg(sysPercent.getOdvLow().toString());
            }
            list.set(i,sysPercent);
        }


        return list;
    }

}
