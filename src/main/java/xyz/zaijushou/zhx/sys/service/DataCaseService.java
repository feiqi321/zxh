package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCaseService {

    public void save(DataCaseEntity dataCaseEntity);

    public void update(DataCaseEntity dataCaseEntity);

    public void delete(DataCaseEntity dataCaseEntity);

    public List<DataCaseEntity> pageDataCaseList(DataCaseEntity dataCaseEntity);

    public List<DataCaseEntity> pageCaseList(DataCaseEntity dataCaseEntity);

    public List<DataCaseEntity> pageCaseInfoList(DataCaseEntity dataCaseEntity);

    void sumCaseMoney(DataCaseEntity bean);

    public void updateStatus(DataCaseEntity bean);

    public void sendOdv(DataCaseEntity bean);

    public void addComment(DataCaseEntity bean);

    public void addColor(DataCaseEntity bean);

    public void addImportant(DataCaseEntity bean);

    public void addCollectStatus(DataCaseEntity bean);

    public void addCollectArea(DataCaseEntity bean);

    public void addMValue(DataCaseEntity bean);

    public void addSynergy(DataCaseEntity bean);
}
