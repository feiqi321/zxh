package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysOperationLogMapper;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogTypeEntity;
import xyz.zaijushou.zhx.sys.service.SysOperationLogService;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    @Resource
    private SysOperationLogMapper operationLogMapper;


    @Override
    public void insertRequest(SysOperationLogEntity operationLog) {
        if (StringUtils.isEmpty(operationLog.getUrl())){
            operationLog.setUrl("/import");
        }
        try {
            if (StringUtils.notEmpty(operationLog.getUrl()) && operationLog.getUrl().indexOf("page")>0){

            }else if (StringUtils.notEmpty(operationLog.getUrl()) && operationLog.getUrl().indexOf("user")>0){

            }else if (StringUtils.notEmpty(operationLog.getUrl()) && operationLog.getUrl().indexOf("organization")>0){

            }else if (StringUtils.notEmpty(operationLog.getUrl()) && (operationLog.getUrl().indexOf("role")>0 || operationLog.getUrl().indexOf("idno")>0 ||  operationLog.getUrl().indexOf("load")>0)){

            }else if (StringUtils.notEmpty(operationLog.getUrl()) && (operationLog.getUrl().indexOf("query")>0 || operationLog.getUrl().indexOf("Select")>0 || operationLog.getUrl().indexOf("List")>0 || operationLog.getUrl().indexOf("list")>0 || operationLog.getUrl().indexOf("select")>0 || operationLog.getUrl().indexOf("lastCase")>0 || operationLog.getUrl().indexOf("nextCase")>0 || operationLog.getUrl().indexOf("updateRemak")>0 ||  operationLog.getUrl().indexOf("send")>0 ||  operationLog.getUrl().indexOf("Send")>0)){

            }else if (StringUtils.notEmpty(operationLog.getUrl()) && (operationLog.getUrl().indexOf("statistics")>0 || operationLog.getUrl().indexOf("Detail")>0 || operationLog.getUrl().indexOf("detail")>0 || operationLog.getUrl().indexOf("export")>0 || operationLog.getUrl().indexOf("Export")>0)){

            }else {
                operationLogMapper.insertRequest(operationLog);
            }
        }catch(Exception e){

        }
    }

    @Override
    public void updateResponse(SysOperationLogEntity operationLog) {
        if (StringUtils.isEmpty(operationLog.getUrl())){

        }else {
            operationLogMapper.updateResponse(operationLog);
        }
    }

    @Override
    public PageInfo<SysOperationLogEntity> pageLogs(SysOperationLogEntity operationLog) {
        List<SysOperationLogEntity> list = operationLogMapper.pageLogs(operationLog);
        return PageInfo.of(list);
    }

    @Override
    public List<SysOperationLogTypeEntity> listLogType(SysOperationLogTypeEntity sysOperationLogTypeEntity) {
        return operationLogMapper.listLogType(sysOperationLogTypeEntity);
    }
}
