package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.Notice;
import xyz.zaijushou.zhx.sys.service.PointService;

/**
 * Created by looyer on 2019/6/27.
 */
@RestController
@RequestMapping("/point")
public class PointController {

    @Autowired
    private PointService pointService;

    @ApiOperation(value = "查询", notes = "查询")
    @PostMapping("/count")
    public Object findOne() {
        return pointService.myCount();

    }

    @ApiOperation(value = "保存", notes = "保存")
    @PostMapping("/save")
    public Object save(@RequestBody Notice notice) {
        if(notice.getReceiveUsers()==null || notice.getReceiveUsers().length==0){
            WebResponse webResponse = WebResponse.buildResponse();
            webResponse.setCode("500");
            webResponse.setMsg("接收人必须选择");
            return webResponse;
        }
        pointService.save(notice);
        return WebResponse.buildResponse();

    }

    @ApiOperation(value = "修改", notes = "修改")
    @PostMapping("/update")
    public Object update(@RequestBody Notice notice) {
        if(notice.getReceiveUsers()==null || notice.getReceiveUsers().length==0){
            WebResponse webResponse = WebResponse.buildResponse();
            webResponse.setCode("500");
            webResponse.setMsg("接收人必须选择");
            return webResponse;
        }
        pointService.update(notice);
        return WebResponse.buildResponse();

    }

    @ApiOperation(value = "删除", notes = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody Notice notice) {

        pointService.delete(notice);
        return WebResponse.buildResponse();

    }
    @ApiOperation(value = "列表查询", notes = "列表查询")
    @PostMapping("/personPageList")
    public Object personPageList(@RequestBody Notice notice) {
        return  pointService.pagePersonList(notice);

    }

    @ApiOperation(value = "列表查询", notes = "列表查询")
    @PostMapping("/pageList")
    public Object pageList(@RequestBody Notice notice) {
        return  pointService.pageList(notice);

    }

}
