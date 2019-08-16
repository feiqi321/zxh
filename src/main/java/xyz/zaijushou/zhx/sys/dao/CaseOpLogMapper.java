package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.CaseOpLog;

import java.util.List;

/**
 * Created by looyer on 2019/8/15.
 */
public interface CaseOpLogMapper {

    void addCaseOpLog(CaseOpLog caseOpLog);

    List<CaseOpLog> pageCaseOpLog(CaseOpLog caseOpLog);

}
