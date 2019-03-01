package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataOpLog;
import xyz.zaijushou.zhx.sys.entity.FileList;
import xyz.zaijushou.zhx.sys.service.FileListService;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by looyer on 2019/3/1.
 */
@Api("案件详情/附件")
@RestController
public class FileListController {

    @Autowired
    private FileListService fileListService;
    @Value(value = "${detailFile_path}")
    private String detailFile;

    @ApiOperation(value = "分頁查询", notes = "分頁查询")
    @PostMapping("/dataLFile/pageDataFile")
    public Object pageDataFile(@RequestBody FileList bean) {

        List<FileList> list = fileListService.listFile(bean);
        return WebResponse.success(list);
    }

    @ApiOperation(value = "保存附件信息", notes = "保存附件信息")
    @PostMapping("/dataFile/save")
    public Object update(MultipartFile file, FileList bean ) throws IOException {
        String fileName = file.getOriginalFilename();
        file.transferTo(new File(detailFile+fileName));
        bean.setFileName(fileName);
        fileListService.saveFile(bean);
        return WebResponse.success();

    }

}
