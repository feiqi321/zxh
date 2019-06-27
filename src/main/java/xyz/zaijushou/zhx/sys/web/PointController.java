package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;

/**
 * Created by looyer on 2019/6/27.
 */
@RestController
@RequestMapping("/point")
public class PointController {

    @ApiOperation(value = "查询", notes = "查询")
    @PostMapping("/count")
    public Object findOne() {
        return WebResponse.success(2);

    }

}
