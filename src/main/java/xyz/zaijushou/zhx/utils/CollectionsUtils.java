package xyz.zaijushou.zhx.utils;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.common.entity.TreeEntity;
import xyz.zaijushou.zhx.sys.entity.SysOrganizationEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CollectionsUtils {

    public static  <T extends CommonEntity> Map<Integer, T> listToMap(List<T> list) {
        Map<Integer, T> map = new HashMap<>();
        if(CollectionUtils.isEmpty(list)) {
            return map;
        }
        for(T t : list) {
            map.put(t.getId(), t);
        }
        return map;
    }

    public static <T extends TreeEntity> List<T> listToTree(List<T> list) {
        if(CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Integer rootId = null;
        Map<Integer, T> entityMap = listToMap(list);
        for(T entity : list) {
            //判断根节点id
            if(rootId == null || entity.getParent().getId() < rootId) {
                rootId = entity.getParent().getId();
            }
            if(!entityMap.containsKey(entity.getParent().getId())) {
                continue;
            }
            if(entityMap.get(entity.getParent().getId()).getChildren() == null) {
                entityMap.get(entity.getParent().getId()).setChildren(new ArrayList<>());
            }
            entityMap.get(entity.getParent().getId()).getChildren().add(entity);
        }
        list = new ArrayList<>();
        for(Map.Entry<Integer, T> entry : entityMap.entrySet()) {
            if(entry.getValue().getParent().getId().equals(rootId)) {
                list.add(entry.getValue());
            }
        }
        return list;
    }
}
