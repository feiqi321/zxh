package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SysDictionaryServiceImpl implements SysDictionaryService {

    @Resource
    private SysDictionaryMapper dictionaryMapper;

    @Override
    public void saveDataDictionary(SysDictionaryEntity dictionary){
        if(StringUtils.notEmpty(dictionary)){//非空判断
            //获取当前最大的排序值
            List<SysDictionaryEntity> dictionaryList = this.getDataList(dictionary.getDictionaryId(),null);
            if(StringUtils.notEmpty(dictionaryList)){
                dictionary.setNumber(dictionaryList.size()+1);//排序值+1
            }else{
                dictionary.setNumber(1);//默认为1
            }
            dictionaryMapper.insertDataDictionary(dictionary);
        }
    }

    @Override
    public void updateDataDictionary(SysDictionaryEntity dictionary){
        dictionaryMapper.updateDataDictionary(dictionary);
    }
    @Override
    public List<SysDictionaryEntity> getDataList(Integer dictionaryId, String name){
        return dictionaryMapper.getDataList(dictionaryId,name);
    }

    @Override
    public SysDictionaryEntity getDataById(Integer id){
       return dictionaryMapper.getDataById(id);
    }
    @Override
    public void deleteById(Integer id){
        dictionaryMapper.deleteById(id);
    }

}
