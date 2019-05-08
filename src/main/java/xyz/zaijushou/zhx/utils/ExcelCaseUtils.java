package xyz.zaijushou.zhx.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.constant.ExcelEnum;
import xyz.zaijushou.zhx.sys.entity.DataCaseEntity;
import xyz.zaijushou.zhx.sys.entity.StatisticReturn;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExcelCaseUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelCaseUtils.class);

    private static FormulaEvaluator evaluator;


    public static <T> List<T> importExcel(MultipartFile file, ExcelEnum[] enums, Class<T> entityClazz) throws IOException {
        List<T> resultList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        String fileName = file.getOriginalFilename();
        Workbook workbook;
        if(StringUtils.isNotEmpty(fileName) && fileName.length() >= 5 && ".xlsx".equals(fileName.substring(fileName.length() - 5))) {
            workbook = new XSSFWorkbook(inputStream);
        } else {
            workbook = new HSSFWorkbook(inputStream);
        }
        evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        Map<String, ExcelEnum> excelEnumMap = new HashMap<>();
        for (ExcelEnum value : enums) {
            excelEnumMap.put(value.getCol(), value);
        }
        Sheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(0);
        Map<Integer, ExcelEnum> colMap = new HashMap<>();
        //logger.debug("列数：{}", header.getLastCellNum());
        for (int i = 0; i < header.getLastCellNum(); i++) {
            Cell cell = header.getCell(i);
            if(cell == null) {
                colMap.put(i, null);
                continue;
            }
            String cellValue = cell.getStringCellValue();

            colMap.put(i, excelEnumMap.get(cellValue.toString().trim()));
        }
        for (int i = 1; i <= sheet.getPhysicalNumberOfRows()-1; i++) {
            try {
                Row row = sheet.getRow(i);
                T entity = entityClazz.getConstructor().newInstance();
                if (row==null){
                    entity = null;
                    continue;
                }
                if ((row.getCell(0)==null || row.getCell(0).toString().equals("")) && (row.getCell(1)==null ||  row.getCell(1).toString().equals("")) && (row.getCell(2)==null || row.getCell(2).toString().equals(""))  && (row.getCell(3)==null ||  row.getCell(3).toString().equals(""))){
                    entity = null;
                    continue;
                }
                for (int k = 0; k < header.getLastCellNum(); k++) {
                    Cell cell = row.getCell(k);
                    /*if (k==92){
                        System.out.println(111);
                    }*/
                    ExcelEnum excelEnum = colMap.get(k);
                    if (excelEnum == null) {
                        continue;
                    }
                    String attr = excelEnum.getAttr();
                    Matcher matcher = Pattern.compile("\\[\\d\\]\\.").matcher(attr);
                    if (matcher.find()) {
                        int index = Integer.parseInt(matcher.group(0).substring(1, 2));
                        String firstAttr = attr.substring(0, matcher.start());
                        String secondAttr = attr.substring(matcher.end());
                        Method firstAttrGetMethod = entityClazz.getMethod("get" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1));
                        Object object = firstAttrGetMethod.invoke(entity);
                        if (object == null) {
                            Method firstAttrSetMethod = entityClazz.getMethod("set" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1), List.class);
                            firstAttrSetMethod.invoke(entity, new ArrayList<>());
                        }
                        List subList = (List) firstAttrGetMethod.invoke(entity);
                        if (subList.size() == index) {
                            Class clazz = excelEnum.getAttrClazz()[0];
                            Constructor constructor = clazz.getConstructor();
                            Object obj = constructor.newInstance();
                            subList.add(obj);
                        }
                        Method secondAttrSetMethod = excelEnum.getAttrClazz()[0].getMethod("set" + secondAttr.substring(0, 1).toUpperCase() + secondAttr.substring(1), excelEnum.getAttrClazz()[1]);

                        secondAttrSetMethod.invoke(subList.get(index), cellValue(cell, excelEnum.getAttrClazz()[1]));
                    } else {
                        matcher = Pattern.compile("\\.").matcher(attr);
                        if (matcher.find()) {
                            String firstAttr = attr.substring(0, matcher.start());
                            String secondAttr = attr.substring(matcher.end());
                            Method firstAttrGetMethod = entityClazz.getMethod("get" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1));
                            Object object = firstAttrGetMethod.invoke(entity);
                            if (object == null) {
                                Class clazz = excelEnum.getAttrClazz()[0];
                                Constructor constructor = clazz.getConstructor();
                                Object obj = constructor.newInstance();
                                Method firstAttrSetMethod = entityClazz.getMethod("set" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1), excelEnum.getAttrClazz()[0]);
                                firstAttrSetMethod.invoke(entity, obj);
                            }
                            object = firstAttrGetMethod.invoke(entity);
                            Method secondAttrSetMethod = excelEnum.getAttrClazz()[0].getMethod("set" + secondAttr.substring(0, 1).toUpperCase() + secondAttr.substring(1), excelEnum.getAttrClazz()[1]);
                            secondAttrSetMethod.invoke(object, cellValue(cell, excelEnum.getAttrClazz()[1]));
                        } else {
                            Method method = entity.getClass().getMethod("set" + attr.substring(0, 1).toUpperCase() + attr.substring(1), excelEnum.getAttrClazz()[0]);
                            method.invoke(entity, cellValue(cell, excelEnum.getAttrClazz()[0]));
                        }
                    }

                }
                if (entity!=null) {
                    resultList.add(entity);
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                logger.error("excel解析错误：{}", e);
                throw new IOException("excel解析错误", e);
            } catch (ParseException e) {
                logger.error("excel日期解析错误：{}", e);
                throw new IOException("excel日期解析错误", e);
            } catch (Exception e) {
                logger.error("excel解析错误：{}", e);
                throw new IOException("excel解析错误", e);
            }
        }
        return resultList;
    }

    private static Object cellValue(Cell cell, Class clazz) throws ParseException {
        if(cell == null) {
            return null;
        }
        CellType cellType;
        if(CellType.FORMULA == cell.getCellType()) {
            cellType = evaluator.evaluate(cell).getCellType();
        } else {
            cellType = cell.getCellType();
        }

        Object result;

        switch (cellType) {
            case STRING:
                result = cell.getStringCellValue();
                if (result == null) {

                } else if (clazz.equals(BigDecimal.class)) {
                    result = new BigDecimal((String) result);
                } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                    result = Integer.parseInt((String) result);
                } else if (clazz.equals(Double.class) || clazz.equals(double.class)) {
                    result = Double.parseDouble((String) result);
                } else if (clazz.equals(Date.class)) {
                    if(((String) result).indexOf("-")>0) {
                        result = new SimpleDateFormat("yyyy-MM-dd").parse((String) result);
                    }else{
                        result = new SimpleDateFormat("yyyy/MM/dd").parse((String) result);
                    }
                }
                break;
            case NUMERIC:
                DecimalFormat df = new DecimalFormat("0");
                if (clazz.equals(Date.class)) {
                    double value = cell.getNumericCellValue();
                    Date date = DateUtil.getJavaDate(value);
                    result = date;
                } else if(clazz.equals(String.class)) {
                    result = "" + df.format(cell.getNumericCellValue());
                } else {
                    if (clazz.equals(BigDecimal.class)) {
                        result = new BigDecimal(cell.getNumericCellValue());
                    } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
                        result = new Double(cell.getNumericCellValue()).intValue();
                    } else {
                        result = cell.getNumericCellValue();
                    }
                }
                break;
            default:
                result = null;
        }
        //logger.info("result:{}", result);
        return result;
    }

    public static <T> void exportExcel(List<T> data, ExcelEnum[] enums, String fileName, HttpServletResponse response) throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        //渲染表头
        Arrays.sort(enums, Comparator.comparingInt(ExcelEnum::getSort));
        Map<Integer, ExcelEnum> excelEnumMap = new HashMap<>();
        for (ExcelEnum value : enums) {
            excelEnumMap.put(excelEnumMap.size(), value);

        }
        Row header = sheet.createRow(0);
        for (int i = 0; i < excelEnumMap.size(); i++) {
            Cell cell = header.createCell(i);
            cell.setCellValue(enums[i].getCol());
        }
        for (int i = 1; i <= data.size(); i++) {
            try {
                T entity = data.get(i - 1);
                Row row = sheet.createRow(i);
                XSSFCellStyle style = workbook.createCellStyle();
                DataCaseEntity dataCaseEntity = (DataCaseEntity)entity;
                if (dataCaseEntity.getColor()!=null && dataCaseEntity.getColor().equals("RED")){
                    XSSFColor color = new XSSFColor(new java.awt.Color(255, 0, 0));
                    style.setFillForegroundColor(color);
                }else if (dataCaseEntity.getColor()!=null && dataCaseEntity.getColor().equals("BLUE")){
                    XSSFColor color = new XSSFColor(new java.awt.Color(0, 0, 255));
                    style.setFillForegroundColor(color);
                }else if (dataCaseEntity.getColor()!=null && dataCaseEntity.getColor().equals("ORANGE")){
                    XSSFColor color = new XSSFColor(new java.awt.Color(250, 128, 144));
                    style.setFillForegroundColor(color);
                }else if (dataCaseEntity.getColor()!=null && dataCaseEntity.getColor().equals("ZI")){
                    XSSFColor color = new XSSFColor(new java.awt.Color(160, 32, 240));
                    style.setFillForegroundColor(color);
                }else if (dataCaseEntity.getColor()!=null && dataCaseEntity.getColor().equals("ZONG")){
                    XSSFColor color = new XSSFColor(new java.awt.Color(210, 180, 140));
                    style.setFillForegroundColor(color);
                }else{
                    XSSFColor color = new XSSFColor(new java.awt.Color(255, 255, 255));
                    style.setFillForegroundColor(color);
                }

                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                for (int k = 0; k < excelEnumMap.size(); k++) {
                    Cell cell = row.createCell(k);

                    ExcelEnum excelEnum = excelEnumMap.get(k);
                    if(excelEnum == null || StringUtils.isEmpty(excelEnum.getAttr())) {
                        continue;
                    }
                    cell.setCellStyle(style);
                    String attr = excelEnum.getAttr();
                    Matcher matcher = Pattern.compile("\\[\\d\\]\\.").matcher(attr);
                    if (matcher.find()) {
                        Matcher threeLevelMatcher = Pattern.compile("\\..+\\[\\d\\]\\.").matcher(attr);
                        if(threeLevelMatcher.find()) {
                            String[] attrs = attr.split("\\.");
                            Method firstAttrGetMethod = entity.getClass().getMethod("get" + attrs[0].substring(0, 1).toUpperCase() + attrs[0].substring(1));
                            Object object = firstAttrGetMethod.invoke(entity);
                            if (object == null) {
                                continue;
                            }
                            String secondAttr = attrs[1].substring(0, attrs[1].length() - 3);
                            Method secondAttrSetMethod = excelEnum.getAttrClazz()[0].getMethod("get" + secondAttr.substring(0, 1).toUpperCase() + secondAttr.substring(1));
                            Object secondObj = secondAttrSetMethod.invoke(object);
                            if (secondObj == null) {
                                continue;
                            }
                            int index = Integer.parseInt(matcher.group(0).substring(1, 2));
                            List list = (List) secondObj;
                            if (list.size() == 0 || index >= list.size()) {
                                continue;
                            }
                            Method thirdAttrSetMethod = excelEnum.getAttrClazz()[1].getMethod("get" + attrs[2].substring(0, 1).toUpperCase() + attrs[2].substring(1));
                            Object thirdObj = thirdAttrSetMethod.invoke(list.get(index));
                            setCellValue(thirdObj, cell, excelEnum.getAttrClazz()[2]);
                        }else {
                            int index = Integer.parseInt(matcher.group(0).substring(1, 2));
                            String firstAttr = attr.substring(0, matcher.start());
                            String secondAttr = attr.substring(matcher.end());
                            Method firstAttrGetMethod = entity.getClass().getMethod("get" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1));
                            Object object = firstAttrGetMethod.invoke(entity);
                            if (object == null) {
                                continue;
                            }
                            List subList = (List) firstAttrGetMethod.invoke(entity);
                            if (subList.size() == 0 || index >= subList.size()) {
                                continue;
                            }
                            Method secondAttrSetMethod = excelEnum.getAttrClazz()[0].getMethod("get" + secondAttr.substring(0, 1).toUpperCase() + secondAttr.substring(1));
                            Object obj = secondAttrSetMethod.invoke(subList.get(index));
                            setCellValue(obj, cell, excelEnum.getAttrClazz()[1]);
                        }
                    } else {

                        matcher = Pattern.compile("\\..+\\.").matcher(attr);
                        if(matcher.find()) {
                            String[] attrs = attr.split("\\.");
                            Method firstAttrGetMethod = entity.getClass().getMethod("get" + attrs[0].substring(0, 1).toUpperCase() + attrs[0].substring(1));
                            Object object = firstAttrGetMethod.invoke(entity);
                            if (object == null) {
                                continue;
                            }
                            Method secondAttrSetMethod = excelEnum.getAttrClazz()[0].getMethod("get" + attrs[1].substring(0, 1).toUpperCase() + attrs[1].substring(1));
                            Object secondObj = secondAttrSetMethod.invoke(object);
                            if (secondObj == null) {
                                continue;
                            }
                            Method thirdAttrSetMethod = excelEnum.getAttrClazz()[1].getMethod("get" + attrs[2].substring(0, 1).toUpperCase() + attrs[2].substring(1));
                            Object thirdObj = thirdAttrSetMethod.invoke(secondObj);
                            setCellValue(thirdObj, cell, excelEnum.getAttrClazz()[2]);
                            continue;
                        }

                        matcher = Pattern.compile("\\.").matcher(attr);
                        if (matcher.find()) {
                            String firstAttr = attr.substring(0, matcher.start());
                            String secondAttr = attr.substring(matcher.end());
                            Method firstAttrGetMethod = entity.getClass().getMethod("get" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1));
                            Object object = firstAttrGetMethod.invoke(entity);
                            if (object == null) {
                                continue;
                            }

                            Method secondAttrSetMethod = excelEnum.getAttrClazz()[0].getMethod("get" + secondAttr.substring(0, 1).toUpperCase() + secondAttr.substring(1));

                            Object obj = secondAttrSetMethod.invoke(object);
                            setCellValue(obj, cell, excelEnum.getAttrClazz()[1]);
                        } else {
                            Method method = entity.getClass().getMethod("get" + attr.substring(0, 1).toUpperCase() + attr.substring(1));
                            setCellValue(method.invoke(entity), cell, excelEnum.getAttrClazz()[0]);
                        }
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                logger.error("excel解析错误：{}", e);
            }
        }


        //下载的文件携带这个名称
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
        //文件下载类型--二进制文件
        response.setContentType("application/octet-stream");
        ServletOutputStream servletOutputStream = response.getOutputStream();

        workbook.write(servletOutputStream);

        servletOutputStream.flush();
        servletOutputStream.close();
//        File file = new File(fileName);
//        if (!file.exists()){
//            file.mkdir();
//        }
//        FileOutputStream out = new FileOutputStream("C:\\fileDownload\\"+fileName);
//        workbook.write(out);
//        out.close();
    }

    private static void setCellValue(Object value, Cell cell, Class clazz) {
        if (value == null) {
            return;
        }
        if (String.class == clazz) {
            String stringValue = value == null ? "" : String.valueOf(value);
            cell.setCellValue(stringValue);
        } else if (Date.class == clazz) {
            try {
                Date dateValue = (Date) value;
                cell.setCellValue(dateValue == null ? "" : new SimpleDateFormat("yyyy-MM-dd").format(dateValue));
            }catch(Exception e){
                if (value==null){
                    cell.setCellValue("");
                }else {
                    cell.setCellValue(value.toString());
                }
            }
        } else if (Double.class == clazz) {
            Double doubleValue = (Double) value;
            cell.setCellValue(doubleValue);
        } else if (Integer.class == clazz) {
            Integer intValue = (Integer) value;
            cell.setCellValue(intValue);
        } else if (BigDecimal.class == clazz) {
            BigDecimal decimalValue = new BigDecimal(value==null?"0":(value.equals("null")?"0":value.toString()));
            //String formatValue = decimalValue == null ? "￥0.00" : "￥"+ FmtMicrometer.fmtMicrometer(decimalValue + "");
            String formatValue = decimalValue == null ? "0.00" :decimalValue + "";
            cell.setCellValue(formatValue);
        } else {
            String toStringValue = value.toString();
            cell.setCellValue(toStringValue);
        }
    }

    public static void exportTempleteMonth(HttpServletResponse response, List<StatisticReturn>  list,String realname)throws IOException{
        OutputStream outputStream = response.getOutputStream();
        String fileName = new String(("month-export").getBytes(), "ISO8859_1");



        response.setHeader(
                "Content-disposition",
                "attachment; filename=" + realname + ".xlsx");// 组装附件名称和格式

        // 创建一个workbook 对应一个excel应用文件
        try (XSSFWorkbook workBook = new XSSFWorkbook()){
            // 创建一个workbook 对应一个excel应用文件
            // 在workbook中添加一个sheet,对应Excel文件中的sheet
            XSSFSheet sheet = workBook.createSheet("电催员电催月统计导出");
            //单元格居中
            XSSFCellStyle cellStyle = workBook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框
            cellStyle.setAlignment(HorizontalAlignment.CENTER);//左右居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
            //设置字体样式
            XSSFFont font = workBook.createFont();
            // 设置字体加粗
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 11);
            cellStyle.setFont(font);  //设置字体大小
            // 构建表头
            XSSFRow titleRow = null;
            XSSFRow bodyRow = null;
            XSSFCell titleCell = null;
            XSSFCell bodyCel = null;
            List<String> titleNameList = new ArrayList<>();

            // 表头每个单元格进行赋值
            for (int i = 0; i < 2; i++) {
                titleRow = sheet.createRow(i);
                titleCell = titleRow.createCell(0);
                sheet.autoSizeColumn(i,true);
                titleCell.setCellStyle(cellStyle);
                titleCell.setCellValue("催收员");
            }
            setMergeStyle(new CellRangeAddress( 0,1 , 0, 0),sheet);

            titleRow = sheet.getRow(0);
            // 设置标题
            if (xyz.zaijushou.zhx.utils.StringUtils.notEmpty(list)){
                for (int i =0 ;i < list.get(0).getList().size();i++){
                    titleCell = titleRow.createCell(i*3+1);
                    sheet.autoSizeColumn(i*3+1);
                    titleCell.setCellStyle(cellStyle);
                    titleCell.setCellValue(list.get(0).getList().get(i).getArea());
                    setMergeStyle(new CellRangeAddress( 0,0 , i*3+1, i*3+3),sheet);
                }
                String[] nameStr = {"有效通电","总通电量","个案量"};
                titleRow = sheet.getRow(1);
                for (int i =0 ;i < list.get(0).getList().size();i++){

                    for (int j=0;j<3;j++){
                        titleCell = titleRow.createCell(i*3+j+1);
                        sheet.autoSizeColumn(i*3+j+1);
                        titleCell.setCellStyle(cellStyle);
                        titleCell.setCellValue(nameStr[j]);
                    }
                }
                //表格内容赋值

                for (int i =0 ;i < list.size();i++){
                    if (xyz.zaijushou.zhx.utils.StringUtils.isEmpty(list.get(i).getOdv())){
                        continue;
                    }
                    bodyRow = sheet.createRow(2+i);
                    bodyCel = bodyRow.createCell(0);
                    sheet.autoSizeColumn(0,true);
                    bodyCel.setCellStyle(cellStyle);
                    bodyCel.setCellValue(list.get(i).getOdv());
                    for (int k=0 ;k<list.get(i).getList().size();k++){
                        bodyCel = bodyRow.createCell(k*3+1);
                        sheet.autoSizeColumn(k*3+1);
                        bodyCel.setCellStyle(cellStyle);
                        bodyCel.setCellValue(list.get(i).getList().get(k).getCountConPhoneNum());

                        titleCell = bodyRow.createCell(k*3+2);
                        sheet.autoSizeColumn(k*3+2);
                        titleCell.setCellStyle(cellStyle);
                        titleCell.setCellValue(list.get(i).getList().get(k).getCountPhoneNum());

                        titleCell = bodyRow.createCell(k*3+3);
                        sheet.autoSizeColumn(k*3+3);
                        titleCell.setCellStyle(cellStyle);
                        titleCell.setCellValue(list.get(i).getList().get(k).getCountCasePhoneNum());
                    }
                }
            }
            workBook.write(outputStream);
        } catch (IOException e) {
            logger.error("导出失败",e);
        } finally {
            if (outputStream != null){
                outputStream.close();
                outputStream.flush();
            }
        }
    }
    //设置合并单元格样式
    static void setMergeStyle(CellRangeAddress cra,XSSFSheet sheet ){
        sheet.addMergedRegion(cra);
        // 使用RegionUtil类为合并后的单元格添加边框
        RegionUtil.setBorderBottom(BorderStyle.THIN, cra, sheet); // 下边框
        RegionUtil.setBorderLeft(BorderStyle.THIN, cra, sheet); // 左边框
        RegionUtil.setBorderRight(BorderStyle.THIN, cra, sheet); // 有边框
        RegionUtil.setBorderTop(BorderStyle.THIN, cra, sheet); // 上边框
    }

    public static void exportTempleteDay(HttpServletResponse response, List<StatisticReturn>  list,String realname)throws IOException{
        OutputStream outputStream = response.getOutputStream();


        response.setHeader(
                "Content-disposition",
                "attachment; filename=" + realname + ".xlsx");// 组装附件名称和格式

        // 创建一个workbook 对应一个excel应用文件
        try (XSSFWorkbook workBook = new XSSFWorkbook()){
            // 创建一个workbook 对应一个excel应用文件
            // 在workbook中添加一个sheet,对应Excel文件中的sheet
            XSSFSheet sheet = workBook.createSheet("电催员电催单日统计导出");
            //单元格居中
            XSSFCellStyle cellStyle = workBook.createCellStyle();
            cellStyle.setBorderBottom(BorderStyle.THIN); //下边框
            cellStyle.setBorderLeft(BorderStyle.THIN);//左边框
            cellStyle.setBorderTop(BorderStyle.THIN);//上边框
            cellStyle.setBorderRight(BorderStyle.THIN);//右边框
            cellStyle.setAlignment(HorizontalAlignment.CENTER);//左右居中
            cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);//上下居中
            //设置字体样式
            XSSFFont font = workBook.createFont();
            // 设置字体加粗
            font.setFontName("微软雅黑");
            font.setFontHeightInPoints((short) 11);
            cellStyle.setFont(font);  //设置字体大小
            // 构建表头
            XSSFRow titleRow = null;
            XSSFRow bodyRow = null;
            XSSFCell titleCell = null;
            XSSFCell bodyCel = null;
            List<String> titleNameList = new ArrayList<>();
            // 表头每个单元格进行赋值
            for (int i = 0; i < 2; i++) {
                titleRow = sheet.createRow(i);
                titleCell = titleRow.createCell(0);
                sheet.autoSizeColumn(i,true);
                titleCell.setCellStyle(cellStyle);
                titleCell.setCellValue("催收员");
            }
            setMergeStyle(new CellRangeAddress( 0,1 , 0, 0),sheet);

            String[] timeStr = {"8:00前","8:00-12:00","12:00-18:00","18:00以后","合计"};
            String[] nameStr = {"有效通电","总通电量","个案量"};
            for(int i=0;i<5;i++){
                titleRow = sheet.getRow(0);
                titleCell = titleRow.createCell(i*3+1);
                sheet.autoSizeColumn(i*3+1);
                titleCell.setCellStyle(cellStyle);
                titleCell.setCellValue(timeStr[i]);
                setMergeStyle(new CellRangeAddress( 0,0 , i*3+1, i*3+3),sheet);
                titleRow = sheet.getRow(1);

                for (int j=0;j<3;j++){
                    titleCell = titleRow.createCell(i*3+j+1);
                    sheet.autoSizeColumn(i*3+j+1);
                    titleCell.setCellStyle(cellStyle);
                    titleCell.setCellValue(nameStr[j]);
                }
            }

            if (xyz.zaijushou.zhx.utils.StringUtils.notEmpty(list)){
                int length = list.get(0).getList().size();
                //表格内容赋值
                for (int i =0 ;i < list.size();i++){
                    if (xyz.zaijushou.zhx.utils.StringUtils.isEmpty(list.get(i).getOdv())){
                        continue;
                    }
                    bodyRow = sheet.createRow(2+i);
                    bodyCel = bodyRow.createCell(0);
                    sheet.autoSizeColumn(0,true);
                    bodyCel.setCellStyle(cellStyle);
                    bodyCel.setCellValue(list.get(i).getOdv());
                    for (int k=0 ;k<list.get(i).getList().size();k++){
                        bodyCel = bodyRow.createCell(k*3+1);
                        sheet.autoSizeColumn(k*3+1);
                        bodyCel.setCellStyle(cellStyle);
                        bodyCel.setCellValue(list.get(i).getList().get(k).getCountConPhoneNum());

                        titleCell = bodyRow.createCell(k*3+2);
                        sheet.autoSizeColumn(k*3+2);
                        titleCell.setCellStyle(cellStyle);
                        titleCell.setCellValue(list.get(i).getList().get(k).getCountPhoneNum());

                        titleCell = bodyRow.createCell(k*3+3);
                        sheet.autoSizeColumn(k*3+3);
                        titleCell.setCellStyle(cellStyle);
                        titleCell.setCellValue(list.get(i).getList().get(k).getCountCasePhoneNum());
                    }

                    bodyCel = bodyRow.createCell(length*3+1);
                    sheet.autoSizeColumn(length*3+1);
                    bodyCel.setCellStyle(cellStyle);
                    bodyCel.setCellValue(list.get(i).getSumConPhoneNum());

                    titleCell = bodyRow.createCell(length*3+2);
                    sheet.autoSizeColumn(length*3+2);
                    titleCell.setCellStyle(cellStyle);
                    titleCell.setCellValue(list.get(i).getSumPhoneNum());

                    titleCell = bodyRow.createCell(length*3+3);
                    sheet.autoSizeColumn(length*3+3);
                    titleCell.setCellStyle(cellStyle);
                    titleCell.setCellValue(list.get(i).getSumCasePhoneNum());
                }
            }
            workBook.write(outputStream);
        } catch (IOException e) {
            logger.error("导出失败",e);
        } finally {
            if (outputStream != null){
                outputStream.close();
                outputStream.flush();
            }
        }
    }
}
