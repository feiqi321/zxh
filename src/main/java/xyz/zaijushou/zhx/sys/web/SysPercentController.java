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
            if (sysPercent.getOdvLowMsg()!=null){
                if (sysPercent.getOdvLowMsg().equals("-")){
                    sysPercent.setOdvLow(new BigDecimal(0));
                    sysPercent.setOdvReward(sysPercent.getOdvBasic());
                }else if (sysPercent.getOdvLowMsg().matches(reg)){
                    sysPercent.setOdvLow(sysPercent.getOdvLowMsg()==null?new BigDecimal(0):new BigDecimal(sysPercent.getOdvLowMsg()));
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
