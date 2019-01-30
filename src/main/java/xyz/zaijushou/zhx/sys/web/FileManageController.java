package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.FileManageDTO;
import xyz.zaijushou.zhx.sys.service.DataCollectionService;
import xyz.zaijushou.zhx.sys.service.FileDownService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by looyer on 2019/1/30.
 */
@Api("文件上传下载管理")
@RestController
public class FileManageController {
    @Autowired
    private FileDownService fileDownService;

    @Value(value = "${rar_path}")
    private String filePath;


    @ApiOperation(value = "下载压缩包", notes = "下载压缩包")
    @GetMapping("/fileManage/download")
    public Object download(HttpServletRequest request,HttpServletResponse response) {


            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                return null;
            }
            String fileName = "枫软催收系统导入模板.rar";

            //下载的文件携带这个名称
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            //文件下载类型--二进制文件
            response.setContentType("application/octet-stream");

            try {
                FileInputStream fis = new FileInputStream(filePath);
                byte[] content = new byte[fis.available()];
                fis.read(content);
                fis.close();

                ServletOutputStream sos = response.getOutputStream();
                sos.write(content);

                sos.flush();
                sos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        return null;
    }


}
