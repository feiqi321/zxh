package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
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
    public PageInfo<SysOperationLogEntity> pageLogs(SysOperationLogEntity operationLog) {
        List<SysOperationLogEntity> list = operationLogMapper.pageLogs(operationLog);
        return PageInfo.of(list);
    }
}
