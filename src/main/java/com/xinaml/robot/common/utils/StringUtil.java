package com.xinaml.robot.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StringUtil {

    private static final String REG = "[\u4e00-\u9fa5]";

    /**
     * 是否包含汉字
     * @param str
     * @return
     */
    public static  boolean isChinese(String str) {
        if (StringUtils.isNotBlank(str)) {
            Pattern p = Pattern.compile(REG);
            Matcher m = p.matcher(str);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 首字母大写
     * @param string
     * @return
     */
    public static String toUpperFirst(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] -= 32;
        return String.valueOf(charArray);
    }

    /**
     * 首字母小写
     * @param string
     * @return
     */
    public static String toLowerFirst(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] += 32;
        return String.valueOf(charArray);
    }

    /**
     * 去掉科学计数法
     * @param val
     * @return
     */
    public static String formatDouble(Double val) {
        if(null==val){
            return "";
        }
        try {
            if (Double.isNaN(val)) {
                return "0";
            }
            double d= new BigDecimal(val).setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
            NumberFormat nf = NumberFormat.getInstance();
            //设置保留多少位小数
            nf.setMaximumFractionDigits(20);
            // 取消科学计数法
            nf.setGroupingUsed(false);
            //返回结果
            return nf.format(d);
        } catch (Exception e) {
            return "0";
        }

    }
}
