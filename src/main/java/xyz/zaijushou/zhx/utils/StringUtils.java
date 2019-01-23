package xyz.zaijushou.zhx.utils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Copyright (C), 2018,北京同创永益科技发展有限公司
 * @Package: cn.com.hatech.common.util.stringutils
 * @ClassName: stringutils
 * @Author: zhuhuanglong
 * @Description: 字符处理用
 * @Date: 2018/12/29 0029 下午 5:11
 * @Version: 1.0
 */
@SuppressWarnings("all")
public class StringUtils {
    public static final String ALLOWED_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.!~*'()";

    /**
     * 转码
     * @param input
     * @return
     */
    public static String encodeURIComponent(String input) {
        if (StringUtils.isEmpty(input)) {
            return input;
        }

        int l = input.length();
        StringBuilder o = new StringBuilder(l * 3);
        try {
            for (int i = 0; i < l; i++) {
                String e = input.substring(i, i + 1);
                if (ALLOWED_CHARS.indexOf(e) == -1) {
                    byte[] b = e.getBytes("utf-8");
                    o.append(getHex(b));
                    continue;
                }
                o.append(e);
            }
            return o.toString();
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getMessage());
        }
        return input;
    }

    /**
     * 解析对象，当对象为null时，返回空字符串
     * @param obj
     * @return 空字符串
     */
    public static Object parseValue(Object obj) {
        if (obj == null) {
            return "";
        }

        return obj;
    }

    public static String parseString(Object obj) {
        if (obj == null) {
            return "";
        }

        return String.valueOf(obj);
    }

    /**
     * 解码
     * @param encodedURI
     * @return
     */
    public static String decodeURIComponent(String encodedURI) {
        char actualChar;

        StringBuffer buffer = new StringBuffer();

        int bytePattern, sumb = 0;

        for (int i = 0, more = -1; i < encodedURI.length(); i++) {
            actualChar = encodedURI.charAt(i);

            switch (actualChar) {
                case '%': {
                    actualChar = encodedURI.charAt(++i);
                    int hb = (Character.isDigit(actualChar) ? actualChar - '0'
                            : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                    actualChar = encodedURI.charAt(++i);
                    int lb = (Character.isDigit(actualChar) ? actualChar - '0'
                            : 10 + Character.toLowerCase(actualChar) - 'a') & 0xF;
                    bytePattern = (hb << 4) | lb;
                    break;
                }
                case '+': {
                    bytePattern = ' ';
                    break;
                }
                default: {
                    bytePattern = actualChar;
                }
            }

            if ((bytePattern & 0xc0) == 0x80) { // 10xxxxxx
                sumb = (sumb << 6) | (bytePattern & 0x3f);
                if (--more == 0)
                    buffer.append((char) sumb);
            } else if ((bytePattern & 0x80) == 0x00) { // 0xxxxxxx
                buffer.append((char) bytePattern);
            } else if ((bytePattern & 0xe0) == 0xc0) { // 110xxxxx
                sumb = bytePattern & 0x1f;
                more = 1;
            } else if ((bytePattern & 0xf0) == 0xe0) { // 1110xxxx
                sumb = bytePattern & 0x0f;
                more = 2;
            } else if ((bytePattern & 0xf8) == 0xf0) { // 11110xxx
                sumb = bytePattern & 0x07;
                more = 3;
            } else if ((bytePattern & 0xfc) == 0xf8) { // 111110xx
                sumb = bytePattern & 0x03;
                more = 4;
            } else { // 1111110x
                sumb = bytePattern & 0x01;
                more = 5;
            }
        }
        return buffer.toString();
    }

    private static String getHex(byte buf[]) {
        StringBuilder o = new StringBuilder(buf.length * 3);
        for (int i = 0; i < buf.length; i++) {
            int n = buf[i] & 0xff;
            o.append("%");
            if (n < 0x10) {
                o.append("0");
            }
            o.append(Long.toString(n, 16).toUpperCase());
        }
        return o.toString();
    }

    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    /**
     * 判断某个对象是否为空 集合类、数组做特殊处理
     *
     * @param obj
     * @return 如为空，返回true,否则false
     * @author YZH
     */
    @SuppressWarnings("unchecked")
    public static boolean isEmpty(Object obj) {
        if (obj == null)
            return true;

        // 如果不为null，需要处理几种特殊对象类型
        if (obj instanceof String) {
            return obj.equals("") || obj.equals("null");
        } else if (obj instanceof Integer) {
            int value = (Integer)obj;
            // 对象为数组
            return value == 0;
        } else if (obj instanceof Long) {
            long value = (Long) obj;
            // 对象为数组
            return value == 0;
        } else if (obj instanceof Collection) {
            // 对象为集合
            Collection coll = (Collection) obj;
            return coll.size() == 0;
        } else if (obj instanceof Map) {
            // 对象为Map
            Map map = (Map) obj;
            return map.size() == 0;
        } else if (obj.getClass().isArray()) {
            // 对象为数组
            return Array.getLength(obj) == 0;
        } else {
            // 其他类型，只要不为null，即不为empty
            return false;
        }
    }

    /**
     * 转码
     * @param str
     * @return
     */
/*	public static String encodeBase64(String str){
		if (str == null){
			return null;
		}else{
			return (new sun.misc.BASE64Encoder()).encode(str.getBytes() );
		}
	}*/

    /**
     * 解码
     * @param args
     */
/*	public static String  decodeBase64(String str){
		if (str == null) {
			return null;
		}else{
			BASE64Decoder decoder = new BASE64Decoder();
            try {
            	byte[] b = decoder.decodeBuffer(str);
            	return new String(b);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
		}
	}*/

    /**
     * 使用StringTokenizer类将字符串按分隔符转换成字符数组
     * @param string 字符串
     * @param divisionChar 分隔符
     * @return 字符串数组
     * @see [类、类#方法、类#成员]
     */
    public static String[] stringAnalytical(String string, String divisionChar)
    {
        int i = 0;
        StringTokenizer tokenizer = new StringTokenizer(string, divisionChar);

        String[] str = new String[tokenizer.countTokens()];

        while (tokenizer.hasMoreTokens())
        {
            str[i] = new String();
            str[i] = tokenizer.nextToken();
            i++;
        }

        return str;
    }

    /**
     * 字符串解析，不使用StringTokenizer类和java.lang.String的split()方法
     * 将字符串根据分割符转换成字符串数组
     * @param string 字符串
     * @param c 分隔符
     * @return 解析后的字符串数组
     */
    public static String[] stringAnalytical(String string, char c)
    {
        //字符串中分隔符的个数
        int count = 0;

        //如果不含分割符则返回字符本身
        if (string.indexOf(c) == -1)
        {
            return new String[]{string};
        }

        char[] cs = string.toCharArray();

        //过滤掉第一个和最后一个是分隔符的情况
        for (int i = 1; i < cs.length -1; i++)
        {
            if (cs[i] == c)
            {
                count++; //得到分隔符的个数
            }
        }

        String[] strArray = new String[count + 1];
        int k = 0, j = 0;
        String str = string;

        //去掉第一个字符是分隔符的情况
        if ((k = str.indexOf(c)) == 0)
        {
            str = string.substring(k + 1);
        }

        //检测是否包含分割符，如果不含则返回字符串
        if (str.indexOf(c) == -1)
        {
            return new String[]{str};
        }

        while ((k = str.indexOf(c)) != -1)
        {
            strArray[j++] = str.substring(0, k);
            str = str.substring(k + 1);
            if ((k = str.indexOf(c)) == -1 && str.length() > 0)
            {
                strArray[j++] = str.substring(0);
            }
        }

        return strArray;
    }

    /**
     * 去除null以及空格串
     * @return:
     * @author: YZH
     */
    public static String noNull(Object s) {
        if (s == null)
            return "";
        else
            return s.toString().trim();
    }

    /**
     *
     * {将文件名中的汉字转为UTF8编码的串,以便下载时能正确显示另存的文件名}
     * @param s 原文件名
     * @return  重新编码后的文件名
     * @author: YZH
     */
    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if ((c >= 0) && (c <= 255)) {
                sb.append(c);
            }
            else {
                byte[] b;

                try {
                    b = Character.toString(c).getBytes("UTF-8");
                }
                catch (Exception ex) {
                    b = new byte[0];
                }
                for (int j = 0; j < b.length; j++) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }
                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }
        return sb.toString();
    }

    public static boolean arryContain(String[]arr,String s2){
        for(int i=0;i<arr.length;i++){
            String si=arr[i];
            if(si.equals(s2)){
                return true;
            }
        }
        return false;
    }
    //格式化时间
    public static String formatNum(Object num,String pattern){
        DecimalFormat df = new DecimalFormat(pattern);
        String db = df.format(num);
        return db;
    }
    /**
     *
     * @param 要四舍五入的数(0.045)
     * @param precision(2) 保留位数 0.05
     * @return
     */
    public static String round(double val, int precision) {
        Double ret = null;
        try {
            double factor = Math.pow(10, precision);
            ret = Math.floor(val * factor + 0.5) / factor;
            return ret+"";
        } catch (Exception e) {
            e.printStackTrace();
            return val+"";
        }
    }

    //字符串转换成日期(包括年月日时分秒)
    public static Date stringToDateTime(String str,String pattern){
        SimpleDateFormat sdf=new SimpleDateFormat(pattern);
        Date dt=null;
        try {
            dt = sdf.parse(str);
            return dt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String checkTips(String tips) {
        StringBuffer sb = new StringBuffer();
        sb.append("<script type=\"text/javascript\">").append("alert('").append(tips)
                .append("');window.history.back();").append("</script>");
        return sb.toString();
    }


    /**
     * 隐藏手机号中间4位
     */
    public static String hidePhoneNumber(String phoneNumber){

        return phoneNumber.substring(0,3)+"****"+phoneNumber.substring(7);
    }

    /**
     * 隐藏姓名，只显示姓
     */
    public static String hideFullname(String fullname){
        if (fullname == null || "".equals(fullname)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        sb.append(fullname.substring(0, 1));
        if (fullname.length()>1) {
            for (int i = 1; i < fullname.length(); i++) {
                sb.append("*");
            }
        }
        return sb.toString();
    }

    public static String listToString(List<String> list) {
        if (list == null || list.size() < 1) {
            return "";
        }

        StringBuilder result=new StringBuilder();
        boolean flag=false;
        for (String string : list) {
            if (flag) {
                result.append(",");
            }else {
                flag=true;
            }
            result.append(string);
        }
        return result.toString();
    }

    /**
     * 读取字符串第i次出现特定符号的位置
     * @param string
     * @param i
     * @param character
     * @return
     */
    public static int getCharacterPosition(String string ,int i,String character){
        //这里是获取"/"符号的位置
        // Matcher slashMatcher = Pattern.compile("/").matcher(string);
        Matcher slashMatcher = Pattern.compile(character).matcher(string);
        int mIdx = 0;
        while(slashMatcher.find()) {
            mIdx++;
            //当"/"符号第三次出现的位置
            if(mIdx == i){
                break;
            }
        }
        return slashMatcher.start();
    }

    public static Boolean contain(String str, String value) {
        if (StringUtils.isEmpty(str)) {
            return false;
        }
        try {
            List list = Arrays.asList(str.split(","));
            return list.contains(value);
        } catch (Exception e) {
            return false;
        }

    }

    //判断字符串是否为数字的方法
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

    // 判断一个字符串是否都为数字
    public static boolean isDigit(String strNum) {
        Pattern pattern = Pattern.compile("[0-9]+");
        Matcher matcher = pattern.matcher((CharSequence) strNum);
        return matcher.matches();
    }
    
    //打包主类用
    public static void main(String[] args) {
		
	}
}
