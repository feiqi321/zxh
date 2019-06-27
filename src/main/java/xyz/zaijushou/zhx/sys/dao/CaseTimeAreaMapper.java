package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.CaseTimeAreaEntity;

import java.util.List;

@Mapper
public interface CaseTimeAreaMapper {
    List<CaseTimeAreaEntity> listAll();

    void saveOrg(CaseTimeAreaEntity caseTimeAreaEntity);

    void updateOrg(CaseTimeAreaEntity caseTimeAreaEntity);
}
