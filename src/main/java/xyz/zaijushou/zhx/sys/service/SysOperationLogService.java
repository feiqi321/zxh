package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;

import java.util.List;

public interface SysOperationLogService {
    void insertRequest(SysOperationLogEntity operationLog);

    void updateResponse(SysOperationLogEntity operationLog);

    List<SysOperationLogEntity> pageLogs(SysOperationLogEntity operationLog);
}
