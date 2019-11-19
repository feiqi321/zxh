package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.SelectFilterEntity;

/**
 * Created by looyer on 2019/3/21.
 */
public interface SelectFilterService {

    public void save(SelectFilterEntity bean);

    public SelectFilterEntity selectByModule(SelectFilterEntity bean);

    void updateModule(SelectFilterEntity bean);

    SelectFilterEntity selectByModule2(SelectFilterEntity bean);
}
