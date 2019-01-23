package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;

@Mapper
public interface SysOperationLogMapper {
    void insertRequest(SysOperationLogEntity operationLog);

    void updateResponse(SysOperationLogEntity operationLog);
}
