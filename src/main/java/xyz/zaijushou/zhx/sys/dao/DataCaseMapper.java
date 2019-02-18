package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Mapper
public interface DataCaseMapper {

    public int saveCase(DataCaseEntity bean);

    public void updateCase(DataCaseEntity bean);

    public void deleteById(Integer id);

    public List<DataCaseEntity> pageDataCase(DataCaseEntity bean);

    public List<DataCaseEntity> pageCaseInfo(DataCaseEntity bean);

    public List<DataCaseEntity> pageCaseList(DataCaseEntity bean);

    public List<DataCaseEntity> pageBatchBoundsCaseList(DataCaseEntity bean);

    public void sumCaseMoney(DataCaseEntity bean);

    public void sendOdv(DataCaseEntity bean);

    public void sendOdvByProperty(DataCaseEntity bean);

    public void updateStatus(DataCaseEntity bean);

    public void addComment(DataCaseEntity bean);

    public void addColor(DataCaseEntity bean);

    public void addImportant(DataCaseEntity bean);

    public void addCollectStatus(DataCaseEntity bean);

    public void addCollectArea(DataCaseEntity bean);

    public void addMValue(DataCaseEntity bean);

    public void addSynergy(DataCaseEntity bean);

    public List<DataCaseEntity> pageSynergyInfo(DataCaseEntity bean);

    public int countSynergyInfo(DataCaseEntity bean);

    public void updateSynergy(DataCaseEntity bean);

    public List<DataCaseEntity> pageCaseTel(DataCaseEntity bean);

    public int countCaseTel(DataCaseEntity bean);

    public List<DataCaseEntity> listAllCaseInfo(DataCaseEntity bean);


    public void updateComment(DataCaseEntity bean);

    List<DataCaseEntity> listBySeqNoSet(DataCaseEntity queryEntity);

    void updateBySeqNo(DataCaseEntity entity);
}
