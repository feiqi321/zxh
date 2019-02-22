package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ArchiveSortEnum;
import xyz.zaijushou.zhx.sys.dao.DataArchiveAddressMapper;
import xyz.zaijushou.zhx.sys.dao.DataArchiveMapper;
import xyz.zaijushou.zhx.sys.dao.DataArchiveRemarkMapper;
import xyz.zaijushou.zhx.sys.dao.DataArchiveTelMapper;
import xyz.zaijushou.zhx.sys.entity.DataArchiveAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataArchiveEntity;
import xyz.zaijushou.zhx.sys.entity.DataArchiveRemarkEntity;
import xyz.zaijushou.zhx.sys.entity.DataArchiveTelEntity;
import xyz.zaijushou.zhx.sys.service.DataArchiveService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataArchivesServiceImpl implements DataArchiveService {
    @Resource
    private DataArchiveMapper dataArchiveMapper;
    @Resource
    private DataArchiveAddressMapper dataArchiveAddressMapper;
    @Resource
    private DataArchiveTelMapper dataArchiveTelMapper;
    @Resource
    private DataArchiveRemarkMapper dataArchiveRemarkMapper;

    @Override
    public void save(DataArchiveEntity dataArchiveEntity){
        String address = "";
        String mobile = "";
        String remark ="";
        List<DataArchiveAddressEntity> addressEntityList = dataArchiveEntity.getAddressList();
        List<DataArchiveTelEntity> telEntityList = dataArchiveEntity.getTelList();
        List<DataArchiveRemarkEntity> remarkList = dataArchiveEntity.getRemarkList();
        for (int i=0;i<addressEntityList.size();i++){
            DataArchiveAddressEntity dataArchiveAddressEntity = addressEntityList.get(i);
            address = address+","+dataArchiveAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataArchiveTelEntity dataArchiveTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataArchiveTelEntity.getTel();
        }
        for (int m=0;m<remarkList.size();m++){
            DataArchiveRemarkEntity dataArchiveRemarkEntity = remarkList.get(m);
            remark = remark + "," + dataArchiveRemarkEntity.getRemark();
        }
        dataArchiveEntity.setAddress(address);
        dataArchiveEntity.setMobile(mobile);
        int key = dataArchiveMapper.saveArchive(dataArchiveEntity);
        for (int i=0;i<addressEntityList.size();i++){
            DataArchiveAddressEntity dataArchiveAddressEntity = addressEntityList.get(i);
            dataArchiveAddressEntity.setArchiveId(dataArchiveEntity.getId());
            address = address+","+dataArchiveAddressEntity.getAddress();
            dataArchiveAddressMapper.saveAddress(dataArchiveAddressEntity);
        }
        for (int j=0;j<telEntityList.size();j++){
            DataArchiveTelEntity dataArchiveTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataArchiveTelEntity.getTel();
            dataArchiveTelEntity.setArchiveId(dataArchiveEntity.getId());
            dataArchiveTelMapper.saveTel(dataArchiveTelEntity);
        }
        for (int j=0;j<remarkList.size();j++){
            DataArchiveRemarkEntity dataArchiveRemarkEntity = remarkList.get(j);
            remark = remark + "," + dataArchiveRemarkEntity.getRemark();
            dataArchiveRemarkEntity.setArchiveId(dataArchiveEntity.getId());
            dataArchiveRemarkMapper.saveRemark(dataArchiveRemarkEntity);
        }

    }
    @Override
    public void update(DataArchiveEntity dataArchiveEntity){
        String address = "";
        String mobile = "";
        String remark ="";
        List<DataArchiveAddressEntity> addressEntityList = dataArchiveEntity.getAddressList();
        List<DataArchiveTelEntity> telEntityList = dataArchiveEntity.getTelList();
        List<DataArchiveRemarkEntity> remarkList = dataArchiveEntity.getRemarkList();
        for (int i=0;i<addressEntityList.size();i++){
            DataArchiveAddressEntity dataArchiveAddressEntity = addressEntityList.get(i);
            address = address+","+dataArchiveAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataArchiveTelEntity dataArchiveTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataArchiveTelEntity.getTel();
        }
        for (int m=0;m<remarkList.size();m++){
            DataArchiveRemarkEntity dataArchiveRemarkEntity = remarkList.get(m);
            remark = remark + "," + dataArchiveRemarkEntity.getRemark();
        }
        dataArchiveEntity.setAddress(address);
        dataArchiveEntity.setMobile(mobile);
        DataArchiveAddressEntity dataArchiveAddressEntity = new DataArchiveAddressEntity();
        dataArchiveAddressEntity.setArchiveId(dataArchiveEntity.getId());
        dataArchiveAddressMapper.deleteAddress(dataArchiveAddressEntity);
        DataArchiveTelEntity dataArchiveTelEntity = new DataArchiveTelEntity();
        dataArchiveTelEntity.setArchiveId(dataArchiveEntity.getId());
        dataArchiveTelMapper.deleteTel(dataArchiveTelEntity);
        for (int i=0;i<addressEntityList.size();i++){
            DataArchiveAddressEntity temp = addressEntityList.get(i);
            temp.setArchiveId(dataArchiveEntity.getId());
            dataArchiveAddressMapper.saveAddress(temp);
        }
        for (int j=0;j<telEntityList.size();j++){
            DataArchiveTelEntity temp = telEntityList.get(j);
            temp.setArchiveId(dataArchiveEntity.getId());
            dataArchiveTelMapper.saveTel(temp);
        }
        for (int j=0;j<remarkList.size();j++){
            DataArchiveRemarkEntity dataArchiveRemarkEntity = remarkList.get(j);
            dataArchiveRemarkEntity.setArchiveId(dataArchiveEntity.getId());
            dataArchiveRemarkMapper.saveRemark(dataArchiveRemarkEntity);
        }
        dataArchiveMapper.updateArchive(dataArchiveEntity);
    }
    @Override
    public void delete(DataArchiveEntity dataArchiveEntity){
        DataArchiveAddressEntity dataArchiveAddressEntity = new DataArchiveAddressEntity();
        dataArchiveAddressEntity.setArchiveId(dataArchiveEntity.getId());
        dataArchiveAddressMapper.deleteAddress(dataArchiveAddressEntity);
        DataArchiveTelEntity dataArchiveTelEntity = new DataArchiveTelEntity();
        dataArchiveTelEntity.setArchiveId(dataArchiveEntity.getId());
        dataArchiveTelMapper.deleteTel(dataArchiveTelEntity);
        DataArchiveRemarkEntity dataArchiveRemarkEntity = new DataArchiveRemarkEntity();
        dataArchiveRemarkEntity.setArchiveId(dataArchiveEntity.getId());
        dataArchiveRemarkMapper.deleteRemark(dataArchiveRemarkEntity);
        dataArchiveMapper.deleteById(dataArchiveEntity.getId());
    }
    @Override
    public WebResponse pageDataArchiveList(DataArchiveEntity dataArchiveEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        dataArchiveEntity.setOrderBy(ArchiveSortEnum.getEnumByKey(dataArchiveEntity.getOrderBy()).getValue());
        List<DataArchiveEntity> list =  dataArchiveMapper.pageDataArchive(dataArchiveEntity);
        //int count = dataArchiveMapper.countDataArchive(dataArchiveEntity);
        for (int i=0;i<list.size();i++){
            DataArchiveEntity temp = list.get(i);
            DataArchiveAddressEntity dataArchiveAddressEntity = new DataArchiveAddressEntity();
            dataArchiveAddressEntity.setArchiveId(temp.getId());
            List<DataArchiveAddressEntity> addressEntityList = dataArchiveAddressMapper.findAll(dataArchiveAddressEntity);
            temp.setAddressList(addressEntityList);
            DataArchiveTelEntity dataArchiveTelEntity = new DataArchiveTelEntity();
            dataArchiveTelEntity.setArchiveId(temp.getId());
            List<DataArchiveTelEntity> telEntityList = dataArchiveTelMapper.findAll(dataArchiveTelEntity);
            temp.setTelList(telEntityList);
            DataArchiveRemarkEntity dataArchiveRemarkEntity = new DataArchiveRemarkEntity();
            dataArchiveRemarkEntity.setArchiveId(temp.getId());
            List<DataArchiveRemarkEntity> remarkEntityList = dataArchiveRemarkMapper.findAll(dataArchiveRemarkEntity);
            temp.setRemarkList(remarkEntityList);
            list.set(i,temp);
        }

        webResponse.setData(PageInfo.of(list));

        return webResponse;
    }

    public WebResponse detail(DataArchiveEntity dataArchiveEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        DataArchiveEntity temp = new DataArchiveEntity();
        DataArchiveAddressEntity dataArchiveAddressEntity = new DataArchiveAddressEntity();
        dataArchiveAddressEntity.setArchiveId(temp.getId());
        List<DataArchiveAddressEntity> addressEntityList = dataArchiveAddressMapper.findAll(dataArchiveAddressEntity);
        temp.setAddressList(addressEntityList);
        DataArchiveTelEntity dataArchiveTelEntity = new DataArchiveTelEntity();
        dataArchiveTelEntity.setArchiveId(temp.getId());
        List<DataArchiveTelEntity> telEntityList = dataArchiveTelMapper.findAll(dataArchiveTelEntity);
        temp.setTelList(telEntityList);
        webResponse.setData(temp);
        return webResponse;
    }
}
