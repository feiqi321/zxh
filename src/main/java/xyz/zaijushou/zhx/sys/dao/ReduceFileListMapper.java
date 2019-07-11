package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.FileList;
import xyz.zaijushou.zhx.sys.entity.ReduceFileList;

import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
public interface ReduceFileListMapper {

    public void saveFile(ReduceFileList fileList);

    public List<ReduceFileList> listFile(ReduceFileList fileList);

    public void delete(ReduceFileList fileList);

}
