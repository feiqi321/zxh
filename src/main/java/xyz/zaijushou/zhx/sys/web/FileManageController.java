package xyz.zaijushou.zhx.sys.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import xyz.zaijushou.zhx.common.web.WebResponse;
import xyz.zaijushou.zhx.sys.dao.ReduceMapper;
import xyz.zaijushou.zhx.sys.entity.DataCollectionEntity;
import xyz.zaijushou.zhx.sys.entity.Letter;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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
    @Value(value = "${detailFile_path}")
    private String detailFile;
    @Value(value = "${userFile_path}")
    private String userFile;
    @Autowired
    private FileManageService fileManageService;
    @Resource
    private ReduceMapper reduceMapper;
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
        String fileName = "案人数据导入模板.xls";

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

    @ApiOperation(value = "下载用户模板", notes = "下载用户模板")
    @PostMapping("/fileManage/downloadUser")
    public Object downloadUser(HttpServletRequest request, HttpServletResponse response) throws Exception {


        File imageFile = new File(userFile);
        if (!imageFile.exists()) {
            System.out.println("文件不存在");
            return null;
        }
        String fileName = "在职员工导入模板.xlsx";

        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");

        try {
            FileInputStream fis = new FileInputStream(userFile);
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


    @ApiOperation(value = "下载减免附件", notes = "下载减免附件")
    @PostMapping("/reduce/download")
    public Object reduceDownload(HttpServletRequest request, HttpServletResponse response, @RequestBody DataCollectionEntity bean) throws Exception {
        Map<String, byte[]> files = new HashMap<String, byte[]>();
        if (StringUtils.isEmpty(bean.getIds())){
            return WebResponse.error("500","文件名称为空");
        }

        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");
        try {
            for(Integer id:bean.getIds()){
                bean.setId(id);
                DataCollectionEntity info = reduceMapper.findById(bean);
                if (StringUtils.notEmpty(info) && StringUtils.notEmpty(info.getFileUuid()) && StringUtils.notEmpty(info.getFileName())){
                    //下载的文件携带这个名称
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(info.getFileName(), "UTF-8"));
                    FileInputStream fis = new FileInputStream(detailFile+info.getFileUuid());
                    byte[] f = new byte[fis.available()];
                    if (f != null){
                        fis.read(f);
                        fis.close();

                        ServletOutputStream sos = response.getOutputStream();
                        sos.write(f);

                        sos.flush();
                        sos.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @ApiOperation(value = "信函下载", notes = "信函下载")
    @PostMapping("/letter/download")
    public Object letterDownload(HttpServletRequest request, HttpServletResponse response,@RequestBody Letter letter) throws Exception {
        String fileName = "信函.doc";

        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");

        try {
            //String content = "<html><head><title>这是个测试demo</title></head><body><font color='red'>这是个测试demo的内容</font></body></html>";
            String content = fileManageService.findDocString(letter);
            InputStream is = new ByteArrayInputStream(content.getBytes("GBK"));
            ServletOutputStream out = response.getOutputStream();

            int byteRead = 0;
            byte[] buffer = new byte[512];
            while ((byteRead = is.read(buffer)) != -1) {
                out.write(buffer, 0, byteRead);
            }

            is.close();
            out.flush();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
