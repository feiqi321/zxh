package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.TableColumnsWidthSettingsMapper;
import xyz.zaijushou.zhx.sys.entity.SysTableColumnEntity;
import xyz.zaijushou.zhx.sys.service.TableColumnsWidthSettingsService;

import javax.annotation.Resource;
import java.util.List;


/**
 * @author lsl
 * @version [1.0.0, 2019/8/20,13:34]
 */
@Service
@Transactional
public class TableColumnsWidthSettingsServiceImpl implements TableColumnsWidthSettingsService {
    @Resource
    private TableColumnsWidthSettingsMapper tableColumnsWidthSettingsMapper;

    @Override
    public Object findTableInformationByTableId(String tableid) {
        return tableColumnsWidthSettingsMapper.findTableInformationByTableId(tableid);
    }

    @Override
    public Object addTableInformation(List<SysTableColumnEntity> list) {
        tableColumnsWidthSettingsMapper.removeOldTableInformation(list.get(0).getTableid(), list.get(0).getUserid());
        for (SysTableColumnEntity sysTableColumnEntity : list) {
            if (sysTableColumnEntity.getColumnwidth() == null) {
                sysTableColumnEntity.setColumnwidth(sysTableColumnEntity.getMinwidth());
                tableColumnsWidthSettingsMapper.addTableInformation(sysTableColumnEntity);
            } else {
                tableColumnsWidthSettingsMapper.addTableInformation(sysTableColumnEntity);
            }
        }
        return WebResponse.success();
    }
}
