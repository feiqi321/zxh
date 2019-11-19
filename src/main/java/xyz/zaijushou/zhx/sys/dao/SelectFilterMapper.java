package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.SelectFilterEntity;

/**
 * Created by looyer on 2019/3/21.
 */
@Mapper
public interface SelectFilterMapper {

    public void saveFilter(SelectFilterEntity bean);

    public void updateFilter(SelectFilterEntity bean);


    public SelectFilterEntity selectByModule(SelectFilterEntity bean);

    Integer updateTable(SelectFilterEntity bean);

    SelectFilterEntity selectByModule2(SelectFilterEntity bean);

    void saveTable(SelectFilterEntity bean);
}
