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
import xyz.zaijushou.zhx.sys.entity.ReduceFileList;
import xyz.zaijushou.zhx.sys.service.FileManageService;
import xyz.zaijushou.zhx.sys.service.ReduceService;
import xyz.zaijushou.zhx.utils.StringUtils;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
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
    @Autowired
    private ReduceService reduceService;
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

        try {
            int[] ids = bean.getIds();
            List fileNameList = new ArrayList();
            List filesList = new ArrayList();
            for(int i=0;i<ids.length;i++){
                ReduceFileList reduceFileList = new ReduceFileList();
                reduceFileList.setReduceId(ids[i]);
                List<ReduceFileList> fileList =  reduceService.listFile(reduceFileList);
                for (int j=0;j<fileList.size();j++){
                    ReduceFileList temp = fileList.get(j);
                    fileNameList.add(temp.getFileName());
                    filesList.add(detailFile+temp.getFileId());
                }
            }
            String[] fileNames = new String[fileNameList.size()];
            String[] files = new String[filesList.size()];

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            String downloadFilename = sdf.format(new Date())+".zip";//文件的名称
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            fileNameList.toArray(fileNames);
            filesList.toArray(files);

            for (int i=0;i<files.length;i++) {

                zos.putNextEntry(new ZipEntry(fileNames[i]));
                FileInputStream fis = new FileInputStream(new File(files[i]));
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
            zos.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

        /*Map<String, byte[]> files = new HashMap<String, byte[]>();
        if (StringUtils.isEmpty(bean.getIds())){
            return WebResponse.error("500","文件名称为空");
        }


        try {
            for(Integer id:bean.getIds()){
                //文件下载类型--二进制文件
                response.setContentType("application/octet-stream");
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
        return null;*/
    }

    @ApiOperation(value = "下载单个减免附件", notes = "下载单个减免附件")
    @PostMapping("/reduce/sigle/download")
    public Object reduceSingleDownload(HttpServletRequest request, HttpServletResponse response, @RequestBody ReduceFileList bean) throws Exception {

        try {
                //文件下载类型--二进制文件
                response.setContentType("application/octet-stream");

                if (StringUtils.notEmpty(bean) && StringUtils.notEmpty(bean.getFileId())){
                    //下载的文件携带这个名称
                    response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(bean.getFileName(), "UTF-8"));
                    FileInputStream fis = new FileInputStream(detailFile+bean.getFileId());
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    @ApiOperation(value = "打包下载", notes = "打包下载")
    @PostMapping("/reduce/zip/download")
    public Object downloadZip(HttpServletRequest request, HttpServletResponse response){

        try {
            String downloadFilename = "中文.zip";//文件的名称
            downloadFilename = URLEncoder.encode(downloadFilename, "UTF-8");//转换中文否则可能会产生乱码
            response.setContentType("application/octet-stream");// 指明response的返回对象是文件流
            response.setHeader("Content-Disposition", "attachment;filename=" + downloadFilename);// 设置在下载框默认显示的文件名
            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
            String[] fileNames = new String[]{"1.xml","1.editorconfig"};
            String[] files = new String[]{"D:/detailFile/1.xml","D:/detailFile/1eded35815e5428d9d9a9c06f798ed78.editorconfig"};
            for (int i=0;i<files.length;i++) {

                zos.putNextEntry(new ZipEntry(fileNames[i]));
                FileInputStream fis = new FileInputStream(new File(files[i]));
                //InputStream fis = url.openConnection().getInputStream();
                byte[] buffer = new byte[1024];
                int r = 0;
                while ((r = fis.read(buffer)) != -1) {
                    zos.write(buffer, 0, r);
                }
                fis.close();
            }
            zos.flush();
            zos.close();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
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
