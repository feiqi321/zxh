package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.Notice;

import java.util.List;

/**
 * Created by looyer on 2019/6/27.
 */
@Mapper
public interface PointMapper {

    List<Notice> list(Notice notice);

    List<Notice> pageList(Notice notice);

    void save(Notice notice);

    void view(Notice notice);

    void update(Notice notice);

    void delete(Notice notice);

}
