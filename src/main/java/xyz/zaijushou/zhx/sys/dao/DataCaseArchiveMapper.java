package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseArchive;

import java.util.List;

/**
 * Created by looyer on 2019/3/7.
 */
@Mapper
public interface DataCaseArchiveMapper {

    public List<DataCaseArchive> listByCaseId(DataCaseArchive bean);

    public void save(DataCaseArchive bean);

}
