package xyz.zaijushou.zhx.sys.web;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;
import xyz.zaijushou.zhx.utils.CollectionsUtils;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/organization")
public class SysOrganizationController {

    public Object treeOrganizaiton(@RequestBody SysOrganizationEntity organization) {
        List<SysOrganizationEntity> list = new ArrayList<>();
        list = CollectionsUtils.listToTree(list);
        return list;
    }

}
