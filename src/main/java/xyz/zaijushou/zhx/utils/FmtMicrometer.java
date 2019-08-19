package xyz.zaijushou.zhx.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by looyer on 2019/3/4.
 */
public class FmtMicrometer {

    /**
     * @Title: fmtMicrometer
     * @Description: 格式化数字为千分位
     * @param text
     * @return    设定文件
     * @return String    返回类型
     */
    public static String fmtMicrometer(String text) {
        DecimalFormat df = null;
        if (text.indexOf(".") > 0) {
            if (text.length() - text.indexOf(".") - 1 == 0) {
                df = new DecimalFormat("###,##0.");
            } else if (text.length() - text.indexOf(".") - 1 == 1) {
                df = new DecimalFormat("###,##0.0");
            } else if (text.length() - text.indexOf(".") - 1 == 2) {
                df = new DecimalFormat("###,##0.00");
            } else if (text.length() - text.indexOf(".") - 1 > 2) {
                df = new DecimalFormat("###,##0.00");
            } else {
                df = new DecimalFormat("###,##0");
            }
        } else {
            df = new DecimalFormat("###,##0");
        }
        double number = 0.0;
        try {
            number = Double.parseDouble(text);
        } catch (Exception e) {
            number = 0.0;
        }
        return df.format(number);
    }

    public static void main(String args[]){

        System.out.println(FmtMicrometer.fmtMicrometer(new BigDecimal("10022.223").stripTrailingZeros()+""));

    }
}
