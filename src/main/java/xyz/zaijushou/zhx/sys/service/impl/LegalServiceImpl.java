package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CaseSortEnum;
import xyz.zaijushou.zhx.constant.LegalSortEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.LegalFeeMapper;
import xyz.zaijushou.zhx.sys.dao.LegalHandleMapper;
import xyz.zaijushou.zhx.sys.dao.LegalMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.LegalService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.FmtMicrometer;
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
    //1 已经审核 2审核中 0未申请      未退案0/正常1/暂停2/关档3/退档4/全部5
    public WebResponse pageDataLegal(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        if(org.apache.commons.lang3.StringUtils.isEmpty(bean.getOrderBy())){
            bean.setSort("desc");
            bean.setOrderBy("id");
        }else {
            bean.setOrderBy(LegalSortEnum.getEnumByKey(bean.getOrderBy()).getValue());
        }
        List<LegalEntity> dataCaseEntities = legalMapper.pageDataLegal(bean);
        for (int i=0;i<dataCaseEntities.size();i++){
            LegalEntity legalEntity = dataCaseEntities.get(i);
            if (legalEntity!=null && legalEntity.getLegalStatus()!=null && legalEntity.getLegalStatus()==1){
                legalEntity.setLegalStatusMsg("已审核");
            }else{
                legalEntity.setLegalStatus(0);
                legalEntity.setLegalStatusMsg("未审核");
            }
            if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("1")){
                legalEntity.setProgressMsg("判决");
            }else if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("2")){
                legalEntity.setProgressMsg("收案");
            }else{
                legalEntity.setProgressMsg("未判决");
            }
            if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==0){
                legalEntity.setLegalTypeMsg("未退案");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==1){
                legalEntity.setLegalTypeMsg("正常");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==2){
                legalEntity.setLegalTypeMsg("暂停");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==3){
                legalEntity.setLegalTypeMsg("关档");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==4){
                legalEntity.setLegalTypeMsg("退档");
            }


            SysUserEntity user = getUserInfo();
            if (StringUtils.isNotEmpty(legalEntity.getOwner()) && user.getId()==Integer.valueOf(legalEntity.getOwner())){
                legalEntity.setCuurentUser(true);
            }else{
                legalEntity.setCuurentUser(false);
            }
            if (StringUtils.isNotEmpty(legalEntity.getOwner())){
                SysUserEntity mine = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ legalEntity.getOwner(), SysUserEntity.class);
                legalEntity.setOwnerName(mine.getUserName());
            }else{
                legalEntity.setOwnerName("");
            }
            legalEntity.setCostMsg(legalEntity.getCost()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(legalEntity.getCost()+""));
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
            if (legalEntity!=null && legalEntity.getLegalStatus()!=null && legalEntity.getLegalStatus()==1){
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
            if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==0){
                legalEntity.setLegalTypeMsg("未退案");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==1){
                legalEntity.setLegalTypeMsg("正常");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==2){
                legalEntity.setLegalTypeMsg("暂停");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==3){
                legalEntity.setLegalTypeMsg("关档");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==4){
                legalEntity.setLegalTypeMsg("退档");
            }

            if (StringUtils.isNotEmpty(legalEntity.getOwner())){
                SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ legalEntity.getOwner(), SysUserEntity.class);
                legalEntity.setOwnerName(user.getUserName());
            }else{
                legalEntity.setOwnerName("");
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
        for (int i=0;i<dataCaseEntities.size();i++){
            LegalEntity legalEntity = dataCaseEntities.get(i);
            if (legalEntity!=null && legalEntity.getLegalStatus()!=null && legalEntity.getLegalStatus()==1){
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
            if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==0){
                legalEntity.setLegalTypeMsg("未退案");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==1){
                legalEntity.setLegalTypeMsg("正常");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==2){
                legalEntity.setLegalTypeMsg("暂停");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==3){
                legalEntity.setLegalTypeMsg("关档");
            }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==4){
                legalEntity.setLegalTypeMsg("退档");
            }

            if (StringUtils.isNotEmpty(legalEntity.getOwner())){
                SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ legalEntity.getOwner(), SysUserEntity.class);
                legalEntity.setOwnerName(userTemp.getUserName());
            }else{
                legalEntity.setOwnerName("");
            }
            legalEntity.setCostMsg(legalEntity.getCost()==null?"": "￥"+ FmtMicrometer.fmtMicrometer(legalEntity.getCost()+""));
            dataCaseEntities.set(i,legalEntity);
        }
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
        SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ userId, SysUserEntity.class);
        return userTemp;
    }

    public WebResponse detail(LegalEntity bean){
        WebResponse webResponse = WebResponse.buildResponse();
        LegalDetail legalDetail = new LegalDetail();
        LegalEntity legalEntity = legalMapper.selectByPrimaryKey(bean.getId());

        if (legalEntity!=null && legalEntity.getLegalStatus()!=null && legalEntity.getLegalStatus()==1){
            legalEntity.setLegalStatusMsg("已审核");
        }else{
            legalEntity.setLegalStatus(0);
            legalEntity.setLegalStatusMsg("未审核");
        }
        if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("1")){
            legalEntity.setProgressMsg("判决");
        }else if (legalEntity.getProgress()!=null && legalEntity.getProgress().equals("2")){
            legalEntity.setProgressMsg("收案");
        }else{
            legalEntity.setProgressMsg("未判决");
        }
        if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==0){
            legalEntity.setLegalTypeMsg("未退案");
        }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==1){
            legalEntity.setLegalTypeMsg("正常");
        }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==2){
            legalEntity.setLegalTypeMsg("暂停");
        }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==3){
            legalEntity.setLegalTypeMsg("关档");
        }else if (legalEntity.getLegalType()!=null && legalEntity.getLegalType()==4){
            legalEntity.setLegalTypeMsg("退档");
        }

        if (StringUtils.isNotEmpty(legalEntity.getOwner())){
            SysUserEntity userTemp = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ legalEntity.getOwner(), SysUserEntity.class);
            legalEntity.setOwnerName(userTemp.getUserName());
        }else{
            legalEntity.setOwnerName("");
        }

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
