package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONArray;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
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
import java.util.Date;
import java.util.List;

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
     * @param dictionary
     */
    @Override
    public void saveDataDictionary(SysDictionaryEntity dictionary){
        if(StringUtils.notEmpty(dictionary)){//非空判断

            dictionary.setNumber(1);//默认为1
            dictionary.setCreateTime(new Date());
            //获取用户信息
            //dictionary.setCreateUser(getUserInfo());//
            //插入数据
            dictionaryMapper.saveDataDictionary(dictionary);
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

    //获取用户信息
    private SysUserEntity getUserInfo(){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        return sysUserService.findUserInfoWithoutPasswordById(user);
    }

    /**
     * 查询数据列表
     * @param dictionary
     * @return
     */
    @Override
    public List<SysDictionaryEntity> getDataList(SysDictionaryEntity dictionary){

        //查询枚举数据
        List<SysDictionaryEntity> dictionaryList = dictionaryMapper.getDataList(dictionary.getDictionaryId(),dictionary.getName());

        dictionaryList = CollectionsUtils.listToTree(dictionaryList);
        //将枚举数据存入缓存中
        stringRedisTemplate.opsForValue().set(RedisKeyPrefix.DATA_DICTIONARY + dictionary.getDictionaryId(), JSONArray.toJSONString(dictionaryList));

        return dictionaryList;
    }

    /**
     * 根据数据Id查询数据
     * @param dictionary
     * @return
     */
    @Override
    public SysDictionaryEntity getDataById(SysDictionaryEntity dictionary){
       return dictionaryMapper.getDataById(dictionary.getId());
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
