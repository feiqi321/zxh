package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCaseArchiveMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseArchive;
import xyz.zaijushou.zhx.sys.service.DataCaseArchiveService;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by looyer on 2019/3/7.
 */
@Service
public class DataCaseArchiveServiceImpl implements DataCaseArchiveService {

    @Resource
    private DataCaseArchiveMapper dataCaseArchiveMapper;

    public List<DataCaseArchive> listByCaseId(DataCaseArchive bean){
        return dataCaseArchiveMapper.listByCaseId(bean);
    }

    public void save(DataCaseArchive bean){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        bean.setCreateTime(sdf.format(new Date()));
        dataCaseArchiveMapper.save(bean);
    }

}
