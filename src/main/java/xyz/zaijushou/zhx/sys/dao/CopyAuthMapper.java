package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.CopyAuth;

import java.util.List;

/**
 * Created by looyer on 2019/8/3.
 */
@Mapper
public interface CopyAuthMapper {

    public List<CopyAuth> list();

    public void update(CopyAuth copyAuth);

}
