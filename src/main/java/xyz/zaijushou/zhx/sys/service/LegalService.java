package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.LegalEntity;

/**
 * Created by looyer on 2019/2/12.
 */
public interface LegalService {


    public void saveLegal(LegalEntity bean);

    public void updateLegal(LegalEntity bean);

    public WebResponse pageDataLegal(LegalEntity bean);

    public void deleteLegal(LegalEntity bean);
}
