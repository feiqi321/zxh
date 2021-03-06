package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseTelEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCaseTelMapper {

    public int saveTel(DataCaseTelEntity entity);

    public int insertBatchTel(List<DataCaseTelEntity> list);

    public void updateTel(DataCaseTelEntity entity);

    public void deleteTel(DataCaseTelEntity entity);

    public List<DataCaseTelEntity> findAll(DataCaseTelEntity entity);

    public void updateCaseTelStatus(DataCaseTelEntity bean);

    public List<DataCaseTelEntity> findByCaseIds(int[] caseIds);

    public void updateSameTelStatus(DataCaseTelEntity entity);

}
