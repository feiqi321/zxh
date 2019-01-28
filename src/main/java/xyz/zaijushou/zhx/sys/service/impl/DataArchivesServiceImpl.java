package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.DataArchiveAddressMapper;
import xyz.zaijushou.zhx.sys.dao.DataArchiveMapper;
import xyz.zaijushou.zhx.sys.dao.DataArchiveTelMapper;
import xyz.zaijushou.zhx.sys.entity.DataArchiveAddressEntity;
import xyz.zaijushou.zhx.sys.entity.DataArchiveEntity;
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

    @Override
    public void save(DataArchiveEntity dataArchiveEntity){
        String address = "";
        String mobile = "";
        List<DataArchiveAddressEntity> addressEntityList = dataArchiveEntity.getAddressList();
        List<DataArchiveTelEntity> telEntityList = dataArchiveEntity.getTelList();
        for (int i=0;i<addressEntityList.size();i++){
            DataArchiveAddressEntity dataArchiveAddressEntity = addressEntityList.get(i);
            address = address+","+dataArchiveAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataArchiveTelEntity dataArchiveTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataArchiveTelEntity.getTel();
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

    }
    @Override
    public void update(DataArchiveEntity dataArchiveEntity){
        String address = "";
        String mobile = "";
        List<DataArchiveAddressEntity> addressEntityList = dataArchiveEntity.getAddressList();
        List<DataArchiveTelEntity> telEntityList = dataArchiveEntity.getTelList();
        for (int i=0;i<addressEntityList.size();i++){
            DataArchiveAddressEntity dataArchiveAddressEntity = addressEntityList.get(i);
            address = address+","+dataArchiveAddressEntity.getAddress();
        }
        for (int j=0;j<telEntityList.size();j++){
            DataArchiveTelEntity dataArchiveTelEntity = telEntityList.get(j);
            mobile = mobile + "," + dataArchiveTelEntity.getTel();
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
        dataArchiveMapper.deleteById(dataArchiveEntity.getId());
    }
    @Override
    public List<DataArchiveEntity> pageDataArchiveList(DataArchiveEntity dataArchiveEntity){
       List<DataArchiveEntity> list =  dataArchiveMapper.pageDataArchive(dataArchiveEntity);
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
           list.set(i,temp);
       }


       return list;
    }
}
