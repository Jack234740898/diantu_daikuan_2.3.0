package com.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/*
 * 页面的跳转，Intent常量的设置
 */
public class IntentUtil {

	/*
	 * 无回调的
	 */
	public static void activityForward(Context activity, Class clazz,
			Bundle bundle, boolean isFinish) {
		Intent intent = new Intent(activity, clazz);
		if(!(activity instanceof FragmentActivity)){
			//intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		if (null != bundle)
			intent.putExtras(bundle);
		activity.startActivity(intent);
		/*
		 * if(!isFinish){
		 * ((Activity)activity).overridePendingTransition(R.anim.push_left_in,
		 * R.anim.push_left_out); return; }
		 */
		if (isFinish && activity instanceof Activity){
			((Activity) activity).finish();
			//((Activity) activity).overridePendingTransition(R.anim.enter_in, R.anim.exit_out);  
		}
			
	}

	/*
	 * 可回调的
	 */
	public static void startActivityForResult(Activity activity, Class clazz,
			int requestCode, Bundle bundle) {
		Intent intent = new Intent(activity, clazz);
		if(!(activity instanceof Activity)){
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}
		if (null != bundle) {
			intent.putExtras(bundle);
			activity.startActivityForResult(intent, requestCode);
		} else {
			activity.startActivityForResult(intent, requestCode);
		}
	}

	/*
	 * 启动一个服务
	 */
	public static void serviceForward(Context activity, Class clazz,
			Bundle bundle, boolean isFinish) {
		Intent intent = new Intent(activity, clazz);
		if (null != bundle)
			intent.putExtras(bundle);
		activity.startService(intent);
		if (isFinish && activity instanceof Activity)
			((Activity) activity).finish();
	}
}
