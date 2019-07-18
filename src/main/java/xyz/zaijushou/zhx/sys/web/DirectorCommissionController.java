package xyz.zaijushou.zhx.sys.web;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.sys.service.DirectorCommissionService;

import javax.annotation.Resource;

/**
 * @author lsl
 * 总监提成查询
 */
@RestController
@RequestMapping("/directorCommission")
public class DirectorCommissionController {

    @Resource
    private DirectorCommissionService directorCommissionService;

    @PostMapping("/query")
    public Object findDirectorCommission() {
        return directorCommissionService.findDirectorCommission();
    }
}
