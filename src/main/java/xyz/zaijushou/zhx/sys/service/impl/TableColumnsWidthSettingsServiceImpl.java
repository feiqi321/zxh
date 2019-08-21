package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.TableColumnsWidthSettingsMapper;
import xyz.zaijushou.zhx.sys.entity.SysTableColumnDTO;
import xyz.zaijushou.zhx.sys.entity.SysTableColumnEntity;
import xyz.zaijushou.zhx.sys.service.TableColumnsWidthSettingsService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,13:34]
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class TableColumnsWidthSettingsServiceImpl implements TableColumnsWidthSettingsService {
    @Resource
    private TableColumnsWidthSettingsMapper tableColumnsWidthSettingsMapper;

    @Override
    public Object findTableInformationByTableId(String tableid) {
        Integer userid = JwtTokenUtil.tokenData().getInteger("userId");
        return tableColumnsWidthSettingsMapper.findTableInformationByTableId(tableid, userid);
    }

    @Override
    public Object addTableInformation(SysTableColumnDTO sysTableColumnDTO) {
        Integer userid = JwtTokenUtil.tokenData().getInteger("userId");
        tableColumnsWidthSettingsMapper.removeOldTableInformation(sysTableColumnDTO.getTableid(), userid);
        List<SysTableColumnEntity> list = sysTableColumnDTO.getColumns();
        for (SysTableColumnEntity sysTableColumnEntity : list) {
            sysTableColumnEntity.setTableid(sysTableColumnDTO.getTableid());
            sysTableColumnEntity.setUserid(userid);
            tableColumnsWidthSettingsMapper.addTableInformation(sysTableColumnEntity);
        }
        return WebResponse.success();
    }
}
