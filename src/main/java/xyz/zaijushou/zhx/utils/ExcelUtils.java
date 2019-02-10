package xyz.zaijushou.zhx.utils;

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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class ExcelUtils {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static <T> List<T> importExcel(MultipartFile file, ExcelEnum[] enums, Class<T> entityClazz) throws IOException {
        List<T> resultList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        Workbook workbook = new XSSFWorkbook(inputStream);
        Map<String, ExcelEnum> excelEnumMap = new HashMap<>();
        for (ExcelEnum value : enums) {
            excelEnumMap.put(value.getCol(), value);
        }
        Sheet sheet = workbook.getSheetAt(0);
        Row header = sheet.getRow(0);
        Map<Integer, ExcelEnum> colMap = new HashMap<>();
        for (int i = 0; i < header.getPhysicalNumberOfCells(); i++) {
            String cell = header.getCell(i).getStringCellValue();
            colMap.put(i, excelEnumMap.get(cell));
        }
        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            try {
                Row row = sheet.getRow(i);
                T entity = entityClazz.getConstructor().newInstance();
                for (int k = 0; k < row.getPhysicalNumberOfCells(); k++) {
                    Cell cell = row.getCell(k);
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
                            Method firstAttrSetMethod = entityClazz.getMethod("set" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1), excelEnum.getAttrClazz()[0]);
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
                        Method method = entity.getClass().getMethod("set" + attr.substring(0, 1).toUpperCase() + attr.substring(1), excelEnum.getAttrClazz()[0]);
                        method.invoke(entity, cellValue(cell, excelEnum.getAttrClazz()[0]));
                    }

                }
                resultList.add(entity);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                logger.error("excel解析错误：{}", e);
            }
        }
        return resultList;
    }

    private static Object cellValue(Cell cell, Class clazz) {
        Object result;
        switch (cell.getCellType()) {
            case STRING:
                result = cell.getStringCellValue();
                break;
            case NUMERIC:
                if(clazz.equals(Date.class)) {
                    result = cell.getNumericCellValue();
                }else {
                    if(clazz.equals(Integer.class) || clazz.equals(int.class)) {
                        result = new Double(cell.getNumericCellValue()).intValue();
                    } else {
                        result = cell.getNumericCellValue();
                    }
                }
                break;
            default:
                result = null;
        }
        return result;
    }

}
