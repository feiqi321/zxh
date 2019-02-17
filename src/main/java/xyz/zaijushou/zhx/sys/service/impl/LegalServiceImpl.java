package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.LegalFeeMapper;
import xyz.zaijushou.zhx.sys.dao.LegalHandleMapper;
import xyz.zaijushou.zhx.sys.dao.LegalMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.LegalService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by looyer on 2019/2/12.
 */
@Service
public class LegalServiceImpl implements LegalService {
    @Resource
    private LegalMapper legalMapper;
    @Resource
    private LegalFeeMapper legalFeeMapper;
    @Resource
    private LegalHandleMapper legalHandleMapper;

    @Resource
    private SysUserService sysUserService;//用户业务控制层

    public void saveLegal(LegalEntity bean){
        legalMapper.insertSelective(bean);
    }

    public void updateLegal(LegalEntity bean){
        legalMapper.updateByPrimaryKeySelective(bean);
    }

    public WebResponse pageDataLegal(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<LegalEntity> dataCaseEntities = legalMapper.pageDataLegal(bean);
        int count = legalMapper.countDataLegal(bean);

        int totalPageNum = 0 ;
        if (count%bean.getPageSize()>0){
            totalPageNum = count/bean.getPageSize()+1;
        }else{
            totalPageNum = count/bean.getPageSize();
        }
        webResponse.setData(dataCaseEntities);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setTotalNum(count);
        return webResponse;
    }

    public WebResponse pageMyDataLegal(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        SysUserEntity user = getUserInfo();
        bean.setOwner(user.getId()+"");
        List<LegalEntity> dataCaseEntities = legalMapper.pageDataLegal(bean);
        int count = legalMapper.countDataLegal(bean);

        int totalPageNum = 0 ;
        if (count%bean.getPageSize()>0){
            totalPageNum = count/bean.getPageSize()+1;
        }else{
            totalPageNum = count/bean.getPageSize();
        }
        webResponse.setData(dataCaseEntities);
        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setTotalNum(count);
        return webResponse;
    }

    public void deleteLegal(LegalEntity bean){
        legalMapper.deleteByPrimaryKey(bean.getId());
    }

    public void checkLegal(LegalEntity bean){
        SysUserEntity user = getUserInfo();
        bean.setChecker(user.getId()+"");
        legalMapper.checkLegal(bean);
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutPasswordById(user);
    }

    public WebResponse detail(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        LegalDetail legalDetail = new LegalDetail();
        LegalEntity legalEntity = legalMapper.selectByPrimaryKey(bean.getId());
        LegalFee legalFee = new LegalFee();
        legalFee.setLegalId(bean.getId());
        List<LegalFee> feeList = legalFeeMapper.findAllLegalFee(legalFee);
        LegalHandle legalHandle = new LegalHandle();
        legalHandle.setLegalId(bean.getId());
        List<LegalHandle> handleList = legalHandleMapper.findALlLegalHandle(legalHandle);
        legalDetail.setLegalEntity(legalEntity);
        legalDetail.setFeeList(feeList);
        legalDetail.setHandleList(handleList);
        webResponse.setData(legalDetail);
        return webResponse;
    }

}
