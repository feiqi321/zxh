package xyz.zaijushou.zhx.utils;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletResponse;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import xyz.zaijushou.zhx.constant.ExcelEnum;

/**
 * CSVUtils
 */
public class CSVUtils {
	private static Logger logger = LoggerFactory.getLogger(CSVUtils.class);
	private static Pattern NAME_PATTERN = Pattern.compile("\\.");

    public static <T> void export(List<T> data, ExcelEnum[] enums, String fileName, HttpServletResponse response)
			throws IOException {
		//下载的文件携带这个名称
		response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
		//文件下载类型--二进制文件
		response.setContentType("application/csv");
		OutputStream os = response.getOutputStream();
        Arrays.sort(enums, Comparator.comparingInt(ExcelEnum::getSort));
        String[] header = new String[enums.length];
        for (int i = 0; i < enums.length; i++) {
            header[i] = enums[i].getCol();
        }
        OutputStreamWriter osw = null;
        CSVFormat csvFormat = null;
        CSVPrinter csvPrinter = null;
        try {
            osw = new OutputStreamWriter(os, "GBK");
            csvFormat = CSVFormat.DEFAULT.withHeader(header);
            csvPrinter = new CSVPrinter(osw, csvFormat);
            Map<Integer, ExcelEnum> excelEnumMap = new HashMap<>();
            for (ExcelEnum value : enums) {
                excelEnumMap.put(excelEnumMap.size(), value);
            }
            for (int i = 0; i < data.size(); i++) {
                Object val;
                List<String> content = new ArrayList<>();
                    T entity = data.get(i);
                    for (int k = 0; k < excelEnumMap.size(); k++) {
                        ExcelEnum excelEnum = excelEnumMap.get(k);
                        if (excelEnum == null || StringUtils.isEmpty(excelEnum.getAttr())) {
                            continue;
                        }
                        String attr = excelEnum.getAttr();
                        Matcher matcher = NAME_PATTERN.matcher(attr);
                        if (matcher.find()) {
                            String firstAttr = attr.substring(0, matcher.start());
                            String secondAttr = attr.substring(matcher.end());
                            Method firstAttrGetMethod = entity.getClass().getMethod("get" + firstAttr.substring(0, 1).toUpperCase() + firstAttr.substring(1));
                            Object object = firstAttrGetMethod.invoke(entity);
                            if (object == null) {
                                val = firstAttrGetMethod.invoke(entity);
                            } else {
                                Method secondAttrSetMethod = excelEnum.getAttrClazz()[0].getMethod("get" + secondAttr.substring(0, 1).toUpperCase() + secondAttr.substring(1));
                                val = secondAttrSetMethod.invoke(object);
                            }
                        } else {
                            Method method = entity.getClass().getMethod("get" + attr.substring(0, 1).toUpperCase() + attr.substring(1));
                            val = method.invoke(entity);
                        }
                        if (val != null) {
                            if (Date.class.getName().equals(val.getClass().getName())) {
                                content.add("\t" + new SimpleDateFormat("yyyy-MM-dd").format(val));
                            } else {
                                content.add("\t" + val.toString());
                            }
                        } else {
                            content.add("");
                        }
                    }
                    csvPrinter.printRecord(content);
                }
            csvPrinter.printRecord("\r\n");
            os.flush();
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            logger.error("csv解析错误：{}", e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os, csvPrinter);
        }
    }

    private static void close(OutputStream os, CSVPrinter csvPrinter) {
		if (csvPrinter != null) {
			try {
				csvPrinter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				csvPrinter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (os != null) {
			try {
				os.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}