package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelLetterConstant;
import xyz.zaijushou.zhx.sys.entity.Letter;
import xyz.zaijushou.zhx.sys.entity.LetterExportEntity;

import java.util.List;

/**
 * Created by looyer on 2019/2/16.
 */
public interface LetterService {

    public WebResponse pageDataLetter(Letter letter);

    public void confirmSynergy(Letter letter);

    public void cancelLetter(Letter letter);

    public void confirmLetter(Letter letter);

    public void addLetter(Letter letter);

    public WebResponse findByCaseId(Letter letter);

    public List<LetterExportEntity> pageExportList(Letter letter);

    public List<LetterExportEntity> totalExportList(Letter letter);

}
