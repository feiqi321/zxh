package xyz.zaijushou.zhx.sys.dao;

import xyz.zaijushou.zhx.sys.entity.FileList;

import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
public interface FileListMapper {

    public void saveFile(FileList fileList);

    public List<FileList> listFile(FileList fileList);

}
