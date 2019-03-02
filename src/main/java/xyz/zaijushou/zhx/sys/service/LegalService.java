package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.LegalEntity;
import xyz.zaijushou.zhx.sys.entity.LegalFee;
import xyz.zaijushou.zhx.sys.entity.LegalHandle;

/**
 * Created by looyer on 2019/2/12.
 */
public interface LegalService {


    public void saveLegal(LegalEntity bean);

    public void updateLegal(LegalEntity bean);

    public WebResponse pageDataLegal(LegalEntity bean);

    public WebResponse listLegal(LegalEntity bean);

    public WebResponse pageMyDataLegal(LegalEntity bean);

    public void deleteLegal(LegalEntity bean);

    public void checkLegal(LegalEntity bean);

    public WebResponse detail(LegalEntity bean);

    public void saveLegalFee(LegalFee bean);

    public void updateLegalFee(LegalFee bean);

    public void deleteLegalFee(LegalFee bean);

    public void saveLegalHandle(LegalHandle bean);

    public void updateLegalHandle(LegalHandle bean);

    public void deleteLegalHandle(LegalHandle bean);
}
