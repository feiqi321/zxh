package xyz.zaijushou.zhx.sys.web;

import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;
import xyz.zaijushou.zhx.sys.service.SysOrganizationService;
import xyz.zaijushou.zhx.utils.CollectionsUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/organization")
public class SysOrganizationController {

    @Resource
    private SysOrganizationService sysOrganizationService;

    @PostMapping("/treeOrganization")
    public Object treeOrganization(@RequestBody SysOrganizationEntity organization) {
        List<SysOrganizationEntity> list = sysOrganizationService.listAllOrganizations(organization);
        list = CollectionsUtils.listToTree(list);
        return WebResponse.success(list);
    }

    @PostMapping("/save")
    public Object save(@RequestBody SysOrganizationEntity[] organizations) {
        List<SysOrganizationEntity> list = sysOrganizationService.listAllOrganizations(new SysOrganizationEntity());
        Map<Integer, SysOrganizationEntity> originalOrgs = CollectionsUtils.listToMap(list);
        List<Integer> originalOrgIds = new ArrayList<>(originalOrgs.keySet());
        List<SysOrganizationEntity> changeOrgList = CollectionsUtils.treeToList(new ArrayList<>(Arrays.asList(organizations)));
        for(SysOrganizationEntity org : changeOrgList) {
            if(originalOrgIds.contains(org.getId())){
                originalOrgs.remove(org.getId());
            }
        }
        List<SysOrganizationEntity> deletes = new ArrayList<>(originalOrgs.values());
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
