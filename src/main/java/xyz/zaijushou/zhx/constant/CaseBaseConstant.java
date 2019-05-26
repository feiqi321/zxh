package xyz.zaijushou.zhx.constant;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 *
 */
public class CaseBaseConstant {
    /**
     * 4w
     */
    public static BigDecimal CLOW  = new BigDecimal("40000");

    /**
     * 12w
     */
    public static BigDecimal CHIGH  = new BigDecimal("120000");
    public static BigDecimal CMIDDLE = new BigDecimal("80000");
    public static BigDecimal C1  = new BigDecimal("0.01");
    public static BigDecimal C2  = new BigDecimal("0.02");
    public static BigDecimal C3  = new BigDecimal("0.03");
    public static BigDecimal C4  = new BigDecimal("0.04");
    public static BigDecimal C5  = new BigDecimal("0.05");

    public static BigDecimal SLOW  = new BigDecimal("0.65");
    public static BigDecimal SMIDDLE  = new BigDecimal("0.76");
    public static BigDecimal SHIGH  = new BigDecimal("0.85");

    /**
     * 比例系数
     */
    public static BigDecimal RLOW  = new BigDecimal("10");
    public static BigDecimal RMIDDLE  = new BigDecimal("15");
    public static BigDecimal RHIGH  = new BigDecimal("20");

    public static BigDecimal MLOW = new BigDecimal("10000");
    public static BigDecimal MMIDDLE = new BigDecimal("30000");
    public static BigDecimal MHIGH = new BigDecimal("60000");

    public static BigDecimal H1 = new BigDecimal("1");
    public static BigDecimal H2 = new BigDecimal("2");
    public static BigDecimal H3 = new BigDecimal("5");
    public static BigDecimal H4 = new BigDecimal("10");

    public static BigDecimal ROUND = new BigDecimal("100");

    public static int COLLECTION_TYPE = 406;

}
