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
            sysPercentMapper.updatePercent(sysPercent);
        }

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
            sysPercent.setOdvReward2Msg(sysPercent.getOdvReward2()==null?"0":sysPercent.getOdvReward2().stripTrailingZeros().toPlainString());
            sysPercent.setOdvReward3Msg(sysPercent.getOdvReward3()==null?"0":sysPercent.getOdvReward3().stripTrailingZeros().toPlainString());
            sysPercent.setOdvHighBasicMsg(sysPercent.getOdvHighBasic()==null?"0":sysPercent.getOdvHighBasic().stripTrailingZeros().toPlainString());
            sysPercent.setOdvHighRewardMsg(sysPercent.getOdvHighReward()==null?"0":sysPercent.getOdvHighReward().stripTrailingZeros().toPlainString());
            sysPercent.setOdvRewardRange1Msg(sysPercent.getOdvRewardRange1()==null?"0":sysPercent.getOdvRewardRange1().stripTrailingZeros().toPlainString());
            sysPercent.setOdvRewardRange2Msg(sysPercent.getOdvRewardRange2()==null?"0":sysPercent.getOdvRewardRange2().stripTrailingZeros().toPlainString());
            sysPercent.setOdvRewardRange3Msg(sysPercent.getOdvRewardRange3()==null?"0":sysPercent.getOdvRewardRange3().stripTrailingZeros().toPlainString());
            sysPercent.setOdvRewardRange4Msg(sysPercent.getOdvRewardRange4()==null?"0":sysPercent.getOdvRewardRange4().stripTrailingZeros().toPlainString());
            sysPercent.setOdvRewardRange5Msg(sysPercent.getOdvRewardRange5()==null?"0":sysPercent.getOdvRewardRange5().stripTrailingZeros().toPlainString());

            sysPercent.setManageRewardRange1Msg(sysPercent.getManageRewardRange1()==null?"0":sysPercent.getManageRewardRange1().stripTrailingZeros().toPlainString());
            sysPercent.setManageRewardRange2Msg(sysPercent.getManageRewardRange2()==null?"0":sysPercent.getManageRewardRange2().stripTrailingZeros().toPlainString());
            sysPercent.setManageRewardRange3Msg(sysPercent.getManageRewardRange3()==null?"0":sysPercent.getManageRewardRange3().stripTrailingZeros().toPlainString());
            sysPercent.setManageRewardRange4Msg(sysPercent.getManageRewardRange4()==null?"0":sysPercent.getManageRewardRange4().stripTrailingZeros().toPlainString());
            sysPercent.setManageRewardRange5Msg(sysPercent.getManageRewardRange5()==null?"0":sysPercent.getManageRewardRange5().stripTrailingZeros().toPlainString());
            sysPercent.setManageRewardRange6Msg(sysPercent.getManageRewardRange6()==null?"0":sysPercent.getManageRewardRange6().stripTrailingZeros().toPlainString());
            list.set(i,sysPercent);
        }


        return list;
    }

    public SysPercent findRemark(){
        return sysPercentMapper.findRemark();
    }

    public void updateRemark(SysPercent bean){
        sysPercentMapper.updateRemark(bean);
    }

}
