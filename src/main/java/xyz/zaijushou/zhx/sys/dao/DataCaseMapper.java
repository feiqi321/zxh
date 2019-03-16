package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseDetail;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseTelExport;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Mapper
public interface DataCaseMapper {

    public int saveCase(DataCaseEntity bean);

    public void updateCase(DataCaseEntity bean);

    public void deleteById(Integer id);

    public DataCaseDetail detail(DataCaseEntity bean);

    public  DataCaseEntity findById(DataCaseEntity bean);

    public List<DataCaseEntity> pageDataCase(DataCaseEntity bean);

    public List<DataCaseEntity> pageCaseInfo(DataCaseEntity bean);

    public List<DataCaseEntity> pageCaseList(DataCaseEntity bean);

    public List<DataCaseEntity> totalCaseList(DataCaseEntity bean);

    public List<DataCaseEntity> pageBatchBoundsCaseList(DataCaseEntity bean);

    public List<DataCaseEntity> totalBatchBoundsCaseList(DataCaseEntity bean);

    public List<DataCaseEntity> selectCaseList(int[] ids);

    public List<DataCaseEntity> selectDataCaseExportByBatch(DataCaseEntity bean);

    public List<DataCaseTelExport> selectCaseTelListExport(int[] ids);

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

    public int countUserCase(DataCaseEntity bean);



    public void updateComment(DataCaseEntity bean);

    List<DataCaseEntity> listBySeqNoSet(DataCaseEntity queryEntity);

    void updateBySeqNo(DataCaseEntity entity);

    void updateRemark(DataCaseEntity entity);

    List<DataCaseEntity> pageSeqNos(DataCaseEntity dataCaseEntity);

    List<DataCaseEntity> listSynergy(int[] status);

    List<DataCaseEntity> listByBatchNos(String[] batchNos);
    //同批次共债案件
    List<DataCaseEntity> findSameBatchCase(DataCaseEntity dataCaseEntity);
    //共债案件
    List<DataCaseEntity> findSameCase(DataCaseEntity dataCaseEntity);

    void addCollectTimes(DataCaseEntity dataCaseEntity);

    void updateDataCaseByCollect(DataCaseEntity dataCaseEntity);

    void updateDataCaseByCollect2(DataCaseEntity dataCaseEntity);

    void updateDataCaseByCollect3(DataCaseEntity dataCaseEntity);

    void updateDataCaseByCollectAdd(DataCaseEntity dataCaseEntity);

    void updateDataCaseByCollectResult(DataCaseEntity dataCaseEntity);

    void updateRepayMoney(DataCaseEntity dataCaseEntity);

    void updateCpMoney(DataCaseEntity dataCaseEntity);
}
