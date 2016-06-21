package com.sxjs.diantu_daikuan.ui.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;

import com.alibaba.mobileim.login.YWLoginState;
import com.db.DeviceData;
import com.db.UserData;
import com.net.service.UserJsonService;
import com.sxjs.diantu_daikuan.R;
import com.sxjs.diantu_daikuan.ui.service.IMLoginService;
import com.ui.view.StatusBarCompat;
import com.umeng.message.UmengRegistrar;
import com.utils.AnimationUtil;
import com.utils.IntentUtil;
import com.utils.LogUtil;
import com.utils.PackageUtil;
import com.utils.ScreenUtil;
import com.utils.StringUtil;

import java.io.FileInputStream;

/*
 * 启动页面
 */
public class SplashActivity extends BaseActivity implements Callback {

	private static final String TAG = "SplashActivity";
	private static final int delayMillis = 3500;
	private Handler mHandler;
	private int screenW;
	private ImageView imgview;

	@Override
	protected void onInit(Bundle bundle) {
		mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mActivity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		super.onInit(bundle);
		StatusBarCompat.translucentStatusBar(mActivity);
		screenW = ScreenUtil.getWidth(mActivity);
		imgview = (ImageView) findViewById(R.id.imgview);
		show_img();
		showAlphaAnimation(imgview);
		UserData.device_token = UmengRegistrar.getRegistrationId(mActivity);
		LogUtil.d(TAG, "device_token is " + UserData.device_token
				+ ",device_token is " + DeviceData.device_token);
		if (!StringUtil.checkStr(DeviceData.device_token)) {//
			device_register();
		}
		String path = PackageUtil.getPackagePath(mActivity);
		String curApplicationPath = PackageUtil
				.getCurApplicationPath(mActivity);
		LogUtil.d(TAG, "path is " + path + ",curApplicationPath is "
				+ curApplicationPath);
		// initOpenIm();
		// initView();
		mHandler = new Handler(this);
		mHandler.sendEmptyMessageDelayed(1, delayMillis);
	}

	private void show_img() {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(mActivity.getResources(),
				R.drawable.launch_logo, options);
		int oriW = options.outWidth;
		options.inSampleSize = 1;
		if (oriW > screenW) {
			options.inSampleSize = Math.round(oriW * 1.0f / screenW);// Math.round(oriW
																		// *
																		// 1.0f
																		// /
																		// screenW);
		}
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(),
				R.drawable.launch_logo, options);
		imgview.setImageDrawable(new BitmapDrawable(mActivity.getResources(),
				bitmap));
	}

	private void showAlphaAnimation(View view) {
		// 渐变展示启动屏
		AlphaAnimation aa = new AlphaAnimation(0.20f, 1.0f);
		aa.setDuration(delayMillis);
		view.startAnimation(aa);
		aa.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationEnd(Animation arg0) {
				forward_page();
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationStart(Animation animation) {
			}

		});
	}

	private void initView() {
		ImageView imgview = (ImageView) findViewById(R.id.imgview);
		AnimationUtil.showAlphaAnimation(mActivity, imgview, delayMillis - 500);
	}

	private void forward_page() {
		LogUtil.d(TAG, "openIMUserId is "+UserData.openIMUserId+",openIMPassword is "+UserData.openIMPassword);
		if (StringUtil.checkStr(UserData.openIMUserId)
				&& YWLoginState.success != mContext.mOpenImUtil.getIMKit()
						.getIMCore().getLoginState()) {
			mActivity.startService(new Intent(mActivity,
					IMLoginService.class));
		}
		IntentUtil.activityForward(mActivity, MainHomeActivity.class, null,
				true);
	}
	@Override
	public boolean handleMessage(Message msg) {
		switch (msg.what) {
		case 1:
			/*LogUtil.d(TAG, "openIMUserId is " + UserData.openIMUserId
					+ ",openIMPassword is " + UserData.openIMPassword);
			if (StringUtil.checkStr(UserData.openIMUserId)
					&& YWLoginState.success != mContext.mOpenImUtil.getIMKit()
							.getIMCore().getLoginState()) {
				mActivity.startService(new Intent(mActivity,
						IMLoginService.class));
			}
			IntentUtil.activityForward(mActivity, MainHomeActivity.class, null,
					true);*/
			break;
		default:
			break;
		}
		return false;
	}

	private void device_register() {
		new Thread() {
			public void run() {
				UserJsonService userService = new UserJsonService(mContext);
				userService.device_register();
			}
		}.start();
	}

	private boolean isNotFirst() {
		try {
			FileInputStream fis = openFileInput("test.txt");
			if (null == fis)
				return false;
			byte[] buffer = new byte[1024];
			fis.read(buffer);
			String fileContent = new String(buffer); // 读到的值
			if (StringUtil.checkStr(fileContent))
				return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//mHandler.removeMessages(1);
	}

	@Override
	protected int setContentView() {
		// TODO Auto-generated method stub
		return R.layout.splash;
	}

}
