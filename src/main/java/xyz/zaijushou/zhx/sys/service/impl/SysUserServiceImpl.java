package xyz.zaijushou.zhx.sys.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import sun.security.provider.MD5;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.constant.UserSortEnum;
import xyz.zaijushou.zhx.sys.dao.*;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.DataCaseSynergisticService;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.SysRoleService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.JwtTokenUtil;
import xyz.zaijushou.zhx.utils.PinyinTool;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Resource
    private SysUserMapper sysUserMapper;

    @Resource
    private SysToUserRoleMapper sysToUserRoleMapper;

    @Resource
    private DelegatingPasswordEncoder delegatingPasswordEncoder;

    @Resource
    private SysRoleMapper sysRoleMapper;

    @Resource
    private DataCaseMapper dataCaseMapper;

    @Resource
    private SysPasswordMapper sysPasswordMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private DataCollectionMapper dataCollectionMapper;

    @Resource
    private SysOrganizationMapper sysOrganizationMapper;

    @Resource
    private DataCaseSynergisticService dataCaseSynergisticService;

    @Override
    public SysUserEntity findUserInfoWithoutPasswordById(SysUserEntity user) {
        SysToUserRole sysToUserRole = new SysToUserRole();
        sysToUserRole.setUser(user);
        SysUserEntity resultUser = sysUserMapper.findUserInfoWithoutPasswordById(user);

        resultUser.setSameBatch(false);
        resultUser.setBusiData(false);
        DataCollectionEntity dataCollectionEntity = new DataCollectionEntity();
        dataCollectionEntity.setOdv(user.getId()+"");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dataCollectionEntity.setCaseAllotTimeStart(sdf.format(new Date()));
        int distrinbuteNum = dataCollectionMapper.countMyNewCollect(dataCollectionEntity);

        DataCaseSynergisticEntity synergistic = new DataCaseSynergisticEntity();
        synergistic.setApplyStatus("0");
        synergistic.setFinishStatus("0");
        SysNewUserEntity synergisticUser = new SysNewUserEntity();
        synergisticUser.setId(user.getId());
        synergistic.setSynergisticUser(synergisticUser);
        List<DataCaseSynergisticEntity> synergisticTypeCountNumLists =  dataCaseSynergisticService.countNum(synergistic);
        int total = 0;
        for(DataCaseSynergisticEntity  dict : synergisticTypeCountNumLists) {
            total += dict.getCountNum()==null?0:dict.getCountNum();
        }

        int lockUserCount = this.countLockedUser();

        resultUser.setLockAccountNum(lockUserCount);
        resultUser.setDistributeNum(distrinbuteNum);
        resultUser.setSysnergyNum(total);
        resultUser.setRoleName("");

        List<SysToUserRole> roleList = sysUserMapper.listAllUserRolesByUserId(sysToUserRole);

        for (int i=0;i<roleList.size();i++){
            SysToUserRole tempUser = roleList.get(i);
            SysRoleEntity sysRoleEntity = new SysRoleEntity();
            sysRoleEntity.setId(tempUser.getRole().getId());

            SysToRoleMenu sysToMenuRole = new SysToRoleMenu();
            sysToMenuRole.setRole(sysRoleEntity);
            List<SysToRoleMenu> roleMenuList = sysRoleMapper.listAllRoleMenusByRoleId(sysToMenuRole);
            for (int m=0;m<roleMenuList.size();m++){
                SysToRoleMenu menu = roleMenuList.get(m);
                if (menu.getMenu().getId()==49){//在职员工管理
                    resultUser.setRoleName(resultUser.getRoleName()+",member");
                }else if (menu.getMenu().getId()==21){//协催申请
                    resultUser.setRoleName(resultUser.getRoleName()+",synergy");
                }else if (menu.getMenu().getId()==11){//我的案件
                    resultUser.setRoleName(resultUser.getRoleName()+",mycase");
                }

            }

            SysRoleEntity tempRole = sysRoleMapper.selectByRoleId(sysRoleEntity);
            SysNewUserEntity userEntity=new SysNewUserEntity();
            userEntity.setId(user.getId());
            if (tempRole!=null && tempRole.getDataAuth()!=null && tempRole.getDataAuth()!=1){
                List<SysNewUserEntity> list = sysUserMapper.listParent(userEntity);
                while(list.size()>0){
                    for (int m=0;m<list.size();m++){
                        SysNewUserEntity sysNewUserEntityTemp = list.get(m);
                        int count = sysRoleMapper.countDataAuthRole(sysNewUserEntityTemp);
                        if (count>0){
                            resultUser.setSameBatch(true);
                            list = new ArrayList();
                            break;
                        }else{
                            this.foreachData(resultUser,sysNewUserEntityTemp);
                        }
                    }

                }
            }else if (tempRole!=null && tempRole.getDataAuth()!=null && tempRole.getDataAuth()==1){
                resultUser.setSameBatch(true);
            }

            if (tempRole!=null && tempRole.getBusiAuth()!=null && tempRole.getBusiAuth()!=1){
                List<SysNewUserEntity> list = sysUserMapper.listParent(userEntity);
                while(list.size()>0){
                    for (int m=0;m<list.size();m++){
                        SysNewUserEntity sysNewUserEntityTemp = list.get(m);
                        int count = sysRoleMapper.countBusiAuthRole(sysNewUserEntityTemp);
                        if (count>0){
                            resultUser.setBusiData(true);
                            list = new ArrayList();
                            break;
                        }else{
                            this.foreachBusi(resultUser,sysNewUserEntityTemp);
                        }
                    }

                }
            }else if (tempRole!=null && tempRole.getBusiAuth()!=null && tempRole.getBusiAuth()==1){
                resultUser.setBusiData(true);
            }
        }
        SysNewUserEntity temp = sysUserMapper.getDataById(resultUser.getId());
        if(temp.getDepartment()!=null && (temp.getDepartment().indexOf("数据")>0)){
            resultUser.setBusiData(false);
        }

        return resultUser;
    }

    public void foreachData(SysUserEntity resultUser,SysNewUserEntity sysNewUserEntity){
        List<SysNewUserEntity> list = sysUserMapper.listParent(sysNewUserEntity);
        if(list.size()>0){
            for (int m=0;m<list.size();m++){
                SysNewUserEntity sysNewUserEntityTemp = list.get(m);
                int count = sysRoleMapper.countDataAuthRole(sysNewUserEntityTemp);
                if (count>0){
                    resultUser.setSameBatch(true);
                    list = new ArrayList();
                    break;
                }else{
                    this.foreachData(resultUser,sysNewUserEntityTemp);
                }
            }

        }
    }

    public void foreachBusi(SysUserEntity resultUser,SysNewUserEntity sysNewUserEntity){
        List<SysNewUserEntity> list = sysUserMapper.listParent(sysNewUserEntity);
        while(list.size()>0){
            for (int m=0;m<list.size();m++){
                SysNewUserEntity sysNewUserEntityTemp = list.get(m);
                int count = sysRoleMapper.countBusiAuthRole(sysNewUserEntityTemp);
                if (count>0){
                    resultUser.setBusiData(true);
                    list = new ArrayList();
                    break;
                }else{
                    this.foreachBusi(resultUser,sysNewUserEntityTemp);
                }
            }

        }
    }

    @Override
    public SysUserEntity findUserInfoWithoutStatusById(SysUserEntity user) {
        return sysUserMapper.findUserInfoWithoutStatusById(user);
    }

    @Override
    public List<SysToUserRole> listAllUserRoles(SysToUserRole sysToUserRole) {
        return sysUserMapper.listAllUserRoles(sysToUserRole);
    }

    @Override
    public List<SysUserEntity> listAllUsers(SysUserEntity userEntity) {
        List<SysUserEntity> list =  sysUserMapper.listAllUsers(userEntity);
        List<SysOrganizationEntity> orgList = sysOrganizationMapper.listAllOrganizations(new SysOrganizationEntity());
        Map orgMap = new HashMap();
        for (int i=0;i<orgList.size();i++){
            SysOrganizationEntity org = orgList.get(i);
            orgMap.put(org.getId()+"",org.getOrgName());
;        }
        for (int i=0;i<list.size();i++){
            SysUserEntity sysUserEntity = list.get(i);
            sysUserEntity.setDeptName(orgMap.get(sysUserEntity.getDepartment())==null?"":orgMap.get(sysUserEntity.getDepartment()).toString());
            list.set(i,sysUserEntity);
        }
        return list;
    }

    @Override
    public SysUserEntity findPasswordInfoByUsername(SysUserEntity user) {
        return sysUserMapper.findPasswordInfoByLoginName(user);
    }

    /**
     * 保存用户
     * @param userEntity
     */
    @Transactional
    public WebResponse saveUserBatch(SysNewUserEntity userEntity) throws Exception{
        WebResponse webResponse = WebResponse.buildResponse();
        SysUserEntity temp = new SysUserEntity();
        temp.setUserName(userEntity.getUserName());
        SysUserEntity user = this.getLoginName(temp);
        userEntity.setLoginNameCount(user.getLoginNameCount());
        userEntity.setUserName(user.getUserName());
        if (StringUtils.isEmpty(userEntity.getLoginName())) {
            userEntity.setLoginName(user.getLoginName());
            userEntity.setNumber(user.getLoginName());
        }else{
            userEntity.setNumber(userEntity.getLoginName());
        }

        SysPasswordEntity passwordEntity = sysPasswordMapper.selectPassword();

        String password = passwordEntity.getPassword();

        userEntity.setPassword(delegatingPasswordEncoder.encode(DigestUtils.md5Hex(password.trim())));//保存加密密码
        userEntity.setCreateTime(new Date());
        userEntity.setDeleteFlag(0);//默认正常
        sysUserMapper.saveNewUser(userEntity);
        //保存角色中间表
        SysUserRoleEntity roleEntity = new SysUserRoleEntity();
        roleEntity.setUserId(userEntity.getId());
        if (StringUtils.notEmpty(userEntity.getRoleList())) {
            for (SysRoleEntity role : userEntity.getRoleList()){
                roleEntity.setRoleId(role.getId());
                sysToUserRoleMapper.saveUserRole(roleEntity);
            }
        }
        //存入redis
        SysNewUserEntity newBean = sysUserMapper.getDataById(userEntity.getId());
        if (StringUtils.notEmpty(newBean)){
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_INFO + userEntity.getId(), JSONObject.toJSONString(newBean));
        }
        return webResponse.success();
    }

    /**
     * 保存用户
     * @param userEntity
     */
    @Override
    @Transactional
    public WebResponse saveUser(SysNewUserEntity userEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        if (userEntity.getLoginNameCount()==0){
            userEntity.setLoginNameCount(1);
        }
        if(StringUtils.isEmpty(userEntity.getLoginName())){
            return webResponse.error("500","用户账号为空");
        }
        SysNewUserEntity bean = new SysNewUserEntity();
        bean.setUserName(userEntity.getUserName());
        //判断用户username是否重复
        int countUserName = sysUserMapper.countUserNameAndNumber(bean);
        if(countUserName > 0){
            return webResponse.error("500","新增的用户名重复");
        }else {
            bean.setNumber(userEntity.getNumber());
            int countNumber = sysUserMapper.countUserNameAndNumber(bean);
            if (countNumber > 0){
                return webResponse.error("500","新增的用户编号重复");
            }
        }
        //userEntity.setLoginName(userEntity.getNumber());//编号作为登录名
        SysPasswordEntity passwordEntity = sysPasswordMapper.selectPassword();

        String password = "";
        if (passwordEntity == null){
            password = "zhx12345";
        }else {
            password = passwordEntity.getPassword();
        }
        userEntity.setPassword(delegatingPasswordEncoder.encode(DigestUtils.md5Hex(password.trim())));//保存加密密码
        userEntity.setCreateTime(new Date());
        userEntity.setDeleteFlag(0);//默认正常
//        if (userEntity.getStatus() == 1){//在职
//            userEntity.setEnable(1);//启动
//        }else {//离职
//            userEntity.setEnable(0);//锁定
//            userEntity.setLeaveTime(new Date());//保存离职日期
//        }
        sysUserMapper.saveNewUser(userEntity);
        //保存角色中间表
        SysUserRoleEntity roleEntity = new SysUserRoleEntity();
        roleEntity.setUserId(userEntity.getId());
        if (StringUtils.notEmpty(userEntity.getRoleList())) {
            for (SysRoleEntity role : userEntity.getRoleList()){
                roleEntity.setRoleId(role.getId());
                sysToUserRoleMapper.saveUserRole(roleEntity);
            }
        }
        //存入redis
        SysNewUserEntity newBean = sysUserMapper.getDataById(userEntity.getId());
        newBean.setDepartment(newBean.getDepartId());
        if (StringUtils.notEmpty(newBean)){
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_INFO + userEntity.getId(), JSONObject.toJSONString(newBean));
        }
        return webResponse.success();
    }

    /**
     * 修改用户
     * @param userEntity
     */
    @Override
    public WebResponse updateUser(SysNewUserEntity userEntity){
        WebResponse webResponse = WebResponse.buildResponse();
        if (userEntity.getStatus() == 0){
            userEntity.setEnable(0);
        }

        SysNewUserEntity bean = new SysNewUserEntity();
        bean.setId(userEntity.getId());
        bean.setUserName(userEntity.getUserName());


        sysUserMapper.updateUser(userEntity);

        //保存角色中间表
        SysUserRoleEntity roleEntity = new SysUserRoleEntity();
        roleEntity.setUserId(userEntity.getId());
        sysToUserRoleMapper.deleteUserRole(roleEntity);
        if (StringUtils.notEmpty(userEntity.getRoleList())) {
            for (SysRoleEntity role : userEntity.getRoleList()){
                roleEntity.setRoleId(role.getId());
                sysToUserRoleMapper.saveUserRole(roleEntity);
            }
        }
        //存入redis
        SysNewUserEntity newBean = sysUserMapper.getDataById(userEntity.getId());
        if (StringUtils.notEmpty(newBean)){
            stringRedisTemplate.opsForValue().set(RedisKeyPrefix.USER_INFO + userEntity.getId(), JSONObject.toJSONString(newBean));
        }
        sysRoleService.refreshRoleRedis();
        return  webResponse.success();
    }

    @Override
    public void updateDept(SysNewUserEntity userEntity){

        sysUserMapper.updateDept(userEntity);
    }
    @Override
    public void batchDelete(SysNewUserEntity userEntity){

        sysUserMapper.batchDelete(userEntity);
        int[] ids =userEntity.getIds();
        for (int i=0;i<ids.length;i++){
            stringRedisTemplate.delete(RedisKeyPrefix.USER_INFO + ids[i]);
        }
    }

    @Override
    public void updateDataStatus(SysNewUserEntity userEntity){
        if (userEntity.getStatus() == 0){
            userEntity.setEnable(0);
            userEntity.setLeaveTime(new Date());//保存离职日期
        }
        if(userEntity.getEnable() == 1){//解锁
            userEntity.setLoginFailTimes(0);
        }
        sysUserMapper.updateDataStatus(userEntity);
    }

    @Override
    public void updateDataBatchStatus(SysNewUserEntity userEntity){
        if (userEntity.getStatus() == 0){
            userEntity.setEnable(0);
            userEntity.setLeaveTime(new Date());//保存离职日期
        }
        if(userEntity.getEnable() == 1){//解锁
            userEntity.setLoginFailTimes(0);
        }
        sysUserMapper.updateDataBatchStatus(userEntity);
    }


    @Override
    public int deleteById(SysNewUserEntity userEntity){
        int result = 1;
        //判断用户是否存在有效的案件
        DataCaseEntity bean = new DataCaseEntity();
        bean.setOdv(userEntity.getUserName());
        int count = dataCaseMapper.countUserCase(bean);
        if ( count > 0){
            return count;
        }
        sysUserMapper.deleteById(userEntity.getId());
        stringRedisTemplate.delete(RedisKeyPrefix.USER_INFO + userEntity.getId());
        return result;
    }


    /**
     * 查询用户列表
     * @param userEntity
     * @return
     */
    @Override
    public PageInfo<SysNewUserEntity> userDataList(SysNewUserEntity userEntity){
        if (userEntity.getPageSize() == null){
            userEntity.setPageSize(10);
        }
        if (userEntity.getPageNum() == null){
            userEntity.setPageNum(1);
        }
        if(StringUtils.isEmpty(userEntity.getOrderBy())){
            userEntity.setOrderBy("username");
        }else {
            userEntity.setOrderBy(UserSortEnum.getEnumByKey(userEntity.getOrderBy()).getValue());
        }
        if (StringUtils.isEmpty(userEntity.getSort())){
            userEntity.setSort(" desc");
        }

        if (userEntity.getIdStrs()!=null && userEntity.getIdStrs().length>0) {
            int[] ids = new int[userEntity.getIdStrs().length];
            for (int i = 0; i < userEntity.getIdStrs().length; i++) {
                try{
                    ids[i] = Integer.parseInt(userEntity.getIdStrs()[i]);
                }catch (Exception e){
                    ids[i] = 0;
                }
            }
            userEntity.setIds(ids);
        }

        List<SysNewUserEntity> list = sysUserMapper.userDataList(userEntity);

        for (int i=0;i<list.size();i++){
            SysNewUserEntity sysNewUserEntity = list.get(i);
            if (sysNewUserEntity.getLoginFailTimes()>=3 ||  sysNewUserEntity.getEnable()==0){
                sysNewUserEntity.setEnable(0);
                sysNewUserEntity.setEnableMsg("已锁定");
                sysNewUserEntity.setColor("RED");
            }else{
                sysNewUserEntity.setEnableMsg("正常");
                sysNewUserEntity.setColor("BLACK");
            }
        }
        int count = sysUserMapper.countUserData(userEntity);
        if(CollectionUtils.isEmpty(list)) {
            return new PageInfo<>();
        }
        PageInfo<SysNewUserEntity> userInfo = PageInfo.of(list);
        userInfo.setTotal(count);
        return userInfo;
    }

    /**
     * 查询用户信息
     * @param userEntity
     * @return
     */
    @Override
    public SysNewUserEntity getDataById(SysNewUserEntity userEntity){
        SysNewUserEntity userInfoEntity = new SysNewUserEntity();
        userInfoEntity = sysUserMapper.getDataById(userEntity.getId());
        return userInfoEntity;
    }

    @Override
    public List<SysNewUserEntity> getDataByRoleName(SysNewUserEntity userEntity){
        List<SysNewUserEntity> userInfoEntity = sysUserMapper.getDataByRoleName(userEntity);
        return userInfoEntity;
    }
    public List<SysNewUserEntity> getDataByRoleNameForList(SysNewUserEntity userEntity){
        List<SysNewUserEntity> userInfoEntity = sysUserMapper.getDataListByRoleName(userEntity);
        return userInfoEntity;
    }
    /**
     * 根据用户属性查询用户列表
     * 属性：姓名
     * @param user
     * @return
     */
    @Override
    public List<SysUserEntity> listUsers(SysUserEntity user) {
        return sysUserMapper.listUsers(user);
    }

    @Override
    public void passwordReset(SysNewUserEntity user) throws  BadCredentialsException{
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        user.setId(userId);
        SysNewUserEntity queryUser = selectPasswordInfoById(user);
        if(delegatingPasswordEncoder.matches(DigestUtils.md5Hex(user.getOldPassword()), queryUser.getPassword())) {
            user.setId(userId);
            user.setPassword(delegatingPasswordEncoder.encode(DigestUtils.md5Hex(user.getPassword())));
            sysUserMapper.passwordReset(user);
        } else {
            throw new BadCredentialsException("密码错误");
        }
    }

    public void setUserPassword(SysNewUserEntity user){

        Integer userId = user.getId();
        if (userId == null){
            return ;
        }
        user.setId(userId);
        SysPasswordEntity passwordEntity = sysPasswordMapper.selectPassword();
        if (passwordEntity == null){
            return ;
        }
        user.setPassword(delegatingPasswordEncoder.encode(DigestUtils.md5Hex(passwordEntity.getPassword().trim())));
        sysUserMapper.passwordReset(user);
    }


    private SysNewUserEntity selectPasswordInfoById(SysNewUserEntity user) {
        return sysUserMapper.selectPasswordInfoById(user);
    }

    @Override
    public void passwordResetByAdmin(SysNewUserEntity user) {
        user.setPassword(delegatingPasswordEncoder.encode(user.getPassword()));
        sysUserMapper.passwordReset(user);
    }

    @Override
    public void updateLoginFailTimes(SysUserEntity user) {
        sysUserMapper.updateLoginFailTimes(user);
    }

    public SysUserEntity getLoginName(SysUserEntity user) throws Exception{
        PinyinTool tool = new PinyinTool();
        user.setLoginName( tool.toPinYin(user.getUserName(),"", PinyinTool.Type.LOWERCASE));
        int count = sysUserMapper.countByLoginName(user);
        if (count ==0){
            user.setLoginNameCount(1);
            return user;
        }else{
            user.setLoginNameCount(count+1);
            user.setLoginName( tool.toPinYin(user.getUserName(),"", PinyinTool.Type.LOWERCASE)+(count+1));
            user.setUserName(user.getUserName()+(count+1));
            return user;
        }
    }

    @Override
    public List<SysNewUserEntity> listByNameSet(SysNewUserEntity queryUser) {
        if(queryUser == null || CollectionUtils.isEmpty(queryUser.getNamesSet())) {
            return new ArrayList<>();
        }
        return sysUserMapper.listByNameSet(queryUser);
    }

    @Override
    public List<SysNewUserEntity> listByDepartIdsSet(SysNewUserEntity queryUser) {
        return sysUserMapper.listByDepartIdsSet(queryUser);
    }

    @Override
    public void insertUserList(List<SysNewUserEntity> list) {
        try{
            for (SysNewUserEntity userInfo : list){
                this.saveUserBatch(userInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertSimple(List<SysNewUserEntity> list) {
        try{
            for (SysNewUserEntity userInfo : list){
                SysUserEntity tempUser = new SysUserEntity();
                tempUser.setUserName(userInfo.getUserName());
                List<SysUserEntity> userList = sysUserMapper.listUsersByName(tempUser);
                if (userList.size()>0){
                    sysUserMapper.saveNewUser(userInfo);
                }else{
                    sysUserMapper.updateDeptByName(userInfo);
                }
                this.saveUserBatch(userInfo);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void insertDeptSimple(List<DepartmentEntity> list){
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        for (int i=0;i<list.size();i++){
            DepartmentEntity departmentEntity = list.get(i);
            SysOrganizationEntity sysOrganizationEntity = sysOrganizationMapper.findByName(departmentEntity.getDownDept());
            if (sysOrganizationEntity==null){
                SysOrganizationEntity organizationEntity = new SysOrganizationEntity();
                organizationEntity.setOrgName(departmentEntity.getDownDept());
                SysOrganizationEntity upDept = sysOrganizationMapper.findByName(departmentEntity.getUpDept());
                if (upDept!=null) {
                    SysUserEntity createUser  = new SysUserEntity();
                    createUser.setId(userId);
                    organizationEntity.setCreateUser(createUser);
                    organizationEntity.setParent(upDept);
                    SysOrganizationEntity sortOrg = sysOrganizationMapper.selectMaxSort();
                    String sort =Integer.parseInt(sortOrg.getSort()==null?"0":(sortOrg.getSort().equals("")?"0":sortOrg.getSort()))+1+"";
                    String all = "000000";
                    organizationEntity.setSort(all.substring(0,all.length()-sort.length())+sort);
                    sysOrganizationMapper.saveOrg(organizationEntity);
                }
            }else{
                SysOrganizationEntity upDept = sysOrganizationMapper.findByName(departmentEntity.getUpDept());
                if (upDept!=null) {
                    sysOrganizationEntity.setParent(upDept);
                    sysOrganizationMapper.updateOrg(sysOrganizationEntity);
                }

            }
        }

    }

    public List<SysNewUserEntity> userExportList(SysNewUserEntity userEntity){

        if(StringUtils.isEmpty(userEntity.getOrderBy())){
            userEntity.setOrderBy("userName");
        }else {
            userEntity.setOrderBy(UserSortEnum.getEnumByKey(userEntity.getOrderBy()).getValue());
        }
            if (StringUtils.isEmpty(userEntity.getSort())){
            userEntity.setSort(" desc");
        }

        List<SysNewUserEntity> list = sysUserMapper.userExportList(userEntity);
        for (int i=0;i<list.size();i++){
            SysNewUserEntity user = list.get(i);
            List<SysRoleEntity> roleList = user.getRoleList();
            String roleName = "";
            for(int j=0;j<roleList.size();j++){
                SysRoleEntity role = roleList.get(j);

                if (StringUtils.notEmpty(role.getRoleName())) {
                    roleName = roleName+","+role.getRoleName();
                }
            }
            if (StringUtils.notEmpty(roleName)) {
                user.setRole(roleName.substring(1));
            }
            list.set(i,user);
        }
        return list;
    }

    /**
     * 查询锁定的用户数
     * @return
     */
    public int countLockedUser(){
        return sysUserMapper.countLockedUser();
    }


    public UserTree userTree(){
        UserTree userTree = new UserTree();
        SysOrganizationEntity sysOrganizationEntity = new SysOrganizationEntity();
        sysOrganizationEntity.setId(0);
        List<SysOrganizationEntity> rootList = sysOrganizationMapper.listAllOrganizationsByParentId(sysOrganizationEntity);
        SysOrganizationEntity root = rootList.get(0);
        userTree.setId(root.getId());
        userTree.setName(root.getOrgName());
        userTree.setType("dept");
        sysOrganizationEntity.setId(root.getId());

        SysNewUserEntity sysNewUserEntity = new SysNewUserEntity();
        sysNewUserEntity.setDepartment(root.getId()+"");

        this.curcleUserTree(userTree,sysOrganizationEntity,sysNewUserEntity);

        return userTree;
    }

    public UserTree userTreeByRoleId(SysNewUserEntity userEntity){
        UserTree userTree = new UserTree();
        SysOrganizationEntity sysOrganizationEntity = new SysOrganizationEntity();
        sysOrganizationEntity.setId(0);
        List<SysOrganizationEntity> rootList = sysOrganizationMapper.listAllOrganizationsByParentId(sysOrganizationEntity);
        SysOrganizationEntity root = rootList.get(0);
        userTree.setId(root.getId());
        userTree.setName(root.getOrgName());
        userTree.setType("dept");
        sysOrganizationEntity.setId(root.getId());

        SysNewUserEntity sysNewUserEntity = new SysNewUserEntity();
        sysNewUserEntity.setDepartment(root.getId()+"");
        sysNewUserEntity.setRoles(userEntity.getRoles());

        this.curcleUserTree(userTree,sysOrganizationEntity,sysNewUserEntity);

        return userTree;
    }

    public Boolean curcleUserTree(UserTree userTree,SysOrganizationEntity sysOrganizationEntity,SysNewUserEntity sysNewUserEntity ){
        List<UserTree> childList = new ArrayList<UserTree>();
        List<SysOrganizationEntity> deptLeafList = sysOrganizationMapper.listAllOrganizationsByParentId(sysOrganizationEntity);
        if (deptLeafList.size()>0){
            for (int i=0;i<deptLeafList.size();i++){
                SysOrganizationEntity temp = deptLeafList.get(i);
                UserTree tempTree = new UserTree();
                tempTree.setId(temp.getId());
                tempTree.setName(temp.getOrgName());
                tempTree.setType("dept");

                SysNewUserEntity tempUser = new SysNewUserEntity();
                tempUser.setDepartment(temp.getId()+"");
                tempUser.setRoles(sysNewUserEntity.getRoles());
                boolean b = curcleUserTree(tempTree,temp,tempUser);
                if (b) {
                    childList.add(tempTree);
                }
            }
        }

        List<SysNewUserEntity> userLeafList = sysUserMapper.listUserByDept(sysNewUserEntity);
        if (userLeafList.size()>0){
            for (int i=0;i<userLeafList.size();i++){
                SysNewUserEntity temp = userLeafList.get(i);
                UserTree tempTree = new UserTree();
                tempTree.setId(temp.getId());
                tempTree.setName(temp.getUserName());
                tempTree.setType("user");
                childList.add(tempTree);
            }
        }
        if (childList.size()>0) {
            userTree.setChildren(childList);
            return true;
        }else{
            return false;
        }
    }


}
