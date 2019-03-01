package xyz.zaijushou.zhx.sys.service;

import xyz.zaijushou.zhx.sys.entity.FileList;

import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
public interface FileListService {

    public void saveFile(FileList bean);

    public List<FileList> listFile(FileList bean);

}
