package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.CaseOpLog;

import java.util.List;

/**
 * Created by looyer on 2019/8/17.
 */
public interface CaseOpLogService {

    WebResponse pageCaseOpLog(CaseOpLog caseOpLog);

}
