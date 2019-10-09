package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysPercentMapper;
import xyz.zaijushou.zhx.sys.entity.SysPercent;
import xyz.zaijushou.zhx.sys.entity.SysStandard;
import xyz.zaijushou.zhx.sys.service.SysPercentService;
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

    public void updatePercent(List<SysPercent> list) {
        for (int i = 0; i < list.size(); i++) {
            SysPercent sysPercent = list.get(i);
            sysPercentMapper.updatePercent(sysPercent);
        }

    }

    public List<SysPercent> listPencent() {
        List<SysPercent> list = sysPercentMapper.listPencent();

        for (int i = 0; i < list.size(); i++) {
            SysPercent sysPercent = list.get(i);
            if (StringUtils.isEmpty(sysPercent.getOdvLow())) {
                sysPercent.setOdvLowMsg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //基础提成
                    sysPercent.setOdvLowMsg(sysPercent.getOdvLow().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    //委按金额百分比
                    sysPercent.setOdvLowMsg(sysPercent.getOdvLow().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊2")){

                    sysPercent.setOdvLowMsg(sysPercent.getOdvLow().stripTrailingZeros().toPlainString());
                }

            }
            if (StringUtils.isEmpty(sysPercent.getOdvBasic())) {
                sysPercent.setOdvBasicMsg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //低标
                    sysPercent.setOdvBasicMsg(sysPercent.getOdvBasic().divide(new BigDecimal(10000)).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    //低标
                    sysPercent.setOdvBasicMsg(sysPercent.getOdvBasic().divide(new BigDecimal(10000)).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊2")){
                    //户
                    sysPercent.setOdvBasicMsg(sysPercent.getOdvBasic().stripTrailingZeros().toPlainString());
                }
            }
            if (StringUtils.isEmpty(sysPercent.getOdvReward())) {
                sysPercent.setOdvRewardMsg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //低标提成
                    sysPercent.setOdvRewardMsg(sysPercent.getOdvReward().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    //低标委按百分比
                    sysPercent.setOdvRewardMsg(sysPercent.getOdvReward().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊2")){
                    //户
                    sysPercent.setOdvRewardMsg(sysPercent.getOdvReward().stripTrailingZeros().toPlainString());
                }
            }
            if (StringUtils.isEmpty(sysPercent.getOdvHighBasic())) {
                sysPercent.setOdvHighBasicMsg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //高标
                    sysPercent.setOdvHighBasicMsg(sysPercent.getOdvHighBasic().divide(new BigDecimal(10000)).stripTrailingZeros().toPlainString());
                }else{
                    //未使用
                    sysPercent.setOdvHighBasicMsg("0");
                }

            }
            if (StringUtils.isEmpty(sysPercent.getOdvHighReward())) {
                sysPercent.setOdvHighRewardMsg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //高标提成
                    sysPercent.setOdvHighRewardMsg(sysPercent.getOdvHighReward().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else{
                    //未使用
                    sysPercent.setOdvHighRewardMsg("0");
                }
            }

            if (StringUtils.isEmpty(sysPercent.getOdvReward2())){
                sysPercent.setOdvReward2Msg("0");
            }else{
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //未使用
                    sysPercent.setOdvReward2Msg("0");
                }else if (sysPercent.getEnable().equals("特殊1")){
                    //基础提成超过委按金额百分比
                    sysPercent.setOdvReward2Msg(sysPercent.getOdvReward2() == null ? "0" : sysPercent.getOdvReward2().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊2")){
                    //户
                    sysPercent.setOdvReward2Msg(sysPercent.getOdvReward2() == null ? "0" : sysPercent.getOdvReward2().stripTrailingZeros().toPlainString());


                }
            }


            if (StringUtils.isEmpty(sysPercent.getOdvReward3())) {
                sysPercent.setOdvReward3Msg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //未使用
                    sysPercent.setOdvReward3Msg("0");
                }else if (sysPercent.getEnable().equals("特殊1")){
                    //低标提成超过委按金额百分比
                    sysPercent.setOdvReward3Msg(sysPercent.getOdvReward3()==null?"0":sysPercent.getOdvReward3().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊2")){
                    //户
                    sysPercent.setOdvReward3Msg(sysPercent.getOdvReward3() == null ? "0" : sysPercent.getOdvReward3().stripTrailingZeros().toPlainString());

                }
            }

           /* if (StringUtils.isEmpty(sysPercent.getOdvHighBasic())) {
                sysPercent.setOdvHighBasicMsg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    sysPercent.setOdvHighRewardMsg(sysPercent.getOdvHighBasic()==null?"0":sysPercent.getOdvHighBasic().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else{
                    sysPercent.setOdvHighRewardMsg(sysPercent.getOdvHighBasic()==null?"0":sysPercent.getOdvHighBasic().stripTrailingZeros().toPlainString());
                }
            }*/
            if (StringUtils.isEmpty(sysPercent.getOdvRewardRange1())) {
                sysPercent.setOdvRewardRange1Msg("0");
            } else {
                if (sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setOdvRewardRange1Msg(sysPercent.getOdvRewardRange1()==null?"0":sysPercent.getOdvRewardRange1().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }else{
                    sysPercent.setOdvRewardRange1Msg(sysPercent.getOdvRewardRange1()==null?"0":sysPercent.getOdvRewardRange1().stripTrailingZeros().toPlainString());
                }
            }
            if (StringUtils.isEmpty(sysPercent.getOdvRewardRange2())) {
                sysPercent.setOdvRewardRange2Msg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    sysPercent.setOdvRewardRange2Msg(sysPercent.getOdvRewardRange2()==null?"0":sysPercent.getOdvRewardRange2().stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    sysPercent.setOdvRewardRange2Msg(sysPercent.getOdvRewardRange2()==null?"0":sysPercent.getOdvRewardRange2().stripTrailingZeros().toPlainString());
                }else if(sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setOdvRewardRange2Msg(sysPercent.getOdvRewardRange2()==null?"0":sysPercent.getOdvRewardRange2().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }
            }
            if (StringUtils.isEmpty(sysPercent.getOdvRewardRange3())) {
                sysPercent.setOdvRewardRange3Msg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    sysPercent.setOdvRewardRange3Msg(sysPercent.getOdvRewardRange3()==null?"0":sysPercent.getOdvRewardRange3().stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    sysPercent.setOdvRewardRange3Msg(sysPercent.getOdvRewardRange3()==null?"0":sysPercent.getOdvRewardRange3().stripTrailingZeros().toPlainString());
                }else if(sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setOdvRewardRange3Msg(sysPercent.getOdvRewardRange3()==null?"0":sysPercent.getOdvRewardRange3().stripTrailingZeros().toPlainString());
                }
            }
            if (StringUtils.isEmpty(sysPercent.getOdvRewardRange4())) {
                sysPercent.setOdvRewardRange4Msg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    sysPercent.setOdvRewardRange4Msg(sysPercent.getOdvRewardRange4()==null?"0":sysPercent.getOdvRewardRange4().stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    sysPercent.setOdvRewardRange4Msg(sysPercent.getOdvRewardRange4()==null?"0":sysPercent.getOdvRewardRange4().stripTrailingZeros().toPlainString());
                }else if(sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setOdvRewardRange4Msg(sysPercent.getOdvRewardRange4()==null?"0":sysPercent.getOdvRewardRange4().multiply(new BigDecimal(100)).stripTrailingZeros().toPlainString());
                }
            }
            if (StringUtils.isEmpty(sysPercent.getOdvRewardRange5())) {
                sysPercent.setOdvRewardRange5Msg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    sysPercent.setOdvRewardRange5Msg(sysPercent.getOdvRewardRange5()==null?"0":sysPercent.getOdvRewardRange5().stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    sysPercent.setOdvRewardRange5Msg(sysPercent.getOdvRewardRange5()==null?"0":sysPercent.getOdvRewardRange5().stripTrailingZeros().toPlainString());
                }else if(sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setOdvRewardRange5Msg(sysPercent.getOdvRewardRange5()==null?"0":sysPercent.getOdvRewardRange5().stripTrailingZeros().toPlainString());
                }
            }
            //sysPercent.setOdvRewardRange5Msg(sysPercent.getOdvRewardRange5() == null ? "0" : sysPercent.getOdvRewardRange5().stripTrailingZeros().toPlainString());

            if (StringUtils.isEmpty(sysPercent.getManageReward())) {
                sysPercent.setManageRewardMsg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //经理提成低标
                    sysPercent.setManageRewardMsg(sysPercent.getManageReward().stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    sysPercent.setManageRewardMsg(sysPercent.getManageReward().stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setManageRewardMsg(sysPercent.getManageReward().stripTrailingZeros().toPlainString());
                }

            }

            if (StringUtils.isEmpty(sysPercent.getManageRewardRange1())) {
                sysPercent.setManageRewardRange1Msg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //经理提成/人
                    sysPercent.setManageRewardRange1Msg(sysPercent.getManageRewardRange1() == null ? "0" : sysPercent.getManageRewardRange1().stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    //经理提成/人
                    sysPercent.setManageRewardRange1Msg(sysPercent.getManageRewardRange1() == null ? "0" : sysPercent.getManageRewardRange1().stripTrailingZeros().toPlainString());
                }else if(sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setManageRewardRange1Msg(sysPercent.getManageRewardRange1() == null ? "0" : sysPercent.getManageRewardRange1().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
                }
            }

            if (StringUtils.isEmpty(sysPercent.getManageRewardRange2())) {
                sysPercent.setManageRewardRange2Msg("0");
            } else {
                if (sysPercent.getEnable().equals("阶梯累加")){
                    //经理提成 百分比
                    sysPercent.setManageRewardRange2Msg(sysPercent.getManageRewardRange2() == null ? "0" : sysPercent.getManageRewardRange2().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
                }else if (sysPercent.getEnable().equals("特殊1")){
                    //经理提成 百分比
                    sysPercent.setManageRewardRange2Msg(sysPercent.getManageRewardRange2() == null ? "0" : sysPercent.getManageRewardRange2().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
                }else if(sysPercent.getEnable().equals("特殊2")){
                    sysPercent.setManageRewardRange2Msg(sysPercent.getManageRewardRange2() == null ? "0" : sysPercent.getManageRewardRange2().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
                }
            }

            sysPercent.setManageRewardRange3Msg(sysPercent.getManageRewardRange3() == null ? "0" : sysPercent.getManageRewardRange3().stripTrailingZeros().toPlainString());

            if (sysPercent.getEnable().equals("特殊2")) {
                sysPercent.setManageRewardRange4Msg(sysPercent.getManageRewardRange4() == null ? "0" : sysPercent.getManageRewardRange4().multiply(new BigDecimal("100")).stripTrailingZeros().toPlainString());
            }

            sysPercent.setManageRewardRange5Msg(sysPercent.getManageRewardRange5() == null ? "0" : sysPercent.getManageRewardRange5().stripTrailingZeros().toPlainString());
            sysPercent.setManageRewardRange6Msg(sysPercent.getManageRewardRange6() == null ? "0" : sysPercent.getManageRewardRange6().stripTrailingZeros().toPlainString());
            list.set(i, sysPercent);
        }


        return list;
    }

    public SysPercent findRemark() {
        return sysPercentMapper.findRemark();
    }

    public void updateRemark(SysPercent bean) {
        sysPercentMapper.updateRemark(bean);
    }

    @Override
    public SysStandard listStandard() {
        SysStandard sysStandard = sysPercentMapper.listStandard();
        if (sysStandard==null){
            sysStandard = new SysStandard();
        }
            sysStandard.setStandard1Msg(sysStandard.getStandard1() == null ? "0" : sysStandard.getStandard1().stripTrailingZeros().toPlainString());
            sysStandard.setStandard2Msg(sysStandard.getStandard2() == null ? "0" : sysStandard.getStandard2().stripTrailingZeros().toPlainString());
            sysStandard.setReward1Msg(sysStandard.getReward1() == null ? "0" : sysStandard.getReward1().stripTrailingZeros().toPlainString());
            sysStandard.setReward2Msg(sysStandard.getReward2() == null ? "0" : sysStandard.getReward2().stripTrailingZeros().toPlainString());
            sysStandard.setReward3Msg(sysStandard.getReward3() == null ? "0" : sysStandard.getReward3().stripTrailingZeros().toPlainString());
        return sysStandard;
    }

    @Override
    public void updateStandard(SysStandard sysStandard) {
        sysStandard.setStandard1(sysStandard.getStandard1()==null?new BigDecimal(0):new BigDecimal(sysStandard.getStandard1Msg()));
        sysStandard.setStandard2(sysStandard.getStandard2()==null?new BigDecimal(0):new BigDecimal(sysStandard.getStandard2Msg()));
        sysStandard.setReward1(sysStandard.getReward1()==null?new BigDecimal(0):new BigDecimal(sysStandard.getReward1Msg()));
        sysStandard.setReward2(sysStandard.getReward2()==null?new BigDecimal(0):new BigDecimal(sysStandard.getReward2Msg()));
        sysStandard.setReward3(sysStandard.getReward3()==null?new BigDecimal(0):new BigDecimal(sysStandard.getReward3Msg()));
        sysPercentMapper.updateStandard(sysStandard);
    }

}
