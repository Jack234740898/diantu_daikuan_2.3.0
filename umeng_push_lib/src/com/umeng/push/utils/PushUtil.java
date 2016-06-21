package com.umeng.push.utils;

import android.content.Context;

import com.umeng.message.PushAgent;
import com.umeng.message.UmengRegistrar;

public class PushUtil {

	/*
	 * 在应用的主Activity onCreate() 函数中开启推送服务
	 */
	public static void pushEnable(Context context,boolean enable){
		PushAgent mPushAgent = PushAgent.getInstance(context);
		if(enable){
			mPushAgent.enable();
		}else{
			mPushAgent.disable();
		}
	}
	
	/*
	 * 统计应用启动数据
	 * 在所有的Activity 的onCreate 函数添加
	 */
	public static void onAppStart(Context context){
		PushAgent.getInstance(context).onAppStart();
	}
	
	/*
	 * 获取设备的Device Token（可选）
	 * 获取Device Token的代码需要放在mPushAgent.enable();后面，注册成功以后调用才能获得Device Token。
	 */
	public static String getRegistrationId(Context context){
		return UmengRegistrar.getRegistrationId(context);
	}
	
	/*
	 * isMergeNotificaiton 默认值为true,false展示多条，
	 * 是否合并多个通知栏
	 */
	private static void setMergeNotificaiton(Context context,boolean isMergeNotificaiton){
		PushAgent.getInstance(context).setMergeNotificaiton(isMergeNotificaiton);
	}
}
