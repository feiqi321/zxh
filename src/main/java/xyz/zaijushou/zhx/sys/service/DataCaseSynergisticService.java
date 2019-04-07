package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergisticEntity;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergyDetailEntity;

import java.util.List;

public interface DataCaseSynergisticService {
    PageInfo<DataCaseSynergisticEntity> pageSynergisticList(DataCaseSynergisticEntity synergistic);

    PageInfo<DataCaseSynergisticEntity> pageSynergisticExportList(DataCaseSynergisticEntity synergistic);

    void approve(DataCaseSynergisticEntity synergistic);

    void saveApply(DataCaseSynergyDetailEntity bean);

    void saveResult(DataCaseSynergyDetailEntity bean);

    void finish(DataCaseSynergisticEntity synergistic);

    List<DataCaseSynergisticEntity> listSynergistic(DataCaseSynergisticEntity synergistic);

    List<SysDictionaryEntity> listAllTypes();

    List<DataCaseSynergisticEntity> countNum(DataCaseSynergisticEntity synergistic);

    List<DataCaseSynergisticEntity> listByIdsSet(DataCaseSynergisticEntity queryEntity);

    void updateInfo(DataCaseSynergisticEntity entity);

    void updateInfoByCaseId(DataCaseSynergisticEntity entity);

    void saveBatch(List<DataCaseSynergisticEntity> synergisticList);
}
