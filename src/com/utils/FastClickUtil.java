package com.utils;

/*
 * 快速点击
 */
public class FastClickUtil {

	private static final String TAG = "FastClickUtil";
	private static long clickTime=0L;
	public static boolean isFastClick(){
		long time = System.currentTimeMillis();
		LogUtil.d(TAG, "FastClickUtil==time is "+time+",clickTime is "+clickTime);
		long diffTime = clickTime-time;
		if(diffTime>0L&&diffTime<900L){
			clickTime=0L;
			return true;
		}
		clickTime = time;
		return false;
	}
	
	private static long clickTime2=0L;
	public static boolean isFastClickPlay(){
		long time = System.currentTimeMillis();
		LogUtil.d(TAG, "FastClickUtil==time is "+time+",clickTime2 is "+clickTime2);
		long diffTime = clickTime2-time;
		if(diffTime>0L&&diffTime<700L){
			clickTime2=0L;
			return true;
		}
		clickTime2 = time;
		return false;
	}
}
