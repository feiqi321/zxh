package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCase;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Mapper
public interface DataCaseMapper {

    public void saveCase(DataCase bean);

    public void updateCase(DataCase bean);

    public void deleteById(Integer id);

    public List<DataCase> pageDataCase(DataCase bean);

    public DataCase selectCaseById(Integer id);

}
