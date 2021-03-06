package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogTypeEntity;

import java.util.List;

@Mapper
public interface SysOperationLogMapper {
    void insertRequest(SysOperationLogEntity operationLog);

    void updateResponse(SysOperationLogEntity operationLog);

    List<SysOperationLogEntity> pageLogs(SysOperationLogEntity operationLog);

    List<SysOperationLogTypeEntity> listLogType(SysOperationLogTypeEntity sysOperationLogTypeEntity);
}
