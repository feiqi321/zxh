package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.ExcelDepartmentConstant;
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

    @PostMapping("/listChildOrganizationBy")
    public Object listChildOrganizationBy(@RequestBody SysOrganizationEntity organization) {
        List<SysOrganizationEntity> list = sysOrganizationService.listChildOrganizationBy(organization);
        return WebResponse.success(list);
    }

    @PostMapping("/save")
    public Object save(@RequestBody SysOrganizationEntity organizations) {
        sysOrganizationService.updateDepartment(organizations);
        return WebResponse.success();
    }

    @PostMapping("/delete")
    public Object delete(@RequestBody SysOrganizationEntity organizations) {
        return sysOrganizationService.deleteSelectDepartment(organizations);
    }


    private void modify(SysOrganizationEntity org) {
        if (org.getId() == null || org.getId() < 0) {
            sysOrganizationService.saveOrg(org);
        } else {
            if (org.getId() < 0) {
                sysOrganizationService.saveOrg(org);
            } else {
                sysOrganizationService.updateOrg(org);
            }
        }
        if (CollectionUtils.isEmpty(org.getChildren())) {
            return;
        }
        for (SysOrganizationEntity child : org.getChildren()) {
            child.setParent(org);
            modify(child);
        }
    }

    @ApiOperation(value = "导入部门", notes = "导入部门")
    @PostMapping("/import")
    public Object departmentImport(MultipartFile file) throws IOException {
        List<DepartmentEntity> deptList = ExcelUserUtils.importExcel(file, ExcelDepartmentConstant.Department.values(), DepartmentEntity.class);
        ;

        if (deptList.size() == 0) {
            return WebResponse.success("更新0条数据");
        }
        for (DepartmentEntity bean : deptList) {
            SysOrganizationEntity orgEntityOne = sysOrganizationService.findByName(bean.getUpDept());
            if (StringUtils.isEmpty(orgEntityOne)) {//为空查询上级部门
                orgEntityOne.setOrgName(bean.getUpDept());
                sysOrganizationService.saveOrg(orgEntityOne);
            }
            SysOrganizationEntity orgEntityTwo = sysOrganizationService.findByName(bean.getDownDept());
            orgEntityTwo.setParent(orgEntityOne);
            orgEntityTwo.setOrgName(bean.getDownDept());
            if (StringUtils.isEmpty(orgEntityTwo.getId())) {//为空查询上级部门
                sysOrganizationService.saveOrg(orgEntityTwo);
            } else {
                sysOrganizationService.updateOrg(orgEntityTwo);
            }
            if ("员工".equals(bean.getFlag()) && StringUtils.notEmpty(bean.getUserName())) {
                SysNewUserEntity userBean = new SysNewUserEntity();
                userBean.setUserName(bean.getUserName());
                sysUserService.saveUser(userBean);
            }
        }
        WebResponse webResponse = WebResponse.buildResponse();
        webResponse.setCode("100");
        return WebResponse.success();
    }

    @PostMapping("/queryStaff")
    public Object findStaffNumber() {
        return sysOrganizationService.findStaffNumber();
    }


    @PostMapping("/moveUpDown")
    public Object moveUpDown(@RequestParam("id") Integer id, @RequestParam("sort") String sort,
                             @RequestParam("id1") Integer id1, @RequestParam("sort1") String sort1) {
        sysOrganizationService.moveUpDown(id, sort, id1, sort1);
        return WebResponse.success();
    }

    @PostMapping("/findTableData")
    public Object findTableData(@RequestParam("id") Integer id) {
        List<SysOrganizationEntity> list = sysOrganizationService.findTableData(id);
        return WebResponse.success(list);
    }

    @PostMapping("/addDept")
    public Object addDept(@RequestBody SysOrganizationEntity organizations) {
        System.out.println(organizations);
        sysOrganizationService.addDept(organizations);
        return WebResponse.success();
    }
}
