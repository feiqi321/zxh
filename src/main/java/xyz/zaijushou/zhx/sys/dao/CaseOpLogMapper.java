package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.CaseOpLog;

import java.util.List;

/**
 * Created by looyer on 2019/8/15.
 */
@Mapper
public interface CaseOpLogMapper {

    void addCaseOpLog(CaseOpLog caseOpLog);

    void addCaseOpLogList(List<CaseOpLog> list);

    List<CaseOpLog> pageCaseOpLog(CaseOpLog caseOpLog);

}
