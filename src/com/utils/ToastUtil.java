package com.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.sxjs.diantu_daikuan.MyApplication;

public class ToastUtil {
	private static Toast toast = null;
	/**
	 * 提示信息
	 */
	public static void showToast(Context context, int textID, boolean isLongTime) {
		
		/*if (isLongTime) {
			toast = Toast.makeText(context, textID, Toast.LENGTH_LONG);
		} else {
			toast = Toast.makeText(context, textID, Toast.LENGTH_SHORT);
		}*/
		if(toast == null){
			toast = Toast.makeText(context, textID, isLongTime?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
		}else{
			toast.setText(textID);
			toast.setDuration(isLongTime?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
		}//getDeviceWidthHeight()[1]/8;
		final int y = ScreenUtil.getHeight(context)/8;
		toast.setGravity(Gravity.BOTTOM, 0, y);
		toast.show();
	}
	
	/**
	 * 提示信息
	 * textID和text只能传其1
	 */
	public static void showToast(final Context context, final int textID, final String text,final boolean isLongTime) {
		final String textStr;
		final int y = ScreenUtil.getHeight(context)/8;//ScreenUtil.getHeight(context)/8;
		if(Looper.myLooper() != Looper.getMainLooper()){
			MyApplication.mContext.runOnUiThread(new Runnable() {
				@Override
				public void run() {		
					show(context, textID, text, isLongTime, y);
				}
			});
		}else{
			show(context, textID, text, isLongTime, y);
		}
		
	}

	private static void show(final Context context, final int textID,
			final String text, final boolean isLongTime, final int y) {
		if(StringUtil.checkStr(text)){
			if(toast == null){
				toast = Toast.makeText(context, text, isLongTime?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
			}else{
				toast.setText(text);
				toast.setDuration(isLongTime?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
			}
		}else{
			if(toast == null){
				toast = Toast.makeText(context, textID, isLongTime?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
			}else{
				toast.setText(textID);
				toast.setDuration(isLongTime?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
			}
		}
		
		toast.setGravity(Gravity.BOTTOM, 0, y);
		toast.show();
	}
	
}
