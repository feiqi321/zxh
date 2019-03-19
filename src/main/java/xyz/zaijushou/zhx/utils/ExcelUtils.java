package xyz.zaijushou.zhx.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import xyz.zaijushou.zhx.constant.ExcelEnum;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
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
public class ExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

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
        Map<String, ExcelEnum> excelEnumMap = new HashMap<>();
        for (ExcelEnum value : enums) {
            excelEnumMap.put(value.getCol(), value);
        }
        Sheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(0);
        Map<Integer, ExcelEnum> colMap = new HashMap<>();
        logger.debug("列数：{}", header.getLastCellNum());
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
                if (row.getCell(0)==null && row.getCell(1)==null && row.getCell(2)==null  && row.getCell(3)==null){
                    entity = null;
                    continue;
                }
                for (int k = 0; k < header.getLastCellNum(); k++) {
                    Cell cell = row.getCell(k);
                    /*if (k==85){
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

        Object result;

        switch (cell.getCellType()) {
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
                    Date date = org.apache.poi.ss.usermodel.DateUtil.getJavaDate(value);
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
                result = cell.getStringCellValue();
        }
       logger.info("result:{}", result);
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
                for (int k = 0; k < excelEnumMap.size(); k++) {
                    logger.debug(k + "");
                    Cell cell = row.createCell(k);
                    ExcelEnum excelEnum = excelEnumMap.get(k);
                    if(excelEnum == null || StringUtils.isEmpty(excelEnum.getAttr())) {
                        continue;
                    }
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
            BigDecimal decimalValue = (BigDecimal) value;
            String formatValue = decimalValue == null ? "￥0.00" : "￥"+ FmtMicrometer.fmtMicrometer(decimalValue + "");
            cell.setCellValue(formatValue);
        } else {
            String toStringValue = value.toString();
            cell.setCellValue(toStringValue);
        }
    }


}
