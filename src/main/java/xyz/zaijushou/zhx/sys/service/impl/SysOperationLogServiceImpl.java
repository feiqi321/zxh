package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysOperationLogMapper;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    @Resource
    private SysOperationLogMapper operationLogMapper;

    @Override
    public void insertRequest(SysOperationLogEntity operationLog) {
        operationLogMapper.insertRequest(operationLog);
    }

    @Override
    public void updateResponse(SysOperationLogEntity operationLog) {
        operationLogMapper.updateResponse(operationLog);
    }

    @Override
    public List<SysOperationLogEntity> pageLogs(SysOperationLogEntity operationLog) {
        return operationLogMapper.pageLogs(operationLog);
    }
}
