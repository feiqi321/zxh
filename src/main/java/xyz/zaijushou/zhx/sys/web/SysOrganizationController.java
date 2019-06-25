package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.ApiOperation;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelDepartmentConstant;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.DepartmentEntity;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;
import xyz.zaijushou.zhx.utils.ExcelUserUtils;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/organization")
public class SysOrganizationController {

    @Resource
    private SysOrganizationService sysOrganizationService;

    @Resource
    private SysUserService sysUserService;

    @PostMapping("/treeOrganization")
    public Object treeOrganization(@RequestBody SysOrganizationEntity organization) {
        List<SysOrganizationEntity> list = sysOrganizationService.listAllOrganizations(organization);
        list = CollectionsUtils.listToTree(list);
        return WebResponse.success(list);
    }

    @PostMapping("/listOrganization")
    public Object listOrganization(@RequestBody SysOrganizationEntity organization) {
        List<SysOrganizationEntity> list = sysOrganizationService.listAllOrganizations(organization);
        return WebResponse.success(list);
    }

    @PostMapping("/listChildOrganization")
    public Object listChildOrganization(@RequestBody SysOrganizationEntity organization) {
        List<SysOrganizationEntity> list = sysOrganizationService.listChildOrganization(organization);
        return WebResponse.success(list);
    }

    @PostMapping("/save")
    public Object save(@RequestBody SysOrganizationEntity[] organizations) {
        List<SysOrganizationEntity> list = sysOrganizationService.listAllOrganizations(new SysOrganizationEntity());
        Map<Integer, SysOrganizationEntity> originalOrgs = CollectionsUtils.listToMap(list);
        List<Integer> originalOrgIds = new ArrayList<>(originalOrgs.keySet());
        List<SysOrganizationEntity> orgList = new ArrayList<>(Arrays.asList(organizations));
        CollectionsUtils.treeResetSort(orgList, 0);
        List<SysOrganizationEntity> changeOrgList = CollectionsUtils.treeToList(orgList);
        for(SysOrganizationEntity org : changeOrgList) {
            if(originalOrgIds.contains(org.getId())){
                originalOrgs.remove(org.getId());
            }
        }
        List<SysOrganizationEntity> deletes = new ArrayList<>(originalOrgs.values());
        if(!CollectionUtils.isEmpty(deletes)) {
            Set<String> deparIdsSet = new HashSet<>();
            for(SysOrganizationEntity org : deletes) {
                deparIdsSet.add("" + org.getId());
            }
            SysNewUserEntity queryUser = new SysNewUserEntity();
            queryUser.setDepartIdsSet(deparIdsSet);
            List<SysNewUserEntity> userList = sysUserService.listByDepartIdsSet(queryUser);
            if(!CollectionUtils.isEmpty(userList)) {
                return WebResponse.error(WebResponseCode.COMMON_ERROR.getCode(), "待删除的部门尚有员工，不得删除");
            }
        }
        for(SysOrganizationEntity org : deletes) {
            sysOrganizationService.deleteOrg(org);
        }
        for(SysOrganizationEntity org : organizations) {
            modify(org);
        }

        list = CollectionsUtils.listToTree(sysOrganizationService.listAllOrganizations(new SysOrganizationEntity()));
        return WebResponse.success(list);
    }

    private void modify(SysOrganizationEntity org) {
        if(org.getId() == null || org.getId() < 0) {
            sysOrganizationService.saveOrg(org);
        } else {
            if (org.getId() < 0){
                sysOrganizationService.saveOrg(org);
            }else {
                sysOrganizationService.updateOrg(org);
            }
        }
        if(CollectionUtils.isEmpty(org.getChildren())) {
            return;
        }
        for(SysOrganizationEntity child : org.getChildren()) {
            child.setParent(org);
            modify(child);
        }
    }

    @ApiOperation(value = "导入部门", notes = "导入部门")
    @PostMapping("/import")
    public Object departmentImport(MultipartFile file) throws IOException {
        List<DepartmentEntity> deptList = ExcelUserUtils.importExcel(file, ExcelDepartmentConstant.Department.values(), DepartmentEntity.class);;

        if(deptList.size() == 0) {
            return WebResponse.success("更新0条数据");
        }
        for (DepartmentEntity bean : deptList){
            SysOrganizationEntity orgEntityOne = sysOrganizationService.findByName(bean.getUpDept());
            if (StringUtils.isEmpty(orgEntityOne)){//为空查询上级部门
                orgEntityOne.setOrgName(bean.getUpDept());
                sysOrganizationService.saveOrg(orgEntityOne);
            }
            SysOrganizationEntity orgEntityTwo = sysOrganizationService.findByName(bean.getDownDept());
            orgEntityTwo.setParent(orgEntityOne);
            orgEntityTwo.setOrgName(bean.getDownDept());
            if (StringUtils.isEmpty(orgEntityTwo.getId())){//为空查询上级部门
                sysOrganizationService.saveOrg(orgEntityTwo);
            }else{
                sysOrganizationService.updateOrg(orgEntityTwo);
            }
            if ("员工".equals(bean.getFlag()) && StringUtils.notEmpty(bean.getUserName())){
                SysNewUserEntity userBean = new SysNewUserEntity();
                userBean.setUserName(bean.getUserName());
                sysUserService.saveUser(userBean);
            }
        }
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse.setCode("100");
        return WebResponse.success();
    }
}
