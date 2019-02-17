package xyz.zaijushou.zhx.sys.service.impl;

import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.LetterMapper;
import xyz.zaijushou.zhx.sys.entity.Letter;
import xyz.zaijushou.zhx.sys.service.LetterService;

import javax.annotation.Resource;

/**
 * Created by looyer on 2019/2/16.
 */
public class LetterServiceImpl implements LetterService {

    @Resource
    private LetterMapper letterMapper;

    public WebResponse pageDataLetter(Letter letter){
        WebResponse webResponse = WebResponse.buildResponse();

        return webResponse;
    }

    public void confirmSynergy(Letter letter){

    }


    public void cancelLetter(Letter letter){

    }

    public void confirmLetter(Letter letter){

    }


}
