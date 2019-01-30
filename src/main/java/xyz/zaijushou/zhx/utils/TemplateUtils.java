package xyz.zaijushou.zhx.utils;

import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class TemplateUtils {

    public static String templateReplace(String template, Object source) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Matcher matcher;
        while ((matcher = Pattern.compile("\\$\\{\\s*\\w+\\s*\\}").matcher(template)).find()) {
            String match = matcher.group(0);
            String attr = match.replace("$", "").replace("{", "").replace("}", "").trim();
            Method method = source.getClass().getMethod("get" + attr.substring(0, 1).toUpperCase() + attr.substring(1));
            Object result = method.invoke(source);
            String resultStr;
            if(result == null) {
                resultStr = "";
            } else if(result instanceof Date) {
                resultStr = new SimpleDateFormat("yyyy年MM月dd日").format(result);
            } else {
                resultStr = result.toString();
            }
            template = template.replace(matcher.group(0), resultStr);
        }
        return template;
    }

}
