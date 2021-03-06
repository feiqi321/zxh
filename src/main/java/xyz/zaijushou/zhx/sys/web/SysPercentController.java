package xyz.zaijushou.zhx.sys.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysPercent;
import xyz.zaijushou.zhx.sys.entity.SysStandard;
import xyz.zaijushou.zhx.sys.service.SysPercentService;
import xyz.zaijushou.zhx.utils.StringUtils;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by looyer on 2019/3/29.
 */
@RestController
public class SysPercentController {
    @Autowired
    private SysPercentService sysPercentService;


    @PostMapping("/updatePercent")
    public Object updatePercent(@RequestBody List<SysPercent> list) {
        String reg = "\\d+(\\.\\d+)?";
        for (int i=0;i<list.size();i++){
            SysPercent sysPercent = list.get(i);


           if (StringUtils.notEmpty(sysPercent.getOdvLowMsg())){
                if (sysPercent.getOdvLowMsg().equals("-")){
                    sysPercent.setOdvLow(new BigDecimal(0));
                    sysPercent.setOdvReward(sysPercent.getOdvBasic());
                }else if (sysPercent.getOdvLowMsg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //基础提成
                        sysPercent.setOdvLow(sysPercent.getOdvLowMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvLowMsg()).divide(new BigDecimal(100)));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        //委按金额百分比
                        sysPercent.setOdvLow(sysPercent.getOdvLowMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvLowMsg()).divide(new BigDecimal(100)));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        // 元/户
                        sysPercent.setOdvLow(sysPercent.getOdvLowMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvLowMsg()));
                    }
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvBasicMsg())){
                if (sysPercent.getOdvBasicMsg().equals("-")){
                    sysPercent.setOdvBasic(new BigDecimal(0));
                }else if (sysPercent.getOdvBasicMsg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //低标
                        sysPercent.setOdvBasic(sysPercent.getOdvBasicMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvBasicMsg()).multiply(new BigDecimal(10000)));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        //低标
                        sysPercent.setOdvBasic(sysPercent.getOdvBasicMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvBasicMsg()).multiply(new BigDecimal(10000)));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        //低标 户
                        sysPercent.setOdvBasic(sysPercent.getOdvBasicMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvBasicMsg()));
                    }
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvRewardMsg())){
                if (sysPercent.getOdvRewardMsg().equals("-")){
                    sysPercent.setOdvReward(new BigDecimal(0));
                }else if (sysPercent.getOdvRewardMsg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //低标提成
                        sysPercent.setOdvReward(sysPercent.getOdvRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardMsg()).divide(new BigDecimal(100)));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        //低标委按百分比
                        sysPercent.setOdvReward(sysPercent.getOdvRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardMsg()).divide(new BigDecimal(100)));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        //户
                        sysPercent.setOdvReward(sysPercent.getOdvRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardMsg()));
                    }

                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvReward2Msg())){
                if (sysPercent.getOdvReward2Msg().equals("-")){
                    sysPercent.setOdvReward2(new BigDecimal(0));
                }else if (sysPercent.getOdvReward2Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //未使用这个
                        sysPercent.setOdvReward2(new BigDecimal("0"));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        //基础提成超过委按金额百分比
                        sysPercent.setOdvReward2(sysPercent.getOdvReward2Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvReward2Msg()).divide(new BigDecimal(100)));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        //户
                        sysPercent.setOdvReward2(sysPercent.getOdvReward2Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvReward2Msg()));
                    }

                }else{
                    return WebResponse.error( "500","低标提成格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvReward3Msg())){
                if (sysPercent.getOdvReward3Msg().equals("-")){
                    sysPercent.setOdvReward3(new BigDecimal(0));
                }else if (sysPercent.getOdvReward3Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //未使用这个
                        sysPercent.setOdvReward3(new BigDecimal("0"));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        //低标提成超过委按金额百分比
                        sysPercent.setOdvReward3(sysPercent.getOdvReward3Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvReward3Msg()).divide(new BigDecimal(100)));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        //户
                        sysPercent.setOdvReward3(sysPercent.getOdvReward3Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvReward3Msg()));
                    }
                }else{
                    return WebResponse.error( "500","低标提成格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvHighBasicMsg())){
                if (sysPercent.getOdvHighBasicMsg().equals("-")){
                    sysPercent.setOdvHighBasic(new BigDecimal(0));
                }else if (sysPercent.getOdvHighBasicMsg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //高标
                        sysPercent.setOdvHighBasic(sysPercent.getOdvHighBasicMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvHighBasicMsg()).multiply(new BigDecimal(10000)));
                    }else{
                        //未使用
                        sysPercent.setOdvHighBasic(new BigDecimal(0));
                    }

                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvHighRewardMsg())){
                if (sysPercent.getOdvHighRewardMsg().equals("-")){
                    sysPercent.setOdvHighReward(new BigDecimal(0));
                }else if (sysPercent.getOdvHighRewardMsg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //高标提成
                        sysPercent.setOdvHighReward(sysPercent.getOdvHighRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvHighRewardMsg()).divide(new BigDecimal(100)));
                    }else{
                        sysPercent.setOdvHighReward(sysPercent.getOdvHighRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvHighRewardMsg()));
                    }

                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardMsg())){
                if (sysPercent.getManageRewardMsg().equals("-")){
                    sysPercent.setManageReward(new BigDecimal(0));
                }else if (sysPercent.getManageRewardMsg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //经理低标
                        sysPercent.setManageReward(sysPercent.getManageRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardMsg()));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        sysPercent.setManageReward(sysPercent.getManageRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardMsg()));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        sysPercent.setManageReward(sysPercent.getManageRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardMsg()));
                    }

                }else{
                    return WebResponse.error( "500","经理提成格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvRewardRange1Msg())){
                if (sysPercent.getOdvRewardRange1Msg().equals("-")){
                    sysPercent.setOdvRewardRange1(new BigDecimal(0));
                }else if (sysPercent.getOdvRewardRange1Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //未使用
                        sysPercent.setOdvRewardRange1(new BigDecimal(0));
//                        sysPercent.setOdvRewardRange1(sysPercent.getOdvRewardRange1Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange1Msg()));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        //未使用
                        sysPercent.setOdvRewardRange1(new BigDecimal(0));
//                        sysPercent.setOdvRewardRange1(sysPercent.getOdvRewardRange1Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange1Msg()));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        sysPercent.setOdvRewardRange1(sysPercent.getOdvRewardRange1Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange1Msg()).divide(new BigDecimal(100)));
                    }
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvRewardRange2Msg())){
                if (sysPercent.getOdvRewardRange2Msg().equals("-")){
                    sysPercent.setOdvRewardRange2(new BigDecimal(0));
                }else if (sysPercent.getOdvRewardRange2Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        //未使用
                        sysPercent.setOdvRewardRange2(new BigDecimal(0));
//                        sysPercent.setOdvRewardRange2(sysPercent.getOdvRewardRange2Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange2Msg()));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        //未使用
                        sysPercent.setOdvRewardRange2(new BigDecimal(0));
//                        sysPercent.setOdvRewardRange2(sysPercent.getOdvRewardRange2Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange2Msg()));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        sysPercent.setOdvRewardRange2(sysPercent.getOdvRewardRange2Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange2Msg()).divide(new BigDecimal(100)));
                    }

                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvRewardRange3Msg())){
                if (sysPercent.getOdvRewardRange3Msg().equals("-")){
                    sysPercent.setOdvRewardRange3(new BigDecimal(0));
                }else if (sysPercent.getOdvRewardRange3Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("阶梯累加")){
                        sysPercent.setOdvRewardRange3(sysPercent.getOdvRewardRange3Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange3Msg()));
                    }else if (sysPercent.getEnable().equals("特殊1")){
                        sysPercent.setOdvRewardRange3(sysPercent.getOdvRewardRange3Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange3Msg()).divide(new BigDecimal(100)));
                    }else if (sysPercent.getEnable().equals("特殊2")){
                        sysPercent.setOdvRewardRange3(sysPercent.getOdvRewardRange3Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange3Msg()));
                    }

                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvRewardRange4Msg())){
                if (sysPercent.getOdvRewardRange4Msg().equals("-")){
                    sysPercent.setOdvRewardRange4(new BigDecimal(0));
                }else if (sysPercent.getOdvRewardRange4Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("特殊2")){
                        sysPercent.setOdvRewardRange4(sysPercent.getOdvRewardRange4Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange4Msg()).divide(new BigDecimal(100)));
                    }else{
                        //未使用
                        sysPercent.setOdvRewardRange4(new BigDecimal(0));
//                        sysPercent.setOdvRewardRange4(sysPercent.getOdvRewardRange4Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange4Msg()));
                    }
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvRewardRange5Msg())){
                if (sysPercent.getOdvRewardRange5Msg().equals("-")){
                    sysPercent.setOdvRewardRange5(new BigDecimal(0));
                }else if (sysPercent.getOdvRewardRange5Msg().matches(reg)){
                    sysPercent.setOdvRewardRange5(sysPercent.getOdvRewardRange5Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardRange5Msg()));
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardRange1Msg())){
                if (sysPercent.getManageRewardRange1Msg().equals("-")){
                    sysPercent.setManageRewardRange1(new BigDecimal(0));
                }else if (sysPercent.getManageRewardRange1Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("特殊2")){
                        //百分比
                        sysPercent.setManageRewardRange1(sysPercent.getManageRewardRange1Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardRange1Msg()).divide(new BigDecimal(100)));
                    }else{
                        //经理提成/人
                        sysPercent.setManageRewardRange1(sysPercent.getManageRewardRange1Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardRange1Msg()));
                    }

                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardRange2Msg())){
                if (sysPercent.getManageRewardRange2Msg().equals("-")){
                    sysPercent.setManageRewardRange2(new BigDecimal(0));
                }else if (sysPercent.getManageRewardRange2Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("特殊2")){
                        sysPercent.setManageRewardRange1(sysPercent.getManageRewardRange1Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardRange1Msg()).divide(new BigDecimal(100)));
                    }else{
                        //经理提成百分比
                        sysPercent.setManageRewardRange2(sysPercent.getManageRewardRange2Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardRange2Msg()).divide(new BigDecimal(100)));

                    }

                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardRange3Msg())){
                if (sysPercent.getManageRewardRange3Msg().equals("-")){
                    sysPercent.setManageRewardRange3(new BigDecimal(0));
                }else if (sysPercent.getManageRewardRange3Msg().matches(reg)){

                    sysPercent.setManageRewardRange3(sysPercent.getManageRewardRange3Msg() == null ? new BigDecimal(0) : new BigDecimal(sysPercent.getManageRewardRange3Msg()));

                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardRange4Msg())){
                if (sysPercent.getManageRewardRange4Msg().equals("-")){
                    sysPercent.setManageRewardRange4(new BigDecimal(0));
                }else if (sysPercent.getManageRewardRange4Msg().matches(reg)){
                    if (sysPercent.getEnable().equals("特殊2")) {
                        sysPercent.setManageRewardRange4(sysPercent.getManageRewardRange4Msg() == null ? new BigDecimal(0) : new BigDecimal(sysPercent.getManageRewardRange4Msg()).divide(new BigDecimal(100)));
                    }else{
//                        sysPercent.setManageRewardRange4(sysPercent.getManageRewardRange4Msg() == null ? new BigDecimal(0) : new BigDecimal(sysPercent.getManageRewardRange4Msg()).divide(new BigDecimal(100)));
                    }
                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardRange5Msg())){
                if (sysPercent.getManageRewardRange5Msg().equals("-")){
                    sysPercent.setManageRewardRange5(new BigDecimal(0));
                }else if (sysPercent.getManageRewardRange5Msg().matches(reg)){
                    sysPercent.setManageRewardRange5(sysPercent.getManageRewardRange5Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardRange5Msg()));
                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardRange6Msg())){
                if (sysPercent.getManageRewardRange6Msg().equals("-")){
                    sysPercent.setManageRewardRange6(new BigDecimal(0));
                }else if (sysPercent.getManageRewardRange6Msg().matches(reg)){
                    sysPercent.setManageRewardRange6(sysPercent.getManageRewardRange6Msg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardRange6Msg()));
                }else{
                    return WebResponse.error( "500","高标格式不对");
                }
            }
            list.set(i,sysPercent);
        }

        sysPercentService.updatePercent(list);

        return WebResponse.success();

    }

    @PostMapping("/listPencent")
    public Object listPencent() {

        List list = sysPercentService.listPencent();

        return WebResponse.success(list);

    }

    @PostMapping("/findRemark")
    public Object findRemark() {

        SysPercent bean = sysPercentService.findRemark();

        return WebResponse.success(bean);

    }

    @PostMapping("/updateRemark")
    public Object updateRemark(@RequestBody SysPercent bean) {
        sysPercentService.updateRemark(bean);
        return WebResponse.success();
    }

    /**
     * 查询奖金标准
     * @return
     */
    @PostMapping("/listStandard")
    public Object listStandard() {
        return WebResponse.success(sysPercentService.listStandard());
    }

    /**
     * 修改奖金标准
     * @param sysStandard
     * @return
     */
    @PostMapping("/updateStandard")
    public Object updateStandard(@RequestBody SysStandard sysStandard) {
        sysPercentService.updateStandard(sysStandard);
        return WebResponse.success();
    }

}
