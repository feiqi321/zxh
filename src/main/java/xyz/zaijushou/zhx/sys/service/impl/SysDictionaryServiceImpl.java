package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class SysDictionaryServiceImpl implements SysDictionaryService {

    @Resource
    private SysDictionaryMapper dictionaryMapper;
    @Resource
    private SysUserService sysUserService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    /**
     * 保存数据
     * @param dictionarys
     */
    @Override
    public void saveDataDictionary(SysDictionaryEntity[] dictionarys){
        if(StringUtils.notEmpty(dictionarys)){//非空判断
            List<SysDictionaryEntity> dictList = new ArrayList<SysDictionaryEntity>();
            //递归查询列表
            for (SysDictionaryEntity dictionaryEntity : dictionarys){
                dictList.add(dictionaryEntity);
                getChildDataInfo(dictionaryEntity.getId(),dictList);
            }

            if (dictList.get(0).getType() == 1){
                for(SysDictionaryEntity org : dictList) {
                    dictionaryMapper.deleteById(org.getId());
                }
            }

            for(SysDictionaryEntity org : dictionarys) {
                modify(org);
            }
        }
    }

    private void modify(SysDictionaryEntity org) {
        if(org.getId() == null) {
            dictionaryMapper.saveDataAfter(org);
        } else {
            org.setDeleteFlag(0);
            dictionaryMapper.updateDataDictionary(org);
        }
        if(CollectionUtils.isEmpty(org.getChildren())) {
            return;
        }
        for(SysDictionaryEntity child : org.getChildren()) {
            child.setParent(org);
            modify(child);
        }
    }

    /**
     * 更新数据
     * @param dictionary
     */
    @Override
    public void updateDataDictionary(SysDictionaryEntity dictionary){
        dictionary.setUpdateTime(new Date());//更新时间
       // dictionary.setUpdateUser(getUserInfo());//获取更新用户
        //保存更新数据
        dictionaryMapper.updateDataDictionary(dictionary);
    }

    /**
     * 查询数据列表
     * @param dictionary
     * @return
     */
    @Override
    public List<SysDictionaryEntity> getDataList(SysDictionaryEntity dictionary){

        //查询枚举数据
        List<SysDictionaryEntity> dictionaryList = dictionaryMapper.getDataList(dictionary);

        if (dictionary.getParent().getId() == 0){//只查询做菜单目录
            //将枚举数据存入缓存中
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_DICTIONARY + dictionary.getParent().getId(), JSONArray.toJSONString(dictionaryList));
            return dictionaryList;
        }else {
            List<SysDictionaryEntity> dictList = new ArrayList<SysDictionaryEntity>();
            //递归查询列表
            for (SysDictionaryEntity dictionaryEntity : dictionaryList){
                dictList.add(dictionaryEntity);
                getChildDataInfo(dictionaryEntity.getId(),dictList);
            }
            dictList = CollectionsUtils.listToTree(dictList);
            //将枚举数据存入缓存中
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_DICTIONARY + dictionary.getParent().getId(), JSONArray.toJSONString(dictList));
            return dictList;
        }
    }

    //递归查询
    private void getChildDataInfo(Integer parentId,List<SysDictionaryEntity> dictList){
        List<SysDictionaryEntity> dictionaryList = dictionaryMapper.getDataByParentId(parentId);
        if (StringUtils.isEmpty(dictionaryList)){
            return ;
        }
        for (SysDictionaryEntity dictionaryEntity : dictionaryList){
            dictList.add(dictionaryEntity);
            getChildDataInfo(dictionaryEntity.getId(),dictList);
        }
    }

    /**
     * 根据数据Id查询数据
     * @param dictionary
     * @return
     */
    @Override
    public SysDictionaryEntity getDataById(SysDictionaryEntity dictionary){
        List<SysDictionaryEntity> dictionaryList = dictionaryMapper.getDataById(dictionary.getId());
        if(StringUtils.notEmpty(dictionaryList)){
            return dictionaryList.get(0);
        }
       return null;
    }

    /**
     * 物理删除数据
     * @param dictionary
     */
    @Override
    public void deleteById(SysDictionaryEntity dictionary){
        dictionaryMapper.deleteById(dictionary.getId());
    }

}
