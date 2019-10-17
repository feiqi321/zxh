package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.FollowUpData;

import java.util.List;

/**
 * @author lsl
 * @version [1.0.0, 2019/10/16,21:24]
 */
public interface FollowUpSetMapper {
    List<FollowUpData> list();

    void update(FollowUpData FollowUpData);
}
