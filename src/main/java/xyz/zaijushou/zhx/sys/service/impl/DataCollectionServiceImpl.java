package xyz.zaijushou.zhx.sys.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import xyz.zaijushou.zhx.sys.dao.DataCollectionMapper;
import xyz.zaijushou.zhx.sys.dao.DataCollectionTelMapper;
import xyz.zaijushou.zhx.sys.entity.CollectionReturnEntity;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Service
public class DataCollectionServiceImpl implements DataCollectionService {
    @Resource
    private DataCollectionMapper dataCollectionMapper;
    @Resource
    private DataCollectionTelMapper dataCollectionTelMapper;

    @Override
    public void save(DataCollectionEntity dataCollectionEntity){

    }
    @Override
    public void update(DataCollectionEntity dataCollectionEntity){
    }
    @Override
    public void delete(DataCollectionEntity dataCollectionEntity){

    }
    @Override
    public List<DataCollectionEntity> pageDataCollectionList(DataCollectionEntity dataCollectionEntity){
       List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollection(dataCollectionEntity);
       for (int i=0;i<list.size();i++){
           DataCollectionEntity temp = list.get(i);
           list.set(i,temp);
       }
       return list;
    }

    @Override
    public CollectionReturnEntity pageMyCase(DataCollectionEntity dataCollectionEntity){
        CollectionReturnEntity collectionReturn = new CollectionReturnEntity();
        List<DataCollectionEntity> list =  dataCollectionMapper.pageDataCollect(dataCollectionEntity);
        int countCase = 0;//列表案量
        BigDecimal sumMoney = new BigDecimal("0.00");//列表金额
        int countCasePay = 0;//列表还款案量
        BigDecimal sumPayMoney = new BigDecimal("0.00");//列表还款数额
        BigDecimal sumRepay = new BigDecimal("0.00");//列表CP值
        BigDecimal sumBank = new BigDecimal("0.00");//列表PTP值
        List<String> caseIds = new ArrayList<String>();//案件ID数组
        if(StringUtils.isEmpty(list)) {
            collectionReturn.setList(new PageInfo<>());
            return  collectionReturn;
        }
        for (int i=0;i<list.size();i++){
           for (DataCollectionEntity collection : list){
               if(!caseIds.contains(collection.getCaseId())){
                   caseIds.add(collection.getCaseId());
                   ++countCase;
                   sumMoney = sumMoney.add(collection.getMoney());
                   if (collection.getCaseStatus() == 1){
                       ++countCasePay;
                       sumPayMoney = sumPayMoney.add(collection.getEnRepayAmt());
                   }
               }
               sumRepay = sumRepay.add(collection.getRepayAmt());
               sumBank = sumBank.add(collection.getBankAmt());
           }
        }
        collectionReturn.setList(PageInfo.of(list));
        collectionReturn.setCountCase(countCase);
        collectionReturn.setCountCasePay(countCasePay);
        collectionReturn.setSumBank(sumBank);
        collectionReturn.setSumMoney(sumMoney);
        collectionReturn.setSumRepay(sumRepay);
        collectionReturn.setSumPayMoney(sumPayMoney);
        return collectionReturn;
    }
}
