package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;

public interface SysOperationLogService {
    void insertRequest(SysOperationLogEntity operationLog);

    void updateResponse(SysOperationLogEntity operationLog);
}
