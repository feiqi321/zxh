package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.*;

import java.util.List;

/**
 * Created by looyer on 2019/2/12.
 */
public interface FileManageService {

    public WebResponse batchCaseTel(List<DataCaseTelEntity> list);

    public WebResponse batchCaseAddress(List<DataCaseAddressEntity> list);

    public WebResponse batchCaseInterest(List<DataCaseInterestEntity> list);

    public WebResponse batchCaseComment(List<DataCaseEntity> list);

    public WebResponse batchArchive(List<DataArchiveEntity> list);

    public WebResponse batchCollect(List<DataCollectionEntity> list);

    public WebResponse batchLetter(List<Letter> list);

}
