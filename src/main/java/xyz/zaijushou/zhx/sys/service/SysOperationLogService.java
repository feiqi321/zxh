package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogTypeEntity;

import java.util.List;

public interface SysOperationLogService {
    void insertRequest(SysOperationLogEntity operationLog);

    void updateResponse(SysOperationLogEntity operationLog);

    PageInfo<SysOperationLogEntity> pageLogs(SysOperationLogEntity operationLog);

    List<SysOperationLogTypeEntity> listLogType(SysOperationLogTypeEntity sysOperationLogTypeEntity);
}
