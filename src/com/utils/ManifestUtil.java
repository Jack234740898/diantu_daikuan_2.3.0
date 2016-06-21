package com.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class ManifestUtil {
	/**
	 * 根据包名设置版本类型信息
	 * 1、com.yishengjia.patients 2、com.yishengjia.doctor
	 * 1、医生版  2、患者版
	 * @param context
	 * @return
	 */
	public static String getClient(Context context) {
		String packageName;
		try {
			packageName = getPackageInfo(context).packageName;
		} catch (NameNotFoundException e) {
			packageName = "";
		}
		if ("com.yishengjia.patients".equals(packageName)) {
			return "2";
		} 
		return "1";
	}
	/**
	 * 获取版本代码
	 * 应用版本号
	 * @param context
	 * @return
	 */
	public static int getVersionCode(Context context) {
		int versionCode;
		try {
			versionCode = getPackageInfo(context).versionCode;
		} catch (NameNotFoundException e) {
			versionCode = 0;
		}
		return versionCode;
	}
	/**
	 * 获取版本名称
	 * 应用版本名
	 * @param context
	 * @return
	 */
	public static String getVerisionName(Context context) {
		String versionCode;
		try {
			versionCode = getPackageInfo(context).versionName;
		} catch (NameNotFoundException e) {
			versionCode = "";
		}
		return versionCode;
	}
	
	public static String getActivityMetaValue(Context context, ComponentName componentName, String key) {
		String className;
		try {
			className = getActivityMetaValue(context, componentName).metaData.getString(key);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			className = "";
		} catch (Exception e) {
			className = "";
		}
		return className;
	}
	
	private static PackageInfo getPackageInfo(Context context)
			throws NameNotFoundException {
		PackageManager manager = context.getPackageManager();
		PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
		return info;
	}
	
	private static ActivityInfo getActivityMetaValue(Context context, ComponentName componentName) throws NameNotFoundException {
		PackageManager manager = context.getPackageManager();
		ActivityInfo activityInfo = manager.getActivityInfo(componentName, PackageManager.GET_META_DATA);
		return activityInfo;
	}
	
}
