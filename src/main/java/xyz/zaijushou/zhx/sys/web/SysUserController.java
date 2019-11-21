package xyz.zaijushou.zhx.sys.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.parameters.P;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.*;
import xyz.zaijushou.zhx.sys.dao.SysUserMapper;
import xyz.zaijushou.zhx.sys.entity.*;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.sys.service.SysRoleService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Api("用户操作")
@RestController
@RequestMapping(value = "/user")
public class SysUserController {
    private static Logger logger = LoggerFactory.getLogger(SysUserController.class);
    @Resource
    private SysUserService sysUserService;
    @Resource
    private SysOrganizationService sysOrganizationService;
    @Resource
    private SysRoleService sysRoleService;
    @Resource
    private SysUserMapper sysUserMapper;

    @ApiIgnore
    @ApiOperation(value = "测试", notes = "测试接口")
    @PostMapping("index")
    public Object index() {
        return WebResponse.success();
    }

//    @PreAuthorize("hasAuthority('auth_userinfo')")
    @PostMapping("userInfo")
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    public Object userInfo(HttpServletRequest request) {
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        if (userId==null){
            return WebResponse.success(user);
        }
        user.setId(userId);
        user = sysUserService.findUserInfoWithoutPasswordById(user);
        return WebResponse.success(user);
    }

    @PostMapping("userHomeInfo")
    @ApiOperation(value = "根据token获取用户信息", notes = "根据token获取用户信息")
    public Object userHomeInfo(HttpServletRequest request) {
        Integer userId = JwtTokenUtil.tokenData().getInteger("userId");
        SysUserEntity user = new SysUserEntity();
        user.setId(userId);
        user = sysUserService.findUserInfoWithoutPasswordById(user);
        return WebResponse.success(user);
    }

    @ApiOperation(value = "新增用户", notes = "新增用户")
    @PostMapping("/insert")
    public Object saveData(@RequestBody SysNewUserEntity userEntity) {
        WebResponse webResponse = sysUserService.saveUser(userEntity);
        return webResponse;
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @PostMapping("/update")
    public Object updateData(@RequestBody SysNewUserEntity userEntity) {
        WebResponse webResponse = sysUserService.updateUser(userEntity);
        return webResponse;
    }

    @ApiOperation(value = "修改坐席号", notes = "修改坐席号")
    @PostMapping("/updateOfficePhone")
    public Object updateOfficePhone(@RequestBody SysNewUserEntity userEntity) {
        return sysUserService.updateOfficePhone(userEntity);
    }

    @ApiOperation(value = "修改用户部门", notes = "修改用户信息")
    @PostMapping("/updateDept")
    public Object updateDept(@RequestBody SysNewUserEntity userEntity) {
         sysUserService.updateDept(userEntity);
        return WebResponse.success();
    }

    @ApiOperation(value = "批量删除用户", notes = "批量删除用户")
    @PostMapping("/batchDelete")
    public Object batchDelete(@RequestBody SysNewUserEntity userEntity) {
        sysUserService.batchDelete(userEntity);
        return WebResponse.success();
    }


    @ApiOperation(value = "修改状态", notes = "修改状态")
    @PostMapping("/update/status")
    public Object updateDataStatus(@RequestBody SysNewUserEntity userEntity) {
        sysUserService.updateDataStatus(userEntity);
        return WebResponse.success();
    }

    @ApiOperation(value = "批量修改状态", notes = "批量修改状态")
    @PostMapping("/update/batchStatus")
    public Object updateDataBatchStatus(@RequestBody SysNewUserEntity userEntity) {
        sysUserService.updateDataBatchStatus(userEntity);
        return WebResponse.success();
    }

    @ApiOperation(value = "员工删除", notes = "员工删除")
    @PostMapping("/delete/id")
    public Object deleteById(@RequestBody SysNewUserEntity userEntity) {
        int result = sysUserService.deleteById(userEntity);
        if(result == 1){
            return WebResponse.success();
        }else {
            return WebResponse.error("500","用户有案件管理关联，不能删除");
        }
    }

    @ApiOperation(value = "查询用户数据列表", notes = "查询用户数据列表")
    @PostMapping("/select/list")
    public Object getDataList(@RequestBody SysNewUserEntity userEntity) {
        logger.info("开始查询");
        if (xyz.zaijushou.zhx.utils.StringUtils.notEmpty(userEntity.getDepartment())) {
            userEntity = sysUserService.findAllDepts(userEntity);
            logger.info("机构查询完毕");
        }else{
            userEntity.setDepartmens(null);
        }

        PageInfo<SysNewUserEntity> userEntityList = sysUserService.pageDataList(userEntity);
        logger.info("结束查询");
        return WebResponse.success(userEntityList);

    }

    @ApiOperation(value = "查询部门用户数据树", notes = "查询部门用户数据树")
    @PostMapping("/select/userTree")
    public Object userTree() {

        UserTree userTree = sysUserService.userTree();

        return WebResponse.success(userTree);
    }

    @ApiOperation(value = "查询角色用户数据树", notes = "查询角色用户数据树")
    @PostMapping("/select/userRoleTree")
    public Object userRoleTree() {
        List<UserTree> list = sysUserService.userRoleTree();
        return WebResponse.success(list);
    }

    @ApiOperation(value = "查询部门用户数据树", notes = "查询部门用户数据树")
    @PostMapping("/select/userDeptTree")
    public Object userDeptTree() {

        UserTree userTree = sysUserService.userDeptTree();

        return WebResponse.success(userTree);
    }

    @ApiOperation(value = "查询部门用户数据树", notes = "查询部门用户数据树")
    @PostMapping("/select/userTreeByRoleId")
    public Object userTreeByRoleId(@RequestBody  SysNewUserEntity userEntity) {

        UserTree userTree = sysUserService.userTreeByRoleId(userEntity);

        return WebResponse.success(userTree);
    }

    @ApiOperation(value = "查询指定用户数据", notes = "查询指定用户数据")
    @PostMapping("/select/id")
    public Object getDataById(@RequestBody SysNewUserEntity userEntity) {
        SysNewUserEntity userInfoEntity = sysUserService.getDataById(userEntity);
        return WebResponse.success(userInfoEntity);
    }

    @ApiOperation(value = "通过角色查询员工列表", notes = "通过角色查询员工列表")
    @PostMapping("/select/role")
    public Object getDataByRoleName(@RequestBody SysNewUserEntity userEntity) {
        List<SysNewUserEntity> userInfoEntity = sysUserService.getDataByRoleName(userEntity);
        return WebResponse.success(userInfoEntity);
    }

    @ApiOperation(value = "通过角色查询员工下拉列表", notes = "通过角色查询员工下拉列表")
    @PostMapping("/select/roleList")
    public Object getDataByRoleNameForList(@RequestBody SysNewUserEntity userEntity) {
        List<SysNewUserEntity> userInfoEntity = sysUserService.getDataByRoleNameForList(userEntity);
        return WebResponse.success(userInfoEntity);
    }

    @ApiOperation(value = "获取职位列表", notes = "获取职位列表")
    @PostMapping("/select/position")
    public Object getPosition() {
        List<SysNewUserEntity> userInfoEntity = new ArrayList<SysNewUserEntity>();
        SysNewUserEntity user1 = new SysNewUserEntity();
        user1.setJob(1);
        user1.setPosition("业务员");
        userInfoEntity.add(user1);
        SysNewUserEntity user2 = new SysNewUserEntity();
        user2.setJob(2);
        user2.setPosition("职能经理");
        userInfoEntity.add(user2);
        return WebResponse.success(userInfoEntity);
    }

    @ApiOperation(value = "密码重置", notes = "重置用户密码")
    @PostMapping("/passwordReset")
    public Object passwordReset(@RequestBody SysNewUserEntity user) {
        if(user == null || user.getOldPassword() == null || user.getPassword() == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "缺少参数");
        }
        try{
            sysUserService.passwordReset(user);
        }catch (BadCredentialsException e) {
            return WebResponse.error("500",e.getMessage());
        }

        return WebResponse.success();
    }

    @ApiOperation(value = "用户密码重置到初始密码", notes = "用户密码重置到初始密码")
    @PostMapping("/set/password")
    public Object setUserPassword(@RequestBody SysNewUserEntity user) {
        sysUserService.setUserPassword(user);
        return WebResponse.success();
    }

    @ApiOperation(value = "查询本人的坐席号", notes = "查询本人的坐席号")
    @PostMapping("/selectMine")
    public Object selectMine(@RequestBody SysNewUserEntity user) {
        return sysUserService.selectMine();
    }

    @ApiOperation(value = "管理员重置用户密码", notes = "管理员重置用户密码")
    @PostMapping("/passwordResetByAdmin")
    public Object passwordResetByAdmin(@RequestBody SysNewUserEntity user) {
        if(user == null || user.getPassword() == null || user.getId() == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "缺少参数");
        }
        sysUserService.passwordResetByAdmin(user);
        return WebResponse.success();
    }

    @ApiOperation(value = "解除用户密码锁定", notes = "管理员解除由于用户密码输错被锁定")
    @PostMapping("/accountPasswordUnlock")
    public Object accountPasswordUnlock(@RequestBody SysUserEntity user) {
        if(user == null || user.getLoginName() == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "请输入用户登录名");
        }
        user = sysUserService.findPasswordInfoByUsername(user);
        if(user == null) {
            return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "用户登录名输入有误");
        }
        user.setLoginFailTimes(0);
        sysUserService.updateLoginFailTimes(user);
        return WebResponse.success();
    }


    @PostMapping("/getLoginName")
    public Object getLoginName(@RequestBody SysUserEntity user)  {
        try {
            user =  sysUserService.getLoginName(user);
        }catch (Exception e){
            e.printStackTrace();
        }
        return WebResponse.success(user);
    }

    @ApiOperation(value = "导入用户", notes = "导入用户")
    @PostMapping("/import")
    public Object userImport(MultipartFile file) throws IOException {
        List<SysNewUserEntity> userList = ExcelUserUtils.importExcel(file, ExcelUserConstant.UserInfo.values(), SysNewUserEntity.class);

        if(userList.size() == 0) {
            return WebResponse.success("更新0条数据");
        }
        int count = 0;
        List<SysOrganizationEntity>  orgList = sysOrganizationService.listAllOrganizations(new SysOrganizationEntity());
        Map orgMap = new HashMap();
        for (int i=0;i<orgList.size();i++){
            SysOrganizationEntity org = orgList.get(i);
            orgMap.put(org.getOrgName(),org);
        }
        Map roleMap = new HashMap();
        List<SysRoleEntity> roleList = sysRoleService.listAllRoles();
        for (int j=0;j<roleList.size();j++){
            SysRoleEntity sysRole = roleList.get(j);
            roleMap.put(sysRole.getRoleName(),sysRole);
        }
        List<SysUserEntity> users = sysUserService.listAllUsers(new SysUserEntity());
        List<String> usernames = new ArrayList<String>();
        List<String> loginnames = new ArrayList<String>();
        for (SysUserEntity user : users) {
            usernames.add(user.getUserName());
            if(StringUtils.isNotEmpty(user.getLoginName())){
                loginnames.add(user.getLoginName());
            }
        }
        for (SysNewUserEntity userInfo : userList){
            ++count;
            if (StringUtils.isEmpty(userInfo.getDepartment())){
                return WebResponse.error("500","第"+count+"条记录没有填入部门");
            }
            if (orgMap.get(userInfo.getDepartment())==null){
                return WebResponse.error("500","第"+count+"条记录部门不存在");
            }
            userInfo.setDepartId(((SysOrganizationEntity)orgMap.get(userInfo.getDepartment())).getId()+"");
            if (StringUtils.isEmpty(userInfo.getRole())){
                return WebResponse.error("500","第"+count+"条记录没有填入角色");
            }
            String[] roles = userInfo.getRole().split(",");
            List<SysRoleEntity> roleRequestList = new ArrayList<SysRoleEntity>();
            for (int m =0;m<roles.length;m++) {
                if (roleMap.get(roles[m]) == null) {
                    return WebResponse.error("500", "第" + count + "条记录角色不存在");
                }else{
                    SysRoleEntity sysRoleEntity = (SysRoleEntity)roleMap.get(roles[m]);
                    roleRequestList.add(sysRoleEntity);
                }
            }
            userInfo.setRoleList(roleRequestList);
            if (StringUtils.isEmpty(userInfo.getUserName())){
                return WebResponse.error("500","第"+count+"条记录没有填入姓名");
            }
            if (usernames.contains(userInfo.getUserName())){
                return WebResponse.error("500","第"+count+"条记录员工姓名["+userInfo.getUserName()+"]存在重复记录");
            }
            if (loginnames.contains(userInfo.getLoginName())){
                return WebResponse.error("500","第"+count+"条记录账号["+userInfo.getLoginName()+"]存在重复记录");
            }
        }
        sysUserService.insertUserList(userList);
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse.setCode("100");
        return WebResponse.success();
    }

    @ApiOperation(value = "导出用户数据列表", notes = "导出用户数据列表")
    @PostMapping("/select/exportList")
    public Object exportList(@RequestBody SysNewUserEntity userEntity,HttpServletResponse response) throws IOException, InvalidFormatException {

        List<SysNewUserEntity> list = sysUserService.userExportList(userEntity);
        ExcelUtils.exportExcel(list,
                ExcelUserConstant.UserInfoExport.values(),
                "用户全量导出" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx",
                response
        );
        return null;
    }


    @ApiOperation(value = "简单导入用户", notes = "简单导入用户")
    @PostMapping("/simpleImport")
    public Object simpleImport(MultipartFile file, @RequestParam("updateStatus") String updateStatus) {
        InputStream inputStream=null;
        try {
             String fileName = file.getOriginalFilename();
             inputStream = file.getInputStream();
             Workbook workbook = null;
            if (StringUtils.isNotEmpty(fileName) && fileName.length() >= 5 && ".xlsx".equals(fileName.substring(fileName.length() - 5))) {
                workbook = new XSSFWorkbook(inputStream);
            } else {
                workbook = new HSSFWorkbook(inputStream);
            }
            List<SysOrganizationEntity> orgList = sysOrganizationService.listAllOrganizations(new SysOrganizationEntity());
            Map orgMap = new HashMap();
            for (int i = 0; i < orgList.size(); i++) {
                SysOrganizationEntity org = orgList.get(i);
                orgMap.put(org.getOrgName(), org);
            }

            List<DepartmentEntity> deptList = ExcelUserUtils.importExcel(file, ExcelUserConstant.Dept.values(), DepartmentEntity.class);
            if (deptList.size() == 0) {
                return WebResponse.error("500", "部门为空");
            }
            for (int i = 0; i < deptList.size(); i++) {
                DepartmentEntity dept = deptList.get(i);
                if (StringUtils.isEmpty(dept.getUpDept())) {
                    dept.setUpDept("武汉众汇信");
                }
                if (StringUtils.isEmpty(dept.getDownDept())) {
                    return WebResponse.error("500", "第" + i + "条记录没有填入部门");
                }
            }
            sysUserService.insertDeptSimple(deptList);

            List<SysNewUserEntity> userList = ExcelSimpleUserUtils.importExcel(file, ExcelUserConstant.UserInfo.values(), SysNewUserEntity.class);

            // excel用户名重复
            HashSet<String> namelist = new HashSet<>();
            for (SysNewUserEntity userInfo3 : userList) {
                namelist.add(userInfo3.getLoginName());
            }
            if (namelist.size() < userList.size()) {
                return WebResponse.error("500", "excel用户名重复，导入失败");
            }
            ArrayList<ImportResultDTO> importResultlist = new ArrayList<>();
            int count = 0;

            // 用户名为空时
            for (SysNewUserEntity userInfo : userList) {
                ImportResultDTO importResultDTO1 = new ImportResultDTO();
                StringBuilder stringBuilder = new StringBuilder();
                // 查父级节点
                List<DepartmentEntity> list = sysUserService.findParentDept(userInfo.getDepartment());
                Collections.reverse(list);
                for (DepartmentEntity departmentEntity : list) {
                    if (!departmentEntity.getDownDept().equals("武汉众汇信")) {
                        stringBuilder.append("/").append(departmentEntity.getDownDept());
                    }
                }
                if (StringUtils.isEmpty(userInfo.getLoginName())) {
                    importResultDTO1.setDepartment(stringBuilder.toString());
                    importResultDTO1.setUsername(userInfo.getUserName());
                    importResultDTO1.setRole(userInfo.getRole());
                    importResultDTO1.setResult("导入失败，用户名为空");
                    importResultDTO1.setColorStatus(0);
                    importResultlist.add(importResultDTO1);
                }
            }
            // 部门为空时
            for (SysNewUserEntity userInfo : userList) {
                ImportResultDTO importResultDTO1 = new ImportResultDTO();
                if (StringUtils.isEmpty(userInfo.getDepartment())) {
                    importResultDTO1.setUsername(userInfo.getUserName());
                    importResultDTO1.setRole(userInfo.getRole());
                    importResultDTO1.setLoginName(userInfo.getLoginName());
                    importResultDTO1.setResult("导入失败，部门为空");
                    importResultDTO1.setColorStatus(0);
                    importResultlist.add(importResultDTO1);
                }
            }
            // 姓名为空时
            for (SysNewUserEntity userInfo1 : userList) {
                ImportResultDTO importResultDTO1 = new ImportResultDTO();
                if (StringUtils.isEmpty(userInfo1.getUserName())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    // 查父级节点
                    List<DepartmentEntity> list = sysUserService.findParentDept(userInfo1.getDepartment());
                    Collections.reverse(list);
                    for (DepartmentEntity departmentEntity : list) {
                        if (!departmentEntity.getDownDept().equals("武汉众汇信")) {
                            stringBuilder.append("/").append(departmentEntity.getDownDept());
                        }
                    }
                    importResultDTO1.setDepartment(stringBuilder.toString());
                    importResultDTO1.setRole(userInfo1.getRole());
                    importResultDTO1.setLoginName(userInfo1.getLoginName());
                    importResultDTO1.setResult("导入失败，姓名为空");
                    importResultDTO1.setColorStatus(0);
                    importResultlist.add(importResultDTO1);
                }
            }
            for (SysNewUserEntity userInfo : userList) {
                ImportResultDTO importResultDTO = new ImportResultDTO();
                if (StringUtils.isEmpty(userInfo.getRole())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    // 查父级节点
                    List<DepartmentEntity> list = sysUserService.findParentDept(userInfo.getDepartment());
                    Collections.reverse(list);
                    for (DepartmentEntity departmentEntity : list) {
                        if (!departmentEntity.getDownDept().equals("武汉众汇信")) {
                            stringBuilder.append("/").append(departmentEntity.getDownDept());
                        }
                    }
                    importResultDTO.setDepartment(stringBuilder.toString());
                    importResultDTO.setUsername(userInfo.getUserName());
                    importResultDTO.setLoginName(userInfo.getLoginName());
                    importResultDTO.setResult("导入失败，角色为空");
                    importResultDTO.setColorStatus(0);
                    importResultlist.add(importResultDTO);
                }
            }
            for (SysNewUserEntity userInfo : userList) {
                if (StringUtils.isNotEmpty(userInfo.getDepartment())) {
                    userInfo.setDepartId(((SysOrganizationEntity) orgMap.get(userInfo.getDepartment())).getId() + "");
                }
                if (StringUtils.isNotEmpty(userInfo.getRole())
                        && StringUtils.isNotEmpty(userInfo.getUserName())
                        && StringUtils.isNotEmpty(userInfo.getDepartment())
                        && StringUtils.isNotEmpty(userInfo.getLoginName())) {
                    String[] roleName = userInfo.getRole().split(",");
                    List<SysRoleEntity> roleList = sysUserMapper.findRole(roleName);
                    ImportResultDTO importResultDTO = new ImportResultDTO();
                    if (roleName.length == roleList.size()) {
                        importResultDTO.setRole(userInfo.getRole());
                        StringBuilder stringBuilder = new StringBuilder();
                        // 查父级节点
                        List<DepartmentEntity> list = sysUserService.findParentDept(userInfo.getDepartment());
                        Collections.reverse(list);
                        for (DepartmentEntity departmentEntity : list) {
                            if (!departmentEntity.getDownDept().equals("武汉众汇信")) {
                                stringBuilder.append("/").append(departmentEntity.getDownDept());
                            }
                        }
                        importResultDTO.setDepartment(stringBuilder.toString());
                        importResultDTO.setUsername(userInfo.getUserName());
                        importResultDTO.setLoginName(userInfo.getLoginName());
                        int count3 = sysUserMapper.findLoginName(userInfo.getLoginName());
                        if (count3 == 1 && updateStatus.equals("1")) {
                            importResultDTO.setResult("成功，数据更新");
                            importResultDTO.setColorStatus(1);
                        }
                        if (count3 >= 1 && updateStatus.equals("2")) {
                            importResultDTO.setResult("更新失败，用户名重复");
                            importResultDTO.setColorStatus(0);
                        }
                        if (count3 == 0 && updateStatus.equals("2") || count3 == 0 && updateStatus.equals("1")) {
                            importResultDTO.setResult("成功，导入成功");
                            importResultDTO.setColorStatus(1);
                        }
                        importResultlist.add(importResultDTO);
                        userInfo.setRoleList(roleList);
                        sysUserService.insertSimple1(userList,updateStatus);
                    } else {
                        return WebResponse.error("500", "第" + count + "条记录角色不存在");
                    }
                }
            }
            WebResponse webResponse = WebResponse.buildResponse();
            webResponse.setCode("100");
            return WebResponse.success(importResultlist);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        return null;
    }
}