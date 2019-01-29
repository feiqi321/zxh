package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/25.
 */
@Mapper
public interface DataCaseMapper {

    public int saveCase(DataCaseEntity bean);

    public void updateCase(DataCaseEntity bean);

    public void deleteById(Integer id);

    public List<DataCaseEntity> pageDataCase(DataCaseEntity bean);

    public List<DataCaseEntity> pageCaseInfo(DataCaseEntity bean);

    public void sumCaseMoney(DataCaseEntity bean);

}
