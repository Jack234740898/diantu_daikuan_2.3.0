package com.utils;

import android.util.Log;

import com.sxjs.diantu_daikuan.AppConfig;
public class LogUtil {

	public static void i(String tag, String message) {
		if (AppConfig.IS_DEBUG)
			Log.i(tag, message);
	}

	public static void e(String tag, String message) {
		if (AppConfig.IS_DEBUG)
			Log.e(tag, message);
	}

	public static void e(String tag, String message, Throwable throwble) {
		if (AppConfig.IS_DEBUG)
			Log.e(tag, message, throwble);
	}

	public static void w(String tag, String message) {
		if (AppConfig.IS_DEBUG)
			Log.w(tag, message);
	}

	public static void w(String tag, String message, Throwable throwble) {
		if (AppConfig.IS_DEBUG)
			Log.w(tag, message, throwble);
	}

	public static void d(String tag, String message) {
		if (AppConfig.IS_DEBUG)
			Log.d(tag, message);
	}

	public static void v(String tag, String message) {
		if (AppConfig.IS_DEBUG)
			Log.v(tag, message);
	}

	public static void e(Throwable e) {
		if (AppConfig.IS_DEBUG)
			e.printStackTrace();
	}

	public static void p(Object e) {
		if (AppConfig.IS_DEBUG)
			System.out.println(e.toString());
	}
}
