package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;

/**
 * Created by looyer on 2019/1/30.
 */
@Api("文件上传下载管理")
@RestController
public class FileManageController {

    @Value(value = "${rar_path}")
    private String filePath;
    @Value(value="${archive_path}")
    private String archivePath;
    @Value(value="${case_path}")
    private String casePath;


    @ApiOperation(value = "下载压缩包", notes = "下载压缩包")
    @PostMapping("/fileManage/download")
    public Object download(HttpServletRequest request, HttpServletResponse response) throws Exception {


        File imageFile = new File(filePath);
        if (!imageFile.exists()) {
            return null;
        }
        String fileName = "众汇信催收系统导入模板.rar";


        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
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

    @ApiOperation(value = "下载案件导入模板", notes = "下载案件导入模板")
    @PostMapping("/fileManage/downloadCase")
    public Object downloadCase(HttpServletRequest request, HttpServletResponse response) throws Exception {


        File imageFile = new File(casePath);
        if (!imageFile.exists()) {
            System.out.println("文件不存在");
            return null;
        }
        String fileName = "案件导入模板.rar";

        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");

        try {
            FileInputStream fis = new FileInputStream(casePath);
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

    @ApiOperation(value = "下载案人模板", notes = "下载案人模板")
    @PostMapping("/fileManage/downloadArchive")
    public Object downloadArchive(HttpServletRequest request, HttpServletResponse response) throws Exception {


        File imageFile = new File(archivePath);
        if (!imageFile.exists()) {
            System.out.println("文件不存在");
            return null;
        }
        String fileName = "案人数据导入模板.xlsx";

        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");

        try {
            FileInputStream fis = new FileInputStream(archivePath);
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
