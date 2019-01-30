package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.DataCaseAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.DataCaseTelEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCaseServiceImpl implements DataCaseService {
    @Resource
    private DataCaseMapper dataCaseMapper;
    @Resource
    private DataCaseAddressMapper dataCaseAddressMapper;
    @Resource
    private DataCaseTelMapper dataCaseTelMapper;
    @Resource
    private DataCaseCommentMapper dataCaseCommentMapper;
    @Resource
    private DataCaseInterestMapper dataCaseInterestMapper;
    @Resource
    private DataCaseRepayMapper dataCaseRepayMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层

    @Override
    public void save(DataCaseEntity dataCaseEntity){
        String address = "";
        String mobile = "";
        List<DataCaseAddressEntity> addressEntityList = dataCaseEntity.getAddressList();
        List<DataCaseTelEntity> telEntityList = dataCaseEntity.getTelList();
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = addressEntityList.get(i);
            address = address+","+dataCaseAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity dataCaseTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataCaseTelEntity.getTel();
        }
        int id = dataCaseMapper.saveCase(dataCaseEntity);
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = addressEntityList.get(i);
            dataCaseAddressEntity.setCaseId(id);
            address = address+","+dataCaseAddressEntity.getAddress();
            dataCaseAddressMapper.saveAddress(dataCaseAddressEntity);
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity dataCaseTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataCaseTelEntity.getTel();
            dataCaseTelEntity.setCaseId(id);
            dataCaseTelMapper.saveTel(dataCaseTelEntity);
        }

    }
    @Override
    public void update(DataCaseEntity dataCaseEntity){
        String address = "";
        String mobile = "";
        List<DataCaseAddressEntity> addressEntityList = dataCaseEntity.getAddressList();
        List<DataCaseTelEntity> telEntityList = dataCaseEntity.getTelList();
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity dataCaseAddressEntity = addressEntityList.get(i);
            address = address+","+dataCaseAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity dataCaseTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataCaseTelEntity.getTel();
        }
        DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
        dataCaseAddressEntity.setCaseId(dataCaseEntity.getId());
        dataCaseAddressMapper.deleteAddress(dataCaseAddressEntity);
        DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
        dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
        dataCaseTelMapper.deleteTel(dataCaseTelEntity);
        for (int i=0;i<addressEntityList.size();i++){
            DataCaseAddressEntity temp = addressEntityList.get(i);
            temp.setCaseId(dataCaseEntity.getId());
            dataCaseAddressMapper.saveAddress(temp);
        }
        for (int j=0;j<telEntityList.size();j++){
            DataCaseTelEntity temp = telEntityList.get(j);
            temp.setCaseId(dataCaseEntity.getId());
            dataCaseTelMapper.saveTel(temp);
        }
        dataCaseMapper.updateCase(dataCaseEntity);
    }
    @Override
    public void delete(DataCaseEntity dataCaseEntity){
        DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
        dataCaseAddressEntity.setCaseId(dataCaseEntity.getId());
        dataCaseAddressMapper.deleteAddress(dataCaseAddressEntity);
        DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
        dataCaseTelEntity.setCaseId(dataCaseEntity.getId());
        dataCaseTelMapper.deleteTel(dataCaseTelEntity);
        dataCaseMapper.deleteById(dataCaseEntity.getId());
    }
    @Override
    public List<DataCaseEntity> pageDataCaseList(DataCaseEntity dataCaseEntity){
       List<DataCaseEntity> list =  dataCaseMapper.pageDataCase(dataCaseEntity);
       for (int i=0;i<list.size();i++){
           DataCaseEntity temp = list.get(i);
           DataCaseAddressEntity dataCaseAddressEntity = new DataCaseAddressEntity();
           dataCaseAddressEntity.setCaseId(temp.getId());
           List<DataCaseAddressEntity> addressEntityList = dataCaseAddressMapper.findAll(dataCaseAddressEntity);
           temp.setAddressList(addressEntityList);
           DataCaseTelEntity dataCaseTelEntity = new DataCaseTelEntity();
           dataCaseTelEntity.setCaseId(temp.getId());
           List<DataCaseTelEntity> telEntityList = dataCaseTelMapper.findAll(dataCaseTelEntity);
           temp.setTelList(telEntityList);
           list.set(i,temp);
       }
       return list;
    }

    @Override
    public List<DataCaseEntity> pageCaseList(DataCaseEntity dataCaseEntity){
        List<DataCaseEntity> list =  dataCaseMapper.pageDataCase(dataCaseEntity);
        return list;
    }


    /**
     * 催收模块-我的案件-列表查询
     * @param dataCaseEntity
     * @return
     */
    @Override
    public List<DataCaseEntity> pageCaseInfoList(DataCaseEntity dataCaseEntity){
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        //获取当前用户名
        SysUserEntity user = getUserInfo();
        if (StringUtils.isEmpty(user)){
            return list;
        }
        dataCaseEntity.setName(user.getUserName());//当前用户
        list =  dataCaseMapper.pageDataCase(dataCaseEntity);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);

            list.set(i,temp);
        }
        return list;
    }

    private SysUserEntity getUserInfo (){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutPasswordById(user);
    }

    /**
     *  催收管理-统计
     * @param bean
     */
    @Override
    public void sumCaseMoney(DataCaseEntity bean){
        dataCaseMapper.sumCaseMoney(bean);
        return ;
    }
}
