package xyz.zaijushou.zhx.sys.service;

import com.github.pagehelper.PageInfo;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.*;

import java.util.List;
import java.util.Map;

/**
 * Created by looyer on 2019/1/25.
 */
public interface DataCaseService {

    public void save(DataCaseEntity dataCaseEntity);

    public void update(DataCaseEntity dataCaseEntity);

    public void delete(List<DataCaseEntity> list);

    public List<DataCaseEntity> pageDataCaseList(DataCaseEntity dataCaseEntity);

    public WebResponse pageCaseList(DataCaseEntity dataCaseEntity) throws Exception;

    public WebResponse pageCaseListOnly(DataCaseEntity dataCaseEntity) throws Exception;

    public List<DataCaseEntity> pageCaseInfoList(DataCaseEntity dataCaseEntity);

    void sumCaseMoney(DataCaseEntity bean);

    public void updateStatus(DataCaseEntity bean);

    public void sendOdv(DataCaseEntity bean);

    public void sendOdvByProperty(DataCaseEntity bean);

    public void autoSendByProperty(DataCaseEntity bean);

    public WebResponse autoSendByPropertyResult(DataCaseEntity bean);

    public WebResponse querySendByProperty(DataCaseEntity bean);

    public void addComment(DataCaseEntity bean);

    public void addWarning(DataCaseEntity bean);

    public void addColor(DataCaseEntity bean);

    public void addImportant(DataCaseEntity bean);

    public void addCollectStatus(DataCaseEntity bean);

    public void addCollectArea(DataCaseEntity bean);

    public void addMValue(DataCaseEntity bean);

    public void addSynergy(DataCaseEntity bean);

    public void updateSynergy(DataCaseEntity bean);

    public WebResponse pageSynergyInfo(DataCaseEntity dataCaseEntity);

    public WebResponse pageCaseTel(DataCaseEntity dataCaseEntity);

    List<DataCaseEntity> listBySeqNoSet(DataCaseEntity queryEntity);

    void updateBySeqNo(DataCaseEntity entity);

    void updateCaseList(List<DataCaseEntity> dataCaseEntities);

    void saveCaseList(List<DataCaseEntity> dataCaseEntities,String batchNo);

    public List<DataCaseEntity> pageCaseListExport(DataCaseEntity dataCaseEntity);

    public List<DataCaseEntity> selectCaseListExport(DataCaseEntity bean);

    public List<DataCaseEntity> selectDataCaseExportByBatch(DataCaseEntity dataCaseEntity);

    public List<DataCaseTelExport> selectCaseTelListExport(DataCaseEntity bean);

    public List<DataCaseEntity> totalCaseListExport(DataCaseEntity dataCaseEntity);

    public DataCaseDetail detail(DataCaseEntity bean);

    public void updateCase(DataCaseDetail bean);

    public DataCaseDetail findById(DataCaseEntity bean);

    public void updateCaseTelStatus(DataCaseTelEntity bean);

    public void updateCaseAddressStatus(DataCaseAddressEntity bean);

    //地址
    public List<DataCaseAddressEntity> findAddressListByCaseId(DataCaseEntity bean);

    public List<DataCaseTelEntity> findTelListByCaseId(DataCaseEntity bean);

    public void updateRemark(DataCaseEntity bean) throws Exception;

    public DataCaseTelEntity saveCaseTel(DataCaseTelEntity bean);

    public void addBatchCaseTel(DataCaseTelEntity bean);

    public void delCaseTel(DataCaseTelEntity bean);

    public WebResponse synchroSameTel(DataCaseEntity bean);

    public List<DataCaseEntity> sameCaseList(DataCaseEntity bean);

    public Map sameBatchCaseList(DataCaseEntity bean);

    public void saveCaseAddress(DataCaseAddressEntity bean);

    public void delCaseAddress(DataCaseAddressEntity bean);

    PageInfo<DataCaseEntity> listSeqNos(DataCaseEntity dataCaseEntity);

    public List<DataCaseCommentEntity> listComment(DataCaseEntity dataCaseEntity);

    public DataCaseCommentEntity detailComment(DataCaseCommentEntity bean);

    public void updateComment(DataCaseCommentEntity bean);

    public void delComment(DataCaseCommentEntity bean);

    public List<DataCaseInterestEntity> listInterest(DataCaseEntity dataCaseEntity);

    public List<DataCaseEntity> listSynergy(DataCaseEntity dataCaseEntity);

    public List<DataCaseEntity> listByBatchNos(String[] batchNos);

    public void updateCollectInfo(DataCaseEntity dataCaseEntity);

    DataCaseEntity nextCase(DataCaseEntity bean);

    DataCaseEntity lastCase(DataCaseEntity bean);

     void doDataCase(List<DataCaseEntity> caseEntityList);
}
