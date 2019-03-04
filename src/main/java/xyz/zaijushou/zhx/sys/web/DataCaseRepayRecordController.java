package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.DataCaseRepayRecordEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseRepayRecordService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/repayRecord")
public class DataCaseRepayRecordController {

    @Resource
    private DataCaseRepayRecordService dataCaseRepayRecordService;

    @PostMapping("/list")
    public Object list(@RequestBody DataCaseRepayRecordEntity entity) {
        PageInfo<DataCaseRepayRecordEntity> pageInfo = dataCaseRepayRecordService.pageRepayRecordList(entity);
        return WebResponse.success(pageInfo);
    }

    @PostMapping("/revoke")
    public Object revoke(@RequestBody DataCaseRepayRecordEntity entity) {
        if(entity == null || entity.getIds() == null || entity.getIds().length == 0) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请输入ids");
        }
        dataCaseRepayRecordService.revoke(entity);
        return WebResponse.success();
    }

    @PostMapping("/save")
    public Object save(@RequestBody DataCaseRepayRecordEntity entity) {
        dataCaseRepayRecordService.save(entity);
        return WebResponse.success();
    }




}
