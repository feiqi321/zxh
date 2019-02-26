package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataCaseSynergisticMapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergisticEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseSynergisticService;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DataCaseSynergisticServiceImpl implements DataCaseSynergisticService {

    @Resource
    private DataCaseSynergisticMapper dataCaseSynergisticMapper;

    @Override
    public PageInfo<DataCaseSynergisticEntity> pageSynergisticList(DataCaseSynergisticEntity synergistic) {
        List<DataCaseSynergisticEntity> synergisticList = dataCaseSynergisticMapper.pageSynergisticList(synergistic);
        return PageInfo.of(synergisticList);
    }

    @Override
    public void approve(DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticMapper.updateApplyStatus(synergistic);
    }

    @Override
    public void finish(DataCaseSynergisticEntity synergistic) {
        dataCaseSynergisticMapper.updateFinishStatus(synergistic);
    }

    @Override
    public List<DataCaseSynergisticEntity> listSynergistic(DataCaseSynergisticEntity synergistic) {
        return dataCaseSynergisticMapper.listSynergistic(synergistic);
    }
}
