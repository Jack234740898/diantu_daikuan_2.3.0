package com.utils;

import android.content.Context;

import com.sxjs.diantu_daikuan.AppConfig;
import com.umeng.analytics.MobclickAgent;

/*
 * umeng统计
 */
public class UmenStatisticsUtil {

	public static void onResume(Context context){
		if(!AppConfig.IS_TEST){
			MobclickAgent.onResume(context);
		}
	}
	
	public static void onPause(Context context){
		if(!AppConfig.IS_TEST){
			MobclickAgent.onPause(context);
		}
	}
	
	/*
	 * 自定义事件
	 */
	public static void onEvent(Context context,String event){
		if(!AppConfig.IS_TEST){
			MobclickAgent.onEvent(context, event);
		}
	}
}
