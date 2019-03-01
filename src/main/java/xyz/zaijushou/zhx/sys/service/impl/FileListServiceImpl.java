package xyz.zaijushou.zhx.sys.service.impl;

import org.springframework.stereotype.Service;
import xyz.zaijushou.zhx.sys.dao.FileListMapper;
import xyz.zaijushou.zhx.sys.entity.FileList;
import xyz.zaijushou.zhx.sys.service.FileListService;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
@Service
public class FileListServiceImpl implements FileListService {

    @Resource
    private FileListMapper fileListMapper;

    public void saveFile(FileList bean){
        fileListMapper.saveFile(bean);
    }

    public List<FileList> listFile(FileList bean){
        return  fileListMapper.listFile(bean);
    }

}
