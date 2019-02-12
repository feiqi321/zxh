package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCaseAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseTelEntity;

import java.util.List;

/**
 * Created by looyer on 2019/2/12.
 */
public interface FileManageService {

    public WebResponse batchCaseTel(List<DataCaseTelEntity> list);

    public WebResponse batchCaseAddress(List<DataCaseAddressEntity> list);

}
