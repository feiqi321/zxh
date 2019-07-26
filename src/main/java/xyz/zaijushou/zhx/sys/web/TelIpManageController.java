package xyz.zaijushou.zhx.sys.web;

import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.TelIpManage;
import xyz.zaijushou.zhx.sys.service.TelIpManageService;
import xyz.zaijushou.zhx.sys.service.impl.DataCollectServiceImpl;
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
    private static Logger logger = LoggerFactory.getLogger(TelIpManageController.class);
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
        logger.info("发送消息给呼叫中心："+url);
        String result = restTemplateUtil.doPostTestTwo(url,telIpManage.getContext());
        logger.info("呼叫中心返回消息："+result);
        if (StringUtils.notEmpty(result) && result.indexOf("Successfully")>0){
            return WebResponse.success();
        }else{
            return WebResponse.error("500","拨号失败");
        }



    }
    @ApiOperation(value = "批量呼出", notes = "批量呼出")
    @PostMapping("/sendBatch")
    public Object sendBatch(@RequestBody TelIpManage telIpManage) {

        String url = "http://"+telIpManage.getAddress()+"/openapi/V2.0.6/CallMultiNumbers";
        logger.info("发送消息给呼叫中心："+url);
        logger.info("发送内容:"+telIpManage.getContext());
        String result = restTemplateUtil.doPostTestTwo(url,telIpManage.getContext());
        logger.info("呼叫中心返回消息："+result);
        if (StringUtils.notEmpty(result) && result.indexOf("Successfully")>0){
            return WebResponse.success();
        }else{
            return WebResponse.error("500","拨号失败");
        }



    }


}
