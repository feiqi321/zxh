package xyz.zaijushou.zhx.sys.dao;

import org.apache.ibatis.annotations.Mapper;
import xyz.zaijushou.zhx.sys.entity.DataCaseCommentEntity;

import java.util.List;

/**
 * Created by looyer on 2019/1/26.
 */
@Mapper
public interface DataCaseCommentMapper {

    public void saveComment(DataCaseCommentEntity entity);

    public void deleteComment(DataCaseCommentEntity entity);

    public List<DataCaseCommentEntity> findAll(DataCaseCommentEntity entity);

    public DataCaseCommentEntity detail(DataCaseCommentEntity entity);

    public void updateComment(DataCaseCommentEntity entity);

    public void delComment(DataCaseCommentEntity entity);

}
