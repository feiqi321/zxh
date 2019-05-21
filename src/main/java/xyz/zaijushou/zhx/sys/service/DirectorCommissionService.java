package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;

/**
 * @author lsl
 * @version [1.0.0, 2019/5/9,14:26]
 */
public interface DirectorCommissionService {
    /**
     * 总监提成查询
     * @return 结果
     */
    WebResponse findDirectorCommission();
}
