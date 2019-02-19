package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.CaseSortEnum;
import xyz.zaijushou.zhx.constant.ColorEnum;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.SynergySortEnum;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseService;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.RedisUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.*;

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
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private SysUserService sysUserService;//用户业务控制层
    @Autowired
    private SysDictionaryService sysDictionaryService;
    @Resource
    private SysDictionaryMapper sysDictionaryMapper;
    @Resource
    private DataCaseContactsMapper dataCaseContactsMapper;
    @Resource
    private DataCaseRemarkMapper dataCaseRemarkMapper;

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
        dataCaseEntity.setOrderBy(CaseSortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
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
    public WebResponse pageCaseList(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        String[] clients = dataCaseEntity.getClients();
        if (clients == null || clients.length==0 || org.apache.commons.lang3.StringUtils.isEmpty(clients[0])){
            dataCaseEntity.setClientFlag(null);
        }else{
            dataCaseEntity.setClientFlag("1");
        }
        List<DataCaseEntity> list = new ArrayList<DataCaseEntity>();
        if (dataCaseEntity.isBatchBonds()){
            list = dataCaseMapper.pageBatchBoundsCaseList(dataCaseEntity);
        }else {
            list = dataCaseMapper.pageCaseList(dataCaseEntity);
        }
        webResponse.setData(PageInfo.of(list));
        return webResponse;
    }


    /**
     * 催收模块-我的案件-列表查询（废弃）
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
    //未退案0/正常1/暂停2/关档3/退档4/全部5
    @Override
    public void updateStatus(DataCaseEntity bean){
        dataCaseMapper.updateStatus(bean);
    }

    @Override
    public void sendOdv(DataCaseEntity bean){
        dataCaseMapper.sendOdv(bean);
    }
    @Override
    public void sendOdvByProperty(DataCaseEntity bean){
        dataCaseMapper.sendOdvByProperty(bean);
    }
    @Override
    public void addComment(DataCaseEntity bean){
        dataCaseMapper.addComment(bean);
    }
    @Override
    public void addColor(DataCaseEntity bean){
        String color = ColorEnum.getEnumByKey(bean.getColor()).getValue();
        bean.setColor(color);
        dataCaseMapper.addColor(bean);

        DataCollectionEntity dataCollectionEntity = new DataCollectionEntity();
        dataCollectionEntity.setColor(color);
        dataCollectionEntity.setCaseId(bean.getId()+"");
        dataCollectionMapper.addColor(dataCollectionEntity);
    }
    @Override
    public void addImportant(DataCaseEntity bean){
        dataCaseMapper.addImportant(bean);
    }
    @Override
    public void addCollectStatus(DataCaseEntity bean){
        dataCaseMapper.addCollectStatus(bean);
        DataCollectionEntity dataCollectionEntity = new DataCollectionEntity();
        dataCollectionEntity.setCaseId(bean.getId()+"");
        dataCollectionEntity.setCollectStatus(bean.getCollectStatus());
        dataCollectionMapper.addCollectStatus(dataCollectionEntity);
    }
    @Override
    public void addCollectArea(DataCaseEntity bean){
        dataCaseMapper.addCollectArea(bean);
    }
    @Override
    public void addMValue(DataCaseEntity bean){
        dataCaseMapper.addMValue(bean);
    }

    //2 申请中  1 最终同意申请  3待协催 4撤销申请
    @Override
    public void addSynergy(DataCaseEntity bean){
        dataCaseMapper.addSynergy(bean);
    }

    public void updateSynergy(DataCaseEntity bean){
        dataCaseMapper.updateSynergy(bean);
    }
    //部门案件 --- 来电查询
    public WebResponse pageSynergyInfo(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        List<DataCaseEntity> list =  dataCaseMapper.pageSynergyInfo(dataCaseEntity);
        int count = dataCaseMapper.countSynergyInfo(dataCaseEntity);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user.getUserName());
            list.set(i,temp);
        }
        webResponse.setData(list);
        int totalPageNum = 0 ;
        if (count%dataCaseEntity.getPageSize()>0){
            totalPageNum = count/dataCaseEntity.getPageSize()+1;
        }else{
            totalPageNum = count/dataCaseEntity.getPageSize();
        }

        webResponse.setTotalPageNum(totalPageNum);
        webResponse.setTotalNum(count);
        return webResponse;
    }

    //部门案件 --- 来电查询
    public WebResponse pageCaseTel(DataCaseEntity dataCaseEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        if (StringUtils.isEmpty(dataCaseEntity.getOrderBy())){
            dataCaseEntity.setOrderBy("id");
            dataCaseEntity.setSort("desc");
        }

        dataCaseEntity.setOrderBy(SynergySortEnum.getEnumByKey(dataCaseEntity.getOrderBy()).getValue());
        List<DataCaseEntity> list =  dataCaseMapper.pageCaseTel(dataCaseEntity);
        for (int i=0;i<list.size();i++){
            DataCaseEntity temp = list.get(i);
            SysUserEntity user = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getOdv(), SysUserEntity.class);
            temp.setOdv(user.getUserName());
            SysUserEntity user2 = RedisUtils.entityGet(RedisKeyPrefix.USER_INFO+ temp.getName(), SysUserEntity.class);
            temp.setName(user2.getUserName());
            SysDictionaryEntity dictionary2 = new SysDictionaryEntity();
            dictionary2.setId(temp.getCollectStatus());
            SysDictionaryEntity sysDictionaryEntity2 = sysDictionaryService.getDataById(dictionary2);
            temp.setCollectStatusMsg(sysDictionaryEntity2==null?"":sysDictionaryEntity2.getName());
            list.set(i,temp);
        }
        webResponse.setData(PageInfo.of(list));

        return webResponse;
    }

    @Override
    public List<DataCaseEntity> listBySeqNoSet(DataCaseEntity queryEntity) {
        return dataCaseMapper.listBySeqNoSet(queryEntity);
    }

    @Override
    public void updateBySeqNo(DataCaseEntity entity) {
        dataCaseMapper.updateBySeqNo(entity);
    }

    @Transactional
    @Override
    public void updateCaseList(List<DataCaseEntity> dataCaseEntities) {
        List<SysDictionaryEntity> dictionaryList = sysDictionaryMapper.getDataList(new SysDictionaryEntity());
        Map<String, SysDictionaryEntity> dictMap = new HashMap<>();
        for(SysDictionaryEntity entity : dictionaryList) {
            dictMap.put(entity.getName(), entity);
        }
        //下面这段代码逻辑，判断导入的省份是否在字典里，如果存在，则更新案件省份的id，并相应继续判断地市和区县
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            DataCaseEntity entity = dataCaseEntities.get(i);
            if(entity.getProvince() != null && !StringUtils.isEmpty(entity.getProvince().getName()) && dictMap.containsKey(entity.getProvince().getName())) {
                dataCaseEntities.get(i).getProvince().setId(dictMap.get(entity.getProvince().getName()).getId());
                if(entity.getCity() != null && !StringUtils.isEmpty(entity.getCity().getName()) && dictMap.containsKey(entity.getCity().getName())) {
                    if(dictMap.get(entity.getProvince().getName()).getId().equals(dictMap.get(entity.getCity().getName()).getParent().getId())) {
                        dataCaseEntities.get(i).getCity().setId(dictMap.get(entity.getCity().getName()).getId());
                        if(entity.getCounty() != null && !StringUtils.isEmpty(entity.getCounty().getName()) && dictMap.containsKey(entity.getCounty().getName())) {
                            if(dictMap.get(entity.getCity().getName()).getId().equals(dictMap.get(entity.getCounty().getName()).getParent().getId())) {
                                dataCaseEntities.get(i).getCounty().setId(dictMap.get(entity.getCounty().getName()).getId());
                            }
                        }
                    }
                }
            }
        }
        Set<Integer> caseIdsSet = new HashSet<>();
        DataCaseEntity updateCaseEntity = new DataCaseEntity();
        List<DataCaseRemarkEntity> remarks = new ArrayList<>();
        List<DataCaseContactsEntity> contacts = new ArrayList<>();
        for(DataCaseEntity entity : dataCaseEntities) {
            dataCaseMapper.updateBySeqNo(entity);
            caseIdsSet.add(entity.getId());
            for(DataCaseRemarkEntity remark : entity.getCaseRemarks()) {
                remark.setCaseId(entity.getId());
                remarks.add(remark);
            }
            int i = 0;
            for(DataCaseContactsEntity contact : entity.getContacts()) {
                contact.setCaseId(entity.getId());
                contacts.add(contact);
                contact.setSort((++ i) * 10);
            }
        }
        updateCaseEntity.setCaseRemarks(remarks);
        updateCaseEntity.setContacts(contacts);
        //批量删除备注
        DataCaseRemarkEntity remarkEntity = new DataCaseRemarkEntity();
        remarkEntity.setCaseIdsSet(caseIdsSet);
        dataCaseRemarkMapper.deleteCaseRemarkBatchByCaseIds(remarkEntity);
        //批量新增备注
        dataCaseRemarkMapper.insertCaseRemarkBatch(updateCaseEntity);
        //批量删除联系人
        DataCaseContactsEntity contactsEntity = new DataCaseContactsEntity();
        contactsEntity.setCaseIdsSet(caseIdsSet);
        dataCaseContactsMapper.deleteCaseContactsBatchByCaseIds(contactsEntity);
        //批量新增联系人
        dataCaseRemarkMapper.insertCaseRemarkBatch(updateCaseEntity);
    }

    @Transactional
    @Override
    public void saveCaseList(List<DataCaseEntity> dataCaseEntities) {
        List<SysDictionaryEntity> dictionaryList = sysDictionaryMapper.getDataList(new SysDictionaryEntity());
        Map<String, SysDictionaryEntity> dictMap = new HashMap<>();
        for(SysDictionaryEntity entity : dictionaryList) {
            dictMap.put(entity.getName(), entity);
        }
        //下面这段代码逻辑，判断导入的省份是否在字典里，如果存在，则更新案件省份的id，并相应继续判断地市和区县
        for(int i = 0; i < dataCaseEntities.size(); i ++) {
            DataCaseEntity entity = dataCaseEntities.get(i);
            if(entity.getProvince() != null && !StringUtils.isEmpty(entity.getProvince().getName()) && dictMap.containsKey(entity.getProvince().getName())) {
                dataCaseEntities.get(i).getProvince().setId(dictMap.get(entity.getProvince().getName()).getId());
                if(entity.getCity() != null && !StringUtils.isEmpty(entity.getCity().getName()) && dictMap.containsKey(entity.getCity().getName())) {
                    if(dictMap.get(entity.getProvince().getName()).getId().equals(dictMap.get(entity.getCity().getName()).getParent().getId())) {
                        dataCaseEntities.get(i).getCity().setId(dictMap.get(entity.getCity().getName()).getId());
                        if(entity.getCounty() != null && !StringUtils.isEmpty(entity.getCounty().getName()) && dictMap.containsKey(entity.getCounty().getName())) {
                            if(dictMap.get(entity.getCity().getName()).getId().equals(dictMap.get(entity.getCounty().getName()).getParent().getId())) {
                                dataCaseEntities.get(i).getCounty().setId(dictMap.get(entity.getCounty().getName()).getId());
                            }
                        }
                    }
                }
            }
        }
        DataCaseEntity updateCaseEntity = new DataCaseEntity();
        List<DataCaseRemarkEntity> remarks = new ArrayList<>();
        List<DataCaseContactsEntity> contacts = new ArrayList<>();
        for(DataCaseEntity entity : dataCaseEntities) {
            dataCaseMapper.saveCase(entity);
            for(DataCaseRemarkEntity remark : entity.getCaseRemarks()) {
                remark.setCaseId(entity.getId());
                remarks.add(remark);
            }
            int i = 0;
            for(DataCaseContactsEntity contact : entity.getContacts()) {
                contact.setCaseId(entity.getId());
                contacts.add(contact);
                contact.setSort((++ i) * 10);
            }
        }
        updateCaseEntity.setCaseRemarks(remarks);
        updateCaseEntity.setContacts(contacts);
        //批量新增备注
        dataCaseRemarkMapper.insertCaseRemarkBatch(updateCaseEntity);
        //批量新增联系人
        dataCaseContactsMapper.insertCaseContactsBatch(updateCaseEntity);
    }

}
