package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataArchiveMapper;
import xyz.zaijushou.zhx.sys.entity.DataArchiveEntity;
import xyz.zaijushou.zhx.sys.service.DataArchiveService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataArchivesServiceImpl implements DataArchiveService {
    @Resource
    private DataArchiveMapper dataArchiveMapper;

    @Override
    public void save(DataArchiveEntity dataArchiveEntity){
        dataArchiveMapper.saveArchive(dataArchiveEntity);
    }
    @Override
    public void update(DataArchiveEntity dataArchiveEntity){
        dataArchiveMapper.updateArchive(dataArchiveEntity);
    }
    @Override
    public void delete(DataArchiveEntity dataArchiveEntity){
        dataArchiveMapper.deleteById(dataArchiveEntity.getId());
    }
    @Override
    public List<DataArchiveEntity> pageDataArchiveList(DataArchiveEntity dataArchiveEntity){
       List<DataArchiveEntity> list =  dataArchiveMapper.pageDataArchive(dataArchiveEntity);
       return list;
    }
}
