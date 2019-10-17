package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.FollowUpData;

import java.util.List;

/**
 * @author lsl
 * @version [1.0.0, 2019/10/16,21:17]
 */
public interface FollowUpSetService {
     List<FollowUpData> list();

     void update(FollowUpData FollowUpData);
}
