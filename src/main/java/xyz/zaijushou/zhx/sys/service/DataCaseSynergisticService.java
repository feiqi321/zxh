package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergisticEntity;

import java.util.List;

public interface DataCaseSynergisticService {
    PageInfo<DataCaseSynergisticEntity> pageSynergisticList(DataCaseSynergisticEntity synergistic);

    void approve(DataCaseSynergisticEntity synergistic);

    void finish(DataCaseSynergisticEntity synergistic);

    List<DataCaseSynergisticEntity> listSynergistic(DataCaseSynergisticEntity synergistic);
}
