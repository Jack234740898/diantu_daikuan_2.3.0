package com.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.sxjs.diantu_daikuan.R;
import com.ui.view.DecelerateAccelerateInterpolator;

public class AnimationUtil {

	private static final String TAG = "AnimationUtil";
	/*
	 * 进入动画
	 */
	public static void activityAnimat(Context context,View root,boolean isEnter){
		Animation a = null;
		if(isEnter){
			a = AnimationUtils.loadAnimation(context, R.anim.enter_in);
		}else{
			a = AnimationUtils.loadAnimation(context, R.anim.exit_out);
		}
	    a.reset();
	    root.clearAnimation();
	    root.startAnimation(a);   
	}
	/*
	 * 透明度渐变动画
	 */
    public static void fadeAnimat(Context context,View root,boolean isFadein){
    	Animation a = null;
    	if(isFadein){
    		a = AnimationUtils.loadAnimation(context, R.anim.fadein);
    	}else{
    		a = AnimationUtils.loadAnimation(context, R.anim.fadeout);
    	}
    	a.reset();
	    root.clearAnimation();
	    root.startAnimation(a);   
	}
    
    /*
     * 缩放动画
     */
    public static void scaleAnima(Context context,View view,long durationMillis){
    	Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_anim);
    	animation.setDuration(durationMillis);
    	view.setAnimation(animation);
    	animation.start();
    }
    
    /*
     * 渐变展示启动屏
     */
    public static void showAlphaAnimation(Context context,View view,long durationMillis){
    	 AlphaAnimation aa = new AlphaAnimation(0.2f,1.0f);
         aa.setDuration(durationMillis);
         view.startAnimation(aa);
         aa.setAnimationListener(new AnimationListener()
         {
             @Override
             public void onAnimationEnd(Animation arg0) {
                // redirectTo();
             }
             @Override
             public void onAnimationRepeat(Animation animation) {}
             @Override
             public void onAnimationStart(Animation animation) {}
                                                                           
         });
    }
    
    /**
     * 创建循环平移动画
     * final View view
     */
    public static TranslateAnimation showTranslateAnim(Activity context, float fromX, float toX,final View view) {
        TranslateAnimation tlAnim = new TranslateAnimation(fromX, toX, 0, 0);
        //自动计算时间
        //long duration = (long) (Math.abs(toX - fromX) * 1.0f / ScreenUtil.getWidth(context) * 4000);
        tlAnim.setDuration(6500);
        tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
        tlAnim.setFillAfter(true);
        tlAnim.setRepeatMode(Animation.RESTART);
        tlAnim.setRepeatCount(Animation.INFINITE);
        tlAnim.setFillEnabled(true);
        tlAnim.setFillAfter(true);
        tlAnim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {		
				LogUtil.d(TAG, "onAnimationStart==");
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {	
				LogUtil.d(TAG, "onAnimationRepeat==");
				//view.clearAnimation();
				//view.startAnimation(animation);
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				LogUtil.d(TAG, "onAnimationEnd==");
			}
		});
        view.clearAnimation();
        view.setAnimation(tlAnim);
        view.startAnimation(tlAnim);
        tlAnim.start();
        return tlAnim;
    }
}
