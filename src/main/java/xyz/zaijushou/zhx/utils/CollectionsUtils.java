package xyz.zaijushou.zhx.utils;

import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import xyz.zaijushou.zhx.common.entity.CommonEntity;
import xyz.zaijushou.zhx.common.entity.TreeEntity;
import xyz.zaijushou.zhx.constant.RedisKeyPrefix;
import xyz.zaijushou.zhx.sys.entity.SysOperationLogEntity;
import xyz.zaijushou.zhx.sys.entity.SysUserEntity;

import java.util.*;

@Component
public class CollectionsUtils {

    public static <T extends CommonEntity> Map<Integer, T> listToMap(List<T> list) {
        Map<Integer, T> map = new HashMap<>();
        if (CollectionUtils.isEmpty(list)) {
            return map;
        }
        for (T t : list) {
            if(t == null) {
                continue;
            }
            map.put(t.getId(), t);
        }
        return map;
    }

    public static <T extends TreeEntity> List<T> listToTree(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>();
        }
        Integer rootId = null;
        Map<Integer, T> entityMap = listToMap(list);
        for (T entity : list) {
            //判断根节点id
            if (rootId == null || entity.getParent().getId() < rootId) {
                rootId = entity.getParent().getId();
            }
            if (!entityMap.containsKey(entity.getParent().getId())) {
                continue;
            }
            if (entityMap.get(entity.getParent().getId()).getChildren() == null) {
                entityMap.get(entity.getParent().getId()).setChildren(new ArrayList<>());
            }
            entityMap.get(entity.getParent().getId()).getChildren().add(entity);
        }
        list = new ArrayList<>();
        for (Map.Entry<Integer, T> entry : entityMap.entrySet()) {
            if (entry.getValue().getParent().getId().equals(rootId)) {
                list.add(entry.getValue());
            }
        }
        treeSort(list);
        return list;
    }

    public static <T extends TreeEntity> void treeSort(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        list.sort(new TreeEntityComparator<>());
        for (T t : list) {
            treeSort(t.getChildren());
        }
        return;
    }

    public static <T extends CommonEntity> void userInfoSet(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            return;
        }
        Map<Integer, SysUserEntity> userInfos = listUserInfoGetByIds(list);
        for(int i = 0; i < list.size(); i ++) {
            if(list.get(i) != null) {
                if(list.get(i).getCreateUser() != null &&
                        list.get(i).getCreateUser().getId() != null &&
                        userInfos.containsKey(list.get(i).getCreateUser().getId())) {
                    BeanUtils.copyProperties(userInfos.get(list.get(i).getCreateUser().getId()), list.get(i).getCreateUser());
                }
                if(list.get(i).getUpdateUser() != null &&
                        list.get(i).getUpdateUser().getId() != null &&
                        userInfos.containsKey(list.get(i).getUpdateUser().getId())) {
                    BeanUtils.copyProperties(userInfos.get(list.get(i).getUpdateUser().getId()), list.get(i).getUpdateUser());
                }
            }
        }
    }

    private static <T extends CommonEntity> Map<Integer, SysUserEntity> listUserInfoGetByIds(List<T> list) {
        if(CollectionUtils.isEmpty(list)) {
            return new HashMap<>();
        }
        Set<String> idSet = new HashSet<>();
        for(T t : list) {
            idSet.add(RedisKeyPrefix.USER_INFO + t.getCreateUser().getId());
            idSet.add(RedisKeyPrefix.USER_INFO + t.getUpdateUser().getId());
        }

        List<SysUserEntity> users = RedisUtils.scanEntityWithKeys(idSet, SysUserEntity.class);
        return listToMap(users);
    }


    private static class TreeEntityComparator<T extends TreeEntity> implements Comparator<T> {

        @Override
        public int compare(T t1, T t2) {
            if (t1.getSort() == null) {
                return -1;
            }
            return t1.getSort().compareTo(t2.getSort());
        }
    }

    // public static <T extends TreeEntity> int treeResetSort(List<T> list, int sort) {
    //     if(CollectionUtils.isEmpty(list)) {
    //         return sort;
    //     }
    //     for(int i = 0; i < list.size(); i ++) {
    //         sort += 10;
    //         StringBuilder sortStr = new StringBuilder();
    //         for(int k = String.valueOf(sort).length(); k < 6; k ++) {
    //             sortStr.append(0);
    //         }
    //         sortStr.append(sort);
    //         list.get(i).setSort(sortStr.toString());
    //     }
    //     for(int i = 0; i < list.size(); i ++) {
    //         sort = treeResetSort(list.get(i).getChildren(), sort);
    //     }
    //     return sort;
    // }

    public static <T extends TreeEntity> List<T> treeToList(List<T> list) {
        List<T> returnList = new ArrayList<>();
        if (CollectionUtils.isEmpty(list)) {
            return returnList;
        }
        for (T t : list) {
            if (!CollectionUtils.isEmpty(t.getChildren())) {
                returnList.addAll(traversalChildren(t));
            }
        }
        return returnList;
    }

    private static <T extends TreeEntity> List<T> traversalChildren(T t) {
        List<T> returnList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(t.getChildren())) {
            for (int i = 0; i < t.getChildren().size(); i++) {
                T child = (T) t.getChildren().get(i);
                returnList.addAll(traversalChildren(child));
            }
//            t.setChildren(null);
        }
        returnList.add(t);
        return returnList;
    }


}
