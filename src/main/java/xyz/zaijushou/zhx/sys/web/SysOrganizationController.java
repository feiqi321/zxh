package xyz.zaijushou.zhx.sys.web;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.constant.WebResponseCode;
import xyz.zaijushou.zhx.sys.entity.SysNewUserEntity;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.sys.service.SysUserService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;

import javax.annotation.Resource;
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

}
