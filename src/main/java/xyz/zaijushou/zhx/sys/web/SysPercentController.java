package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SelectFilterEntity;
import xyz.zaijushou.zhx.sys.entity.SysPercent;
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
                    sysPercent.setOdvLow(sysPercent.getOdvLowMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvLowMsg()));
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvBasicMsg())){
                if (sysPercent.getOdvBasicMsg().equals("-")){
                    sysPercent.setOdvBasic(new BigDecimal(0));
                }else if (sysPercent.getOdvBasicMsg().matches(reg)){
                    sysPercent.setOdvBasic(sysPercent.getOdvBasicMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvBasicMsg()));
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvRewardMsg())){
                if (sysPercent.getOdvRewardMsg().equals("-")){
                    sysPercent.setOdvReward(new BigDecimal(0));
                }else if (sysPercent.getOdvRewardMsg().matches(reg)){
                    sysPercent.setOdvReward(sysPercent.getOdvRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvRewardMsg()));
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvHighBasicMsg())){
                if (sysPercent.getOdvHighBasicMsg().equals("-")){
                    sysPercent.setOdvHighBasic(new BigDecimal(0));
                }else if (sysPercent.getOdvHighBasicMsg().matches(reg)){
                    sysPercent.setOdvHighBasic(sysPercent.getOdvHighBasicMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvHighBasicMsg()));
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getOdvHighRewardMsg())){
                if (sysPercent.getOdvHighRewardMsg().equals("-")){
                    sysPercent.setOdvHighReward(new BigDecimal(0));
                }else if (sysPercent.getOdvHighRewardMsg().matches(reg)){
                    sysPercent.setOdvHighReward(sysPercent.getOdvHighRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvHighRewardMsg()));
                }else{
                    return WebResponse.error( "500","低标格式不对");
                }
            }
            if (StringUtils.notEmpty(sysPercent.getManageRewardMsg())){
                if (sysPercent.getManageRewardMsg().equals("-")){
                    sysPercent.setManageReward(new BigDecimal(0));
                }else if (sysPercent.getManageRewardMsg().matches(reg)){
                    sysPercent.setManageReward(sysPercent.getManageRewardMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getManageRewardMsg()));
                }else{
                    return WebResponse.error( "500","低标格式不对");
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

}
