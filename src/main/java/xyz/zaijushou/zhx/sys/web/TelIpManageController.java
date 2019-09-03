package xyz.zaijushou.zhx.sys.web;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CallCenter;
import xyz.zaijushou.zhx.sys.entity.TelIpManage;
import xyz.zaijushou.zhx.sys.service.TelIpManageService;
import xyz.zaijushou.zhx.utils.RestTemplateUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

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
    @PostMapping("/queryCallCenters")
    public Object queryCallCenters() {
        return WebResponse.success(telIpManageService.queryCallCenters());
    }

    @ApiOperation(value = "新增", notes = "新增")
    @PostMapping("/addCallCenter")
    public Object addCallCenter(@RequestBody CallCenter callCenter) {
        telIpManageService.addCallCenter(callCenter);
        return WebResponse.success();
    }

    @ApiOperation(value = "查询单条", notes = "查询单条")
    @PostMapping("/queryCallCenter")
    public Object queryCallCenter(@RequestBody Map<String,Integer> map) {
        return WebResponse.success(telIpManageService.queryCallCenter(map.get("callCenterID")));
    }

    @ApiOperation(value = "修改", notes = "修改")
    @PostMapping("/updateCallCenter")
    public Object updateCallCenter(@RequestBody CallCenter callCenter) {
        telIpManageService.updateCallCenter(callCenter);
        return WebResponse.success();
    }

    @ApiOperation(value = "删除", notes = "删除")
    @PostMapping("/deleteCallCenters")
    public Object deleteCallCenters(@RequestBody List<Integer> callCenterIDs) {
        telIpManageService.deleteCallCenters(callCenterIDs);
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
        String ss = "\"error\":0";
        if (StringUtils.notEmpty(result) && (result.indexOf("Successfully")>0 || result.indexOf(ss)>0)){
            return WebResponse.success();
        }else{
            return WebResponse.error("500","拨号失败");
        }
    }
}
