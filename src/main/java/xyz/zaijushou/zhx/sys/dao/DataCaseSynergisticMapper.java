package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergisticEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseSynergyDetailEntity;

import java.util.List;

@Mapper
public interface DataCaseSynergisticMapper {
    List<DataCaseSynergisticEntity> pageSynergisticList(DataCaseSynergisticEntity synergistic);

    List<DataCaseSynergisticEntity> pageSynergisticExportList(DataCaseSynergisticEntity synergistic);

    void updateApplyStatus(DataCaseSynergisticEntity synergistic);

    void saveApply(DataCaseSynergyDetailEntity bean);

    void saveResult(DataCaseSynergyDetailEntity bean);

    void updateFinishStatus(DataCaseSynergisticEntity synergistic);

    List<DataCaseSynergisticEntity> listSynergistic(DataCaseSynergisticEntity synergistic);

    List<DataCaseSynergisticEntity> countNum(DataCaseSynergisticEntity synergistic);

    List<DataCaseSynergisticEntity> listByIdsSet(DataCaseSynergisticEntity queryEntity);

    void updateInfo(DataCaseSynergisticEntity entity);

    void updateInfoByCaseId(DataCaseSynergisticEntity entity);

    void saveBatch(List<DataCaseSynergisticEntity> synergisticList);
}
