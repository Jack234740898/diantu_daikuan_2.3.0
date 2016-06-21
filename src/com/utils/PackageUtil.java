package com.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

public class PackageUtil {

	/*
	 * 获取该程序的安装包路径
	 */
	public static String getPackagePath(Context context) {
		// PackageManager pm = context.getPackageManager();
		return context.getPackageResourcePath();
	}

	/*
	 * 获取当前程序路径
	 */
	public static String getCurApplicationPath(Context context) {
		// PackageManager pm = context.getPackageManager();
		return context.getFilesDir().getAbsolutePath();
	}

	/*
	 * 得到应用的版本号
	 */
	public static String getVersionName(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	/*
	 * 得到应用的版本号
	 */
	public static int getVersionCode(Context context) {
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(),
					0);
			return info.versionCode;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
}
