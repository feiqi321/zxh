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

        sysPercentMapper.updatePercent(list);
    }

    public List<SysPercent> listPencent(){
        List<SysPercent> list = sysPercentMapper.listPencent();

        for (int i=0;i<list.size();i++){
            SysPercent sysPercent = list.get(i);
            if (StringUtils.isEmpty(sysPercent.getOdvLow())){
                sysPercent.setOdvLowMsg("0");
            }else{
                sysPercent.setOdvLowMsg(sysPercent.getOdvLow().stripTrailingZeros().toPlainString());
            }
            if (StringUtils.isEmpty(sysPercent.getOdvBasic())){
                sysPercent.setOdvBasicMsg("0");
            }else{
                sysPercent.setOdvBasicMsg(sysPercent.getOdvBasic().stripTrailingZeros().toPlainString());
            }
            if (StringUtils.isEmpty(sysPercent.getOdvReward())){
                sysPercent.setOdvRewardMsg("0");
            }else{
                sysPercent.setOdvRewardMsg(sysPercent.getOdvReward().stripTrailingZeros().toPlainString());
            }
            if (StringUtils.isEmpty(sysPercent.getOdvHighBasic())){
                sysPercent.setOdvHighBasicMsg("0");
            }else{
                sysPercent.setOdvHighBasicMsg(sysPercent.getOdvHighBasic().stripTrailingZeros().toPlainString());
            }
            if (StringUtils.isEmpty(sysPercent.getOdvHighReward())){
                sysPercent.setOdvHighRewardMsg("0");
            }else{
                sysPercent.setOdvHighRewardMsg(sysPercent.getOdvHighReward().stripTrailingZeros().toPlainString());
            }
            if (StringUtils.isEmpty(sysPercent.getManageReward())){
                sysPercent.setManageRewardMsg("0");
            }else{
                sysPercent.setManageRewardMsg(sysPercent.getManageReward().stripTrailingZeros().toPlainString());
            }

            list.set(i,sysPercent);
        }


        return list;
    }

}
