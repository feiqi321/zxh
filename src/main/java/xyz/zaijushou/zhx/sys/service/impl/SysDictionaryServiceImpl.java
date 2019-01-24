package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.SysDictionaryMapper;
import xyz.zaijushou.zhx.sys.entity.SysDictionaryEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;
import xyz.zaijushou.zhx.sys.service.SysDictionaryService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
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


    /**
     * 保存数据
     * @param dictionary
     */
    @Override
    public void saveDataDictionary(SysDictionaryEntity dictionary){
        if(StringUtils.notEmpty(dictionary)){//非空判断

            dictionary.setNumber(1);//默认为1
            dictionary.setStatus(1);//默认启用
            dictionary.setCreateTime(new Date());
            //获取用户信息
            //dictionary.setCreateUser(getUserInfo());//
            //插入数据
            dictionaryMapper.insertDataDictionary(dictionary);
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
     * @param dictionaryId
     * @param name
     * @return
     */
    @Override
    public List<SysDictionaryEntity> getDataList(Integer dictionaryId, String name){
        return dictionaryMapper.getDataList(dictionaryId,name);
    }

    /**
     * 根据数据Id查询数据
     * @param id
     * @return
     */
    @Override
    public SysDictionaryEntity getDataById(Integer id){
       return dictionaryMapper.getDataById(id);
    }

    /**
     * 物理删除数据
     * @param id
     */
    @Override
    public void deleteById(Integer id){
        dictionaryMapper.deleteById(id);
    }

}
