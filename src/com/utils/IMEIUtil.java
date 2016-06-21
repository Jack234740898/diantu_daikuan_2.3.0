
package com.utils;

import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * 取IMEI等系统数据
 * 
 * @author Administrator
 */
public class IMEIUtil {

    private static TelephonyManager tm;

    /**
     * 初始化tm对像
     */
    public static void init(Context context) {
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    /**
     * 取IMEI
     * 
     * @param context
     * @return
     */
    public static String getImei(Context context) {
        if (tm == null)
            init(context);
        return tm.getDeviceId();
    }

    /**
     * 取出IMSI
     * 
     * @param context
     * @return
     */
    public static String getIMSI(Context context) {
        if (tm == null)
            init(context);
        return tm.getSubscriberId();
    }

    /**
     * 取出手机号MSISDN，很可能为空
     * 
     * @param context
     * @return
     */
    public static String getTel(Context context) {
        if (tm == null)
            init(context);
        return tm.getLine1Number();
    }

    /**
     * 取出ICCID
     * 
     * @param context
     * @return
     */
    public static String getICCID(Context context) {
        if (tm == null)
            init(context);
        return tm.getSimSerialNumber();
    }
}
