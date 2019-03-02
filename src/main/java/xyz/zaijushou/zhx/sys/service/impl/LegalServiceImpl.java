package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
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
        if (bean.getId()==null || bean.getId()==0){
            legalMapper.insertSelective(bean);
        }else{
            legalMapper.updateByPrimaryKeySelective(bean);
        }

    }

    public void updateLegal(LegalEntity bean){
        legalMapper.updateByPrimaryKeySelective(bean);
    }
    //1 已经审核 2审核中 0未申请
    public WebResponse pageDataLegal(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<LegalEntity> dataCaseEntities = legalMapper.pageDataLegal(bean);
        for (int i=0;i<dataCaseEntities.size();i++){
            LegalEntity legalEntity = dataCaseEntities.get(i);
            if (legalEntity.getLegalStatus()==1){
                legalEntity.setLegalStatusMsg("已审核");
            }else{
                legalEntity.setLegalStatusMsg("未审核");
            }
            if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("1")){
                legalEntity.setProgressMsg("判决");
            }else if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("2")){
                legalEntity.setProgressMsg("收案");
            }else{
                legalEntity.setProgressMsg("未判决");
            }
            dataCaseEntities.set(i,legalEntity);
        }

        webResponse.setData(PageInfo.of(dataCaseEntities));
        return webResponse;
    }

    public WebResponse listLegal(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        List<LegalEntity> dataCaseEntities = legalMapper.listLegal(bean);
        for (int i=0;i<dataCaseEntities.size();i++){
            LegalEntity legalEntity = dataCaseEntities.get(i);
            if (legalEntity.getLegalStatus()==1){
                legalEntity.setLegalStatusMsg("已审核");
            }else{
                legalEntity.setLegalStatusMsg("未审核");
            }
            if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("1")){
                legalEntity.setProgressMsg("判决");
            }else if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("2")){
                legalEntity.setProgressMsg("收案");
            }else{
                legalEntity.setProgressMsg("未判决");
            }
            dataCaseEntities.set(i,legalEntity);
        }

        webResponse.setData(dataCaseEntities);
        return webResponse;
    }

    public WebResponse pageMyDataLegal(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        SysUserEntity user = getUserInfo();
        bean.setOwner(user.getId()+"");
        List<LegalEntity> dataCaseEntities = legalMapper.pageDataLegal(bean);

        webResponse.setData(PageInfo.of(dataCaseEntities));
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


    public void saveLegalFee(LegalFee bean){
        if (bean.getId()==null || bean.getId()==0) {
            legalFeeMapper.insertSelective(bean);
        }else{
            legalFeeMapper.updateByPrimaryKeySelective(bean);
        }

    }

    public void updateLegalFee(LegalFee bean){
        legalFeeMapper.updateByPrimaryKeySelective(bean);
    }

    public void deleteLegalFee(LegalFee bean){
        legalFeeMapper.deleteByPrimaryKey(bean.getId());
    }

    public void saveLegalHandle(LegalHandle bean){
        if (bean.getId()==null || bean.getId()==0){
            legalHandleMapper.insertSelective(bean);
        }else{
            legalHandleMapper.updateByPrimaryKeySelective(bean);
        }

    }

    public void updateLegalHandle(LegalHandle bean){
        legalHandleMapper.updateByPrimaryKeySelective(bean);
    }

    public void deleteLegalHandle(LegalHandle bean){
        legalHandleMapper.deleteByPrimaryKey(bean.getId());
    }

}
