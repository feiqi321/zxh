package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.TelIpManage;
import xyz.zaijushou.zhx.sys.service.TelIpManageService;
import xyz.zaijushou.zhx.utils.RestTemplateUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/4/19.
 */
@RestController
@RequestMapping("/telIp")
public class TelIpManageController {
    @Resource
    private RestTemplateUtil restTemplateUtil;
    @Autowired
    private TelIpManageService telIpManageService;

    @ApiOperation(value = "查询", notes = "查询")
    @PostMapping("/findOne")
    public Object findOne() {
        return WebResponse.success(telIpManageService.findOne());

    }

    @ApiOperation(value = "修改", notes = "修改")
    @PostMapping("/update")
    public Object update(@RequestBody List<TelIpManage> list) {
        TelIpManage telIpManage = list.get(0);
        telIpManageService.update(telIpManage);
        return WebResponse.success();

    }

    @ApiOperation(value = "发送", notes = "发送")
    @PostMapping("/send")
    public Object send(@RequestBody TelIpManage telIpManage) {

        String url = "http://"+telIpManage.getAddress()+"/openapi/V2.0.6/CallNumber";

        String result = restTemplateUtil.doPostTestTwo(url,telIpManage.getContext());
        if (StringUtils.notEmpty(result) && result.indexOf("Successfully")>0){
            return WebResponse.success();
        }else{
            return WebResponse.error("500","拨号失败");
        }



    }



}
