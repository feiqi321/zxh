package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
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

            if (StringUtils.isEmpty(dictList.get(0).getType())){
                for(SysDictionaryEntity org : dictList) {
                    dictionaryMapper.deleteById(org);
                    stringRedisTemplate.opsForValue().set(RedisKeyPrefix.SYS_DIC + org.getId(), JSONArray.toJSONString(org));
                    stringRedisTemplate.opsForValue().set(RedisKeyPrefix.SYS_DIC  + org.getName(), JSONObject.toJSONString(org));
                }
            }

            for(SysDictionaryEntity org : dictionarys) {
                modify(org);
            }
        }
    }

    private void modify(SysDictionaryEntity org) {
        if(org.getId() == null || org.getId() < 0) {
            dictionaryMapper.saveDataAfter(org);
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.SYS_DIC + org.getId(), JSONArray.toJSONString(org));
        } else {
            org.setDeleteFlag(0);
            dictionaryMapper.updateDataDictionary(org);
            if (org.getStatus()==0) {
                stringRedisTemplate.delete(RedisKeyPrefix.SYS_DIC + org.getId());
            }else{
                stringRedisTemplate.opsForValue().set(RedisKeyPrefix.SYS_DIC + org.getId(), JSONArray.toJSONString(org));
            }
        }
        if(CollectionUtils.isEmpty(org.getChildren())) {
            return;
        }
        for(SysDictionaryEntity child : org.getChildren()) {
            SysDictionaryEntity parent = new SysDictionaryEntity();
            parent.setId(org.getId());
            child.setParent(parent);
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
        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.SYS_DIC + dictionary.getId(), JSONArray.toJSONString(dictionary));
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
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.SYS_DIC + dictionary.getParent().getId(), JSONArray.toJSONString(dictionaryList));
            return dictionaryList;
        }else {
            List<SysDictionaryEntity> dictList = new ArrayList<SysDictionaryEntity>();
            //递归查询列表
            for (SysDictionaryEntity dictionaryEntity : dictionaryList){
                dictList.add(dictionaryEntity);
                getChildDataInfo(dictionaryEntity.getId(),dictList);
            }
            if (dictionary !=null && dictionary.getType() !=null && dictionary.getType()==1){

            }else{
                dictList = CollectionsUtils.listToTree(dictList);
            }

            //将枚举数据存入缓存中
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.SYS_DIC + dictionary.getParent().getId(), JSONArray.toJSONString(dictList));
            return dictList;
        }
    }

    /**
     * 查询数据集合
     * @return
     */
    public Map<String,Object> loadByType(){
        Map<String,Object>  map = new HashMap<>();
        //查询枚举数据
        List<SysDictionaryEntity> dictionaryList = dictionaryMapper.loadByType();
        List<SysDictionaryEntity> clientList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> repayMethodList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> repayRemarkList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> contextList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> legalTypeList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> collectAreaList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> caseTypeList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> accountAgeList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> collectStatusList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> synerTypeList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> reduceStatusList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> bbList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> collectResultList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> relationList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> telTypeList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> methodList = new ArrayList<SysDictionaryEntity>();//谈判方式
        List<SysDictionaryEntity> importantList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> moduleList = new ArrayList<SysDictionaryEntity>();
        List<SysDictionaryEntity> areaList = new ArrayList<SysDictionaryEntity>();


        for (int i=0;i<dictionaryList.size();i++){
            SysDictionaryEntity sysDictionaryEntity = dictionaryList.get(i);
            if (sysDictionaryEntity.getParent().getId()==300){//委托方
                clientList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==393){//还款方式
                repayMethodList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==394){//还款备注
                repayRemarkList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==395){//申请协催内容
                contextList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==396){//诉讼案件类型
                legalTypeList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==398){//催收区域
                collectAreaList.add(sysDictionaryEntity);
            } else if (sysDictionaryEntity.getParent().getId()==399){//案件类型
                caseTypeList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==400){//逾期账龄
                accountAgeList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==401){//催收状态
                collectStatusList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==402){//协催类型
                synerTypeList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==404){//减免状态
                reduceStatusList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==405){//报备状态
                bbList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==406){//催收结果
                collectResultList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==407){//与案人关系
                relationList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==408){//电话类型
                telTypeList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==409){//谈判方式
                methodList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==410){//案件重要等级
                importantList.add(sysDictionaryEntity);
            }else if (sysDictionaryEntity.getParent().getId()==411){//催收模板
                moduleList.add(sysDictionaryEntity);
            }else{
                areaList.add(sysDictionaryEntity);
            }


        }
        map.put("委托方",clientList);
        map.put("还款方式",repayMethodList);
        map.put("还款备注",repayRemarkList);
        map.put("申请协催内容",contextList);
        map.put("诉讼案件类型",legalTypeList);
        map.put("催收区域",collectAreaList);
        map.put("案件类型",caseTypeList);
        map.put("逾期账龄",accountAgeList);
        map.put("催收状态",collectStatusList);
        map.put("协催类型",synerTypeList);
        map.put("减免状态",reduceStatusList);
        map.put("报备状态",bbList);
        map.put("催收结果",collectResultList);
        map.put("与案人关系",relationList);
        map.put("电话类型",telTypeList);
        map.put("谈判方式",methodList);
        map.put("案件重要等级",importantList);
        map.put("催收模板",moduleList);
        map.put("地区",areaList);
        return map;
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
     * 删除数据
     * @param dictionary
     */
    @Override
    public void deleteById(SysDictionaryEntity dictionary){
        dictionaryMapper.deleteById(dictionary);
        stringRedisTemplate.delete(RedisKeyPrefix.SYS_DIC + dictionary.getId());
    }

    /**
     * 根据名称获取数据组
     * @param dictionary
     * @return
     */
    @Override
    public  List<SysDictionaryEntity> listDataByName(SysDictionaryEntity dictionary){
        return dictionaryMapper.listDataByName(dictionary);
    }

    /**
     * 根据名称获取数据组
     * @param dictionary
     * @return
     */
    @Override
    public  List<SysDictionaryEntity> listDataByPid(SysDictionaryEntity dictionary){
        return dictionaryMapper.listDataByPid(dictionary);
    }

    @Override
    public  List<SysDictionaryEntity> listDataByDName(SysDictionaryEntity dictionary){
        return dictionaryMapper.listDataByDName(dictionary);
    }

    @Override
    public List<SysDictionaryEntity> findAreaTableData(Integer id) {
        List<SysDictionaryEntity> areaTableData = dictionaryMapper.findAreaTableData(id);
        for (SysDictionaryEntity areaInfo : areaTableData) {
           if(areaInfo.getStatus()==1) {
               areaInfo.setStatusMsg("启用");
           }else {
               areaInfo.setStatusMsg("停用");
           }
        }
        return areaTableData;
    }

    @Override
    public void addArea(SysDictionaryEntity sysDictionaryEntity) {
        dictionaryMapper.addArea(sysDictionaryEntity);
    }

    @Override
    public void updateArea(SysDictionaryEntity sysDictionaryEntity) {
        dictionaryMapper.updateArea(sysDictionaryEntity);
    }

    @Override
    public Object deleteArea(SysDictionaryEntity sysDictionaryEntity) {
        List<SysDictionaryEntity> dictList = new ArrayList<>();
        List<SysDictionaryEntity> list = dictionaryMapper.listAllArea(sysDictionaryEntity);
        if (StringUtils.isEmpty(list)) {
            return dictList;
        }
        //递归查询列表
        for (SysDictionaryEntity dictionaryEntity : list) {
            dictList.add(dictionaryEntity);
            dictionaryMapper.deleteSelectArea(dictionaryEntity);
            getChildData(dictionaryEntity, dictList);
        }
        return WebResponse.success();
    }
    /**
     * 递归删除
     */
    private void getChildData(SysDictionaryEntity entity, List<SysDictionaryEntity> dictList) {
        List<SysDictionaryEntity> sysDictionary = dictionaryMapper.listAllAreaByParentId(entity);
        if (StringUtils.isEmpty(sysDictionary)) {
            return;
        }
        for (SysDictionaryEntity sysDictionaryEntity : sysDictionary) {
            dictionaryMapper.deleteSelectArea(sysDictionaryEntity);
            dictList.add(sysDictionaryEntity);
            getChildData(sysDictionaryEntity, dictList);
        }
    }
}
