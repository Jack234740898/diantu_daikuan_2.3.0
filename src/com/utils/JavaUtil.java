package com.utils;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

public class JavaUtil {
    public static String trimToNull(String str) {
        if (!notNullOrBlank(str)) {
            return null;
        }
        return str.trim();
    }

    public static boolean notNullOrBlank(String str) {
        if (str != null && !"".equals(str.trim()) && !"null".equals(str.trim())
                && !"undefined".equals(str.trim())) {
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static boolean notNullOrBlank(Collection collection) {
        if (collection != null && collection.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 传入字符串为null时返回空字符串
     * 
     * @param o 传入的对象
     * @return
     */
    public static String checkNull(String o) {
        if (o == null) {
            return "";
        }
        return o.trim();
    }

    /**
     * 字符串转日期
     * 
     * @param dateStr 日期字符串
     * @param fromatStr 日期格式
     * @return
     * @throws ParseException
     */
    static public Date strToDate(String dateStr, String fromatStr) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(fromatStr);
        return sdf.parse(dateStr);
    }

    /**
     * 日期转字符串
     * 
     * @param date 日期
     * @param fromatStr 日期格式
     * @return
     */
    static public String DateToStr(Date date, String fromatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromatStr);
        return sdf.format(date);
    }

    static public String getDayString(Date date) {
        String str = "星期";
        int day = date.getDay();
        switch (day) {
            case 0:
                str += "天";
                break;
            case 1:
                str += "一";
                break;
            case 2:
                str += "二";
                break;
            case 3:
                str += "三";
                break;
            case 4:
                str += "四";
                break;
            case 5:
                str += "五";
                break;
            case 6:
                str += "六";
                break;
            default:
                break;
        }
        return str;
    }

    /**
     * 日期转字符串
     * 
     * @param date 日期
     * @param fromatStr 日期格式
     * @return
     * @throws ParseException
     */
    static public String DateStrToStr(String fromdateStr, String fromfromatStr, String tofromatStr)
            throws ParseException {
        String temp = null;
        if (JavaUtil.notNullOrBlank(fromdateStr) && JavaUtil.notNullOrBlank(fromfromatStr)) {
            Date date = strToDate(fromdateStr, fromfromatStr);
            temp = DateToStr(date, tofromatStr);
        }
        return temp;
    }

    /**
     * 计算两个日期之间的天数
     * 
     * @param earydate 日期一
     * @param latedate 日期二
     * @return 天数
     */
    public static int getBetweenDates(Date earydate, Date latedate) {
        long quot = latedate.getTime() - earydate.getTime();
        int dates = (int) (quot / 1000 / 60 / 60 / 24);
        return dates;
    }

    /**
     * 日期计算
     * 
     * @param fromdate 日期
     * @param dateCount 相隔天数
     * @return 计算后的日期
     */
    static public Date getNextDate(Date fromdate, int dateCount) {
        long time = fromdate.getTime();
        long datetime = 1000 * 60 * 60 * 24;
        time += datetime * dateCount;
        return new Date(time);
    }

    /**
     * MD5加密
     * 
     * @param password 密码
     * @return str MD5加密后的密码
     */
    public static String toMD5(String password) {
        MessageDigest md5 = null;
        String str = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            md5.update(password.getBytes());
            str = encodeHex(md5.digest());
            Log.v("=====================", "==========" + str);
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str.toUpperCase();
    }

    public static final String encodeHex(byte[] bytes) {
        StringBuffer buf = new StringBuffer(bytes.length * 2);
        int i;

        for (i = 0; i < bytes.length; i++) {
            if (((int) bytes[i] & 0xff) < 0x10) {
                buf.append("0");
            }
            buf.append(Long.toString((int) bytes[i] & 0xff, 16));
        }
        return buf.toString();
    }
}
